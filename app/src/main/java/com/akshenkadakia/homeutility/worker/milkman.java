package com.akshenkadakia.homeutility.worker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import com.akshenkadakia.homeutility.R;

public class milkman extends AppCompatActivity {

    ActionBar actionBar;
    SharedPreferences pref;
    private EditText e2;
    private EditText e3;
    private Button btn;
    private Button btn2;
    private Button btn3;
    private  Button btn4;
    private TextView t;
    private TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.milkman);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        setTitle("Milkman");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref=getSharedPreferences("Simplify", Context.MODE_PRIVATE);
        e2= (EditText) findViewById(R.id.editText2);
        e3= (EditText) findViewById(R.id.editText3);
        t = (TextView) findViewById(R.id.textView);
        t2 = (TextView) findViewById(R.id.textView4);

        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String mname="";
        switch(month)
        {
            case 0:mname="January";
                break;
            case 1:mname="February";
                break;
            case 2:mname="March";
                break;
            case 3:mname="April";
                break;
            case 4:mname="May";
                break;
            case 5:mname="June";
                break;
            case 6:mname="July";
                break;
            case 7:mname="August";
                break;
            case 8:mname="September";
                break;
            case 9:mname="October";
                break;
            case 10:mname="November";
                break;
            case 11:mname="December";
                break;

        }

        t2.setText(day+" "+mname+","+year);
        e2.setFocusable(true);
        btn= (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String q=e2.getText().toString().trim();
                String c=e3.getText().toString().trim();
                int cost=(Integer.parseInt(c))*(Integer.parseInt(q));
                t.setText(cost+"");
                SharedPreferences.Editor editor=pref.edit();
                int val=pref.getInt("MilkCost",0);
                editor.putInt("MilkCost",(val+cost));
                editor.commit();
                Log.e("MilkCost",pref.getInt("MilkCost",0)+" ");
            }
        });

    }
}

