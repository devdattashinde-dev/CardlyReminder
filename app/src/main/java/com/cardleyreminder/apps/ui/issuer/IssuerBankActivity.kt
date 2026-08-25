package com.cardleyreminder.apps.ui.issuer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.IssuerBankAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.issuer.GetIssuerBankListRequest
import com.cardleyreminder.apps.data.network.IssuerApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.IssuerRepository
import com.cardleyreminder.apps.databinding.ActivityIssuerBankBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class IssuerBankActivity : BaseActivity<IssuerViewModel, ActivityIssuerBankBinding, IssuerRepository>() {

    private var isCardNetwork: Boolean = false
    private var issuerId: String? = null

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

        val layoutManager = GridLayoutManager(this,2)

        binding.rvCalenderDate.layoutManager = layoutManager
        val issuerBankAdapter = IssuerBankAdapter(this)
        binding.rvCalenderDate.adapter =issuerBankAdapter

        binding.icback.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })

        issuerBankAdapter.onItemClick = { bankDetails ->
            bankDetails?.let {
                intent = Intent(this,CardNetworkActivity::class.java)
                intent.putExtra("issuerId", issuerId);
                intent.putExtra("bankId", it.bankId);
                intent.putExtra("bankName", it.bankName);
                intent.putExtra("cardName", it.bankName);
                intent.putExtra("isCardNetwork",isCardNetwork)
                startActivity(intent)
            }
        }

        viewModel.issuerBankListResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {

                            //viewModel.issuerList(it.value.data)
                            issuerBankAdapter.setList(it.value.data)

                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        issuerId = intent.getStringExtra("issuerId")
        isCardNetwork = intent.getBooleanExtra("isCardNetwork",false)
        var issuerName = intent.getStringExtra("issuerName")
        binding.tvTitle.text = issuerName

        issuerId?.let {
            progressBar.show()
            viewModel.getIssuerBankListData(GetIssuerBankListRequest(issuer_id = it))
        }

    }

    override fun getViewModel() = IssuerViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityIssuerBankBinding.inflate(inflater)

    override fun getActivityRepository() = IssuerRepository(
    remoteDataSource.buildApi(IssuerApi::class.java,pref.token))
}