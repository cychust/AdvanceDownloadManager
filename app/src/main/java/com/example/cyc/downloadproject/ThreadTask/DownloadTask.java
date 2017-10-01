package com.example.cyc.downloadproject.ThreadTask;


import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Environment;


import com.example.cyc.downloadproject.Interface.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by cyc on 17-10-1.
 */

public class DownloadTask extends AsyncTask<String,Integer,Integer> {
    public static final int TYPE_SCESS=0;
    public static final int TYPE_FAILED=1;
    public static final int TYPE_PAUSED=2;
    public static final int TYPE_CANCELED=3;

    public static int DOWNLOAD=1;
    public static int PAUSE=2;
    private DownloadListener listener;
    private boolean isCancel=false;
    private boolean isPaused=false;
    private int lastProgress;
    public DownloadTask(DownloadListener listener){
        this.listener=listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        InputStream is=null;
        RandomAccessFile saveFile=null;
        File file=null;
        try{
            long downloadedLength=0;
            String downloadUrl=strings[0];
            String filename=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory= Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS).getPath();
            file=new File(directory+filename);
            if (file.exists()){
                downloadedLength=file.length();
            }
            long contentLength=getContentLength(downloadUrl);
            if(contentLength==0){
                return TYPE_FAILED;
            }
            else if (contentLength==downloadedLength){
                return TYPE_SCESS;
            }
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder()
                    .addHeader("RANGE","bytes="+downloadedLength+"-")
                    .url(downloadUrl)
                    .build();
            Response response=client.newCall(request).execute();
            if(response!=null){
                is=response.body().byteStream();
                saveFile=new RandomAccessFile(file,"rw");
                saveFile.seek(downloadedLength);
                byte[]b=new byte[1024];
                int total=0;
                int len;
                while ((len=is.read(b))!=-1){
                    if(isCancel){
                        return TYPE_CANCELED;
                    }
                    else if (isPaused){
                        return TYPE_PAUSED;
                    }else {
                        total+=len;
                        saveFile.write(b,0,len);
                        int progress=(int)((total+downloadedLength)*100/contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SCESS;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                if(is!=null){
                    is.close();
                }
                if (saveFile!=null){
                    saveFile.close();
                }
                if (isCancel&&file!=null){
                    file.delete();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress=values[0];
        if(progress>lastProgress){
            listener.onProgress(progress);
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
                listener.onSuccess();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            case  TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPause();
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
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response =client.newCall(request).execute();
        if(request!=null&&response.isSuccessful()){
            long contentLength =response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }
}
