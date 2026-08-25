package com.cardleyreminder.apps.ui.calender

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
import androidx.recyclerview.widget.GridLayoutManager
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.CalenderAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.network.CalenderApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.CalenderRepository
import com.cardleyreminder.apps.databinding.ActivityCalenderBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class CalenderActivity : BaseActivity<CalenderViewModel, ActivityCalenderBinding, CalenderRepository>() {

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

        val layoutManager = GridLayoutManager(this,5)

        binding.rvCalenderDate.layoutManager = layoutManager
            val calenderAdapter = CalenderAdapter(this)
        binding.rvCalenderDate.adapter =calenderAdapter

        binding.icback.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })

        viewModel.calenderDateResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{

                        if(it.value.status) {
                            calenderAdapter.setStatementClosingDateList(it.value.statementClosingData)
                            calenderAdapter.setPaymentDueDateList(it.value.paymentDueData)
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        binding.ivInfo.setOnClickListener {
            showCalenderIfoDialog()
        }

        progressBar.show()
        viewModel.getCalenderDateData()


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

    override fun getViewModel() = CalenderViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityCalenderBinding.inflate(inflater)

    override fun getActivityRepository() = CalenderRepository(
    remoteDataSource.buildApi(CalenderApi::class.java,pref.token))
}