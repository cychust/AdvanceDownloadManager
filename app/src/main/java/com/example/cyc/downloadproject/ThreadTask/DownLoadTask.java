package com.example.cyc.downloadproject.ThreadTask;


import android.os.AsyncTask;
import android.os.Environment;


import com.example.cyc.downloadproject.Data.AppConstant;
import com.example.cyc.downloadproject.Interface.DownloadListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by cyc on 17-10-1.
 */

public class DownLoadTask extends AsyncTask<String,Integer,Integer> {
    public static final int TYPE_SCESS=0;
    public static final int TYPE_FAILED=1;
    public static final int TYPE_PAUSED=2;
    public static final int TYPE_CANCELED=3;

    private DownloadListener listener;
    private boolean isCancel=false;
    private boolean isPaused=false;
    private int lastProgress;


    private long startIndex=0;
    private long endIndex=0;

    private int threadId;
    private long contentLength;
    public DownLoadTask(DownloadListener listener,int threadId){
        this.listener=listener;
        this.threadId=threadId;
    }

    @Override
    protected Integer doInBackground(String... strings) {


        InputStream is = null;

        RandomAccessFile saveFile = null;
        File file = null;
        try {
            long downloadedLength = 0;
            String downloadUrl = strings[0];
            String filename = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + filename+threadId);
            if (file.exists()) {
                downloadedLength = file.length();
            }
            long allcontentLength = getContentLength(downloadUrl);

            if (threadId != AppConstant.THREAD_NUM) {
                contentLength = allcontentLength / threadId;
                startIndex = (threadId - 1) * contentLength;
                endIndex = (threadId) * contentLength;
            } else {
                startIndex = (threadId - 1) * contentLength;
                endIndex = allcontentLength;
                contentLength = endIndex - startIndex;
            }
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                return TYPE_SCESS;
            }

            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
            connection.setReadTimeout(8000);
            is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            saveFile = new RandomAccessFile(file, "rwd");
            saveFile.seek(contentLength);
            byte[] b = new byte[1024];
            int total = 0;
            int len = 0;
            while ((len = is.read(b)) != -1) {
                if (isCancel) {
                    return TYPE_CANCELED;
                } else if (isPaused) {
                    return TYPE_PAUSED;
                } else {
                    total += len;
                    saveFile.write(b, 0, len);
                   // int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                    publishProgress(len);
                }
            }
            return TYPE_SCESS;
        }catch (Exception e){
            e.printStackTrace();
    }
    finally {
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
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress=values[0];
        if(progress>lastProgress){
           // listener.onProgress(progress);
            lastProgress=progress;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case TYPE_SCESS:
               // listener.onSuccess();
                break;
            case TYPE_CANCELED:
               // listener.onCanceled();
               break;
            case  TYPE_FAILED:
            //    listener.onFailed();
                break;
            case TYPE_PAUSED:
             //   listener.onPaused();
                break;
            default:break;

        }
    }
    public void pauseDownload(){
        isPaused=true;
    }
    public void cancelDownload(){
        isCancel=true;
    }
    private long getContentLength(String downloadUrl) throws IOException{
        URL url=new URL(downloadUrl);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        int reponseCode=connection.getResponseCode();
        if (reponseCode==200){
            long contentLength=connection.getContentLength();
            return contentLength;
        }
        return 0;
    }
}
