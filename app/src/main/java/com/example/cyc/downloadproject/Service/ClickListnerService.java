package com.example.cyc.downloadproject.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by cyc on 17-10-7.
 */

public class ClickListnerService extends Service {
    public ClickListnerService(){

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
