package com.akshenkadakia.homeutility.medicine;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ListView;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.home.Utils;
import com.akshenkadakia.homeutility.medicine.adapters.DayListAdapter;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.DayData;

import java.util.ArrayList;

public class DayListActivity extends AppCompatActivity {
    String day;
    ListView listView;
    DayListAdapter dayListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);
        day=getIntent().getStringExtra("day");
        //Log.e("day",day);
        setTitle(day);
        day=day.toLowerCase();
        //ProgressDialog pd=new ProgressDialog(this);
        //pd.show();

        DBOpenHelper db= DBOpenHelper.getInstance(this);
        ArrayList<DayData> dayDatas=db.getDayData(day);

        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 46, r.getDisplayMetrics());
        Utils utils=new Utils(this);
        int w=utils.getScreenWidth();
        int width= (int) (((w - padding) / 3)*0.95);

        listView=(ListView)findViewById(R.id.dayMemberList);
        dayListAdapter=new DayListAdapter(this,dayDatas,width,day);
        listView.setAdapter(dayListAdapter);


        //pd.dismiss();
    }
}
