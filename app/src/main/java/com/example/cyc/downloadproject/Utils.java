package com.example.cyc.downloadproject;

import android.os.Environment;
import android.widget.Toast;

import com.example.cyc.downloadproject.Data.AppConstant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;

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

    public static String getSpeed(double speed){
        double temp=speed/1024*1000;
        DecimalFormat df=new DecimalFormat(".##");
        String result=df.format(temp);
        return result+"kb/s";
    }
    public static String getFileSize(long fileSize){
        if (fileSize>1024*1024){
            double temp=(double)fileSize/(1024.0*1024.0);
            DecimalFormat df=new DecimalFormat(".##");
            String result=df.format(temp);
            return result+"MB";
        }
        else {
            double temp=(double)fileSize/1024.0;
            DecimalFormat df=new DecimalFormat(".##");
            String result=df.format(temp);
            return result+"KB";
        }
    }
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
    public void fixFile(String url){
        String directory = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS).getPath();


        String filename = url.substring(url.lastIndexOf("/") + 1);
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(directory + filename)
            );

            for (int k = 1; k <= AppConstant.THREAD_NUM; k++) {
                File file = new File(directory +  "_" + k+filename );
                BufferedInputStream inputStream = new BufferedInputStream(
                        new FileInputStream(file)
                );
                int length = -1;
                long count = 0;
                byte[] bytes = new byte[1024];
                while ((length = inputStream.read(bytes)) != -1) {
                    count += length;
                    outputStream.write(bytes, 0, length);
                    if ((count % 4096 == 0)) {
                        outputStream.flush();
                    }
                }
                outputStream.flush();
                inputStream.close();
                file.delete();
            }
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
