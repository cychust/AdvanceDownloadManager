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
    public int fileSize=0;
    public int downloadLength=0;
    public long oldTime;
    public URLDownload(String URLaddress,int state,int downloadLength,int fileSize,long oldTime){

        this.downloadLength=downloadLength;
        this.fileSize=fileSize;
        this.state=state;
        this.URLaddress=URLaddress;
        this.oldTime=oldTime;


    }

    public URLDownload(String URLaddress,int state,long downTime,int fileSize,int downloadLength,long oldTime){
        this.URLaddress=URLaddress;
        this.fileSize=fileSize;
        this.oldTime=oldTime;
        this.state=state;
        this.downTime=downTime;
        this.downloadLength=downloadLength;
    }
    public void setDownTime(long downTime) {
        this.downTime = downTime;
    }

    public void setOldTime(long oldTime) {
        this.oldTime = oldTime;
    }

    public String getFileName(){
        String filename = URLaddress.substring(URLaddress.lastIndexOf("/") + 1);
        return filename;
    }
    public void setFileSize(int fileSize){
        this.fileSize=fileSize;
    }
    public void setURLaddress(String urLaddress){
        this.URLaddress=urLaddress;
    }




    public void setState(int state) {
        this.state = state;
    }

    public void setDownloadLength(int downloadLength) {
        this.downloadLength = downloadLength;
    }


    public int getState() {
        return state;
    }

    public int getDownloadLength() {
        return downloadLength;
    }

    public long getDownTime() {
        return downTime;
    }

    public int getFileSize() {
        return fileSize;
    }

    public long getOldTime() {
        return oldTime;
    }

    public String getURLaddress() {
        return URLaddress;
    }


}
