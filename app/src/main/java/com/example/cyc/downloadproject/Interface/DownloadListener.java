package com.example.cyc.downloadproject.Interface;

/**
 * Created by cyc on 17-10-1.
 */

public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPause();
    void onCanceled();
}
