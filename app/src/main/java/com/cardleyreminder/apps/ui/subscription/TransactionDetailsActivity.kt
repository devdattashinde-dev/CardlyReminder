package com.cardleyreminder.apps.ui.subscription

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.subscription.TransactionData
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.network.SubscriptionApi
import com.cardleyreminder.apps.data.repository.SubscriptionRepository
import com.cardleyreminder.apps.databinding.ActivityTransactionDetailsBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.convertDate
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class TransactionDetailsActivity : BaseActivity<SubscriptionViewModel, ActivityTransactionDetailsBinding, SubscriptionRepository>() {
    override fun getViewModel() = SubscriptionViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityTransactionDetailsBinding.inflate(inflater)

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

        intent.getStringExtra("paymentID")?.let {
            progressBar.show()
            viewModel.getTransactionDetails(it)
        }
    }

    private fun setData(data: TransactionData) {
        binding.tvPaymentStatus.text = data.paymentStatus ?: ""

        val isPaymentSuccessful = data.paymentStatus == "Payment Successful"
        val iconRes = if (isPaymentSuccessful) R.drawable.ic_successfull else R.drawable.ic_failed
        binding.ivTransactionStatus.setImageDrawable(ResourcesCompat.getDrawable(resources, iconRes, null))

        binding.tvTransactionId.text = data.transactionId ?: ""
        binding.tvPaymentTime.text = convertDate(this, data.createdDate ?: "", "yyyy/MM/dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss")
        binding.tvAmount.text = "${data.currency}${data.amount}"

        if (isPaymentSuccessful) {
            binding.tvSubscriptionId.text = data.subscription ?: ""
            binding.tvSubStartDate.text = convertDate(this, data.start ?: "", "yyyy-MM-dd", "dd-MM-yyyy")
            binding.tvSubEndDate.text = convertDate(this, data.end ?: "", "yyyy-MM-dd", "dd-MM-yyyy")

            binding.tvSubIdLabel.visibility = View.VISIBLE
            binding.tvSubscriptionId.visibility = View.VISIBLE
            //binding.linLaySubDate.visibility = View.VISIBLE
            //binding.linLaySubDateValue.visibility = View.VISIBLE

        } else {
            binding.tvSubIdLabel.visibility = View.GONE
            binding.tvSubscriptionId.visibility = View.GONE
            //binding.linLaySubDate.visibility = View.GONE
            //binding.linLaySubDateValue.visibility = View.GONE
        }
    }

}
