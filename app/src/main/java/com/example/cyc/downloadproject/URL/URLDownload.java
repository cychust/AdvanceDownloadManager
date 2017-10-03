package com.example.cyc.downloadproject.URL;

import android.content.Context;
import android.os.Parcel;

import java.io.File;
import java.io.Serializable;

/**
 * Created by cyc on 17-10-1.
 */

public class URLDownload implements Serializable{
    public String URLaddress;
    public int state=1;
    public long fileSize=0;
  // public File tempFile;
 //  public Context context;
    public double speed;
    public long downloadLength=0;
    public boolean done;
    public URLDownload(String URLaddress,int state,long downloadLength,long fileSize){

        this.downloadLength=downloadLength;
        this.fileSize=fileSize;
        this.state=state;
        this.URLaddress=URLaddress;
        done=downloadLength==fileSize?true:false;
        if (done){
            state=3;
        }

    }
    public void setFileSize(long fileSize){
        this.fileSize=fileSize;
    }
    public void setURLaddress(String urLaddress){
        this.URLaddress=urLaddress;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }
}
