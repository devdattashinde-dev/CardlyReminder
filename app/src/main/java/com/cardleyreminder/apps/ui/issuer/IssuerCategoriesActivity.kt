package com.cardleyreminder.apps.ui.issuer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.IssuerCategoriesAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.network.IssuerApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.IssuerRepository
import com.cardleyreminder.apps.databinding.ActivityIssuerCategoriseBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError
import com.cardleyreminder.apps.util.showInfoDialog

class IssuerCategoriesActivity : BaseActivity<IssuerViewModel, ActivityIssuerCategoriseBinding, IssuerRepository>() {

    private var isCardNetwork: Boolean = false

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

        val layoutManager = LinearLayoutManager(this)

        binding.rvCalenderDate.layoutManager = layoutManager
        val issuerCategoriesAdapter = IssuerCategoriesAdapter(this)
        binding.rvCalenderDate.adapter =issuerCategoriesAdapter

        binding.icback.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })

        issuerCategoriesAdapter.onItemClick = { contact ->

            contact?.let {
                //viewModel.setSelectIssuer(it)
                intent = Intent(this,IssuerBankActivity::class.java)
                intent.putExtra("issuerId", it.issuerId);
                intent.putExtra("issuerName", it.issuerName);
                intent.putExtra("isCardNetwork", isCardNetwork);

                startActivity(intent)
            }


        }

        progressBar.show()
        viewModel.getIssuerListData()

        viewModel.issuerListResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {

                            viewModel.issuerList(it.value.data)
                            issuerCategoriesAdapter.setList(it.value.data)

                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        binding.ivInfo.setOnClickListener {
            it.showInfoDialog(this,
                getString(R.string.please_reach_out),getString(R.string.reach_out_us_message))
        }

        isCardNetwork = intent.getBooleanExtra("isCardNetwork",false)
    }

    override fun getViewModel() = IssuerViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityIssuerCategoriseBinding.inflate(inflater)

    override fun getActivityRepository() = IssuerRepository(
    remoteDataSource.buildApi(IssuerApi::class.java,pref.token))
}