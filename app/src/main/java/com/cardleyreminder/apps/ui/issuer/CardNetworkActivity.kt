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
import com.cardleyreminder.apps.data.adapter.CardNetworkAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.issuer.GetCardNetworkRequest
import com.cardleyreminder.apps.data.network.IssuerApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.IssuerRepository
import com.cardleyreminder.apps.databinding.ActivityCardNetworkBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class CardNetworkActivity : BaseActivity<IssuerViewModel, ActivityCardNetworkBinding, IssuerRepository>() {

    private var isCardNetwork: Boolean = false
    private var issuerId: String? = null
    private var bankId: String? = null

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
        val cardNetworkAdapter = CardNetworkAdapter(this)
        binding.rvCalenderDate.adapter =cardNetworkAdapter

        binding.icback.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })

        viewModel.cardNetworkResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {

                            //viewModel.issuerList(it.value.data)
                            cardNetworkAdapter.setList(it.value.data)

                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        bankId = intent.getStringExtra("bankId")
        issuerId = intent.getStringExtra("issuerId")
        isCardNetwork = intent.getBooleanExtra("isCardNetwork",false)

        var cardName = intent.getStringExtra("cardName")
        if(!cardName.isNullOrEmpty())
            binding.tvTitle.text = cardName
        else if(isCardNetwork)
            binding.tvTitle.text = "Card Network"



        if(issuerId!=null && bankId!=null){
            progressBar.show()
            viewModel.getCardNetworkData(GetCardNetworkRequest(bankId!!))
        }
        cardNetworkAdapter.onItemClick = { cardNetwork ->
            cardNetwork?.let {
                if(isCardNetwork){
                    intent = Intent(this,CardNetworkCreditCardListActivity::class.java)
                    intent.putExtra("issuerId", issuerId)
                    intent.putExtra("bankId",bankId)
                    intent.putExtra("cardNetworkId",it.cardNetworkId)
                    intent.putExtra("cardNetworkName",it.name)

                    startActivity(intent)
                }else{
                    intent = Intent(this,IssuerActivity::class.java)
                    intent.putExtra("issuerId", issuerId);
                    intent.putExtra("bankId",bankId);
                    intent.putExtra("cardNetworkId", it.cardNetworkId);
                    intent.putExtra("cardNetworkName",it.name)
                    startActivity(intent)
                }

            }
        }
    }

    override fun getViewModel() = IssuerViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityCardNetworkBinding.inflate(inflater)

    override fun getActivityRepository() = IssuerRepository(
    remoteDataSource.buildApi(IssuerApi::class.java,pref.token))
}