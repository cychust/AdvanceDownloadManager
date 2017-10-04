package com.example.cyc.downloadproject.ThreadTask;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.example.cyc.downloadproject.Data.AppConstant;
import com.example.cyc.downloadproject.Interface.DownloadListener;
import com.example.cyc.downloadproject.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyc on 17-10-2.
 */

public class DownloadTaskAll  {
    String url;
    DownloadListener listener;
    private boolean isCancel;
    private boolean isPaused;
    private List<DownloadTask> taskList=new ArrayList<>();
    public DownloadTaskAll(String url,DownloadListener listener){
        this.url=url;
        this.listener=listener;
    }
    public void download(String url,DownloadListener listener){

        for (int i=1;i<=AppConstant.THREAD_NUM;i++){
           // new DownLoadTask(listener,i).execute(url);
           DownloadTask task= new DownloadTask(url,i,listener);
            taskList.add(task);
            task.start();

        }

    }
    public void pausedDownload(){
        isPaused=true;
        for (int i=0;i<AppConstant.THREAD_NUM;i++){
            DownloadTask task=taskList.get(i);
            task.pausedThread();

        }
        listener.onPaused(url);
        //listener.onPaused(url);
    }
    public void cancelDownload(){
        isCancel=true;
        for (int i=0;i<AppConstant.THREAD_NUM;i++){
            taskList.get(i).cancelThread();
        }
        listener.onCanceled(url);
       // listener.onCanceled(url);
    }
}
