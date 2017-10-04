package com.example.cyc.downloadproject.ThreadTask;

import android.app.DownloadManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.cyc.downloadproject.Data.AppConstant;
import com.example.cyc.downloadproject.Interface.DownloadListener;
import com.example.cyc.downloadproject.MainActivity;
import com.example.cyc.downloadproject.Service.DownloadService;

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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cyc on 17-10-2.
 */

public class DownloadTask extends Thread {

    private int threadId;
    private String url;
    private DownloadListener listener;

    public boolean isPaused=false;
    public boolean isCancel=false;
    private int taskNum;

    public DownloadTask(String url, int theadId, DownloadListener listener){
        this.listener=listener;
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
                    else if (isPaused) {
                        break;
                    }
                    else {
                        listener.onProgress(len, url);
                        saveFile.write(b, 0, len);
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
    public void pausedThread(){
        isPaused=true;
    }
    public void cancelThread(){
        isCancel=true;
    }
}
