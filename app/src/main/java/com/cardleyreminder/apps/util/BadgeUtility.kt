package com.cardleyreminder.apps.util


import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.ui.home.MainActivity
import me.leolin.shortcutbadger.ShortcutBadger
import java.util.Locale


object BadgeUtility {
    fun setBadge(context: Context, badgeCount: Int) {
        val manufacturer = Build.MANUFACTURER.lowercase(Locale.getDefault())
        if (manufacturer.contains("samsung")) {
            applySamsungBadge(context, badgeCount)
        } else if (manufacturer.contains("xiaomi")) {
            applyXiaomiBadge(context, badgeCount)
        } else {
            applyGenericBadge(context, badgeCount)
        }
    }

    private fun applySamsungBadge(context: Context, badgeCount: Int) {
        try {
            // Samsung-specific badge implementation
            val extras = Bundle()
            extras.putString("package", context.packageName)
            extras.putString("class", MainActivity::class.java.name)
            extras.putInt("badgecount", badgeCount)
            context.contentResolver.call(
                Uri.parse("content://com.sec.badge/apps"),
                "setAppBadgeCount",
                null,
                extras
            )

            val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
            intent.putExtra("badge_count", badgeCount) // Set badge count
            intent.putExtra("badge_count_package_name", context.packageName)
            intent.putExtra("badge_count_class_name", MainActivity::class.java.name)
            context.sendBroadcast(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun applyXiaomiBadge(context: Context, badgeCount: Int) {
        try {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification: Notification = Notification.Builder(context)
                .setSmallIcon(R.drawable.logo_icon)
                .setContentTitle("Title")
                .setContentText("Text")
                .setNumber(badgeCount)
                .build()
            notificationManager.notify(0, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun applyGenericBadge(context: Context, badgeCount: Int) {
        try {
            ShortcutBadger.applyCount(context, badgeCount)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}