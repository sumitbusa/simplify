package com.akshenkadakia.homeutility.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.emergency.DoubleTapService;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    static final int MY_REQUEST=10;
    private GridView gridView;
    private int gridColumnWidth;
    private int gridColumnHeight;
    private final int GRID_COLUMN_NUMBER=2,GRID_PADDING=4,GRID_ROW_NUMBER=3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(getApplicationContext(), DoubleTapService.class));
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST);
        }

        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        String lo=sp.getString(getString(R.string.prefLocale),null);
        Log.e("lang",""+lo);
        if(lo==null){
            AlertDialog.Builder alertBuilder=new AlertDialog.Builder(this);
            alertBuilder.setIcon(R.mipmap.ic_launcher);
            alertBuilder.setTitle("Select Language");
            View dialogView=getLayoutInflater().inflate(R.layout.locale_dialog,null);
            alertBuilder.setView(dialogView);
            final RadioButton english=(RadioButton)dialogView.findViewById(R.id.english);
            final RadioButton hindi=(RadioButton)dialogView.findViewById(R.id.hindi);
            final RadioButton marathi=(RadioButton)dialogView.findViewById(R.id.marathi);

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
                            onRestart();
                            ad.dismiss();
                        }
                    });
                }
            });
            ad.show();
        }
        else
            setLocale(lo);
    }

    public void setLocale(String lo){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.prefLocale),lo);
        editor.commit();
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();//new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        //initialize
        getSupportActionBar().setTitle(R.string.app_name);
        gridView=(GridView)findViewById(R.id.gridView);
        InitializeGridLayout();
        gridView.setAdapter(new HomeGridAdapter(this,gridColumnWidth,gridColumnHeight));
    }

    @Override
    protected void onResume() {
        getSupportActionBar().setTitle(R.string.app_name);
        super.onResume();
    }

    private void InitializeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                GRID_PADDING, r.getDisplayMetrics());

        Utils utils=new Utils(this);
        gridColumnWidth = (int) ((utils.getScreenWidth() - ((GRID_COLUMN_NUMBER + 1) * padding)) / GRID_COLUMN_NUMBER);
        gridColumnHeight= (int) (((utils.getScreenHeight() - ((GRID_ROW_NUMBER + 1) * padding)) / GRID_ROW_NUMBER)*0.85);


        gridView.setNumColumns(GRID_COLUMN_NUMBER);
        gridView.setColumnWidth(gridColumnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exit)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(R.mipmap.ic_launcher)
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setMessage(R.string.confirmExit)
                    .show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case MY_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, MY_REQUEST);
                }
                return;
        }
    }
}
