package com.cardleyreminder.apps.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cardleyreminder.apps.databinding.ActivityOurStoryBinding

class OurStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOurStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOurStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}