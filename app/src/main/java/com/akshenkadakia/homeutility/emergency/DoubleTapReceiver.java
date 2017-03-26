package com.akshenkadakia.homeutility.emergency;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.akshenkadakia.homeutility.R;

/**
 * Created by AkshanK on 9/30/2016.
 */

public class DoubleTapReceiver extends BroadcastReceiver {
    Context cntx;
    Vibrator vibe;
    long DIFF=500;
    long a,seconds_screenoff,seconds_screenon,OLD_TIME,actual_diff,diffrence;
    boolean OFF_SCREEN,ON_SCREEN,sent_msg;
    @Override
    public void onReceive(Context context, final Intent intent) {
        cntx = context;
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Log.v("onReceive", "Power button is pressed.");

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                a = System.currentTimeMillis();
                seconds_screenoff = a;
                OLD_TIME = seconds_screenoff;
                OFF_SCREEN = true;

                new CountDownTimer(5000, 200) {
                    public void onTick(long millisUntilFinished) {
                        if (ON_SCREEN) {
                            if (seconds_screenon != 0 && seconds_screenoff != 0) {
                                actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                                if (actual_diff <= DIFF) {
                                    sent_msg = true;
                                    if (sent_msg) {
                                        //Toast.makeText(cntx, "POWER BUTTON CLICKED 2 TIMES", Toast.LENGTH_LONG).show();
                                        vibe.vibrate(100);
                                        seconds_screenon = 0L;
                                        seconds_screenoff = 0L;
                                        sent_msg = false;
                                        Intent intent1=new Intent(cntx,EmergencyDialog.class);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        cntx.startActivity(intent1);
                                    }
                                } else {
                                    seconds_screenon = 0L;
                                    seconds_screenoff = 0L;

                                }
                            }
                        }
                    }

                    public void onFinish() {
                        seconds_screenoff = 0L;
                    }
                }.start();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                a = System.currentTimeMillis();
                seconds_screenon = a;
                OLD_TIME = seconds_screenoff;
                new CountDownTimer(5000, 200) {
                    public void onTick(long millisUntilFinished) {
                        if (OFF_SCREEN) {
                            if (seconds_screenon != 0 && seconds_screenoff != 0) {
                                actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                                if (actual_diff <= DIFF) {
                                    sent_msg = true;
                                    if (sent_msg) {
                                        //Toast.makeText(cntx, "POWER BUTTON CLICKED 2 TIMES", Toast.LENGTH_LONG).show();
                                        vibe.vibrate(100);
                                        seconds_screenon = 0L;
                                        seconds_screenoff = 0L;
                                        sent_msg = false;
                                        Intent intent1=new Intent(cntx,EmergencyDialog.class);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        cntx.startActivity(intent1);
                                    }
                                } else {
                                    seconds_screenon = 0L;
                                    seconds_screenoff = 0L;
                                }
                            }
                        }

                    }

                    public void onFinish() {

                        seconds_screenon = 0L;
                    }
                }.start();


            }
        }


    private long cal_diff(long seconds_screenon2, long seconds_screenoff2) {
        if (seconds_screenon2 >= seconds_screenoff2) {
            diffrence = (seconds_screenon2) - (seconds_screenoff2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        } else {
            diffrence = (seconds_screenoff2) - (seconds_screenon2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        }

        return diffrence;
    }
}
