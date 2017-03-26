package com.akshenkadakia.homeutility.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.akshenkadakia.homeutility.R;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        String lo=sp.getString(getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void changeL(View view){
        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(this);
        alertBuilder.setIcon(R.mipmap.ic_launcher);
        alertBuilder.setTitle("Select Language");
        View dialogView=getLayoutInflater().inflate(R.layout.locale_dialog,null);
        alertBuilder.setView(dialogView);
        final RadioButton english=(RadioButton)dialogView.findViewById(R.id.english);
        final RadioButton hindi=(RadioButton)dialogView.findViewById(R.id.hindi);
        final RadioButton marathi=(RadioButton)dialogView.findViewById(R.id.marathi);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        String lo=sp.getString(getString(R.string.prefLocale),"en");
        if(lo.equalsIgnoreCase("en"))
            english.setChecked(true);
        else if(lo.equalsIgnoreCase("hi"))
            hindi.setChecked(true);
        else if(lo.equalsIgnoreCase("mr"))
            marathi.setChecked(true);


        alertBuilder.setPositiveButton(R.string.ok,null);
        final AlertDialog ad=alertBuilder.create();

        ad.setOnShowListener(new DialogInterface.OnShowListener(){
            @Override
            public void onShow(DialogInterface dialog) {
                Button b=ad.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(english.isChecked())
                            setLocale("en");
                        else if(hindi.isChecked())
                            setLocale("hi");
                        else if(marathi.isChecked())
                            setLocale("mr");
                        ad.dismiss();
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
            }
        });
        ad.show();
    }
    public void setLocale(String lo){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.prefLocale),lo);
        editor.commit();
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
