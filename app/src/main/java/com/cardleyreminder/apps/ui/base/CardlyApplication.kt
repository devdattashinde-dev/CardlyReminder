package com.cardleyreminder.apps.ui.base


import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.facebook.FacebookSdk;

class CardlyApplication : Application(), ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override fun onCreate() {
        super.onCreate()

        Firebase.messaging.subscribeToTopic("CardlyGroupPush")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to topic successfully")
                } else {
                    Log.e("FCM", "Topic subscription failed", task.exception)
                }
            }


    }

}