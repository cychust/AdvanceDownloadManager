package com.example.cyc.downloadproject.ThreadTask;

import com.example.cyc.downloadproject.URL.URLDownload;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by cyc on 17-10-1.
 */

public class DownLoadTask {

}
/*implements Runnable {

   /* private static final String TAG="DownLoadTask";

    private URLDownload dEntity;
    private String configFPath;
    public DownLoadTask(URLDownload downloadInfo) {
        this.dEntity = downloadInfo;
        configFPath = dEntity.context.getFilesDir().getPath() + "/temp/" + dEntity.tempFile.getName() + ".properties";
    }
    @Override
    public void run() {
        try {

            URL url = new URL(dEntity.downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Range","bytes="+dEntity.startLocation+"-"+dEntity.endLocation);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset","UTF-8");
            conn.setConnectTimeout(TIME_OUT);
            conn.setRequestProperty("User-Agent","Mozilla/4.0(compatible;MSIE 8.0;" +
                    "Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; " +
                    ".NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, " +
                    "application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, " +
                    "application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, " +*/
            //        "application/vnd.ms-powerpoint, application/msword, */*");
          /*  conn.setReadTimeout(2000);
            InputStream is=conn.getInputStream();
            RandomAccessFile file =new RandomAccessFile(dEntity.tempFile,"rwd");
            file.seek(dEntity.startLocation);
            byte[]buffer =new byte[1024];
            int len;
            long currentLocation =dEntity.startLocation;
            while (len=is.read(buffer)!=-1){
                if(isCancel){
                    break;
                }
                if (isStop){
                    break;
                }
                file.write(buffer,0,len);
                synchronized (DownLoadUtil.this){
                    m,
                }
            }
        }
    }
}*/