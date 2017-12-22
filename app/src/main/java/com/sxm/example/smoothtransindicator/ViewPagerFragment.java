package com.sxm.example.smoothtransindicator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFragment extends Fragment {


    public ViewPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.view_pager_fragment, container, false);
        TextView viewById = (TextView) root.findViewById(R.id.textview1);
        String key = getArguments().getString("key");
        viewById.setText(key);
        if("blue".equals(key)){
            viewById.setBackgroundColor(getResources().getColor(R.color.custom_blue));
        }else if("red".equals(key)){
            viewById.setBackgroundColor(getResources().getColor(R.color.custom_red));
        }else {
            viewById.setBackgroundColor(getResources().getColor(R.color.custom_black));
        }
        return root;
    }

}
