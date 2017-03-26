package com.akshenkadakia.homeutility.medicine;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.data.AlarmData;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;
import com.akshenkadakia.homeutility.medicine.data.MedicineData;

import java.util.Locale;

public class BigViewNotiService extends IntentService {
    public static String ACTION_SKIP="ACTION_SKIP";
    public static String ACTION_TAKEN="ACTION_TAKEN";

    private int memberID;
    int medicineID;
    int alarmID;
    FamilyData familyData;
    MedicineData medicineData;
    AlarmData alarmData;
    DBOpenHelper mDB;

    public BigViewNotiService() {
        super("BigViewNoti");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lo=sp.getString(getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        //Log.e("service","service");
        mDB= DBOpenHelper.getInstance(this);
        Bundle extras=intent.getExtras();
        boolean taken=intent.getBooleanExtra(ACTION_TAKEN,false);
        memberID=extras.getInt(AlarmReceiver.MEMBER_ID, -1);
        medicineID=extras.getInt(AlarmReceiver.MEDICINE_ID, -1);
        alarmID=extras.getInt(AlarmReceiver.CALENDER_ID, -1);

        //Log.e("service","service"+taken+":"+medicineID);
        familyData=mDB.getMember(memberID);
        medicineData=mDB.getMedicine(memberID,medicineID);
        alarmData=mDB.getAlarm(memberID,medicineID,alarmID);

        if(taken){
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
                if (left > 0) {
                    left--;
                    medicineData.setLeft(left);
                    if (left <= 3) {
                        builder.setContentTitle(getBaseContext().getString(R.string.runShort));
                        builder.setContentText(familyData.getName() + " "+getString(R.string.isRunOut)+" " + medicineData.getName() + "("+getString(R.string.only)+" " + left + " "+getString(R.string.left)+")!");
                        Notification notification = builder.build();
                        //notification.flags = Notification.PRIORITY_MAX|Notification.FLAG_AUTO_CANCEL;
                        notificationManager.notify(0, notification);
                    }
                    mDB.updateMedicine(medicineData);
                } else {
                    builder.setContentTitle(getString(R.string.runOut));
                    builder.setContentText(familyData.getName() + " "+getString(R.string.isRunOut)+" " + medicineData.getName() + "("+getString(R.string.none)+" "+getString(R.string.left)+")!");
                    Notification notification = builder.build();
                    //notification.flags = Notification.PRIORITY_MAX|Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(0, notification);
                }
            }
        }
        else{
            NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel("" + alarmData.getIid(), alarmData.getIid());
        }
    }
}
