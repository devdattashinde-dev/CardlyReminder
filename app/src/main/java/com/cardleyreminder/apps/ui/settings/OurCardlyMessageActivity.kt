package com.cardleyreminder.apps.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cardleyreminder.apps.databinding.ActivityCardlyMessageBinding

class OurCardlyMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardlyMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardlyMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}