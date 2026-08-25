package com.cardleyreminder.apps.ui.issuer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.CardListAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.home.HomeData
import com.cardleyreminder.apps.data.model.issuer.GetCardDetailsRequest
import com.cardleyreminder.apps.data.network.IssuerApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.IssuerRepository
import com.cardleyreminder.apps.databinding.ActivityCreditCardListBinding
import com.cardleyreminder.apps.ui.addCard.EditCardActivity
import com.cardleyreminder.apps.ui.base.BaseActivity
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError

class CardNetworkCreditCardListActivity : BaseActivity<IssuerViewModel, ActivityCreditCardListBinding, IssuerRepository>() {

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
        cardListAdapter.onItemClick = { card, isDelete ->

            if(isDelete){
                /*progressBar.show()
                viewModel.deleteCard(
                    GetCardDetailsRequest(
                        cardId = card?.cardId!!
                    )
                )*/
                showDeleteCardDialog(card)

            }else{
                val intent = Intent(this, EditCardActivity::class.java)
                intent.putExtra("cardId",card?.cardId)
                startActivity(intent)
            }
        }
        viewModel.cardNetworkCreditCardListData.observe(
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


        var cardNetworkName = intent.getStringExtra("cardNetworkName")
        binding.tvTitle.text = cardNetworkName

        viewModel.getCardNetworkCreditCardListData(bankId,issuerId,cardNetworkId,pagination)
    }
    private fun showDeleteCardDialog(card: HomeData?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.card_delete_confirmation_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val btOk = dialog.findViewById(R.id.btOk) as TextView
        val btCancel = dialog.findViewById(R.id.btCancel) as TextView


        btOk.setOnClickListener {
            progressBar.show()
            viewModel.deleteCard(
                GetCardDetailsRequest(
                    cardId = card?.cardId!!
                )
            )

            dialog.dismiss()
        }

        btCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    override fun getViewModel() = IssuerViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityCreditCardListBinding.inflate(inflater)

    override fun getActivityRepository() = IssuerRepository(
    remoteDataSource.buildApi(IssuerApi::class.java,pref.token))
}