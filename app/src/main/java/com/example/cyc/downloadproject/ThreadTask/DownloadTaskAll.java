package com.example.cyc.downloadproject.ThreadTask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.cyc.downloadproject.Data.AppConstant;
import com.example.cyc.downloadproject.Interface.DownloadListener;
import com.example.cyc.downloadproject.MainActivity;
import com.example.cyc.downloadproject.SQL.Sqlite;

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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cyc on 17-10-2.
 */

public class DownloadTaskAll  {
    String url;
    private DownloadListener listener;
    private Handler mHandler;
    private boolean isDownloading=false;
    private boolean isCancel=false;
    private List<DownloadTask> taskList=new ArrayList<>();
    private Context context;
    private Sqlite sqlite;
 public DownloadTaskAll(String url, Handler handler, Context context){
     this.url=url;
     mHandler=handler;
     this.context=context;
     sqlite=new Sqlite(context);

 }



    public void download(String url){

            for (int i=1;i<=AppConstant.THREAD_NUM;i++){
                // new DownLoadTask(listener,i).execute(url);
                DownloadTask task= new DownloadTask(url,i,mHandler);
                taskList.add(task);
                task.start();
            }
            isDownloading=true;

    }



    public boolean isDownloading() {
        return isDownloading;
    }

    public void setPaused() {
        isDownloading = false;
    }

    public void setCancel(String url){
        isCancel=true;
    }


   public class DownloadTask extends Thread {

        private int threadId;
        private String url;

        private Handler handler;

        public DownloadTask(String url, int theadId, Handler handler){
            // this.listener=listener;
            this.handler=handler;
            this.threadId=theadId;
            this.url=url;

        }

        @Override
        public void run() {
            super.run();

            InputStream is = null;

            RandomAccessFile saveFile = null;
            File file = null;
            try {
                long startIndex=0;
                long endIndex=0;
                long downloadedLength = 0;
                String downloadUrl = url;
                String filename = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
                String directory = Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_DOWNLOADS).getPath();
                file = new File(directory+"_"+threadId + filename);
                if (file.exists()) {
                    downloadedLength = file.length();
                }
                long allcontentLength = getContentLength(downloadUrl);

                //   listener.onStart(allcontentLength);
                long block;
                block = allcontentLength / AppConstant.THREAD_NUM;

                startIndex = (threadId - 1) * block;
                endIndex = (threadId) * block-1;
                if(threadId == AppConstant.THREAD_NUM){
                    endIndex = allcontentLength-1;
                }
                long start=startIndex+downloadedLength;
       /*     if (contentLength == 0) {

            } else if (contentLength == downloadedLength) {

            }*/

      /*      URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
            connection.setReadTimeout(8000);
            is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));*/
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .addHeader("RANGE", "bytes=" + start + "-"+endIndex)
                        .url(downloadUrl)
                        .build();
                Response response = client.newCall(request).execute();
                if (response != null) {
                    is = response.body().byteStream();
                    saveFile = new RandomAccessFile(file, "rw");
                    saveFile.seek(downloadedLength);
                    byte[] b = new byte[1024];
                    int len = -1;
                    while ((len = is.read(b)) != -1) {
                        if (isCancel) {
                            break;
                        }
                        else if (!isDownloading) {
                            break;
                        }
                        else {
                            saveFile.write(b, 0, len);
                            //  listener.onProgress(len, url);
                            Message message=new Message();
                            message.what=5;
                            message.obj=url;
                            message.arg1=len;
                            handler.sendMessage(message);
                        }

                    }
                    response.body().close();
                    // listener.getMax(url);
                }
                else {
                    Log.d("response",threadId+"filed");
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if (is!=null){
                        is.close();
                    }
                    if(saveFile!=null){
                        saveFile.close();
                    }
                    if (isCancel&&file!=null){
                        file.delete();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        private long getContentLength(String downloadUrl) throws IOException  {
       /* URL url=new URL(downloadUrl);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        int reponseCode=connection.getResponseCode();
        if (reponseCode==200){
            long contentLength=connection.getContentLength();
            listener.getMax();
            return contentLength;
        }
        listener.onFailed();*/
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(downloadUrl).build();
            Response response=client.newCall(request).execute();
            if (response!=null&&response.isSuccessful()){
                long contentlength=response.body().contentLength();
                response.body().close();

                return contentlength;
            }

            return 0;
        }
    }
    public void reset(){
        isDownloading=true;
    }

    private boolean isFirstDownload(String downloadPath) {
        return sqlite.isHasDownloadInfos(downloadPath);
    }
}
