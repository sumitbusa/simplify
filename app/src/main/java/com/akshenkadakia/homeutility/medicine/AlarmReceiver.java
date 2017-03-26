package com.akshenkadakia.homeutility.medicine;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.data.AlarmData;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;
import com.akshenkadakia.homeutility.medicine.data.MedicineData;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver{
    public static String MEMBER_ID="memberID";
    public static String MEDICINE_ID="medicineID";
    public static String CALENDER_ID="calenderID";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(context);
        String lo=sp.getString(context.getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
        //Log.e("onR","noti");
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        DBOpenHelper mDB=DBOpenHelper.getInstance(context);

        int memberID=intent.getIntExtra(MEMBER_ID,-1);
        int medicineID=intent.getIntExtra(MEDICINE_ID,-1);
        int alarmID=intent.getIntExtra(CALENDER_ID, -1);

        if(memberID==-1||medicineID==-1||alarmID==-1)
            return;

        FamilyData familyData=mDB.getMember(memberID);

        if(familyData!=null && familyData.isNotifications()) {
            MedicineData medicineData=mDB.getMedicine(memberID, medicineID);

            if(medicineData!=null) {
                //Log.e("onR","medicine");
                AlarmData alarmData=mDB.getAlarm(memberID, medicineID, alarmID);

                if(alarmData!=null) {
                    //Log.e("onR","alarm");
                    int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    int iid=Integer.parseInt(memberID+""+medicineID+""+alarmID);
                    Time alarmTime=alarmData.getTime();
                    if (alarmData.checkDay(day) && iid==alarmData.getIid()) {
                        //Log.e("onR","day");
                        Intent notificationIntent = new Intent(context, NotificationDialog.class);
                        notificationIntent.putExtra(AlarmReceiver.MEMBER_ID, alarmData.getFid());
                        notificationIntent.putExtra(AlarmReceiver.MEDICINE_ID, alarmData.getMid());
                        notificationIntent.putExtra(AlarmReceiver.CALENDER_ID, alarmData.getAid());
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        Notification.Builder builder = new Notification.Builder(context);
                        builder.setContentTitle(context.getString(R.string.medicineTime));
                        builder.setContentText(familyData.getName() + " "+context.getString(R.string.hasToTake)+" " + medicineData.getName());
                        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                        builder.setLights(Color.CYAN, 3000, 3000);
                        builder.setSmallIcon(R.drawable.ic_stat_splash_image);
                        builder.setContentIntent(pendingIntent);

                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
                            //big view
                            Intent skipIntent = new Intent(context, BigViewNotiService.class);
                            skipIntent.putExtra(AlarmReceiver.MEMBER_ID, familyData.getFid());
                            skipIntent.putExtra(AlarmReceiver.MEDICINE_ID, medicineData.getMid());
                            skipIntent.putExtra(AlarmReceiver.CALENDER_ID, alarmData.getAid());
                            skipIntent.putExtra(BigViewNotiService.ACTION_TAKEN, false);
                            PendingIntent skipPIntent = PendingIntent.getService(context, alarmData.getIid(), skipIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            Intent takenIntent = new Intent(context, BigViewNotiService.class);
                            takenIntent.putExtra(BigViewNotiService.ACTION_TAKEN, true);
                            takenIntent.putExtra(AlarmReceiver.MEMBER_ID, familyData.getFid());
                            takenIntent.putExtra(AlarmReceiver.MEDICINE_ID, medicineData.getMid());
                            takenIntent.putExtra(AlarmReceiver.CALENDER_ID, alarmData.getAid());
                            PendingIntent takenPIntent = PendingIntent.getService(context, alarmData.getIid(), takenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            String bigMsg = "Has " + familyData.getName() + " taken " + medicineData.getName() + "?";
                            builder.setStyle(new Notification.BigTextStyle().bigText(bigMsg));
                            //builder.addAction(new Notification.Action(R.drawable.ic_clear_black_48dp, "SKIP", skipPIntent));
                            //builder.addAction(new Notification.Action(R.drawable.ic_done_black_48dp, "TAKEN", takenPIntent));
                            builder.addAction(R.drawable.ic_clear_black_18dp, context.getString(R.string.skip), skipPIntent);
                            builder.addAction(R.drawable.ic_done_black_18dp, context.getString(R.string.taken), takenPIntent);
                        }
                        Notification notification = builder.build();
                        notification.flags = Notification.PRIORITY_MAX | Notification.FLAG_ONGOING_EVENT;

                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify("" + alarmData.getIid(), alarmData.getIid(), notification);

                        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                            if(alert == null) {
                                // alert backup is null, using 2nd backup
                                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                            }
                        Ringtone r = RingtoneManager.getRingtone(context, alert);
                        r.play();
                        try{
                            Thread.sleep(30000);
                            r.stop();
                        }
                        catch (Exception e){
                            Log.e("ring",""+e);
                        }
                    }
                }
            }
        }
    }
}
