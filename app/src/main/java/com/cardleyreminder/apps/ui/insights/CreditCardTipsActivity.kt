package com.cardleyreminder.apps.ui.insights

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.TipsAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.network.InsightsApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.InsightsRepository
import com.cardleyreminder.apps.databinding.ActivityTipsBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class CreditCardTipsActivity : BaseActivity<InsightsViewModel, ActivityTipsBinding, InsightsRepository>() {

    lateinit var adapter : TipsAdapter

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


        binding.icback.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })

        viewModel.tipsDataResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            if(it.value.data.isNotEmpty()){
                                adapter.setListData(it.value.data)
                            }
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })


        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvTips.layoutManager = layoutManager

        adapter = TipsAdapter(this)
        binding.rvTips.adapter = adapter

        progressBar.show()
        viewModel.getTipsData()

    }


    override fun getViewModel() = InsightsViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityTipsBinding.inflate(inflater)

    override fun getActivityRepository() = InsightsRepository(
    remoteDataSource.buildApi(InsightsApi::class.java,pref.token))
}