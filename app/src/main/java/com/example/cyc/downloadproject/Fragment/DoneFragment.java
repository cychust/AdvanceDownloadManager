package com.example.cyc.downloadproject.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cyc.downloadproject.Adapter.DoneAdapter;
import com.example.cyc.downloadproject.Adapter.TaskAdapter;
import com.example.cyc.downloadproject.Interface.DownloadListener;
import com.example.cyc.downloadproject.R;
import com.example.cyc.downloadproject.SQL.Sqlite;
import com.example.cyc.downloadproject.URL.URLDownload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyc on 17-10-3.
 */

public class DoneFragment extends Fragment  {

    private static final String ARG_PARAM1="param1";
    private String mParam1;
    private String mParam2;
    public ArrayList<URLDownload> lists=new ArrayList<>();
    public DoneAdapter adapter=null;
    private Context context;
    public static DoneFragment newInstance(List<URLDownload> lists) {
        DoneFragment fragment = new DoneFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,(ArrayList)lists);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(mParam1);
            mParam2 = getArguments().getString(mParam2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       /* if (lists==null){
            lists=new ArrayList<>();
        }
        if(savedInstanceState!=null){
            lists=(ArrayList)getArguments().getSerializable(ARG_PARAM1);
        }*/
        View view=inflater.inflate(R.layout.fragment,null);
        initDate();
        adapter=new DoneAdapter(lists,getActivity());
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);

        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(inflater.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

//        ProgressBar progressBar=(ProgressBar)recyclerView.findViewById(R.id.progressBar);
        //      progressBar.setProgress(progress);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public void  initDate(){
        Sqlite sqlite=new Sqlite(context);
        lists= sqlite.getURLDoanloaded();
    }


}

