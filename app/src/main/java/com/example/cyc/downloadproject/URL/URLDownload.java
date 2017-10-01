package com.example.cyc.downloadproject.URL;

import android.content.Context;

import java.io.File;

/**
 * Created by cyc on 17-10-1.
 */

public class URLDownload{
    public String URLaddress;
    public long fileSize;
    int category;
    public int threadId;
    public long startLocation;
    public long endLocation;
   public File tempFile;
   public Context context;
   public int typeStatus;
    public URLDownload(Context context,long fileSize,String URLaddress,File file,int threadId,long startLocation
    ,long endLocation,int status){
        this.context=context;
        this.endLocation=endLocation;
        this.fileSize=fileSize;
        this.startLocation=startLocation;
        this.threadId=threadId;
        this.URLaddress=URLaddress;
        this.tempFile=file;
        this.typeStatus=status;
    }
}
