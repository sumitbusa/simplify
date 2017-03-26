package com.akshenkadakia.homeutility.medicine.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.FamilyFragment;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;

import java.util.ArrayList;
import java.util.Locale;

public class FamilyListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<FamilyData> arrayList;
    private AlertDialog alertDialog;
    private LayoutInflater layoutInflater=null;
    private DBOpenHelper mDB;
    private EditText newName;
    private Switch notiSwitch;
    private FamilyFragment familyFragment;

    public FamilyListAdapter(Activity activity, FamilyFragment medicineFragment){
        this.activity=activity;
        mDB=DBOpenHelper.getInstance(activity.getBaseContext());
        this.familyFragment=medicineFragment;
        this.arrayList=mDB.getAllMembers();
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(activity);
        String lo=sp.getString(activity.getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
        layoutInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public FamilyData getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<FamilyData> getArrayList(){
        return arrayList;
    }
    @Override
    public void notifyDataSetChanged() {
        arrayList=mDB.getAllMembers();
        super.notifyDataSetChanged();
    }

    private class Holder{
        ImageView removeButton,editButton;
        TextView memberName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View view;

        if(convertView==null)
            view=(View)layoutInflater.inflate(R.layout.family_list_item,null);
        else
            view=(View)convertView;
        holder.removeButton=(ImageView)view.findViewById(R.id.memberItemRemove);
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                ab.setTitle(R.string.remove);
                FamilyData mn = getItem(position);
                ab.setIcon(R.drawable.ic_local_hospital_black_48dp);
                ab.setMessage(R.string.sureRemove+" " + mn.getName() + "?");
                ab.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FamilyData mn = getItem(position);
                        mDB.deleteMember(mn);
                        notifyDataSetChanged();
                        familyFragment.checkAddMemberText();
                        Toast.makeText(activity, R.string.removed+" " + mn.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                ab.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog = ab.create();
                alertDialog.show();
            }
        });
        holder.removeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast t = Toast.makeText(activity, R.string.remove, Toast.LENGTH_SHORT);
                t.show();
                return true;
            }
        });

        holder.memberName=(TextView)view.findViewById(R.id.memberItemName);
        holder.memberName.setText(arrayList.get(position).getName());

        holder.editButton=(ImageView)view.findViewById(R.id.memberItemEdit);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyData mn = getItem(position);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
                alertBuilder.setTitle(R.string.edit+" " + mn.getName());
                alertBuilder.setIcon(R.drawable.ic_local_hospital_black_48dp);

                View dialogView = layoutInflater.inflate(R.layout.alert_dialog_add_new_member, null);
                alertBuilder.setView(dialogView);
                newName = (EditText) dialogView.findViewById(R.id.newMemberDialogName);
                newName.setText(mn.getName());
                notiSwitch = (Switch) dialogView.findViewById(R.id.newMemberDialogNotificationSwitch);
                notiSwitch.setChecked(mn.isNotifications());
                alertBuilder.setPositiveButton(R.string.update, null);

                alertBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog ad = alertBuilder.create();
                ad.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


                ad.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button b = ad.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String newMemName = newName.getText().toString().trim();
                                //Log.e("name", ":" + newMemName);
                                if (newMemName != null && !newMemName.equals("")) {
                                    FamilyData x=getItem(position);
                                    FamilyData mn = new FamilyData(newMemName,notiSwitch.isChecked(),x.getFid(),x.getAi());

                                    mDB = DBOpenHelper.getInstance(activity.getBaseContext());
                                    //Log.e("updating:", newMemName + ":" + mn.isNotificationStatus());
                                    int i = mDB.updateMember(mn);

                                    if (i > 0) {
                                        notifyDataSetChanged();
                                        ad.dismiss();
                                        Toast.makeText(activity, R.string.update+" "+R.string.successful, Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(activity, R.string.error+" "+R.string.updating, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, R.string.enterName, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                ad.show();
            }
        });
        holder.editButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast t = Toast.makeText(activity, R.string.edit, Toast.LENGTH_SHORT);
                t.show();
                return true;
            }
        });

        return view;
    }


}
