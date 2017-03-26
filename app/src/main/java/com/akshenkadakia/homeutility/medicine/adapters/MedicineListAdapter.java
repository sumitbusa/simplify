package com.akshenkadakia.homeutility.medicine.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.MedicineFragment;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;
import com.akshenkadakia.homeutility.medicine.data.MedicineData;

import java.util.ArrayList;
import java.util.Locale;

public class MedicineListAdapter extends BaseAdapter {
    private Activity activity;
    private FamilyData familyData;
    private AlertDialog alertDialog;
    private LayoutInflater layoutInflater = null;
    private DBOpenHelper mDB;
    private EditText newName;
    private MedicineFragment memberMedicineFragment;
    private ArrayList<MedicineData> medicineDatas;
    static long DELAY_LONG = R.integer.delayLong;

    private class NumberPicker {
        int MIN_VALUE = 0;
        int MAX_VALUE = 999;
        boolean toggler = false, once;
        Repeat r = new Repeat();
        LinearLayout linearLayout;
        Button decrement;
        TextView value;
        int val = 0;
        boolean autoIncrement;
        boolean autoDecrement;
        Button increment;

        public void setListener() {
            decrement.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    autoDecrement = true;
                    new Handler().postDelayed(r, DELAY_LONG);
                    return false;
                }
            });
            decrement.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    if (action == MotionEvent.ACTION_UP && autoDecrement)
                        autoDecrement = false;
                    return false;
                }
            });
            increment.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    autoIncrement = true;
                    new Handler().postDelayed(r, DELAY_LONG);
                    return false;
                }
            });
            increment.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    if (action == MotionEvent.ACTION_UP && autoIncrement)
                        autoIncrement = false;
                    return false;
                }
            });
        }

        public void updateValue() {
            value.setText(String.valueOf(val));
        }

        public void setDecrement() {
            if (val > MIN_VALUE)
                val--;
            if (toggler && val == MIN_VALUE) {
                tablets.linearLayout.setVisibility(View.GONE);
            }
            updateValue();
        }

        public void setIncrement() {
            if (val < MAX_VALUE)
                val++;
            if (toggler && val == MIN_VALUE + 1) {
                tablets.val = 1;
                tablets.updateValue();
                tablets.linearLayout.setVisibility(View.VISIBLE);
                if (!once) {
                    ViewTreeObserver vto = tablets.linearLayout.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            tablets.linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            int e = (tablets.linearLayout.getMeasuredWidth() - tablets.linearLayout.findViewById(R.id.tvTablets).getWidth()) / 3;
                            tablets.decrement.getLayoutParams().width = e;
                            tablets.value.getLayoutParams().width = e;
                            tablets.increment.getLayoutParams().width = e;
                            once = true;
                        }
                    });
                }
            }
            updateValue();
        }

        void checkToggler() {
            if (toggler) {
                tablets.updateValue();
                tablets.linearLayout.setVisibility(View.VISIBLE);
                ViewTreeObserver vto = tablets.linearLayout.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        tablets.linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        int e = (tablets.linearLayout.getMeasuredWidth() - tablets.linearLayout.findViewById(R.id.tvTablets).getWidth()) / 3;
                        tablets.decrement.getLayoutParams().width = e;
                        tablets.value.getLayoutParams().width = e;
                        tablets.increment.getLayoutParams().width = e;
                        once = true;
                        if (packets.val == 0) {
                            tablets.linearLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }

        class Repeat implements Runnable {
            @Override
            public void run() {
                if (autoDecrement) {
                    setDecrement();
                    //Thread.sleep(DELAY_LONG);
                    new Handler().postDelayed(new Repeat(), DELAY_LONG);
                } else if (autoIncrement) {
                    setIncrement();
                    new Handler().postDelayed(new Repeat(), DELAY_LONG);
                }
            }
        }
    }

    private NumberPicker packets, tablets;


    public MedicineListAdapter(Activity activity, MedicineFragment memberMedicineFragment, FamilyData memberName) {
        this.activity = activity;
        this.memberMedicineFragment = memberMedicineFragment;
        this.familyData = memberName;
        mDB = DBOpenHelper.getInstance(activity.getBaseContext());
        medicineDatas = mDB.getAllMedicines(familyData.getFid());
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(activity);
        String lo=sp.getString(activity.getString(R.string.prefLocale),null);
        Locale locale = new Locale(lo);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public int getCount() {
        return medicineDatas.size();
    }

    @Override
    public MedicineData getItem(int position) {
        return medicineDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<MedicineData> getArrayList() {
        return medicineDatas;
    }

    @Override
    public void notifyDataSetChanged() {
        medicineDatas = mDB.getAllMedicines(familyData.getFid());
        super.notifyDataSetChanged();
    }

    private class Holder {
        ImageView removeButton, editButton;
        TextView memberName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View view;

        if (convertView == null)
            view = (View) layoutInflater.inflate(R.layout.family_list_item, null);
        else
            view = (View) convertView;
        holder.removeButton = (ImageView) view.findViewById(R.id.memberItemRemove);
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                ab.setTitle(R.string.remove);
                final MedicineData mn = getItem(position);
                ab.setIcon(R.drawable.ic_local_hospital_black_48dp);
                ab.setMessage(R.string.sureRemove+" " + mn.getName() + "?");
                ab.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int i = mDB.deleteMedicine(familyData.getFid(), medicineDatas.get(position).getMid());
                        if (i > 0) {
                            notifyDataSetChanged();
                            memberMedicineFragment.checkAddMemberText();
                            Toast.makeText(activity, R.string.removed+" " + mn.getName(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, R.string.error+" "+R.string.removing + mn.getName(), Toast.LENGTH_SHORT).show();
                        }
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

        holder.memberName = (TextView) view.findViewById(R.id.memberItemName);
        holder.memberName.setText(getItem(position).getName());

        holder.editButton = (ImageView) view.findViewById(R.id.memberItemEdit);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MedicineData mn = getItem(position);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
                alertBuilder.setTitle(R.string.edit+" " + mn.getName());
                alertBuilder.setIcon(R.drawable.ic_local_hospital_black_48dp);

                View dialogView = layoutInflater.inflate(R.layout.alert_dialog_add_new_medicine, null);
                alertBuilder.setView(dialogView);
                newName = (EditText) dialogView.findViewById(R.id.newMedicineDialogName);
                newName.setText(mn.getName());
                packets = new NumberPicker();
                packets.linearLayout = (LinearLayout) dialogView.findViewById(R.id.Packets);
                packets.val = mn.getPackets();
                packets.decrement = (Button) dialogView.findViewById(R.id.decrementPackets);
                packets.value = (TextView) dialogView.findViewById(R.id.textPackets);
                packets.decrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        packets.setDecrement();
                    }
                });
                packets.increment = (Button) dialogView.findViewById(R.id.incrementPackets);
                packets.increment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        packets.setIncrement();
                    }
                });
                packets.setListener();
                packets.toggler = true;


                tablets = new NumberPicker();
                tablets.linearLayout = (LinearLayout) dialogView.findViewById(R.id.Tablets);
                tablets.MIN_VALUE = 1;
                tablets.val = mn.getTablets();
                tablets.decrement = (Button) dialogView.findViewById(R.id.decrementTablets);
                tablets.value = (TextView) dialogView.findViewById(R.id.textTablets);
                tablets.decrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tablets.setDecrement();
                    }
                });
                tablets.increment = (Button) dialogView.findViewById(R.id.incrementTablets);
                tablets.increment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tablets.setIncrement();
                    }
                });
                tablets.setListener();
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
                        int w = (packets.linearLayout.getWidth() - ad.findViewById(R.id.tvPackets).getWidth()) / 3;
                        packets.decrement.getLayoutParams().width = w;
                        packets.value.getLayoutParams().width = w;
                        packets.increment.getLayoutParams().width = w;
                        packets.updateValue();
                        packets.checkToggler();

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String newMemName = newName.getText().toString().trim();
                                if (newMemName != null && !newMemName.equals("")) {
                                    MedicineData medicineData = new MedicineData();
                                    medicineData.setFid(mn.getFid());
                                    medicineData.setMid(mn.getMid());
                                    medicineData.setName(newMemName);
                                    medicineData.setPackets(packets.val);
                                    medicineData.setTablets(tablets.val);
                                    medicineData.setTotal(medicineData.getPackets() * medicineData.getTablets());
                                    medicineData.setLeft(medicineData.getTotal());
                                    medicineData.setColor(medicineData.getColor());
                                    medicineData.setShape(medicineData.getShape());

                                    int i = mDB.updateMedicine(medicineData);

                                    if (i > 0) {
                                        notifyDataSetChanged();
                                        ad.dismiss();
                                        Toast.makeText(activity, R.string.update+" "+R.string.successful, Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(activity, R.string.error+" "+R.string.updating, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, R.string.enterMedName, Toast.LENGTH_SHORT).show();
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
