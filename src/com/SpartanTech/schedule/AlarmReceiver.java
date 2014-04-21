package com.SpartanTech.schedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
	public AlarmReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		showNotification(context);
	}

	private void showNotification(Context context) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, DueAssignmentActvity.class), 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.example_picture)
				.setContentTitle("Assignment Notification")
				.setContentText("Tap to view upcoming assignments");
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}

}