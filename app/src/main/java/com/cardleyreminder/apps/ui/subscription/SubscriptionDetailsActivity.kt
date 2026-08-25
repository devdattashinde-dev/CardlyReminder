package com.cardleyreminder.apps.ui.subscription

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.subscription.TransactionData
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.network.SubscriptionApi
import com.cardleyreminder.apps.data.repository.SubscriptionRepository
import com.cardleyreminder.apps.databinding.ActivitySubscriptionDetailsBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.convertDate
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError
import com.cardleyreminder.apps.util.toast

class SubscriptionDetailsActivity : BaseActivity<SubscriptionViewModel, ActivitySubscriptionDetailsBinding, SubscriptionRepository>() {
    override fun getViewModel() = SubscriptionViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivitySubscriptionDetailsBinding.inflate(inflater)

    override fun getActivityRepository() = SubscriptionRepository(
        remoteDataSource.buildApi(SubscriptionApi::class.java,pref.token))


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


        binding.icBack.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })


        viewModel.getTransactionDetailsResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {

                            setData(it.value.data!!)

                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        viewModel.cancelSubscriptionResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {

                            binding.root.toast(it.value.message!!)
                            binding.tvCancelSubscription.visibility = View.GONE
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        intent.getStringExtra("paymentID")?.let {
            viewModel.paymentId = it
            progressBar.show()
            viewModel.getTransactionDetails(it)


        }

        binding.tvCancelSubscription.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Subscription")
                .setMessage("Are you sure you want to cancel subscription?")
                .setPositiveButton("Yes") { _, _ ->
                    progressBar.show()
                    viewModel.cancelSubscription(viewModel.paymentId)
                }
                .setNegativeButton("No", null) // Dismiss the dialog
                .show()
        }

    }

    private fun setData(data: TransactionData) {
        //binding.tvPaymentStatus.text = data.paymentStatus ?: ""



        binding.tvAmount.text = "${data.currency}${data.amount}"

        binding.tvSubStartDate.text = convertDate(this, data.start ?: "", "yyyy-MM-dd", "dd-MMMM-yyyy")
        binding.tvSubEndDate.text = convertDate(this, data.end ?: "", "yyyy-MM-dd", "dd-MMMM-yyyy")

        binding.tvPlanName.text = data.planName?:""
        if(data.subscriptionStatus.equals("canceled",true)){
            binding.tvCancelSubscription.visibility =  View.GONE
            binding.tvCancelSubscriptionLabel.visibility =  View.VISIBLE
            binding.tvSubscriptionEndOn.visibility =  View.VISIBLE

            binding.tvSubscriptionEndOn.text = "Your subscription ends on ${convertDate(this, data.end ?: "", "yyyy-MM-dd", "dd-MMMM-yyyy")}"
        }else{
            binding.tvCancelSubscription.visibility =  View.VISIBLE
            binding.tvCancelSubscriptionLabel.visibility =  View.GONE
            binding.tvSubscriptionEndOn.visibility =  View.GONE
        }



    }

}
