package com.enernet.eg.building;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class NeverDieService extends Service {

    public static Intent serviceIntent;

    public void onCreate(){
        Log.i("Service", "Neverdie service started");
    }


    @Override
    public void onTaskRemoved(Intent serviceIntent){
        Log.i("Service", "Neverdie service removed");
        setAlarmTimer();
    }

    public void onDestroy() {
        super.onDestroy();

        Log.i("Service", "Neverdie service destroyed");

        serviceIntent = null;
        setAlarmTimer();
        Thread.currentThread().interrupt();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void setAlarmTimer() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }

}
