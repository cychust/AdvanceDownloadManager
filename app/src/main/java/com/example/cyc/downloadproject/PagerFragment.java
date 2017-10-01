package com.example.cyc.downloadproject;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



/**
 * Created by cyc on 17-9-30.
 */

public class PagerFragment extends Fragment {

    private static final String ARG_PARAM1="param1";
    private String param1;
    public static PagerFragment newInstance(String param1) {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            param1=getArguments().getString(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment,null);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        switch (param1){
            case "任务":

                break;
            case "已完成":
                break;
        }
        return view;
    }

}
