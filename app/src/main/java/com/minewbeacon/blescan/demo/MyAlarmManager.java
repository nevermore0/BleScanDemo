package com.minewbeacon.blescan.demo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.app.Activity;

import com.yuliwuli.blescan.demo.R;

import java.util.Calendar;

public class MyAlarmManager {

    public class AlarmService extends Service {
        private static final String TAG = "test";
        private Calendar mCalendar;
        private AlarmManager mAlarmManager;

        public AlarmService() {

        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mCalendar = (Calendar) intent.getSerializableExtra("calendar");
            int noteId = intent.getIntExtra("noteId", 0);

            //设置广播
            Intent intent2 = new Intent();
            intent2.setAction("com.g.android.RING");
            intent2.putExtra("noteId", noteId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent2, 0);

            //根据不同的版本使用不同的设置方法
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
            } else {
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
            }

            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }


        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.g.android.RING")){
                Intent intent2 = new Intent();
                int noteId = intent.getIntExtra("noteId",1);
                //发送通知
                PendingIntent pi = PendingIntent.getActivity(context,0,intent2,0);
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setContentIntent(pi);
                Notification notification = builder.build();
                manager.notify(1,notification);

                //发送一条清空闹铃图标的广播
                Intent intent1 = new Intent("com.g.android.NoColor");
                intent1.putExtra("noteId",noteId);
                context.sendBroadcast(intent1);
            }
        }
    }
}
