package com.akshenkadakia.homeutility.medicine;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.medicine.adapters.FamilyListAdapter;
import com.akshenkadakia.homeutility.medicine.data.DBOpenHelper;
import com.akshenkadakia.homeutility.medicine.data.FamilyData;

public class FamilyFragment extends Fragment {

    private ListView listView;
    private EditText newName;
    private Switch notiSwitch;
    private DBOpenHelper mDB;
    private TextView fabAddMemberText;
    private FamilyListAdapter familyListAdapter;
    private int lastVisibleItem = 0;
    private boolean isScrollingDown = false;
    private MemberActivity memberActivity;

    public FamilyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        if(familyListAdapter!=null)
            familyListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        memberActivity = ((MemberActivity) getActivity());

        fabAddMemberText = (TextView) view.findViewById(R.id.fabAddMemberText);
        final FloatingActionButton fabAddMember = (FloatingActionButton) view.findViewById(R.id.fabAddMember);

        fabAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setIcon(R.drawable.ic_local_hospital_black_48dp);
                alertBuilder.setTitle(getString(R.string.addNewMember));
                View dialogView = inflater.inflate(R.layout.alert_dialog_add_new_member, null);
                alertBuilder.setView(dialogView);
                newName = (EditText) dialogView.findViewById(R.id.newMemberDialogName);
                notiSwitch = (Switch) dialogView.findViewById(R.id.newMemberDialogNotificationSwitch);
                alertBuilder.setPositiveButton(R.string.add, null);

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
                                    FamilyData mn = new FamilyData(newMemName, notiSwitch.isChecked());
                                    mDB = DBOpenHelper.getInstance(getActivity().getBaseContext());
                                    //Log.e("adding:", newMemName + ":" + mn.isNotificationStatus());
                                    long error = mDB.addNewMember(mn);
                                    if (error == -1l) {
                                        Toast.makeText(getActivity(), R.string.error+" "+R.string.adding+" "+ R.string.member, Toast.LENGTH_SHORT).show();
                                    } else {
                                        familyListAdapter.notifyDataSetChanged();
                                        checkAddMemberText();
                                        ad.dismiss();
                                    }
                                    //Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), R.string.enterName, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                ad.show();
            }
        });

        listView=(ListView)view.findViewById(R.id.medicineMemberListContainer);
        familyListAdapter=new FamilyListAdapter(getActivity(),this);
        listView.setAdapter(familyListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FamilyData mn = familyListAdapter.getItem(position);
                Intent intent=new Intent(getActivity(),MedicineActivity.class);
                intent.putExtra("fid",mn.getFid());
                intent.putExtra("ai",mn.getAi());
                intent.putExtra("notifications",mn.isNotifications());
                intent.putExtra("name",mn.getName());
                getActivity().startActivity(intent);
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
                    lastVisibleItem = firstVisibleItem;
                } else if (firstVisibleItem < lastVisibleItem && isScrollingDown) {
                    fabAddMember.setVisibility(View.VISIBLE);
                    isScrollingDown = false;
                    lastVisibleItem = firstVisibleItem;
                }
            }
        });

        return view;
    }

    public void checkAddMemberText(){
        int count=familyListAdapter.getCount();
        if(count==0)
            fabAddMemberText.setVisibility(View.VISIBLE);
        else
            fabAddMemberText.setVisibility(View.GONE);
    }
}
