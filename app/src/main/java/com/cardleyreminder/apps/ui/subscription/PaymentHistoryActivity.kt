package com.cardleyreminder.apps.ui.subscription

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.PaymentHistoryAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.network.SubscriptionApi
import com.cardleyreminder.apps.data.repository.SubscriptionRepository
import com.cardleyreminder.apps.databinding.ActivityPaymentHistoryBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class PaymentHistoryActivity : BaseActivity<SubscriptionViewModel, ActivityPaymentHistoryBinding, SubscriptionRepository>() {
    override fun getViewModel() = SubscriptionViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityPaymentHistoryBinding.inflate(inflater)

    override fun getActivityRepository() = SubscriptionRepository(
        remoteDataSource.buildApi(SubscriptionApi::class.java,pref.token))

    private lateinit var adapter: PaymentHistoryAdapter


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



        adapter = PaymentHistoryAdapter(this)
        binding.rvPricingOptions.adapter = adapter
        viewModel.getPaymentHistoryResponse.observe(
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

        adapter.onItemClick = {
            val intent = Intent(this@PaymentHistoryActivity, TransactionDetailsActivity::class.java)
            intent.putExtra("paymentID", it.id)
            startActivity(intent)
        }


        progressBar.show()
        viewModel.getPaymentHistory()



    }

}
