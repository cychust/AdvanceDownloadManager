package com.example.cyc.downloadproject.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.cyc.downloadproject.Interface.DownloadListener;
import com.example.cyc.downloadproject.MainActivity;
import com.example.cyc.downloadproject.R;
import com.example.cyc.downloadproject.ThreadTask.DownloadTask;

import java.io.File;

/**
 * Created by cyc on 17-10-1.
 */

public class DownloadService extends Service {
    private DownloadTask downloadTask;

    private String downloadUrl;

    private DownloadListener listener =new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("Downloading...",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Success",-1));

        }

        @Override
        public void onFailed() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Failed",-1));
        }

        @Override
        public void onPause() {
            downloadTask=null;
        }

        @Override
        public void onCanceled() {
            downloadTask=null;
            stopForeground(true);
        }
    };

    private DownloadBinder mBinder =new DownloadBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    class DownloadBinder extends Binder{
        public void  startDownload(String url){
            if(downloadTask==null){
                downloadUrl=url;
                downloadTask=new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1,getNotification("Downloading...",0));
                Toast.makeText(DownloadService.this,"Downloading...",Toast.LENGTH_SHORT).show();
            }
        }
        public void pauseDownload(){
            if(downloadTask!=null){
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload(){
            if(downloadTask!=null){
                downloadTask.cancelDownload();
            }
            else {
                if(downloadUrl!=null){
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
                }
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
}
