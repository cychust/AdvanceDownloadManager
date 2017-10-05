package com.example.cyc.downloadproject;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cyc.downloadproject.Adapter.TaskAdapter;
import com.example.cyc.downloadproject.Interface.DownloadListener;
import com.example.cyc.downloadproject.URL.URLDownload;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cyc on 17-9-30.
 */

public class PagerFragment extends Fragment {

    private static final String ARG_PARAM1="param1";
    private int progress;
    public ArrayList<URLDownload>lists=new ArrayList<>();
    public TaskAdapter adapter;
;
    public static PagerFragment newInstance(ArrayList<URLDownload> lists) {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,lists);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (lists==null){
            lists=new ArrayList<>();
        }
        if(savedInstanceState!=null){
            lists=(ArrayList)getArguments().getSerializable(ARG_PARAM1);
        }

        adapter=new TaskAdapter(lists);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
     /*  if (lists==null){
            lists=new ArrayList<>();
        }
        if(savedInstanceState!=null){
            lists=(ArrayList)getArguments().getSerializable(ARG_PARAM1);
        }*/
        View view=inflater.inflate(R.layout.fragment,null);
       // adapter=new TaskAdapter(lists);

        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(inflater.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

//        ProgressBar progressBar=(ProgressBar)recyclerView.findViewById(R.id.progressBar);
  //      progressBar.setProgress(progress);
        return view;
    }
    public TaskAdapter getAdapter(){
        return adapter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }



}
