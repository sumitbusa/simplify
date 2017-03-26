package com.akshenkadakia.homeutility.emergency;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.akshenkadakia.homeutility.R;

import java.util.Locale;

public class EmergencyActivity extends AppCompatActivity {
    ToggleButton emergenButton;
    RelativeLayout emergenSettings;
    EditText n1,n2,n3;
    CheckBox c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        String lo=sp.getString(getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        emergenButton=(ToggleButton)findViewById(R.id.emergencyButton);
        RelativeLayout rl=(RelativeLayout)findViewById(R.id.emergencyToggleBar);
        emergenSettings=(RelativeLayout)findViewById(R.id.emergencySettings);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergenButton.toggle();
                setPref();
            }
        });

        emergenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPref();
            }
        });

        //SharedPreferences sp=this.getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(EmergencyActivity.this);
        emergenButton.setChecked(sp.getBoolean(getString(R.string.prefEmerMode),true));

        n1=(EditText)findViewById(R.id.number1Input);
        n2=(EditText)findViewById(R.id.number2Input);
        n3=(EditText)findViewById(R.id.number3Input);
        n1.setText(sp.getString(getString(R.string.prefn1),""));
        n2.setText(sp.getString(getString(R.string.prefn2),""));
        n3.setText(sp.getString(getString(R.string.prefn3),""));

        c=(CheckBox)findViewById(R.id.contactEmergency);
        c.setChecked(sp.getBoolean(getString(R.string.prefCall102),false));

        n1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10){
                    //SharedPreferences sp=getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(EmergencyActivity.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.prefn1), s.toString());
                    editor.commit();
                }
                else if(s.length()==0){
                    SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(EmergencyActivity.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.prefn1), null);
                    editor.commit();
                }
            }
        });
        n2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10){
                    //SharedPreferences sp=getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(EmergencyActivity.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.prefn2), s.toString());
                    editor.commit();
                }
                else if(s.length()==0){
                    SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(EmergencyActivity.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.prefn2), null);
                    editor.commit();
                }
            }
        });
        n3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10){
                    //SharedPreferences sp=getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(EmergencyActivity.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.prefn3), s.toString());
                    editor.commit();
                }
                else if(s.length()==0){
                    SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(EmergencyActivity.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.prefn3), null);
                    editor.commit();
                }
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences sp=getPreferences(Context.MODE_PRIVATE);
                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(EmergencyActivity.this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(getString(R.string.prefCall102), c.isChecked());
                editor.commit();
            }
        });
        setPref();
    }

    public void setPref(){
        c.setEnabled(emergenButton.isChecked());
        n1.setEnabled(emergenButton.isChecked());
        n2.setEnabled(emergenButton.isChecked());
        n3.setEnabled(emergenButton.isChecked());
        emergenSettings.setEnabled(emergenButton.isChecked());
        //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.prefEmerMode), emergenButton.isChecked());
        editor.commit();
    }
}
