package com.cardleyreminder.apps.ui.insights

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.FAQAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.network.InsightsApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.InsightsRepository
import com.cardleyreminder.apps.databinding.ActivityFaqBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class FaqActivity : BaseActivity<InsightsViewModel, ActivityFaqBinding, InsightsRepository>() {

    lateinit var faqAdapter : FAQAdapter

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

        viewModel.fAQDataResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            if(it.value.data.isNotEmpty()){
                                faqAdapter.setListData(it.value.data)
                            }
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })


        /*if(intent.hasExtra("pageNo")){

            val pageNo = intent.getStringExtra("pageNo")

            if(pageNo=="1"){
                binding.tvLabel.text = getString(R.string.credit_card_tips)
                binding.imageView.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.img_credit_card_tips,null))
            }else if(pageNo=="2"){
                binding.tvLabel.text = getString(R.string.credit_card_facts)
                binding.imageView.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.img_credit_card_facts,null))
            }

            progressBar.show()
            viewModel.getPageData(
                PageDataRequest(
                    pageId = pageNo!!
                )
            )
        }*/

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvFaq.layoutManager = layoutManager

        faqAdapter = FAQAdapter(this)
        binding.rvFaq.adapter = faqAdapter

        progressBar.show()
        viewModel.getFAQData()

    }


    private fun showCalenderIfoDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.calender_info_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val ivClose = dialog.findViewById(R.id.ivClose) as ImageView



        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun getViewModel() = InsightsViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityFaqBinding.inflate(inflater)

    override fun getActivityRepository() = InsightsRepository(
    remoteDataSource.buildApi(InsightsApi::class.java,pref.token))
}