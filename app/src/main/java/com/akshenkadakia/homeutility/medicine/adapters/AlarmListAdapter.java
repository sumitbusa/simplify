package com.akshenkadakia.homeutility.medicine.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.AlarmFragment;
import com.akshenkadakia.homeutility.medicine.data.AlarmData;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;
import com.akshenkadakia.homeutility.medicine.data.MedicineData;
import com.akshenkadakia.homeutility.medicine.data.SortedList;

import java.sql.Time;
import java.util.Locale;

public class AlarmListAdapter extends BaseAdapter {
    private Activity activity;
    private FamilyData familyData;
    private MedicineData medicineData;
    private LayoutInflater layoutInflater=null;
    private DBOpenHelper mDB;
    private AlarmFragment alarmFragment;
    private SortedList<AlarmData> alarmDatas;

    public AlarmListAdapter(Activity activity,AlarmFragment alarmFragment,FamilyData familyData,MedicineData medicineData){
        this.activity=activity;
        this.alarmFragment=alarmFragment;
        this.familyData=familyData;
        this.medicineData=medicineData;
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(activity);
        String lo=sp.getString(activity.getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
        this.layoutInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDB=DBOpenHelper.getInstance(activity.getBaseContext());
        alarmDatas=mDB.getAllAlarms(medicineData);
        //Log.e("selectedMedicine",medicineData.toString());
    }

    @Override
    public int getCount() {
        return alarmDatas.size();
    }

    @Override
    public AlarmData getItem(int position) {
        return alarmDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        alarmDatas=mDB.getAllAlarms(medicineData);
        super.notifyDataSetChanged();
    }

    private class Holder{
        ImageView deleteButton;
        TextView hours,minutes;
        TextView am_pm;
        CheckBox sun,mon,tue,wed,thu,fri,sat;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View view;
        if(convertView==null)
            view=(View)layoutInflater.inflate(R.layout.alarm_list_item,null);
        else
            view=(View)convertView;

        holder.deleteButton=(ImageView)view.findViewById(R.id.deleteAlarm);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmData ad = getItem(position);
                int i = mDB.deleteAlarm(ad.getFid(), ad.getMid(), ad.getAid());

                if (i > 0) {
                    notifyDataSetChanged();
                    alarmFragment.checkAddAlarmText();
                    Toast.makeText(activity, R.string.delete+" "+R.string.successful, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(activity, R.string.error+" "+R.string.deleting, Toast.LENGTH_SHORT).show();
            }
        });
        holder.deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast t = Toast.makeText(activity, R.string.delete, Toast.LENGTH_SHORT);
                t.show();
                return true;
            }
        });

        AlarmData alarmData=getItem(position);
        //Log.e("getView:alarm",alarmData.toString());
        Time calendar=alarmData.getTime();
        holder.hours=(TextView)view.findViewById(R.id.alarmHourDisplay);
        int hourOfDay=calendar.getHours();
        boolean am;
        if(hourOfDay==0){
            hourOfDay=12;
            am=true;
        }
        else if(hourOfDay<12) {
            am=true;
        }else if(hourOfDay==12) {
            am=false;
        }else{
            hourOfDay-=12;
            am=false;
        }
        String z=check2digitString(hourOfDay);
        holder.hours.setText(z);
        holder.minutes=(TextView)view.findViewById(R.id.alarmMinuteDisplay);
        String x=check2digitString(calendar.getMinutes());
        holder.minutes.setText(x);
        //Log.e("x", z + ":" + x);
        holder.am_pm=(TextView)view.findViewById(R.id.alarmAMPMDisplay);
        if(am)
            holder.am_pm.setText("AM");
        else
            holder.am_pm.setText("PM");


        holder.sun=(CheckBox)view.findViewById(R.id.checkBoxSunday);
        holder.sun.setChecked(alarmData.isSunday());
        holder.sun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDays(0, isChecked, position);
            }
        });

        holder.mon=(CheckBox)view.findViewById(R.id.checkBoxMonday);
        holder.mon.setChecked(alarmData.isMonday());
        holder.mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDays(1, isChecked, position);
            }
        });

        holder.tue=(CheckBox)view.findViewById(R.id.checkBoxTuesday);
        holder.tue.setChecked(alarmData.isTuesday());
        holder.tue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDays(2, isChecked, position);
            }
        });

        holder.wed=(CheckBox)view.findViewById(R.id.checkBoxWednesday);
        holder.wed.setChecked(alarmData.isWednesday());
        holder.wed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDays(3, isChecked, position);
            }
        });

        holder.thu=(CheckBox)view.findViewById(R.id.checkBoxThursday);
        holder.thu.setChecked(alarmData.isThursday());
        holder.thu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDays(4, isChecked, position);
            }
        });

        holder.fri=(CheckBox)view.findViewById(R.id.checkBoxFriday);
        holder.fri.setChecked(alarmData.isFriday());
        holder.fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDays(5, isChecked, position);
            }
        });

        holder.sat=(CheckBox)view.findViewById(R.id.checkBoxSaturday);
        holder.sat.setChecked(alarmData.isSaturday());
        holder.sat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDays(6, isChecked, position);
            }
        });

        return view;
    }

    public synchronized void updateDays(int x,boolean b,int position){
        AlarmData mn = getItem(position);
        switch (x){
            case 0:mn.setSunday(b);
                break;
            case 1:mn.setMonday(b);
                break;
            case 2:mn.setTuesday(b);
                break;
            case 3:mn.setWednesday(b);
                break;
            case 4:mn.setThursday(b);
                break;
            case 5:mn.setFriday(b);
                break;
            case 6:mn.setSaturday(b);
                break;
        }
        int i = mDB.updateAlarm(mn);
        if (i > 0) {
            notifyDataSetChanged();
            //Log.e("update", );
            //Toast.makeText(activity, "Delete Successful", Toast.LENGTH_SHORT).show();
        } else
            Log.e("error","days");
        //Toast.makeText(activity, "Error Deleting", Toast.LENGTH_SHORT).show();
    }

    public String check2digitString(int i){
        if(i<10)
            return "0"+i;
        else
            return ""+i;
    }
}
