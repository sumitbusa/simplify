package com.akshenkadakia.homeutility.medicine;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.adapters.MedicineListAdapter;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;
import com.akshenkadakia.homeutility.medicine.data.MedicineData;


/**
 * A simple {@link Fragment} subclass.
 */
public class MedicineFragment extends Fragment {

    FamilyData familyData;
    private ListView listView;
    private EditText newName;

    static long DELAY_LONG=(long) R.integer.delayLong;

    private class NumberPicker{
        int MIN_VALUE=0;
        int MAX_VALUE=999;
        boolean toggler=false,once;
        Repeat r=new Repeat();
        LinearLayout linearLayout;
        Button decrement;
        TextView value;
        int val=0;
        boolean autoIncrement;
        boolean autoDecrement;
        Button increment;
        public void setListener(){
            decrement.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    autoDecrement = true;
                    new Handler().postDelayed(new Repeat(), DELAY_LONG);
                    return true;
                }
            });
            decrement.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action=event.getAction();
                    if(action==MotionEvent.ACTION_UP && autoDecrement)
                        autoDecrement=false;
                    return false;
                }
            });
            increment.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    autoIncrement = true;
                    new Handler().postDelayed(new Repeat(), DELAY_LONG);
                    return true;
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
        public void updateValue(){
            value.setText(String.valueOf(val));
        }
        synchronized public void setDecrement(){
            if(val>MIN_VALUE)
                val--;
            if(toggler && val==MIN_VALUE){
                tablets.linearLayout.setVisibility(View.GONE);
            }
            updateValue();
        }
        synchronized public void setIncrement(){
            if(val<MAX_VALUE)
                val++;
            if(toggler && val==MIN_VALUE+1){
                tablets.val = 1;
                tablets.updateValue();
                tablets.linearLayout.setVisibility(View.VISIBLE);
                if(!once) {
                    ViewTreeObserver vto = tablets.linearLayout.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            tablets.linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            int e = (tablets.linearLayout.getMeasuredWidth() - tablets.linearLayout.findViewById(R.id.tvTablets).getWidth()) / 3;
                            tablets.decrement.getLayoutParams().width = e;
                            tablets.value.getLayoutParams().width = e;
                            tablets.increment.getLayoutParams().width = e;
                            packets.once = true;
                        }
                    });
                }
            }
            updateValue();
        }

        class Repeat implements Runnable{
            @Override
            public void run() {
                if(autoDecrement) {
                    setDecrement();
                    new Handler().postDelayed(new Repeat(), DELAY_LONG);
                }
                else if(autoIncrement){
                    setIncrement();
                    new Handler().postDelayed(new Repeat(), DELAY_LONG);
                }
            }
        }
    }

    private NumberPicker packets,tablets;
    private DBOpenHelper mDB;
    private TextView fabAddMemberText;
    private MedicineListAdapter medicineListAdapter;
    private int lastVisibleItem = 0;
    private boolean isScrollingDown = false;

    public void setFamilyData(FamilyData familyData) {
        this.familyData = familyData;
    }

    public MedicineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.member)+": " + familyData.getName());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_medicine, container, false);

        fabAddMemberText=(TextView)view.findViewById(R.id.fabAddMedicineText);
        final FloatingActionButton fabAddMember=(FloatingActionButton)view.findViewById(R.id.fabAddMedicine);
        fabAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder=new AlertDialog.Builder(getActivity());
                alertBuilder.setIcon(R.drawable.ic_local_hospital_black_48dp);
                alertBuilder.setTitle(getString(R.string.addNewMed));
                View dialogView=inflater.inflate(R.layout.alert_dialog_add_new_medicine,null);
                alertBuilder.setView(dialogView);
                newName = (EditText) dialogView.findViewById(R.id.newMedicineDialogName);
                packets=new NumberPicker();
                packets.linearLayout=(LinearLayout)dialogView.findViewById(R.id.Packets);
                packets.val=0;
                packets.decrement=(Button)dialogView.findViewById(R.id.decrementPackets);
                packets.value=(TextView)dialogView.findViewById(R.id.textPackets);
                packets.decrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        packets.setDecrement();
                    }
                });
                packets.increment=(Button)dialogView.findViewById(R.id.incrementPackets);
                packets.increment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        packets.setIncrement();
                    }
                });
                packets.setListener();
                packets.toggler=true;

                tablets=new NumberPicker();
                tablets.linearLayout=(LinearLayout)dialogView.findViewById(R.id.Tablets);
                tablets.MIN_VALUE=1;
                tablets.val=1;
                tablets.decrement=(Button)dialogView.findViewById(R.id.decrementTablets);
                tablets.value=(TextView)dialogView.findViewById(R.id.textTablets);
                tablets.decrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tablets.setDecrement();
                    }
                });
                tablets.increment=(Button)dialogView.findViewById(R.id.incrementTablets);
                tablets.increment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tablets.setIncrement();
                    }
                });
                tablets.setListener();

                alertBuilder.setPositiveButton(R.string.add, null);

                alertBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog ad=alertBuilder.create();
                ad.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                ad.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button b=ad.getButton(AlertDialog.BUTTON_POSITIVE);

                        int w=(packets.linearLayout.getWidth()-ad.findViewById(R.id.tvPackets).getWidth())/3;
                        packets.decrement.getLayoutParams().width=w;
                        packets.value.getLayoutParams().width=w;
                        packets.increment.getLayoutParams().width=w;

                        b.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                String newMemName = newName.getText().toString().trim();
                                //Log.e("name", ":" + newMemName + ":");
                                if (newMemName != null && !newMemName.equals("")) {
                                    MedicineData md= new MedicineData();
                                    md.setMid(familyData.incrementAi());
                                    md.setName(newMemName);
                                    md.setPackets(packets.val);
                                    md.setTablets(tablets.val);
                                    md.setTotal(md.getPackets() * md.getTablets());
                                    md.setLeft(md.getTotal());

                                    mDB = DBOpenHelper.getInstance(getActivity().getBaseContext());


                                    if(mDB.updateMember(familyData)>0) {
                                        if(mDB.addNewMedicine(md,familyData.getFid())!=-1l) {
                                            medicineListAdapter.notifyDataSetChanged();
                                            checkAddMemberText();
                                        }
                                    }
                                    else
                                        Log.e("addMed", "error");
                                    ad.dismiss();
                                    Toast.makeText(getActivity(), getString(R.string.added), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.addNewMed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                ad.show();
            }
        });

        listView=(ListView)view.findViewById(R.id.memberMedicineListContainer);
        medicineListAdapter=new MedicineListAdapter(getActivity(),this,familyData);
        listView.setAdapter(medicineListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                AlarmFragment af = new AlarmFragment();
                //mDB= DBOpenHelper.getInstance(getActivity().getBaseContext());
                af.setMedicineData(medicineListAdapter.getItem(position));
                af.setFamilyData(familyData);
                getActivity().getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.gla_back_gone, R.anim.gla_back_come, R.anim.gla_back_gone, R.anim.gla_back_come)
                        .replace(R.id.medicineAlarmFragmentContainer, af, "Alarm")
                        .addToBackStack(null)
                        .commit();
                //Toast.makeText(getActivity(), "cool" , Toast.LENGTH_SHORT).show();
            }
                catch(Exception e){
                Log.e("fragment trans",""+e);}
            }
        });
        checkAddMemberText();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > lastVisibleItem && !isScrollingDown) {
                    isScrollingDown = true;
                    fabAddMember.setVisibility(View.GONE);
                    //lastVisibleItem = firstVisibleItem;
                } else if (firstVisibleItem < lastVisibleItem && isScrollingDown) {
                    fabAddMember.setVisibility(View.VISIBLE);
                    isScrollingDown = false;
                    //lastVisibleItem = firstVisibleItem;
                }
                lastVisibleItem = firstVisibleItem;
            }
        });

        return view;
    }

    public void checkAddMemberText(){
        int count=medicineListAdapter.getCount();
        if(count==0)
            fabAddMemberText.setVisibility(View.VISIBLE);
        else
            fabAddMemberText.setVisibility(View.GONE);
    }
}
