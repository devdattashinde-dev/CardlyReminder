package com.cardleyreminder.apps.ui.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cardleyreminder.apps.databinding.ActivityRateCardlyBinding


class RateCardlyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRateCardlyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateCardlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding.tvContinue.setOnClickListener {
            val uri = Uri.parse("market://details?id=$packageName")
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show()
            }
        }

    }
}