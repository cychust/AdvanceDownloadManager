package com.example.cyc.downloadproject.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cyc.downloadproject.URL.URLDownload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyc on 17-10-5.
 */

public class Sqlite {
    private DBHelper dbHelper;
    public Sqlite(Context context){
        dbHelper=new DBHelper(context);
    }

    public synchronized void saveDownloadInfos(URLDownload urlDownload){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        try {

                String sql="insert into download(url,state,downtime,filesize,downloadlength,oldtime) values (?,?,?,?,?,?)";
                Object[] bindArgs = { urlDownload.getURLaddress(),urlDownload.getState(),urlDownload.getDownTime()
                ,urlDownload.getFileSize(),urlDownload.getDownloadLength()
                ,urlDownload.getOldTime()};
                database.execSQL(sql, bindArgs);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null!=database){
                database.close();
            }
        }
    }
    public synchronized List<URLDownload> getURLDoanload(){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        List<URLDownload>list=new ArrayList<>();
        Cursor cursor=null;
        try {
            String sql="select url,state,downtime,filesize,downloadlength,oldtime from download";
            cursor=database.rawQuery(sql,null);
            while (cursor.moveToNext()){
                URLDownload urlDownload=new URLDownload(cursor.getString(0),cursor.getInt(1),
                        cursor.getLong(2),cursor.getInt(3),cursor.getInt(4),cursor.getLong(5));
                list.add(urlDownload);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (null!=database){
                database.close();
            }
            if (cursor!=null){
                cursor.close();
            }
        }
        return list;
    }

    public synchronized long getURLDoanloadTime(String url){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        long time=0;
        Cursor cursor=null;
        try {
            String sql="select url,downtime from download";
            cursor=database.rawQuery(sql,null);
            while (cursor.moveToNext()){
                String url1=cursor.getString(0);
                if (url.equals(url1)){
                    time=cursor.getLong(1);
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (null!=database){
                database.close();
            }
            if (cursor!=null){
                cursor.close();
            }
        }
        return time;
    }
    public synchronized boolean isHasDownloadInfos(String url) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        int count = -1;
        Cursor cursor = null;
        try {
            // 返回指定列不同值的数目
            String sql = "select count(*)  from download where url=?";
            cursor = database.rawQuery(sql, new String[] { url });
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
            if (null != cursor) {
                cursor.close();
            }
        }
        return count == 0;
    }
    public synchronized ArrayList<URLDownload> getURLDoanloaded(){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        ArrayList<URLDownload>list=new ArrayList<>();
        Cursor cursor=null;
        try {
            String sql="select url,state,downtime,filesize,downloadlength,oldtime from download";
            cursor=database.rawQuery(sql,null);
            while (cursor.moveToNext()){
                URLDownload urlDownload=new URLDownload(cursor.getString(0),cursor.getInt(1),
                        cursor.getLong(2),cursor.getInt(3),cursor.getInt(4),cursor.getLong(5));
                if (urlDownload.state==3) {
                    list.add(urlDownload);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (null!=database){
                database.close();
            }
            if (cursor!=null){
                cursor.close();
            }
        }
        return list;
    }

    public synchronized ArrayList<URLDownload> getURLDoanloading(){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        ArrayList<URLDownload>list=new ArrayList<>();
        Cursor cursor=null;
        try {
            String sql="select url,state,downtime,filesize,downloadlength,oldtime from download";
            cursor=database.rawQuery(sql,null);
            while (cursor.moveToNext()){
                URLDownload urlDownload=new URLDownload(cursor.getString(0),cursor.getInt(1),
                        cursor.getLong(2),cursor.getInt(3),cursor.getInt(4),cursor.getLong(5));
                if (urlDownload.state!=3) {
                    list.add(urlDownload);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (null!=database){
                database.close();
            }
            if (cursor!=null){
                cursor.close();
            }
        }
        return list;
    }
    public synchronized int getURLDoanloadLen(String url){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        int length=0;
        Cursor cursor=null;
        try {
            String sql="select url,downloadlength from download";
            cursor=database.rawQuery(sql,null);
            while (cursor.moveToNext()){

                if (cursor.getString(0).equals(url)){
                    length= cursor.getInt(1);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (null!=database){
                database.close();
            }
            if (cursor!=null){
                cursor.close();
            }
        }
        return length;
    }

    public  synchronized URLDownload query(String url) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        URLDownload urlDownload=null;
        try {
            String sql="select url,state,downtime,filesize,downloadlength,oldtime from Download";
            cursor=database.rawQuery(sql,null);
            while (cursor.moveToNext()) {
                urlDownload = new URLDownload(cursor.getString(0), cursor.getInt(1),
                        cursor.getLong(2), cursor.getInt(3), cursor.getInt(4), cursor.getLong(5));
                if (urlDownload.URLaddress.equals(url)){
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (null!=database){
                database.close();
            }
            if (null!=cursor){
                cursor.close();
            }

        }
        return urlDownload;
    }
    public synchronized void updataStateByUrl(String url,int state) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            String sql = "update download set state=? where url=?";
            Object[] bindArgs = { state, url };
            database.execSQL(sql, bindArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
        }
    }
    public synchronized void updateURLDownload(List<URLDownload> list) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            for (URLDownload urlDownload : list) {
                String sql = "update download set downloadlength=?,state=? where url=?";
                Object[] bindArgs = { urlDownload.getDownloadLength(),
                        urlDownload.getState(), urlDownload.getURLaddress() };
                database.execSQL(sql, bindArgs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
        }
    }
    public synchronized void updateURLDownloadComplete(String url,int completeSize) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            String sql = "update download set downloadlength=? where url=?";
            Object[] bindArgs = { completeSize, url };
            database.execSQL(sql, bindArgs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
        }
    }
    public synchronized void updateURLDownloadTime(String url,long time) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            String sql = "update download set downtime=? where url=?";
            Object[] bindArgs = { time, url };
            database.execSQL(sql, bindArgs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
        }
    }
    public synchronized void deleteURLDownload(String url) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            database.delete("download", "url=?", new String[] { url });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != database) {
                database.close();
            }
        }
    }
    public void closeDB() {
        if (null != dbHelper) {
            dbHelper.close();
        }
    }
}
