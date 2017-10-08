package com.example.cyc.downloadproject.Data;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.util.Patterns.GOOD_IRI_CHAR;

/**
 * Created by cyc on 17-10-8.
 */

public class WebAdress {
    private String scheme;
    private String host;
    private int port;
    private String path;
    private String authInfo;



    private Context context;

    private final int MATCH_GROUP_SCHEME=1;
    private final int MATCH_GROUP_AUTHORITY=2;
    private final int MATCH_GROUP_HOST=3;
    private final int MATCH_GROUP_PORT=4;
    private final int MATCH_GROUP_PATH=5;
    static Pattern addressPattern =Pattern.compile(
            "(?:(http|https|file)\\:\\/\\/)?"+
                    "(?:([-A-Za-z0-9$_.+!*'(),;?&=]+(?:\\:[-A-Za-z0-9$_.+!*'(),;?&=]+)?)@)?" +
                    "(["+GOOD_IRI_CHAR+"%_-]["+GOOD_IRI_CHAR+"%_\\.-]*|\\[0-9a-fA-F:\\.]+\\])?"+
                    "(?:\\:([0-9]*))?"+
                    "(\\/?[^#]*)?"+
                    ".*",Pattern.CASE_INSENSITIVE);

    public static boolean isUrl (String pInput) {
        if(pInput == null){
            return false;
        }
        String regEx = "^(http|https|ftp)//://([a-zA-Z0-9//.//-]+(//:[a-zA-"
                + "Z0-9//.&%//$//-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
                + "2}|[1-9]{1}[0-9]{1}|[1-9])//.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
                + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-4][0-9]|"
                + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-"
                + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
                + "-9//-]+//.)*[a-zA-Z0-9//-]+//.[a-zA-Z]{2,4})(//:[0-9]+)?(/"
                + "[^/][a-zA-Z0-9//.//,//?//'///////+&%//$//=~_//-@]*)*$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(pInput);
        return matcher.matches();
    }
    public WebAdress(String address,Context context)throws RuntimeException{

        this.context=context;
        scheme="";
        host="";
        port=-1;
        path="/";
        authInfo="";

        Matcher matcher=addressPattern.matcher(address);
        String t;
        if (matcher.matches()){
            t= matcher.group(MATCH_GROUP_SCHEME);
            if (t!=null)scheme=t.toLowerCase(Locale.getDefault());
            t=matcher.group(MATCH_GROUP_AUTHORITY);
            if (t!=null)authInfo=t;
            t=matcher.group(MATCH_GROUP_HOST);
            if (t!=null)host=t;
            t= matcher.group(MATCH_GROUP_PORT);
            if (t!=null&&t.length()>0){
                try {
                    port=Integer.parseInt(t);
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                }

            }
            t=matcher.group(MATCH_GROUP_PATH);
            if (t!=null&&t.length()>0){
                if (t.charAt(0)=='/') {
                    path=t;

                }else {
                    path="/"+t;
                }

            }
        }
        else {
            Toast.makeText(context,"地址错误",Toast.LENGTH_SHORT).show();
            throw new RuntimeException("bad address");
        }
        if (port==443&&scheme.equals("")){
            scheme="https";
        }else if (port==-1){
            if (scheme.equals("https")){
                port=443;
            }
            else port=80;
        }
        if (scheme.equals(""))scheme="http";
    }

    @Override
    public String toString() {
        String mport="";
        if ((port!=443&&scheme.equals("https"))||(port!=80&&scheme.equals("http"))){
            mport=":"+ Integer.toString(port);
        }
        String authinfo="";
        if (authInfo.length()>0){
            authinfo=authInfo+"@";
        }
        return scheme+"://"+authinfo+host+port+path;
    }
}
