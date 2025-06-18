package com.example.weather.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.weather.R;
import com.example.weather.utils.TipGenerator;
import com.example.weather.view.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotificationReceiver", "onReceive 실행됨");

        int temp = intent.getIntExtra("temp", 0);
        String desc = intent.getStringExtra("desc");
        Log.d("NotificationReceiver", "받은 temp: " + temp + ", desc: " + desc);

        String tip = TipGenerator.generateTip(temp, desc);

        String channelId = "weather_tip_channel";
        String channelName = "날씨 팁 알림";

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(channel);
        }

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher) // ✅ 반드시 존재하는 아이콘
                .setContentTitle("오늘의 날씨 팁")
                .setContentText(tip)
                .setContentIntent(pi)
                .setAutoCancel(true);

        manager.notify(101, builder.build());
    }
}
