package com.akshenkadakia.homeutility.medicine.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.DayData;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;

public class DayListAdapter extends BaseAdapter {
    ArrayList<DayData> dayDatas;
    Activity activity;
    LayoutInflater layoutInflater;
    int width;
    String day;

    public DayListAdapter(Activity activity, ArrayList<DayData> dayDatas, int width, String day) {
        this.activity = activity;
        this.layoutInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dayDatas = dayDatas;
        this.width = width;
        this.day=day;
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(activity);
        String lo=sp.getString(activity.getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void notifyDataSetChanged() {
        dayDatas= DBOpenHelper.getInstance(activity).getDayData(day);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dayDatas.size();
    }

    @Override
    public DayData getItem(int position) {
        return dayDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null)
            view = (View) convertView;
        else
            view = (View) layoutInflater.inflate(R.layout.day_list_item, null);

        if(position==0){
            Holder holder = new Holder();
            holder.time = (TextView) view.findViewById(R.id.dayListTime);
            holder.time.setText(R.string.time);
            holder.time.setTypeface(Typeface.DEFAULT_BOLD);
            holder.member = (TextView) view.findViewById(R.id.dayListMember);
            holder.member.setText(R.string.member);
            holder.member.setTypeface(Typeface.DEFAULT_BOLD);
            holder.medicine = (TextView) view.findViewById(R.id.dayListMedicine);
            holder.medicine.setText(R.string.medicine);
            holder.medicine.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else {
            DayData dayData = getItem(position);

            Holder holder = new Holder();

            holder.time = (TextView) view.findViewById(R.id.dayListTime);
            Time t = dayData.getAlarmData().getTime();
            int h = t.getHours();
            int m = t.getMinutes();
            String h1, m1 = "" + m;
            String ampm;

            if (h == 0) {
                h1 = "12";
                ampm = "AM";
            } else if (h < 12) {
                if (h < 10)
                    h1 = "0" + h;
                else
                    h1 = "" + h;
                ampm = "AM";
            } else {
                h1 = "" + (h % 12);
                ampm = "PM";
            }
            if (m < 10)
                m1 = "0" + m;

            String time = h1 + ":" + m1 + " " + ampm;
            holder.time.setText(time);

            holder.member = (TextView) view.findViewById(R.id.dayListMember);
            holder.member.setText(dayData.getMember());

            holder.medicine = (TextView) view.findViewById(R.id.dayListMedicine);
            holder.medicine.setText(dayData.getMedicine());
        }
        ViewGroup vg = (ViewGroup) view;
        for (int i = 0; i < vg.getChildCount(); i = i + 2) {
            View v=vg.getChildAt(i);
            int height = vg.getChildAt(i).getLayoutParams().height;
            v.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        }

        return view;
    }

    private class Holder {
        TextView time, member, medicine;
    }
}
