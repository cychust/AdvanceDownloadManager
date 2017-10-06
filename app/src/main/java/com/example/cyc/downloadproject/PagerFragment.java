package com.example.cyc.downloadproject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.cyc.downloadproject.Adapter.TaskAdapter;
import com.example.cyc.downloadproject.Interface.DownloadListener;
import com.example.cyc.downloadproject.SQL.DBHelper;
import com.example.cyc.downloadproject.SQL.Sqlite;
import com.example.cyc.downloadproject.URL.URLDownload;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cyc on 17-9-30.
 */

public class PagerFragment extends Fragment {

    private static final String ARG_PARAM1="param1";
    private int progress;
    private Context context=null;
    public ArrayList<URLDownload>lists=new ArrayList<>();
    public TaskAdapter adapter;
    private String mParam1;
    private String mParam2;
    public PagerFragment(){

    }
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
      /*  if (lists==null){
            lists=new ArrayList<>();
        }
        if(savedInstanceState!=null){
            lists=(ArrayList)getArguments().getSerializable(ARG_PARAM1);
        }*/

        if (getArguments() != null) {
            mParam1 = getArguments().getString(mParam1);
            mParam2 = getArguments().getString(mParam2);
        }

    }

    @Override
    public void onAttach(Context context) {
        this.context=context;

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
        initDate();
        adapter=new TaskAdapter(lists,getActivity());
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

    public void  initDate(){
       Sqlite sqlite=new Sqlite(context);
        lists= sqlite.getURLDoanloading();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
