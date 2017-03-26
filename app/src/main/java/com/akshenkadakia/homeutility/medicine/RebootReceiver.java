package com.akshenkadakia.homeutility.medicine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.akshenkadakia.homeutility.emergency.DoubleTapService;
import com.akshenkadakia.homeutility.medicine.data.AlarmData;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;
import com.akshenkadakia.homeutility.medicine.data.MedicineData;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class RebootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            context.startService(new Intent(context, DoubleTapService.class));
            Log.e("reboot", "boot boot");
            DBOpenHelper mDB= DBOpenHelper.getInstance(context);
            ArrayList<FamilyData> members=mDB.getAllMembers();
            for(FamilyData memberName:members){
                if(memberName.isNotifications()){
                    ArrayList<MedicineData> medicines=mDB.getAllMedicines(memberName.getFid());
                    for (int i=0;i<medicines.size();i++) {
                        MedicineData md=medicines.get(i);
                        int x=md.getMid();
                        ArrayList<AlarmData> alarms=mDB.getAllAlarms(md);
                        for(int j=0;j<alarms.size();i++){
                            AlarmData ad=alarms.get(j);
                            int y=ad.getAid();
                            Time c = new Time(Calendar.getInstance().getTimeInMillis());
                            c.setHours(ad.getTime().getHours());
                            c.setMinutes(ad.getTime().getMinutes());
                            c.setSeconds(0);
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent inte = new Intent(context, AlarmReceiver.class);
                            inte.putExtra(AlarmReceiver.MEMBER_ID, memberName.getFid());
                            inte.putExtra(AlarmReceiver.MEDICINE_ID,x );
                            inte.putExtra(AlarmReceiver.CALENDER_ID, y);
                            int pID=Integer.parseInt(""+memberName.getFid()+""+x+""+y);
                            ad.setIid(pID);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, pID, inte, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTime(), AlarmManager.INTERVAL_DAY, pendingIntent);
                            alarms.set(j,ad);
                        }
                        medicines.set(i, md);
                    }
                    //int d=mDB.updateMember(memberName);
                    Log.e("Reset:",memberName.getName()+":");
                }
            }
        }
    }
}
