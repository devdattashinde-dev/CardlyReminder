package com.cardleyreminder.apps.ui.subscription

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.SubscriptionPlanAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.subscription.TransactionData
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.network.SubscriptionApi
import com.cardleyreminder.apps.data.repository.SubscriptionRepository
import com.cardleyreminder.apps.databinding.ActivitySubscriptionBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.getPaymentIdFromUrl
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class SubscriptionActivity : BaseActivity<SubscriptionViewModel, ActivitySubscriptionBinding, SubscriptionRepository>() {
    override fun getViewModel() = SubscriptionViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivitySubscriptionBinding.inflate(inflater)

    override fun getActivityRepository() = SubscriptionRepository(
        remoteDataSource.buildApi(SubscriptionApi::class.java,pref.token))

    private lateinit var adapter: SubscriptionPlanAdapter
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog,null)
        progressBar = Dialog(this).also {
            it.setContentView(dialogView)
            try {
                it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
        }

        viewModel.showToggle = intent.getBooleanExtra("showToggle",false)

        binding.linlayFreeTrail.visibility = if (viewModel.showToggle == true) View.VISIBLE else View.GONE

        binding.freeTrailSwitchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tvFreeTrail.text = getString(R.string.free_trial_enabled)
            } else {
                binding.tvFreeTrail.text = getString(R.string.not_sure_enable_free_trail)
            }
        }

        showPlanList()

        binding.icBack.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })


        // Handle system back button (Android default back)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(binding.linLayPlanDetails.visibility==View.VISIBLE){
                    showPlanList()
                }else{
                    finish() // Closes activity
                }
            }
        })

        adapter = SubscriptionPlanAdapter(this)
        binding.rvPricingOptions.adapter = adapter
        viewModel.planDataResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            adapter.setData(it.value.data)
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        viewModel.getRedirectUrlResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            it.value.redirectUrl?.let { it1 ->
                                //loadPaymentUrl(it1)
                                showPaymentDialog(it1)
                            }
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        /*viewModel.getTransactionDetailsResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            if(it.value.data?.paymentStatus.equals("Payment Successful")){
                                showPaymentSuccessDialog(it.value.data!!)
                            }else{
                                showPaymentFailedDialog(it.value.data!!)
                            }
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })*/

        /*adapter.setData(arrayListOf<PlanData>(
            PlanData(
                "1",
                "0.99",
                "1",
                "EasyGo",
                "Per Month",
                ""
            ),PlanData(
                "2",
                "6.99",
                "12",
                "Best Value",
                "Yearly",
                "For 1 Year"
            ),
        ))*/


        progressBar.show()
        viewModel.getPlanList()


        binding.btnContinue.setOnClickListener {
            viewModel.selectedPlan = adapter.getSelectedPlan()
            viewModel.selectedPlan.let {
                binding.tvPlangLabel.text = it.heading
                binding.tvPricing.text = "$ ${it.price}"
                binding.tvPlanDuration.text = it.durationLabel
                binding.tvMostPopuler.visibility =  if(it.duration.equals("12"))  View.VISIBLE else View.GONE

                if(it.price!=null && (it.price!!.contains(".",true) && it.price!!.contains(".00")) ){
                    binding.tvCents.visibility = View.GONE
                }else if(it.price!=null && (it.price!!.contains(".",true) && !it.price!!.contains(".00")) ){
                    binding.tvCents.visibility = View.VISIBLE
                }
            }
            showPlanDetails()
        }

        binding.btnJoinNow.setOnClickListener {
            getRedirectUrl()
        }
    }

    private fun getRedirectUrl() {
        progressBar.show()
        viewModel.getRedirectUrl(binding.freeTrailSwitchButton.isChecked)
    }



    private fun showPlanDetails() {
        binding.linLayPlanList.visibility = View.GONE
        binding.linLayPlanDetails.visibility = View.VISIBLE
    }

    private fun showPlanList() {
        binding.linLayPlanList.visibility = View.VISIBLE
        binding.linLayPlanDetails.visibility = View.GONE
    }


    override fun onBackPressed() {
        super.onBackPressed() // Calls the default back action (closes activity)
    }


    private fun showPaymentDialog(redirectUrl: String) {

        Log.d("redirectUrl",redirectUrl)

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.bottom_sheet_payment)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var webView = dialog.findViewById(R.id.webView) as WebView
        var icBack = dialog.findViewById(R.id.icBack) as ImageView

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

                if (url.contains("https://cardlyreminder.com/payment/success")) {

                    val paymentId = getPaymentIdFromUrl(url) ?: "Unknown"

                    dialog.dismiss()

                    val intent = Intent(this@SubscriptionActivity, TransactionDetailsActivity::class.java)
                    intent.putExtra("paymentID", paymentId)
                    startActivity(intent)


                    finish()
                    return true
                } else if (url.contains("https://cardlyreminder.com/payment/error") /*|| url.contains("error")*/) {

                    val paymentId = getPaymentIdFromUrl(url) ?: ""

                    dialog.dismiss()

                    val intent = Intent(this@SubscriptionActivity, TransactionDetailsActivity::class.java)
                    intent.putExtra("paymentID", paymentId)
                    startActivity(intent)

                    finish()
                    return true
                }

                return false // Continue loading
            }
        }

        // ✅ Load the Payment URL
        webView.loadUrl(redirectUrl)


        icBack.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }





    private fun showPaymentFailedDialog(data: TransactionData) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_payment_failed)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // Handle Retry Button
        val btnRetry = dialog.findViewById<TextView>(R.id.btnRetry)
        btnRetry.setOnClickListener {
            dialog.dismiss()
            getRedirectUrl()
        }

        // Handle Cancel Button
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }
}
