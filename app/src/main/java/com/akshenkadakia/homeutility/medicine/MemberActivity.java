package com.akshenkadakia.homeutility.medicine;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.akshenkadakia.homeutility.R;

import java.util.Locale;

public class MemberActivity extends AppCompatActivity {

    MenuItem actionDays, actionList;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        String lo=sp.getString(getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        getSupportActionBar().setTitle(R.string.members);
        getFragmentManager().beginTransaction().add(R.id.medicineFragmentHolder, new FamilyFragment()).commit();
    }

    @Override
    protected void onResume() {
        //getSupportActionBar().setTitle("Members");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_switch, menu);
        this.menu = menu;
        actionDays = menu.findItem(R.id.action_days);
        actionList = menu.findItem(R.id.action_list);

        actionDays.setVisible(true);
        actionList.setVisible(false);
        return true;
    }

    public void toggle() {
        actionDays.setVisible(!actionDays.isVisible());
        actionList.setVisible(!actionList.isVisible());
        if(actionDays.isVisible())
            getSupportActionBar().setTitle(R.string.members);
        else
            getSupportActionBar().setTitle(R.string.days);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                try {
                    FragmentManager fm=this.getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.gla_back_gone, R.anim.gla_back_come, R.anim.gla_back_gone, R.anim.gla_back_come);
                    ft.replace(R.id.medicineFragmentHolder, new FamilyFragment());
                    //ft.addToBackStack(null);
                    ft.commit();
                    //fm.popBackStack();
                } catch (Exception e) {
                    Log.e("Ex:", e.toString());
                }
                toggle();
                return true;

            case R.id.action_days:
                try {
                    FragmentManager fm=this.getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.gla_back_gone, R.anim.gla_back_come, R.anim.gla_back_gone, R.anim.gla_back_come);
                    ft.replace(R.id.medicineFragmentHolder, new DayListFragment());
                    //ft.addToBackStack(null);
                    ft.commit();
                    //fm.popBackStack();
                }catch(Exception e){
                    Log.e("Ex:",e.toString());
                }

                toggle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
