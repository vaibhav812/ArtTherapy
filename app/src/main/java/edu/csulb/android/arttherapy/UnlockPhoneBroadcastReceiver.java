package edu.csulb.android.arttherapy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by vaibhavjain on 3/18/2017.
 */

public class UnlockPhoneBroadcastReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_REQUEST = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Intent activityIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_REQUEST, activityIntent, 0);
            Notification builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.brush)
                    .setContentTitle("Create a drawing!")
                    .setContentText("Come let's create a new drawing!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder);
        }
    }
}
