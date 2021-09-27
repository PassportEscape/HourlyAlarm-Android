package com.pasaporteescaperoom.hourlyalarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmService extends Service
{
   // Alarm alarm = new Alarm();
    private MediaPlayer mediaPlayer;
    Vibrator v;

    public void onCreate()
    {
        super.onCreate();
        Log.i("ALARM_SERVICE", "On Create");
        //mediaPlayer = MediaPlayer.create(this, R.);
        //mediaPlayer.setLooping(false);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //alarm.setAlarm(this);
        Log.i("ALARM_SERVICE", "On Start Command");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.EFFECT_DOUBLE_CLICK));
        } else {
            //deprecated in API 26
            v.vibrate(1000);
        }

        //showNotification(intent);
        return START_STICKY;
    }

    public void showNotification(Intent intent) {
        final Intent emptyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, emptyIntent, 0);

        String alarmTitle = String.format("%s Alarm", intent.getStringExtra("TEST"));

        Notification notification = new NotificationCompat.Builder(this, "CLOCK_001")
                .setContentTitle(alarmTitle)
                .setContentText("Ring Ring .. Ring Ring")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        //mediaPlayer.start();

    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        Log.i("ALARM_SERVICE", "On Start");
        //alarm.setAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.i("ALARM_SERVICE", "On Bind");
        return null;
    }
}