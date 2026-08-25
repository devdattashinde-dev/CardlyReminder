package com.cardleyreminder.apps.ui.insights

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.VideoAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.network.InsightsApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.InsightsRepository
import com.cardleyreminder.apps.databinding.ActivityVideoBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError
import com.google.android.material.tabs.TabLayout

class VideoActivity : BaseActivity<InsightsViewModel, ActivityVideoBinding, InsightsRepository>() {

    lateinit var adapter : VideoAdapter

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

        viewModel.videoDataResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            if(it.value.data!=null){
                                viewModel.videoListData = it.value.data!!
                                adapter.setListData(it.value.data!!.creditCardLiteracyData)
                            }
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {

                    if(tab?.position==0){
                        adapter.setListData(viewModel.videoListData.creditCardLiteracyData)
                    }else if(tab?.position==1){
                        adapter.setListData(viewModel.videoListData.promotionData)
                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            }
        )

        binding.ivInfo.setOnClickListener {
            showIfoDialog()
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvVideo.layoutManager = layoutManager

        adapter = VideoAdapter(this)
        binding.rvVideo.adapter = adapter

        progressBar.show()
        viewModel.getVideoList()

    }


    private fun showIfoDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.video_info_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val ivClose = dialog.findViewById(R.id.ivClose) as ImageView

        val textView  = dialog.findViewById(R.id.tvDisclaimer) as TextView

        val disclaimerHtml = getString(R.string.disclaimer_html)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(disclaimerHtml, Html.FROM_HTML_MODE_COMPACT)
        } else {
            textView.text = Html.fromHtml(disclaimerHtml)
        }


        // Enable clickable links
        textView.movementMethod = LinkMovementMethod.getInstance()

        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun getViewModel() = InsightsViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityVideoBinding.inflate(inflater)

    override fun getActivityRepository() = InsightsRepository(
    remoteDataSource.buildApi(InsightsApi::class.java,pref.token))
}