package com.example.kpmall;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class TrackUserTime extends Service {

    private IBinder binder = new LocalBinder();
    private boolean shouldFinish;
    private int seconds = 0;
    private GroceryItem item;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        TrackUserTime getService(){
            return TrackUserTime.this;
        }
    }

    private void trackTime(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!shouldFinish){
                    try{
                        Thread.sleep(1000);
                        seconds++;
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void setItem(GroceryItem item){
        this.item = item;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        shouldFinish = true;
        int minutes = seconds/60;
        if (minutes>0){
            Utils.ChangeUserPoint(this,item,minutes);
        }
    }
}
