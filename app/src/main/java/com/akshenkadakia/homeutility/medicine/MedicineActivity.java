package com.akshenkadakia.homeutility.medicine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;

import java.util.Locale;


public class MedicineActivity extends AppCompatActivity {

    private FamilyData familyData;

    public FamilyData getFamilyData() {
        return familyData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        Intent intent=getIntent();
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        String lo=sp.getString(getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        familyData=new FamilyData(intent.getStringExtra("name"),intent.getBooleanExtra("notifications", false)
                ,intent.getIntExtra("fid",0),intent.getIntExtra("ai",-1));

        getSupportActionBar().setTitle(getString(R.string.member)+":" + familyData.getName());

        MedicineFragment mf=new MedicineFragment();
        mf.setFamilyData(familyData);
        getFragmentManager().beginTransaction().add(R.id.medicineAlarmFragmentContainer, mf).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        }
        else
            super.onBackPressed();
    }
}
