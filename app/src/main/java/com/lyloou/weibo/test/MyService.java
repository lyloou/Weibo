package com.lyloou.weibo.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = "IWeibo";
    IBinder infoBind;

    @Override
    public IBinder onBind(Intent intent) {
        return infoBind;
    }

    class MyBinder extends Binder {
        public void print(String str) {
            MyService.this.print(str);
        }
    }

    public void print(String str) {
        Log.d(TAG, str);
    }

    @Override
    public void onCreate() {
        print("onCreate");
        infoBind = new MyBinder();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        print("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        print("onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        print("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        print("onRebind");
        super.onRebind(intent);
    }
}
