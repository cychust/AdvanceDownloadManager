package com.example.cyc.downloadproject;

import java.net.URI;

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
        suffix=filename.substring(filename.lastIndexOf("."));
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
}
