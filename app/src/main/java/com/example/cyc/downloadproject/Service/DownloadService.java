package com.example.cyc.downloadproject.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.cyc.downloadproject.Data.AppConstant;
import com.example.cyc.downloadproject.Interface.DownloadListener;
import com.example.cyc.downloadproject.MainActivity;
import com.example.cyc.downloadproject.R;
import com.example.cyc.downloadproject.SQL.Sqlite;
import com.example.cyc.downloadproject.ThreadTask.DownloadTask;
import com.example.cyc.downloadproject.ThreadTask.DownloadTaskAll;
import com.example.cyc.downloadproject.URL.URLDownload;
import com.example.cyc.downloadproject.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cyc on 17-10-1.
 */

public class DownloadService extends Service {
    private DownloadTaskAll downloadTaskAll;
    private Sqlite sqlite;
    private Map<String,DownloadTaskAll>downloadTaskAllMap=new HashMap<>();
    private Map<String,Integer>alldownloadLenMap=new HashMap<>();
    private Map<String,Integer>downloadLenMap=new HashMap<String, Integer>();
  private Handler mHandler=new Handler(){
      @Override
      public void handleMessage(Message msg) {
          if (msg.what==5){
              String url=(String) msg.obj;
              int length=msg.arg1;
              int completeSize =downloadLenMap.get(url);
              completeSize = completeSize + length;
              downloadLenMap.put(url, completeSize);
              sqlite.updateURLDownloadComplete(url,completeSize);
              /* if (completeSize%1024==0) {
                   Intent intent = new Intent();
                   intent.putExtra("progress", completeSize);
                   intent.putExtra("Url", url);
                   intent.setAction("com.example.cyc.progress");
                   sendBroadcast(intent);
               }*/
              long fileSize=alldownloadLenMap.get(url);
              if (completeSize==fileSize){
                  Intent intent1 = new Intent();
                  intent1.putExtra("progress",completeSize);
                  intent1.putExtra("Url", url);
                  intent1.setAction("com.example.cyc.progress");
                  sendBroadcast(intent1);
                  sqlite.updataStateByUrl(url,3);
                  downloadTaskAllMap.remove(url);
                  downloadLenMap.remove(url);
                  alldownloadLenMap.remove(url);
                  String directory = Environment.getExternalStoragePublicDirectory
                          (Environment.DIRECTORY_DOWNLOADS).getPath();
                  Toast.makeText(DownloadService.this, "download=file", Toast.LENGTH_SHORT).show();

                  String filename = url.substring(url.lastIndexOf("/") + 1);
                  try {
                      BufferedOutputStream outputStream = new BufferedOutputStream(
                              new FileOutputStream(directory + filename)
                      );
                      Toast.makeText(DownloadService.this, "wenjian整合", Toast.LENGTH_SHORT).show();
                      for (int k = 1; k <= AppConstant.THREAD_NUM; k++) {
                          File file = new File(directory +  "_" + k+filename );
                          BufferedInputStream inputStream = new BufferedInputStream(
                                  new FileInputStream(file)
                          );
                          int lengTh = -1;
                          long count = 0;
                          byte[] bytes = new byte[1024];
                          while ((lengTh = inputStream.read(bytes)) != -1) {
                              count += length;
                              outputStream.write(bytes, 0, lengTh);
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
      }
  };

    private DownloadBinder mBinder =new DownloadBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        sqlite=new Sqlite(this);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public class DownloadBinder extends Binder{
        public void  startDownload(final String url,int fileSize){
            downloadTaskAll=downloadTaskAllMap.get(url);
            if(downloadTaskAll==null){
              //  downloadUrl=url;
                downloadTaskAll=new DownloadTaskAll(url,mHandler);
                alldownloadLenMap.put(url,fileSize);
                downloadTaskAll.download(url);
                downloadTaskAllMap.put(url,downloadTaskAll);
                downloadLenMap.put(url,0);
                long oldtime=System.currentTimeMillis();
                URLDownload urlDownload=new URLDownload(url,1,0,fileSize,oldtime);
                if (sqlite.isHasDownloadInfos(url)) {
                    sqlite.saveDownloadInfos(urlDownload);
                }
                startForeground(1,getNotification("Task"+downloadTaskAllMap.size(),0));
                Toast.makeText(DownloadService.this,"Downloading...",Toast.LENGTH_SHORT).show();
            }
        }
        public void pauseDownload(String url){
            DownloadTaskAll downloadTaskAll=downloadTaskAllMap.get(url);

            if(downloadTaskAll!=null){
                downloadTaskAll.pausedDownload();
            }
            sqlite.updataStateByUrl(url,2);
            long currentTime= System.currentTimeMillis();
            long downloadTime=currentTime-sqlite.getURLDoanloadTime(url);
            sqlite.updateURLDownloadTime(url,downloadTime);
            downloadTaskAllMap.remove(downloadTaskAll);
        }
        public void cancelDownload(){
            if(downloadTaskAll!=null){
                downloadTaskAll.cancelDownload();
            }
            else {
            /*    if(!=null){
                    String filename=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory= Environment.getExternalStoragePublicDirectory(Environment
                            .DIRECTORY_DOWNLOADS).getPath();
                    File file=new File(directory+filename);
                    if(file.exists()) {
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this,"Download cancel",Toast.LENGTH_SHORT).show();
                }*/
            }
        }
    }
    private Notification getNotification(String title,int progress){
        Intent intent=new Intent(this, MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentIntent(pi);
        if(progress>0){
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);

        }
        return builder.build();
    }
    private NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
