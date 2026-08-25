package com.cardleyreminder.apps.ui.subscription

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cardleyreminder.apps.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentBottomSheetDialog(
    private val redirectUrl: String,
    private val onPaymentSuccess: () -> Unit,
    private val onPaymentFailure: () -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.bottom_sheet_payment, null)
        dialog.setContentView(view)

        webView = view.findViewById(R.id.webView)

        // ✅ Configure WebView
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()

                if (url.contains("success")) {

                    Log.d("PaymentSuccess",url)
                    onPaymentSuccess.invoke()  // ✅ Handle Payment Success
                    dismiss()
                    return true
                } else if (url.contains("failed") || url.contains("error")) {

                    Log.d("PaymentFailed",url)

                    onPaymentFailure.invoke()  // ✅ Handle Payment Failure
                    dismiss()
                    return true
                }

                return false // Continue loading
            }
        }

        // ✅ Load the Payment URL
        webView.loadUrl(redirectUrl)

        return dialog
    }
}