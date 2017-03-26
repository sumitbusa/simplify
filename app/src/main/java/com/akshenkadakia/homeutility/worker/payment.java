package com.akshenkadakia.homeutility.worker;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.akshenkadakia.homeutility.R;

public class payment extends AppCompatActivity {

    SharedPreferences pref;
    private TextView t1;
    private TextView t2;
    private Button b1;
    private Button b2;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        setTitle("Payment");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b1= (Button) findViewById(R.id.button3);
        b2= (Button) findViewById(R.id.button4);
        t1= (TextView) findViewById(R.id.textView5);
        t2= (TextView) findViewById(R.id.textView6);
        pref=getSharedPreferences("Simplify", Context.MODE_PRIVATE);
        int milkcost = pref.getInt("MilkCost",0);
        int ironcost = pref.getInt("IronCost",0);
        t1.setText(milkcost+"");
        t2.setText(ironcost+"");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.setText(0+"");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t2.setText(0+"");
            }
        });
    }
}
