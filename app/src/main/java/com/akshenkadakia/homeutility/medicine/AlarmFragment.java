package com.akshenkadakia.homeutility.medicine;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.adapters.AlarmListAdapter;
import com.akshenkadakia.homeutility.medicine.data.AlarmData;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;
import com.akshenkadakia.homeutility.medicine.data.MedicineData;

import java.sql.Time;
import java.util.Calendar;


public class AlarmFragment extends Fragment {

    private MedicineData medicineData;
    private FamilyData familyData;


    public void setMedicineData(MedicineData medicineData) {
        this.medicineData = medicineData;
    }

    public void setFamilyData(FamilyData familyData) {
        this.familyData = familyData;
    }

    private ListView listView;
    private DBOpenHelper mDB;
    private TextView fabAddAlarmText;
    private AlarmListAdapter alarmListAdapter;
    private int lastVisibleItem = 0;
    private boolean isScrollingDown = false;

    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        getActivity().setTitle(familyData.getName() + ":" + medicineData.getName());

        fabAddAlarmText = (TextView) view.findViewById(R.id.fabAddAlarmText);
        final FloatingActionButton fabAddAlarm = (FloatingActionButton) view.findViewById(R.id.fabAddAlarm);
        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(getActivity(), TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        AlarmData ad = new AlarmData();
                        ad.setFid(medicineData.getFid());
                        ad.setMid(medicineData.getMid());
                        ad.setAid(medicineData.incrementAi());
                        Time time = new Time(Calendar.getInstance().getTimeInMillis());
                        time.setHours(hourOfDay);
                        time.setMinutes(minute);
                        time.setSeconds(0);
                        ad.setTime(time);
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                        intent.putExtra(AlarmReceiver.MEMBER_ID, familyData.getFid());
                        intent.putExtra(AlarmReceiver.MEDICINE_ID, medicineData.getMid());
                        intent.putExtra(AlarmReceiver.CALENDER_ID, ad.getAid());
                        int pID = Integer.parseInt("" + familyData.getFid() + "" + medicineData.getMid() + "" + ad.getAid());
                        ad.setIid(pID);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), pID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time.getTime(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time.getTime(), AlarmManager.INTERVAL_DAY, pendingIntent);

                        mDB = DBOpenHelper.getInstance(getActivity().getBaseContext());
                        int i = mDB.updateMedicine(medicineData);
                        if (i > 0) {
                            if (mDB.addNewAlarm(ad) != -1l) {
                                alarmListAdapter.notifyDataSetChanged();
                                checkAddAlarmText();
                                ///Toast.makeText(getActivity(), R.string.add+" "+R.string.successful, Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(getActivity(),"Update Successful", Toast.LENGTH_SHORT).show();
                        } else
                            Log.e("addAlarm", "Error Updating");
                        //Toast.makeText(getActivity(),"h:"+hourOfDay+" m:"+minute,Toast.LENGTH_SHORT).show();
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false);
                tpd.show();
            }
        });

        listView = (ListView) view.findViewById(R.id.alarmList);
        alarmListAdapter = new AlarmListAdapter(getActivity(), this, familyData, medicineData);
        listView.setAdapter(alarmListAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > lastVisibleItem && !isScrollingDown) {
                    isScrollingDown = true;
                    fabAddAlarm.setVisibility(View.GONE);
                    //lastVisibleItem = firstVisibleItem;
                } else if (firstVisibleItem < lastVisibleItem && isScrollingDown) {
                    fabAddAlarm.setVisibility(View.VISIBLE);
                    isScrollingDown = false;
                    //lastVisibleItem = firstVisibleItem;
                }
                lastVisibleItem = firstVisibleItem;
            }
        });

        checkAddAlarmText();

        return view;
    }

    public void checkAddAlarmText() {
        int count = alarmListAdapter.getCount();
        if (count == 0)
            fabAddAlarmText.setVisibility(View.VISIBLE);
        else
            fabAddAlarmText.setVisibility(View.GONE);
    }


}
