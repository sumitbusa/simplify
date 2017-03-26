package com.akshenkadakia.homeutility.emergency;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import com.akshenkadakia.homeutility.R;

import java.util.Locale;

public class EmergencyDialog extends Activity {
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_dialog);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        String lo=sp.getString(getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        //SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        boolean mode=sp.getBoolean(getString(R.string.prefEmerMode),true);
        Log.e("mode",""+mode);
        if(mode) {
            GPSTracker gps = new GPSTracker(this);
            if (gps.canGetLocation()) {
                final double latitude = gps.getLatitude(),
                        longitude = gps.getLongitude();

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
                builder.setTitle(getString(R.string.call102));
                builder.setMessage(getString(R.string.call102message));
                builder.setNegativeButton(R.string.no, null);
                builder.setPositiveButton(R.string.yes, null);

                alertDialog = builder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button pos = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        pos.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(EmergencyDialog.this);
                                String msg = "Help Me! my location is https://www.google.co.in/maps/search/" + latitude + "," + longitude;
                                String n1 = sp.getString(getString(R.string.prefn1), null);
                                if (n1 != null)
                                    sendMessage(n1, msg);
                                String n2 = sp.getString(getString(R.string.prefn2), null);
                                if (n2 != null)
                                    sendMessage(n2, msg);
                                String n3 = sp.getString(getString(R.string.prefn3), null);
                                if (n3 != null)
                                    sendMessage(n3, msg);
                                boolean c = sp.getBoolean(getString(R.string.prefCall102), false);
                                if (c) {
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:9833781447"));//102"));
                                    try {
                                        startActivity(callIntent);
                                    } catch (Exception e) {
                                        Log.e("emergency", "call fail");
                                    }
                                }
                                alertDialog.dismiss();
                                finish();
                            }
                        });
                        Button neg = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        neg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                finish();
                            }
                        });
                    }
                });
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        alertDialog.dismiss();
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
    }

    public void sendMessage(String phoneNo, String msg){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            smsManager=null;
            //Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            //Toast.makeText(getApplicationContext(),ex.getMessage().toString(),Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
