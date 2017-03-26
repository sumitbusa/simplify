package com.akshenkadakia.homeutility.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import com.akshenkadakia.homeutility.R;

public class ironman extends AppCompatActivity {

    SharedPreferences pref;
    private TextView t1;
    private EditText e1;
    private EditText e2;
    private Button btn;
    private TextView t2;
    Toolbar toolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ironman);

        pref=getSharedPreferences("Simplify", Context.MODE_PRIVATE);
        t1 = (TextView) findViewById(R.id.textView2);
        t2 = (TextView) findViewById(R.id.textView3);
        e1= (EditText) findViewById(R.id.editText);
        e2= (EditText) findViewById(R.id.editText4);
        btn= (Button) findViewById(R.id.button2);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        setTitle("Ironman");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        t1.setText(day+" "+mname+","+year);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q=e1.getText().toString().trim();
                String c=e2.getText().toString().trim();
                int cost=(Integer.parseInt(c))*(Integer.parseInt(q));
                t2.setText(cost+"");
                SharedPreferences.Editor editor=pref.edit();
                int val=pref.getInt("IronCost",0);
                editor.putInt("IronCost",(val+cost));
                editor.commit();
                Log.e("IronCost",pref.getInt("IronCost",0)+" ");
            }
        });

    }
}
