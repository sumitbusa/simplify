package com.akshenkadakia.homeutility.medicine;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akshenkadakia.homeutility.R;
import com.akshenkadakia.homeutility.home.Utils;


public class DayListFragment extends Fragment {


    public DayListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_day_list, container, false);
        //distribute height
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
        Utils utils=new Utils(getActivity());
        int height= (int) (((utils.getScreenHeight() - ((8) * padding)) / 7)*0.95);

        ViewGroup vg=(ViewGroup)view;
        int width=vg.getChildAt(0).getLayoutParams().width;
        for(int i=0;i<vg.getChildCount();i++){
            View v=vg.getChildAt(i);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv=(TextView)v;
                    Intent intent=new Intent(getActivity(),DayListActivity.class);
                    intent.putExtra("day",tv.getText().toString());
                    startActivity(intent);
                }
            });
            v.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        }

        return view;
    }

}
