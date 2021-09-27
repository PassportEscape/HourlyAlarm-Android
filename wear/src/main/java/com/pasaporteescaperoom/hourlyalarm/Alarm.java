package com.pasaporteescaperoom.hourlyalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


public class Alarm extends BroadcastReceiver {
    static final String HOURLY_TAG = "HOURLY_TAG:";

    static final WorkRequest alarmWorkRequest =
            new OneTimeWorkRequest.Builder(AlarmWorker.class)
                    .build();
    private static final int NOT_USED = 0;
    Context context;
    AlarmManager am;
    Intent i;
    PendingIntent pi;

    private static Uri alarmSound;
    // Vibration pattern long array
    private final long[] pattern = {100, 300, 300, 300};
    private NotificationManager mNotificationManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ALARM_SERVICE", "On Receive");

        final MediaPlayer mp = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mp.setLooping(false);
        mp.start();

        /*Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra("TEST", intent.getStringExtra("TEST"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }*/


        boolean acquired = false;
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), HOURLY_TAG);

            wl.setReferenceCounted(true);
            if (wl.isHeld()) {
                wl.acquire();
                acquired = true;
                Toast.makeText(context, "Alarm not held", Toast.LENGTH_SHORT).show(); // For example

            }

            /*Intent intentService = new Intent(context, AlarmService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intentService);
            } else {
                context.startService(intentService);
            }*/
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.EFFECT_DOUBLE_CLICK));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }

            if (acquired) {

                //     am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 6000, pi);
                wl.release();
            }
            Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_SHORT).show(); // For example

        } catch (Exception e) {
            Toast.makeText(context, "ERROR!!!", Toast.LENGTH_SHORT).show(); // For example
            Log.e("ALARM_SERVICE", e.toString());
        }
    }

    public void setAlarm(Context context) {

        am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        i = new Intent(context, Alarm.class);
        pi = PendingIntent.getBroadcast(context, 0, i, 0);

        this.context = context;
        Log.i("ALARM_SERVICE", "Setting alarm");
        Toast.makeText(context, "Alarm SET!", Toast.LENGTH_SHORT).show(); // For example

        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi); // Millisec * Second * Minute
        //showNotification(context);
        //am.set(AlarmManager.RTC_WAKEUP, 30000, pi); // Millisec * Second * Minute
        //   am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 60000, pi);
    }

    public void cancelAlarm(Context context) {
        Toast.makeText(context, "Alarm UNSET!", Toast.LENGTH_SHORT).show(); // For example

        Log.i("ALARM_SERVICE", "Cancelling alarm");
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


    public void showNotification(Context context) {
        Log.i("ALARM_SERVICE", "notification");

        int notificationId = 001;
        // The channel ID of the notification.
        String id = "passport_hourly_clock";
        // Build intent for notification content
        final Intent emptyIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, emptyIntent, 0);

        // Notification channel ID is ignored for Android 7.1.1
        // (API level 25) and lower.
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, id)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("TEST NOT")
                        .setContentText("TEST NOT CONT")
                        .setContentIntent(pendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        // Issue the notification with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());

    }
}
