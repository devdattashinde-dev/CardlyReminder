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
import com.cardleyreminder.apps.data.adapter.IssuerAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.issuer.GetCardTypeRequest
import com.cardleyreminder.apps.data.model.issuer.GetIssuerCardListRequest
import com.cardleyreminder.apps.data.network.IssuerApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.IssuerRepository
import com.cardleyreminder.apps.databinding.ActivityIssuerBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError
import com.google.android.material.tabs.TabLayout

class IssuerActivity : BaseActivity<IssuerViewModel, ActivityIssuerBinding, IssuerRepository>() {

    private var cardNetworkId: String? = null
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
        val issuerAdapter = IssuerAdapter(this)
        binding.rvCalenderDate.adapter =issuerAdapter

        binding.icback.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })

        viewModel.cardTypeResponse.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {
                            viewModel.cardTypeList(it.value.data)
                            binding.tabLayout.removeAllTabs()
                            for(cardType in viewModel.cardTypeList.value!!){
                                binding.tabLayout.addTab(binding.tabLayout.newTab().setText(cardType.cardName))
                                binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                                binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
                                binding.tabLayout.isTabIndicatorFullWidth = true
                            }
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
        cardNetworkId = intent.getStringExtra("cardNetworkId")

        viewModel.getCardTypeListData(GetCardTypeRequest(card_network_id = cardNetworkId!!))

        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewModel.setSelectCardType(tab?.text.toString())
                    //viewModel.getCardListData(GetIssuerCardListRequest())
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            }
        )

        viewModel.issuerCardListResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {
                            viewModel.cardList(it.value.data)
                            issuerAdapter.setList(viewModel.cardDetailsList.value!!)
                        }else {
                            issuerAdapter.setList(emptyList())
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure -> {
                        issuerAdapter.setList(emptyList())
                        handleAPIError(it)
                    }
                }
                progressBar.cancel()
            })

        issuerAdapter.onItemClick = { cardDetails ->
            if(cardDetails!=null){
                cardDetails?.let {

                    if(cardDetails.cardCount?.toInt()!! >0){
                        intent = Intent(this,CreditCardListActivity::class.java)
                        intent.putExtra("issuerId", issuerId)
                        intent.putExtra("bankId",bankId)
                        intent.putExtra("cardNetworkId",cardNetworkId)
                        intent.putExtra("cardTypeId",viewModel.selectedCardTypeId)
                        intent.putExtra("issuerCardId",it.issuerCardId)
                        intent.putExtra("issuerCardName",it.issuerCardName)

                        startActivity(intent)
                    }

                }
            }/*else{
                intent = Intent(this,AddIssuerCardDesignActivity::class.java)
                intent.putExtra("issuerId", issuerId)
                intent.putExtra("bankId",bankId)
                intent.putExtra("cardNetworkId",cardNetworkId)
                intent.putExtra("cardTypeId",viewModel.selectedCardTypeId)

                startActivity(intent)
            }*/

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.selectedCardTypeId?.let { viewModel.getCardListData(GetIssuerCardListRequest(card_type_id = it, user_card_count = "1")) }
    }

    override fun getViewModel() = IssuerViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityIssuerBinding.inflate(inflater)

    override fun getActivityRepository() = IssuerRepository(
    remoteDataSource.buildApi(IssuerApi::class.java,pref.token))
}