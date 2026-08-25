package com.cardleyreminder.apps.util

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.cardleyreminder.apps.ui.home.MainActivity
import me.leolin.shortcutbadger.ShortcutBadger


object BadgeHelper {
    fun setBadge(context: Context?, badgeCount: Int) {
        // Update badge count
        val success = ShortcutBadger.applyCount(context, badgeCount)
        if (!success) {
            // Handle unsupported launchers here
            println("Badge count not supported on this launcher.")
        }

        val extras = Bundle()
        extras.putString("package", context?.packageName)
        extras.putString("class", MainActivity::class.java.name)
        extras.putInt("badgecount", 5)

        context?.contentResolver?.call(
            Uri.parse("content://com.sec.badge/apps"),
            "setAppBadgeCount",
            null,
            extras
        )
    }

    fun clearBadge(context: Context?) {
        // Remove badge count
        ShortcutBadger.removeCount(context)
    }
}