package com.akshenkadakia.homeutility.medicine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.data.AlarmData;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;
import com.akshenkadakia.homeutility.medicine.data.MedicineData;

import java.util.ArrayList;

public class NotificationDialog extends Activity {
    private AlertDialog alertDialog;
    private int memberID;
    int medicineID;
    int alarmID;
    FamilyData familyData;
    MedicineData medicineData;
    AlarmData alarmData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_dialog);

        final DBOpenHelper mDB = DBOpenHelper.getInstance(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        memberID = extras.getInt(AlarmReceiver.MEMBER_ID, -1);
        medicineID = extras.getInt(AlarmReceiver.MEDICINE_ID, -1);
        alarmID = extras.getInt(AlarmReceiver.CALENDER_ID, -1);

        //Log.e("memID",""+memberID);
        //Log.e("medID",""+medicineID);
        //Log.e("almID",""+calendarID);

        familyData = mDB.getMember(memberID);
        medicineData = mDB.getMedicine(memberID, medicineID);
        alarmData = mDB.getAlarm(memberID, medicineID, alarmID);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        builder.setTitle(getString(R.string.medicineTime));
        builder.setMessage("Has " + familyData.getName() + " taken " + medicineData.getName() + "?");
        builder.setNegativeButton(R.string.skip, null);
        builder.setPositiveButton(R.string.taken, null);

        alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button pos = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                pos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel("" + alarmData.getIid(), alarmData.getIid());
                        int left = medicineData.getLeft();
                        int total = medicineData.getTotal();
                        if (total != 0 && medicineData.getPackets() != 0) {
                            Notification.Builder builder = new Notification.Builder(getBaseContext());
                            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                            builder.setLights(Color.RED, 3000, 3000);
                            builder.setAutoCancel(true);
                            builder.setSmallIcon(R.drawable.ic_stat_splash_image);

                            if (left > 1) {
                                left--;
                                medicineData.setLeft(left);
                                mDB.updateMedicine(medicineData);
                                Log.e("left:", "" + medicineData.getLeft());
                            }
                            if (left >= 1 && left <= 3) {
                                builder.setContentTitle(getString(R.string.runShort));
                                builder.setContentText(familyData.getName() + " "+getString(R.string.isRunOut)+" " + medicineData.getName() + "("+getString(R.string.only)+" " + left + " "+getString(R.string.left)+")!");
                                Notification notification = builder.build();
                                //notification.flags = Notification.PRIORITY_MAX|Notification.FLAG_AUTO_CANCEL;
                                notificationManager.notify(0, notification);
                            } else if (left < 1) {
                                builder.setContentTitle(getString(R.string.runOut));
                                builder.setContentText(familyData.getName() + " "+getString(R.string.isRunOut)+" " + medicineData.getName() + "("+getString(R.string.none)+" "+getString(R.string.left)+")!");
                                Notification notification = builder.build();
                                //notification.flags = Notification.PRIORITY_MAX|Notification.FLAG_AUTO_CANCEL;

                                notificationManager.notify(0, notification);
                            }
                        }
                        alertDialog.dismiss();
                        finish();
                    }
                });
                Button neg = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                neg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel("" + alarmData.getIid(), alarmData.getIid());
                        alertDialog.dismiss();
                        finish();
                    }
                });
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        alertDialog.show();
    }
}
