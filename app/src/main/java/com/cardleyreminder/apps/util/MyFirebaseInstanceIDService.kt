package com.cardleyreminder.apps.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.ui.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Random


class MyFirebaseInstanceIDService:  FirebaseMessagingService(){

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("Notification ","Notification Recived "+message)


        val c = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        val date = simpleDateFormat.format(c.time)


        sendNotification(message, message.getNotification()?.getBody())
    }


    private fun sendNotification(message: RemoteMessage, body: String?) {
        var notificationManager: NotificationManager
        val notificationIntent: Intent

        notificationIntent = Intent(applicationContext, SplashActivity::class.java)

        if (message.getData()
                .containsKey("notification_type")
        ) notificationIntent.putExtra(
            "notification_type",
            message.getData().get("notification_type")
        )

        notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_SINGLE_TOP )

        val random = Random()
        val id = random.nextInt(100 - 25) + 25
        val intent = PendingIntent.getActivity(
            applicationContext, id,
            notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationChannel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                getString(R.string.default_notification_channel_id),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager =
                applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        var title: String? = message.getNotification()?.getTitle()
        if (TextUtils.isEmpty(title)) title = applicationContext.getString(R.string.app_name)

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext, getString(R.string.default_notification_channel_id)
        )
            .setContentTitle(title)
            .setContentText(body)
            .setSound(defaultSoundUri)
            .setLights(-0xff0100, 300, 100)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    applicationContext.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setContentIntent(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_SECRET)
        }

        notificationBuilder.setContentTitle(title)

        notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        try {
            notificationManager.notify(
                System.currentTimeMillis().toInt(),
                notificationBuilder.build()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}