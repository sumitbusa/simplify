package com.akshenkadakia.homeutility.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.calender.MainActivity;
import com.akshenkadakia.homeutility.emergency.EmergencyActivity;
import com.akshenkadakia.homeutility.medicine.MemberActivity;
import com.akshenkadakia.homeutility.newspaper.InitActivity;

import java.util.ArrayList;
import java.util.Locale;

public class HomeGridAdapter extends BaseAdapter{

    Activity activity;
    private ArrayList<HomeMenu> arrayList;
    private LayoutInflater layoutInflater=null;
    private int columnWidth,rowHeight;

    public HomeGridAdapter(Activity activity, int columnWidth, int rowHeight){
        this.activity=activity;
        this.columnWidth=columnWidth;
        this.rowHeight=rowHeight;

        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(activity);
        String lo=sp.getString(activity.getString(R.string.prefLocale),"en");
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
        layoutInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        arrayList=new ArrayList<>();
        String[] name={activity.getString(R.string.calender),
                activity.getString(R.string.workers),
                activity.getString(R.string.medicines),
                activity.getString(R.string.emergency),
                activity.getString(R.string.news),
                activity.getString(R.string.settings)};
        //activity.getResources().getStringArray(R.array.fragmentTitle);
        TypedArray id=activity.getResources().obtainTypedArray(R.array.fragmentIcon);
        for (int i=0;i<name.length;i++) {
            arrayList.add(new HomeMenu(name[i],id.getResourceId(i,0)));
        }
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View view;

        if(convertView==null)
            view = layoutInflater.inflate(R.layout.home_grid_item, null);
        else
            view=convertView;
        holder.rl = (RelativeLayout) view.findViewById(R.id.homeGridItemContainer);
        holder.rl.setLayoutParams(new GridView.LayoutParams(columnWidth, rowHeight));
        holder.textView = (TextView) view.findViewById(R.id.homeGridText);
        holder.imageView = (ImageView) view.findViewById(R.id.homeGridImage);
        holder.textView.setText(arrayList.get(position).optionName);
        holder.imageView.setImageResource(arrayList.get(position).iconId);

        int imageWidth,imageHeight;
        imageWidth=(int)(columnWidth*0.6);
        imageHeight=(int)(rowHeight*0.6);
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(imageWidth,imageHeight);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        holder.imageView.setLayoutParams(lp);

        view.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.e("Clicked", arrayList.get(position).optionName);
                HomeMenu clickedItem=arrayList.get(position);
                try {
                    if (clickedItem.optionName.equalsIgnoreCase(activity.getString(R.string.medicines))) {
                        Intent intent = new Intent(activity, MemberActivity.class);
                        activity.startActivity(intent);
                    }
                    else if(clickedItem.optionName.equalsIgnoreCase(activity.getString(R.string.emergency))){
                        Intent intent=new Intent(activity, EmergencyActivity.class);
                        activity.startActivity(intent);
                    }
                    else if(clickedItem.optionName.equalsIgnoreCase(activity.getString(R.string.calender))){
                        Intent intent=new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                    }
                    else if(clickedItem.optionName.equalsIgnoreCase(activity.getString(R.string.settings))){
                        Intent intent=new Intent(activity, SettingActivity.class);
                        activity.startActivity(intent);
                    }
                    else if(clickedItem.optionName.equalsIgnoreCase(activity.getString(R.string.news))){
                        Intent intent=new Intent(activity, InitActivity.class);
                        activity.startActivity(intent);
                    }
                    else if(clickedItem.optionName.equalsIgnoreCase(activity.getString(R.string.workers))){
                        Intent intent=new Intent(activity, com.akshenkadakia.homeutility.worker.MainActivity.class);
                        activity.startActivity(intent);
                    }
                }catch(Exception e){
                    Log.e("Ex:", e.toString());
                }
            }
        });

        return view;
    }

    private class Holder{
        RelativeLayout rl;
        ImageView imageView;
        TextView textView;
    }

    //Menu item Pair
    private class HomeMenu{
        String optionName;
        int iconId;
        HomeMenu(){
            optionName=null;
            iconId=0;
        }
        HomeMenu(String optionName,int iconId){
            this.optionName=optionName;
            this.iconId=iconId;
        }
    }
}
