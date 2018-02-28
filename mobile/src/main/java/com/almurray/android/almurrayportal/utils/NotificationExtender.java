package com.almurray.android.almurrayportal.utils;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.almurray.android.almurrayportal.R;
import com.onesignal.OSNotificationPayload;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import me.leolin.shortcutbadger.ShortcutBadger;

public class NotificationExtender extends NotificationExtenderService {
    public NotificationExtender() {
    }

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                Bitmap bg = BitmapFactory.decodeResource(getApplication().getResources(),
                    R.mipmap.ic_launcher);
//                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
//                SharedPreferences.Editor pE = prefs.edit();
//                Integer nON = prefs.getInt("nON", 0);
//                ShortcutBadger.applyCount(getApplicationContext(), nON+1);
//                pE.putInt("nON",nON+1);
//                pE.commit();
                return builder.setLargeIcon(bg).setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true).setPriority(Notification.PRIORITY_MAX);
            }
        };
        return true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
