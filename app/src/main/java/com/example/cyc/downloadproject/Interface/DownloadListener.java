package com.example.cyc.downloadproject.Interface;

/**
 * Created by cyc on 17-10-1.
 */

public interface DownloadListener {
    void onProgress(Integer progress,String ur);
    void onSuccess(String url);
    void onFailed(String url);
    void onPaused(String url);
    void onCanceled(String url);
    void onStart(long contentLength);
}
