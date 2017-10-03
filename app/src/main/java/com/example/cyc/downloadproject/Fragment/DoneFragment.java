package com.example.cyc.downloadproject.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cyc.downloadproject.Adapter.TaskAdapter;
import com.example.cyc.downloadproject.Interface.DownloadListener;
import com.example.cyc.downloadproject.R;
import com.example.cyc.downloadproject.URL.URLDownload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyc on 17-10-3.
 */

public class DoneFragment extends Fragment  {

    private static final String ARG_PARAM1="param1";
    private int progress;
    private List<URLDownload> lists;
    public static com.example.cyc.downloadproject.PagerFragment newInstance(List<URLDownload> lists) {
        com.example.cyc.downloadproject.PagerFragment fragment = new com.example.cyc.downloadproject.PagerFragment();
        Bundle args = new Bundle();
        args.putCharSequenceArrayList(ARG_PARAM1,(ArrayList)lists);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            lists=(List) getArguments().getCharSequenceArrayList(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment,null);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        TaskAdapter adapter=new TaskAdapter(lists);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(inflater.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
//        ProgressBar progressBar=(ProgressBar)recyclerView.findViewById(R.id.progressBar);
        //      progressBar.setProgress(progress);
        return view;
    }


}

