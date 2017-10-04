package com.example.cyc.downloadproject.URL;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

/**
 * Created by cyc on 17-10-1.
 */

public class URLDownload implements Serializable{
    public String URLaddress;
    public int state=1;
    public long downTime=0;
    public long fileSize=0;
  // public File tempFile;
 //  public Context context;
    public double speed=0.0;
    public long downloadLength=0;
    public boolean done;
    public long oldTime;
    public URLDownload(String URLaddress,int state,long downloadLength,long fileSize,long oldTime){

        this.downloadLength=downloadLength;
        this.fileSize=fileSize;
        this.state=state;
        this.URLaddress=URLaddress;
        done=downloadLength==fileSize?true:false;
        this.oldTime=oldTime;
        if (done){
            state=3;
        }

    }

    public void setDownTime(long downTime) {
        this.downTime = downTime;
    }

    public void setOldTime(long oldTime) {
        this.oldTime = oldTime;
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

 /*   @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(URLaddress);
        public int state=1;
        public long downTime=0;
        public long fileSize=0;
        // public File tempFile;
        //  public Context context;
        public double speed=0.0;
        public long downloadLength=0;
        public boolean done;
        public long oldTime;
        parcel.writeInt(state);
        parcel.writeLong(downTime);
        parcel.writeLong(fileSize);
        parcel.writeDouble(speed);
        parcel.writeLong(downloadLength);
        parcel.
    }*/
}
