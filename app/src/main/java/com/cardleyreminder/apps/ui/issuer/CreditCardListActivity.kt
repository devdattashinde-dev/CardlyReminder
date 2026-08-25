package com.cardleyreminder.apps.ui.issuer

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.CardListAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.network.IssuerApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.IssuerRepository
import com.cardleyreminder.apps.databinding.ActivityCreditCardListBinding
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class CreditCardListActivity : BaseActivity<IssuerViewModel, ActivityCreditCardListBinding, IssuerRepository>() {

    private var issuerCardId: String? = null
    private var cardTypeId: String? = null
    private var cardNetworkId: String? = null
    private var issuerId: String? = null
    private var bankId: String? = null


    var pagination = 1
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    lateinit var cardListAdapter: CardListAdapter;

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
        cardListAdapter = CardListAdapter(this,null)

        viewModel.creditCardListData.observe(
            this,
            Observer {

                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {
                            if(it.value.data.size>0) {
                                cardListAdapter.setData(it.value.data)
                            }
                            /*if(cardListAdapter.cards.size>0){
                                binding.appBarMain.tvNoData.visibility = View.GONE
                            }else{
                                binding.appBarMain.tvNoData.visibility = View.VISIBLE
                            }*/
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        binding.rvCalenderDate.layoutManager = layoutManager
        binding.rvCalenderDate.adapter =cardListAdapter

        binding.icback.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })

        bankId = intent.getStringExtra("bankId")
        issuerId = intent.getStringExtra("issuerId")
        cardNetworkId = intent.getStringExtra("cardNetworkId")
        cardTypeId = intent.getStringExtra("cardTypeId")
        issuerCardId = intent.getStringExtra("issuerCardId")

       /* var issuerCardName = intent.getStringExtra("issuerCardName")
        binding.tvTitle.text = issuerCardName*/

        progressBar.show()
        viewModel.getCreditCardListData(bankId,issuerId,cardNetworkId,cardTypeId,issuerCardId,pagination)
    }

    override fun getViewModel() = IssuerViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityCreditCardListBinding.inflate(inflater)

    override fun getActivityRepository() = IssuerRepository(
    remoteDataSource.buildApi(IssuerApi::class.java,pref.token))
}