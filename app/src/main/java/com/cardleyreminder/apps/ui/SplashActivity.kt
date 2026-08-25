package com.cardleyreminder.apps.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cardleyreminder.apps.UserPreferences
import com.cardleyreminder.apps.databinding.ActivitySplashBinding
import com.cardleyreminder.apps.ui.auth.AuthActivity
import com.cardleyreminder.apps.ui.home.MainActivity
import com.cardleyreminder.apps.util.startNewActivity
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {
    private var token: String?=null
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Timer().schedule(timerTask {
            val pref = UserPreferences(this@SplashActivity)
            val activity =
                if (pref.isLoggedIn) MainActivity::class.java else AuthActivity::class.java
            startNewActivity(activity)
        }, 4000)
    }
}