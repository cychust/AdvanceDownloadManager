package com.example.cyc.downloadproject;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cyc on 17-10-1.
 */

public class Utils {

 public static int APK=1;
 public static int TXT=2;
 public static int MP3=3;
 public static int MP4=4;
 public static int PIC=5;
 public static int ZIP=6;
 public static int OTHER=7;


   public static int getCategory(String filename){
        String suffix;
        suffix=filename.substring(filename.lastIndexOf(".")+1);
        if (suffix.equals("apk")){
            return APK;
        }
        else if(suffix.equals("mp3")){
            return MP3;
        }
        else if (suffix.equals("mp4")){
            return MP4;
        }
        else if (suffix.equals("txt")){
            return TXT;
        }
        else if (suffix.equals("jpg")||suffix.equals("png")){
            return PIC;
        }
        else if(suffix.equals("zip")){
            return ZIP;
        }
        else {
            return OTHER;
        }
    }
    public static void loadconfig(File configFile){


    }
     public static long getContentLength(String downloadUrl) throws IOException {
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
        if (response!=null){
            long contentlength=response.body().contentLength();
            response.body().close();

            return contentlength;
        }

        return 0;
    }
}
