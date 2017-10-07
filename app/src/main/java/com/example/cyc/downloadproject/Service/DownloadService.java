package com.example.cyc.downloadproject.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.cyc.downloadproject.Data.AppConstant;
import com.example.cyc.downloadproject.Data.Utils;
import com.example.cyc.downloadproject.MainActivity;
import com.example.cyc.downloadproject.R;
import com.example.cyc.downloadproject.SQL.Sqlite;
import com.example.cyc.downloadproject.ThreadTask.DownloadTaskAll;
import com.example.cyc.downloadproject.Data.URLDownload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;

/**
 * Created by cyc on 17-10-1.
 */

public class DownloadService extends Service {

    public static final String STARTDOWNLOAD = "startDownload";
    public static final String CHANGESTATE = "changeState";
    public static final String DALETEDOWNLOAD="daleteDown";
    public static final String FINISHED="com.cyc.finished";
    public static final String UPDATEPRGRESS="com.cyc.updateprogress";
    public int i=1;
    private DownloadTaskAll downloadTaskAll;
    private Sqlite sqlite;
    private Map<String,DownloadTaskAll>downloadTaskAllMap=new HashMap<>();
    private Map<String,Integer>alldownloadLenMap=new HashMap<>();
    private Map<String,Integer>downloadLenMap=new HashMap<String, Integer>();
    private Map<String,Integer>downloadIdMap=new HashMap<>();
    private long oldtime;
  private Handler mHandler=new Handler(){
      @Override
      public void handleMessage(Message msg) {
          if (msg.what==5){
              String url=(String) msg.obj;
              int length=msg.arg1;
              int completeSize =downloadLenMap.get(url);
              int  fileSize=alldownloadLenMap.get(url);
              completeSize = completeSize + length;
              downloadLenMap.put(url, completeSize);
              int id=downloadIdMap.get(url);
              int progre=completeSize*100/fileSize;

              //sqlite.updateURLDownloadComplete(url,completeSize);
              if (System.currentTimeMillis()-oldtime>10000) {
                  oldtime=System.currentTimeMillis();
              }else if(System.currentTimeMillis()-oldtime>900){
                  oldtime=System.currentTimeMillis();
                  Intent intent = new Intent();
                  intent.putExtra("progress", completeSize);
                  intent.putExtra("Url", url);
                  intent.setAction(UPDATEPRGRESS);
                  DownloadService.this.sendBroadcast(intent);
                  String fileName= Utils.getFileName(url);
                  getNotificationManager().notify(id,getNotification(fileName,progre));
              }

              if (completeSize==fileSize){
                  Intent intent1 = new Intent();
                  intent1.putExtra("Url", url);
                  intent1.setAction(FINISHED);
                  DownloadService.this.sendBroadcast(intent1);
                  String filename = url.substring(url.lastIndexOf("/") + 1);
                  sqlite.updataStateByUrl(url,3);
                  getNotificationManager().notify(id,getNotification(filename+"下载成功",100));
                  getNotificationManager().notify(1,getNotification("正在下载任务："+downloadTaskAllMap.size(),
                          0));

                  Toast.makeText(DownloadService.this,filename+"下载成功",Toast.LENGTH_SHORT).show();
                  downloadTaskAllMap.remove(url);
                  downloadLenMap.remove(url);
                  alldownloadLenMap.remove(url);
                  downloadIdMap.remove(url);
                  if (downloadTaskAllMap.isEmpty()){
                      stopSelf();
                  }
                  String directory = Environment.getExternalStoragePublicDirectory
                          (Environment.DIRECTORY_DOWNLOADS).getPath();
               //   Toast.makeText(DownloadService.this, "download=file", Toast.LENGTH_SHORT).show();

                  try {
                      BufferedOutputStream outputStream = new BufferedOutputStream(
                              new FileOutputStream(directory + filename)
                      );
                      Toast.makeText(DownloadService.this, "文件整合", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onCreate() {
        super.onCreate();
        sqlite=new Sqlite(this);
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        String url=intent.getStringExtra("Url");
        String flag = intent.getStringExtra("flag");
        int fileSize=intent.getIntExtra("FileSize",0);
        if (flag.equals(STARTDOWNLOAD)){
            startDownload(url,fileSize);
        }
        if (flag.equals(CHANGESTATE)){
            changeState(url,fileSize);
        }
        if (flag.equals(DALETEDOWNLOAD)){
            cancel(url,fileSize);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

        public void startDownload(final String url, int fileSize) {
            downloadTaskAll = downloadTaskAllMap.get(url);
            if (downloadTaskAll == null) {
                //  downloadUrl=url;
                oldtime=System.currentTimeMillis();
                downloadTaskAll = new DownloadTaskAll(url, mHandler, DownloadService.this);
                alldownloadLenMap.put(url, fileSize);
                downloadTaskAll.download(url);
                downloadTaskAllMap.put(url, downloadTaskAll);
                i++;
                downloadIdMap.put(url,i);
                int length = sqlite.getURLDoanloadLen(url);
                downloadLenMap.put(url, length);
                long oldtime = System.currentTimeMillis();
                URLDownload urlDownload = new URLDownload(url, 1, 0, fileSize, oldtime);
                if (sqlite.isHasDownloadInfos(url)) {
                    sqlite.saveDownloadInfos(urlDownload);
                    Toast.makeText(DownloadService.this,"第一次下载",Toast.LENGTH_SHORT).show();
                }
                startForeground(1, getNotification("Task" + downloadTaskAllMap.size(), 0));
                startForeground(i,getNotification(urlDownload.getFileName(),0));
                //Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
            } else {


                downloadTaskAll.download(url);
                Toast.makeText(DownloadService.this,"继续下载",Toast.LENGTH_SHORT).show();
               // int length = sqlite.getURLDoanloadLen(url);
               // downloadLenMap.put(url, length);
              //  long oldtime = System.currentTimeMillis();
              //  URLDownload urlDownload = new URLDownload(url, 1, length, fileSize, oldtime);
                sqlite.updataStateByUrl(url, 1);
                startForeground(1, getNotification("Task" + downloadTaskAllMap.size(), 0));
            }
        }

        public void changeState(String url, int fileSize) {

            DownloadTaskAll downloadTaskall = downloadTaskAllMap.get(url);


                if (downloadTaskall != null) {
                    int downloadLen = downloadLenMap.get(url);
                    if (downloadTaskall.isDownloading()) {
                        downloadTaskall.setPaused();
                        sqlite.updateURLDownloadComplete(url, downloadLen);
                        sqlite.updataStateByUrl(url, 2);
                    } else {
                        downloadTaskall.reset();
                        startDownload(url, fileSize);
                        sqlite.updataStateByUrl(url, 1);
                    }
                } else {
                    startDownload(url, fileSize);
                }
            }

            public void cancel(String url,int fileSize) {
                DownloadTaskAll downloadTaskall = downloadTaskAllMap.get(url);

                if (downloadTaskall != null) {
                    downloadLenMap.remove(url);
                    downloadTaskall.setCancel(url);
                    downloadIdMap.remove(url);
                    downloadTaskAllMap.remove(url);
                    alldownloadLenMap.remove(url);
                }
            }

    private Notification getNotification(String title,int progress){
        Intent intent=new Intent(this, MainActivity.class);
        intent.setAction("qiantaifuwu");
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        if (progress>0){
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }
        builder.setContentIntent(pi);
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
