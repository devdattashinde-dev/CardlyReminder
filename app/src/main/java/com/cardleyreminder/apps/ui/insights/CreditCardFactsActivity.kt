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
import com.cardleyreminder.apps.data.adapter.FactsAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.network.InsightsApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.InsightsRepository
import com.cardleyreminder.apps.databinding.ActivityFactsBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class CreditCardFactsActivity : BaseActivity<InsightsViewModel, ActivityFactsBinding, InsightsRepository>() {

    lateinit var adapter : FactsAdapter

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

        viewModel.factsDataResponse.observe(
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
        binding.rvFacts.layoutManager = layoutManager

        adapter = FactsAdapter(this)
        binding.rvFacts.adapter = adapter

        progressBar.show()
        viewModel.getFactsData()

    }


    override fun getViewModel() = InsightsViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityFactsBinding.inflate(inflater)

    override fun getActivityRepository() = InsightsRepository(
    remoteDataSource.buildApi(InsightsApi::class.java,pref.token))
}