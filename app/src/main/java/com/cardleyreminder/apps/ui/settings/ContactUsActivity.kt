package com.cardleyreminder.apps.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cardleyreminder.apps.databinding.ActivityContactUsBinding


class ContactUsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.icBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding.linLayEmail.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:wecare@cardlyreminder.com") // only email apps should handle this

            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("wecare@cardlyreminder.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "We value your satisfaction")
            startActivity(intent)

            /*val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:wecare@cardlyreminder.com") // Only email apps should handle this
                putExtra(Intent.EXTRA_SUBJECT, "We value your satisfaction") // Subject
                putExtra(Intent.EXTRA_TEXT, "") // Email body
            }

            startActivity(emailIntent)*/

        })

        binding.ivInstagram.setOnClickListener {
            openUrl("https://www.instagram.com/cardlyreminder/")
        }

        binding.ivTiktok.setOnClickListener {
            openUrl("https://www.tiktok.com/@cardlyreminder")
        }

        binding.ivFacebook.setOnClickListener {
            openUrl("https://www.facebook.com/cardlyreminder")
        }

        binding.ivTwitter.setOnClickListener {
            openUrl("https://x.com/CardlyReminder")
        }

        binding.ivLinkedin.setOnClickListener {
            openUrl("https://www.linkedin.com/company/cardlyreminder")
        }

        binding.ivYouTube.setOnClickListener {
            openUrl("https://www.youtube.com/@CardlyReminder")
        }


    }

    fun Context.openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensures it works in all cases
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No browser found to open the URL", Toast.LENGTH_SHORT).show()
        }
    }
}