package com.cardleyreminder.apps.ui.addCard

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.R
import com.cardleyreminder.apps.data.adapter.SelectDateAdapter
import com.cardleyreminder.apps.data.adapter.CardColorAdapter
import com.cardleyreminder.apps.data.adapter.CardNetworkArrayAdapter
import com.cardleyreminder.apps.data.adapter.CardTypeArrayAdapter
import com.cardleyreminder.apps.data.adapter.IssuerArrayAdapter
import com.cardleyreminder.apps.data.adapter.IssuerBankArrayAdapter
import com.cardleyreminder.apps.data.adapter.IssuerCardListArrayAdapter
import com.cardleyreminder.apps.data.model.base.BaseResponse
import com.cardleyreminder.apps.data.model.issuer.AddNewIssuerCardRequest
import com.cardleyreminder.apps.data.model.issuer.BankDetail
import com.cardleyreminder.apps.data.model.issuer.CardDetail
import com.cardleyreminder.apps.data.model.issuer.CardNetwork
import com.cardleyreminder.apps.data.model.issuer.CardType
import com.cardleyreminder.apps.data.model.issuer.EditCardRequest
import com.cardleyreminder.apps.data.model.issuer.EditRetailCardRequest
import com.cardleyreminder.apps.data.model.issuer.GetCardDetailsRequest
import com.cardleyreminder.apps.data.model.issuer.Issuers
import com.cardleyreminder.apps.data.network.IssuerApi
import com.cardleyreminder.apps.data.network.Resource
import com.cardleyreminder.apps.data.repository.IssuerRepository
import com.cardleyreminder.apps.databinding.ActivityAddCardBinding
import com.cardleyreminder.apps.ui.base.BaseFragmentActivity
import com.cardleyreminder.apps.ui.issuer.IssuerViewModel
import com.cardleyreminder.apps.util.handleAPIError
import com.cardleyreminder.apps.util.handleError
import com.cardleyreminder.apps.util.toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditCardActivity : BaseFragmentActivity<IssuerViewModel, ActivityAddCardBinding, IssuerRepository>() {

    private var selectedDueDate: String? = null
    private var selectedStatementDate: String? = null
    private var selectedYear: String? = null
    private var selectedMonth: String? = null
    private lateinit var cardColorAdapter: CardColorAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.icBack.setOnClickListener(View.OnClickListener {
            onBackPressedDispatcher.onBackPressed()
        })




        setAllSpinner()
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


        cardColorAdapter = CardColorAdapter()
        val layoutManager = GridLayoutManager(this, 6)
        binding.rvColor.layoutManager = layoutManager
        binding.rvColor.adapter = cardColorAdapter


        //issuer List
        viewModel.issuerListResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {

                            viewModel.issuerList(it.value.data)
                            var list = it.value.data as ArrayList
                            list.add(0,Issuers(issuerName = getString(R.string.select_issuer)))
                            val adapter = IssuerArrayAdapter(this,R.layout.image_text_spinner_item_layout,R.id.tvLabel,list)
                            binding.spIssuer.adapter = adapter

                            if(!viewModel.card.issuerId.isNullOrBlank()){

                                var index = list.indexOfFirst { item -> item.issuerId == viewModel.card.issuerId }
                                if(index== -1)
                                    index = 0

                                binding.spIssuer.setSelection(index)
                            }


                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        binding.spIssuer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position>0 && viewModel.issuerList.value?.isNotEmpty() == true) {
                    val issuer = parent?.getItemAtPosition(position) as Issuers
                    if(issuer.issuerId!!.toInt()==5 || issuer.issuerName.equals("Bank not listed above?",true)){
                        viewModel.selectedIssuerId = issuer.issuerId


                        binding.conLayIssuerType.visibility = View.GONE
                        binding.conLayCardType.visibility = View.GONE
                        binding.conLayCardSubType.visibility = View.GONE

                        binding.etRetailStoreName.visibility = View.GONE
                        binding.etRetailStoreCreditCard.visibility = View.GONE

                        binding.etIssuerBankName.visibility = View.VISIBLE
                        binding.etCreditCardName.visibility = View.VISIBLE
                        binding.conLayCardNetwork.visibility = View.VISIBLE

                        progressBar.show()
                        viewModel.setSelectIssuer(issuer)
                    }else if(issuer.issuerId!!.toInt()==7 || issuer.issuerName.equals("Retail Credit Cards Issuers",true)){
                        viewModel.selectedIssuerId = issuer.issuerId


                        binding.conLayIssuerType.visibility = View.GONE
                        binding.conLayCardNetwork.visibility = View.GONE
                        binding.conLayCardType.visibility = View.GONE
                        binding.conLayCardSubType.visibility = View.GONE

                        binding.etRetailStoreName.visibility = View.VISIBLE
                        binding.etRetailStoreCreditCard.visibility = View.VISIBLE


                        binding.etIssuerBankName.visibility = View.GONE
                        binding.etCreditCardName.visibility = View.GONE
                    }else{
                        binding.conLayIssuerType.visibility = View.VISIBLE
                        binding.conLayCardNetwork.visibility = View.VISIBLE
                        binding.conLayCardType.visibility = View.VISIBLE
                        binding.conLayCardSubType.visibility = View.VISIBLE

                        binding.etRetailStoreName.visibility = View.GONE
                        binding.etRetailStoreCreditCard.visibility = View.GONE

                        binding.etIssuerBankName.visibility = View.GONE
                        binding.etCreditCardName.visibility = View.GONE

                        progressBar.show()
                        viewModel.setSelectIssuer(issuer)

                    }

                }

            }
        }

        //bank List
        viewModel.issuerBankListResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {

                            viewModel.issuerBankList(it.value.data)

                            val list = it.value.data as ArrayList
                            list.add(0,
                                BankDetail(bankName = getString(R.string.select_issuer_bank))
                            )
                            val adapter = IssuerBankArrayAdapter(this,R.layout.image_text_spinner_item_layout,R.id.tvLabel,list)
                            binding.spIssuerType.adapter = adapter

                            if(!viewModel.card.bankId.isNullOrBlank()){

                                var index = list.indexOfFirst { item -> item.bankId == viewModel.card.bankId }
                                if(index== -1)
                                    index = 0

                                binding.spIssuerType.setSelection(index)

                            }

                        }else {
                            setIssuerTypeEmpty()
                            setCardNetworkEmpty()
                            setCardTypeEmpty()
                            setCardDetailsEmpty()
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->{
                        setIssuerTypeEmpty()
                        setCardNetworkEmpty()
                        setCardTypeEmpty()
                        setCardDetailsEmpty()
                        handleAPIError(it)
                    }
                }
                progressBar.cancel()
            })
        binding.spIssuerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position>0 && viewModel.bankList.value?.isNotEmpty() == true) {

                    viewModel.setSelectIssuerBank(parent?.getItemAtPosition(position) as BankDetail)
                }
            }
        }

        // card type

        viewModel.cardNetworkResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {

                            viewModel.cardNetworkList(it.value.data)
                            val list = it.value.data as ArrayList
                            list.add(0, CardNetwork(name = getString(R.string.select_card_network)))
                            val adapter = CardNetworkArrayAdapter(this,R.layout.image_text_spinner_item_layout,R.id.tvLabel,list)
                            binding.spCardNetwork.adapter = adapter

                            if(!viewModel.card.cardNetworkId.isNullOrBlank()){
                                var index = list.indexOfFirst { item -> item.cardNetworkId == viewModel.card.cardNetworkId }
                                if(index== -1)
                                    index = 0

                                binding.spCardNetwork.setSelection(index)
                            }
                        }else {
                            setCardNetworkEmpty()
                            setCardTypeEmpty()
                            setCardDetailsEmpty()
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->{

                        setCardNetworkEmpty()
                        setCardTypeEmpty()
                        setCardDetailsEmpty()
                        handleAPIError(it)

                    }
                }
                progressBar.cancel()
            })
        binding.spCardNetwork.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position>0 && viewModel.cardNetworkList.value?.isNotEmpty() == true)
                    viewModel.setSelectCardNetwork(parent?.getItemAtPosition(position) as CardNetwork)
            }
        }


        viewModel.cardTypeResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {

                            viewModel.cardTypeList(it.value.data)
                            val list = it.value.data as ArrayList
                            list.add(0, CardType(cardName = getString(R.string.select_card_type)))
                            val adapter = CardTypeArrayAdapter(this,R.layout.image_text_spinner_item_layout,R.id.tvLabel,list)
                            binding.spCardType.adapter = adapter

                            if(!viewModel.card.cardTypeId.isNullOrBlank()){

                                var index = list.indexOfFirst { item -> item.cardTypeId == viewModel.card.cardTypeId }
                                if(index== -1)
                                    index = 0

                                binding.spCardType.setSelection(index)
                            }

                        }else {
                            setCardTypeEmpty()
                            setCardDetailsEmpty()
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->{
                        setCardTypeEmpty()
                        setCardDetailsEmpty()
                        handleAPIError(it)
                    }
                }
                progressBar.cancel()
            })
        binding.spCardType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position>0 && viewModel.cardTypeList.value?.isNotEmpty() == true){
                    viewModel.setSelectCardType(parent?.getItemAtPosition(position) as CardType)

                }
            }
        }

        viewModel.addRetailCardResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {
                            Toast.makeText(this,"Card added successfully", Toast.LENGTH_LONG).show()
                            finish();
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })
        viewModel.issuerCardListResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {

                            viewModel.cardList(it.value.data)

                            val list = it.value.data as ArrayList
                            list.add(0, CardDetail(issuerCardName = getString(R.string.select_your_credit_card)))

                            val adapter = IssuerCardListArrayAdapter(this,R.layout.image_text_spinner_item_layout,R.id.tvLabel,list)
                            binding.spCardSubType.adapter = adapter

                            if(!viewModel.card.issuerCardId.isNullOrBlank()){

                                var index = list.indexOfFirst { item -> item.issuerCardId == viewModel.card.issuerCardId }
                                if(index== -1)
                                    index = 0

                                binding.spCardSubType.setSelection(index)
                            }

                        }else {
                            setCardDetailsEmpty()
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->{
                        setCardDetailsEmpty()
                        handleAPIError(it)
                    }
                }
                progressBar.cancel()
            })
        binding.spCardSubType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position>0 && viewModel.cardDetailsList.value?.isNotEmpty() == true) {
                    val cardDetail = parent?.getItemAtPosition(position) as CardDetail
                    viewModel.setSelectCard(cardDetail)
                    if(viewModel.isSelectedCardOther(cardDetail.issuerCardName!!) == true){
                        binding.etCardName.visibility = View.VISIBLE
                    }else {
                        binding.etCardName.visibility = View.GONE
                    }
                }
            }
        }


        viewModel.cardColorListResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {

                            cardColorAdapter.setList(it.value.data)

                            cardColorAdapter.setSelectedColor(viewModel.card.cardColorId)
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        if(intent.hasExtra("cardId")){
            intent.getStringExtra("cardId")?.let {
                progressBar.show()
                GetCardDetailsRequest(
                    it
                )
            }?.let { viewModel.getCardDetailsData(it) }
        }else{
            getData()
        }





        binding.etCardExp.setOnClickListener(
            View.OnClickListener {
                showDatePicker()
            //createDialogWithoutDateField()?.show()

                /*var dialog  = MonthYearPickerDialog()
                    dialog.setListener { view, year, month, dayOfMonth ->
                        //Toast.makeText(this, "Set date: $year/$month/$dayOfMonth", Toast.LENGTH_LONG).show()
                        selectedMonth = dateArray.get(month)
                        selectedYear = year.toString()
                        binding.etCardExp.setText("$selectedMonth/$selectedYear")
                    }

                (dialog as DialogFragment).show(supportFragmentManager,"date dialog")*/

            }
        )

        binding.tvCloasingDate.setOnClickListener(
            View.OnClickListener {
                var db : AlertDialog? = null
                val adapter = SelectDateAdapter(this)
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.calender_dialog_layout, null)
                val rv = mDialogView.findViewById<RecyclerView>(R.id.rvCalender)
                rv.layoutManager = GridLayoutManager(this,5)
                rv.adapter = adapter
                adapter.onItemClick = { date ->

                    if(selectedDueDate!=null && !viewModel.isValidDates(selectedDueDate!!.toInt(),date)){
                        it.toast("Statement Closing Date cannot be before Payment Due Date")
                    }else{
                        binding.tvCloasingDate.text = if(date<10)"0$date" else date.toString()
                        selectedStatementDate = date.toString()
                        db?.cancel()
                    }

                }
                db = AlertDialog.Builder(this,R.style.ThemeOverlay_AppCompat_Dark).setView(mDialogView).setTitle("Select Date")
                    .show()
            }
        )

        binding.tvDueDate.setOnClickListener(
            View.OnClickListener {
                var db : AlertDialog? = null
                val adapter = SelectDateAdapter(this)
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.calender_dialog_layout, null)
                val rv = mDialogView.findViewById<RecyclerView>(R.id.rvCalender)
                rv.layoutManager = GridLayoutManager(this,5)
                rv.adapter = adapter
                adapter.onItemClick = { date ->

                    if(selectedStatementDate!=null && !viewModel.isValidDates(selectedStatementDate!!.toInt(),date) ){
                        it.toast("Payment Due Date cannot be after Statement Closing Date")
                    }else{
                        binding.tvDueDate.text = if(date<10)"0$date" else date.toString()
                        selectedDueDate = date.toString()
                        db?.cancel()
                    }

                }
                db = AlertDialog.Builder(this,R.style.ThemeOverlay_AppCompat_Dark).setView(mDialogView).setTitle("Select Date")
                    .show()
            }
        )

        binding.tvDone.setOnClickListener(
            View.OnClickListener {
                if(binding.etCardNo.text.isEmpty()){
                    Toast.makeText(this, getString(R.string.enter_4digit_card_identifier),Toast.LENGTH_LONG).show()
                }else if(binding.etCardNo.text.length<4){
                    Toast.makeText(this,
                        getString(R.string.enter_valid_4digit_card_identifier),Toast.LENGTH_LONG).show()
                }else if(viewModel.selectedIssuerId == null){
                    Toast.makeText(this,
                        getString(R.string.select_issuer),Toast.LENGTH_LONG).show()
                }else if(viewModel.selectedIssuerId!!.toInt()==5 || (binding.spIssuer.selectedItem as Issuers).issuerName.equals("Retail Credit Cards Issuers",true)){

                    if(viewModel.selectedCardNetworkId == null){
                        Toast.makeText(this,
                            getString(R.string.select_card_network),Toast.LENGTH_LONG).show()
                    }else if(binding.etIssuerBankName.text.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.enter_name_of_issuer_bank),Toast.LENGTH_LONG).show()
                    }else if(binding.etCreditCardName.text.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.enter_name_of_credit_card),Toast.LENGTH_LONG).show()
                    }else if(selectedStatementDate == null || selectedStatementDate!!.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.select_statement_date),Toast.LENGTH_LONG).show()
                    }else if(selectedDueDate == null || selectedDueDate!!.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.select_due_date),Toast.LENGTH_LONG).show()
                    }else if(binding.etCardExp.text.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.select_card_expire_date),Toast.LENGTH_LONG).show()
                    }else if(binding.etComment.text.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.enter_comment),Toast.LENGTH_LONG).show()
                    }else if(cardColorAdapter.getSelectedColorId()==null){
                        Toast.makeText(this,
                            getString(R.string.select_card_color),Toast.LENGTH_LONG).show()
                    }else{
                        var request = AddNewIssuerCardRequest(
                            cardNumber = binding.etCardNo.text.toString(),
                            issuerId = viewModel.selectedIssuerId.toString(),
                            cardNetworkId=viewModel.selectedCardNetworkId,
                            cardBankName = binding.etIssuerBankName.text.toString(),
                            creditCardName = binding.etCreditCardName.text.toString(),
                            statementClosingDate = binding.tvCloasingDate.text.toString(),
                            paymentDueDate = binding.tvDueDate.text.toString(),
                            cardExpire = binding.etCardExp.text.toString(),
                            comment = binding.etComment.text.toString(),
                            cardColorId = cardColorAdapter.getSelectedColorId().toString(),
                        )

                        progressBar.show()
                        viewModel.addNewIssuerCard(request)
                    }

                }else if(viewModel.selectedIssuerId!!.toInt()==7 || (binding.spIssuer.selectedItem as Issuers).issuerName.equals("Retail Credit Cards Issuers",true)){

                    if(binding.etRetailStoreName.text.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.enter_retail_store_name),Toast.LENGTH_LONG).show()
                    }else if(binding.etRetailStoreCreditCard.text.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.enter_retail_store_credit_card),Toast.LENGTH_LONG).show()
                    }else if(selectedStatementDate == null || selectedStatementDate!!.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.select_statement_date),Toast.LENGTH_LONG).show()
                    }else if(selectedDueDate == null || selectedDueDate!!.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.select_due_date),Toast.LENGTH_LONG).show()
                    }else if(binding.etCardExp.text.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.select_card_expire_date),Toast.LENGTH_LONG).show()
                    }else if(binding.etComment.text.isEmpty()){
                        Toast.makeText(this,
                            getString(R.string.enter_comment),Toast.LENGTH_LONG).show()
                    }else if(cardColorAdapter.getSelectedColorId()==null){
                        Toast.makeText(this,
                            getString(R.string.select_card_color),Toast.LENGTH_LONG).show()
                    }else{
                        var request = EditRetailCardRequest(
                            cardId = viewModel.card.cardId!!,
                            cardNumber = binding.etCardNo.text.toString(),
                            issuerId = viewModel.selectedIssuerId.toString(),
                            retailStoreIssued = binding.etRetailStoreName.text.toString(),
                            retailStoreCreditCard = binding.etRetailStoreCreditCard.text.toString(),
                            statementClosingDate = binding.tvCloasingDate.text.toString(),
                            paymentDueDate = binding.tvDueDate.text.toString(),
                            cardExpire = binding.etCardExp.text.toString(),
                            comment = binding.etComment.text.toString(),
                            cardColorId = cardColorAdapter.getSelectedColorId().toString(),
                        )

                        progressBar.show()
                        viewModel.editRetailCard(request)
                    }

                }else if(viewModel.selectedIssuerBankId == null){
                    Toast.makeText(this,
                        getString(R.string.select_issuer_type),Toast.LENGTH_LONG).show()
                }else if(viewModel.selectedCardTypeId == null){
                    Toast.makeText(this,
                        getString(R.string.select_card_type),Toast.LENGTH_LONG).show()
                }else if(viewModel.selectedCardId == null){
                    Toast.makeText(this,
                        getString(R.string.select_card),Toast.LENGTH_LONG).show()
                }else if(selectedStatementDate == null || selectedStatementDate!!.isEmpty()){
                    Toast.makeText(this,
                        getString(R.string.select_statement_date),Toast.LENGTH_LONG).show()
                }else if(selectedDueDate == null || selectedDueDate!!.isEmpty()){
                    Toast.makeText(this,
                        getString(R.string.select_due_date),Toast.LENGTH_LONG).show()
                }else if(viewModel.selectedCardNetworkId == null){
                    Toast.makeText(this,
                        getString(R.string.select_card_network),Toast.LENGTH_LONG).show()
                }else if(binding.etCardExp.text.isEmpty()){
                    Toast.makeText(this,
                        getString(R.string.select_card_expire_date),Toast.LENGTH_LONG).show()
                }else if(binding.etComment.text.isEmpty()){
                    Toast.makeText(this,
                        getString(R.string.enter_comment),Toast.LENGTH_LONG).show()
                }else if(cardColorAdapter.getSelectedColorId()==null){
                    Toast.makeText(this,
                        getString(R.string.select_card_color),Toast.LENGTH_LONG).show()
                }else{
                    var request = EditCardRequest(
                        card_id = viewModel.card.cardId!!,
                        card_number = binding.etCardNo.text.toString(),
                        issuer_id = viewModel.selectedIssuerId.toString(),
                        bank_id = viewModel.selectedIssuerBankId.toString(),
                        card_type_id = viewModel.selectedCardTypeId.toString(),
                        issuer_card_id = viewModel.selectedCardId.toString(),
                        statement_closing_date = binding.tvCloasingDate.text.toString(),
                        payment_due_date = binding.tvDueDate.text.toString(),
                        card_network_id = viewModel.selectedCardNetworkId.toString(),
                        card_expire = binding.etCardExp.text.toString(),
                        comment = binding.etComment.text.toString(),
                        card_color_id = cardColorAdapter.getSelectedColorId().toString(),
                        issuer_card_name = binding.etCardName.text.toString(),
                    )

                    progressBar.show()
                    viewModel.editCard(request)
                }
            }
        )

        viewModel.editCardResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{

                        progressBar.show()
                        if(it.value.status) {
                            Toast.makeText(this,"Card edited successfully", Toast.LENGTH_LONG).show()
                            finish();
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }

                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        viewModel.editRetailCardResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{

                        progressBar.show()
                        if(it.value.status) {
                            Toast.makeText(this,"Card edited successfully", Toast.LENGTH_LONG).show()
                            finish();
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }

                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        viewModel.cardDetailsResponse.observe(
            this,
            Observer {
                when(it){
                    is Resource.Success ->{
                        if(it.value.status) {
                            //Toast.makeText(this,"Card added successfully", Toast.LENGTH_LONG).show()

                            viewModel.card = it.value.data!!

                            setData()


                            //finish();
                        }else {
                            handleError(it.value as BaseResponse,{ logout()})
                        }
                    }
                    is Resource.Failure ->handleAPIError(it)
                }
                progressBar.cancel()
            })

        /*binding.etCardNo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                playSound()
                return@setOnKeyListener false
            }
            false
        }*/

    }

    private fun setData() {
        viewModel.card.let {
            if(!it.cardNumber.isNullOrBlank()){
                binding.etCardNo.setText(it.cardNumber)
                binding.etCardNo.isEnabled = false
            }

            if(!it.statementClosingDate.isNullOrBlank()){
                binding.tvCloasingDate.text = it.statementClosingDate
                selectedStatementDate = it.statementClosingDate
            }

            if(!it.paymentDueDate.isNullOrBlank()){
                binding.tvDueDate.text = it.paymentDueDate

                selectedDueDate = it.paymentDueDate
            }

            if(!it.cardExpire.isNullOrBlank()){
                binding.etCardExp.setText(it.cardExpire)
            }

            if(!it.comment.isNullOrBlank()){
                binding.etComment.setText(it.comment)
            }

            if(!it.retailStoreIssued.isNullOrBlank()){
                binding.etRetailStoreName.setText(it.retailStoreIssued)
            }

            if(!it.retailStoreCreditCard.isNullOrBlank()){
                binding.etRetailStoreCreditCard.setText(it.retailStoreCreditCard)
            }

            if(!it.cardBankName.isNullOrBlank()){
                binding.etIssuerBankName.setText(it.cardBankName)
            }
            if(!it.creditCardName.isNullOrBlank()){
                binding.etCreditCardName.setText(it.creditCardName)
            }

            getData()
        }
    }

    private fun getData() {
        progressBar.show()
        viewModel.getIssuerListData()
        //viewModel.getCardTypeListData()
        viewModel.getCardColorData()
        //viewModel.getCardNetworkData("0")
    }

    private fun setIssuerEmpty() {
        viewModel.issuerBankList(emptyList())
        val adapter = IssuerArrayAdapter(this,R.layout.image_text_spinner_item_layout,R.id.tvLabel,
            arrayListOf(Issuers(issuerName = getString(R.string.select_issuer))))
        binding.spIssuer.adapter = adapter
        viewModel.selectedIssuerId = null
    }

    private fun setIssuerTypeEmpty() {
        viewModel.issuerBankList(emptyList())

        val adapter = IssuerBankArrayAdapter(this,R.layout.image_text_spinner_item_layout,R.id.tvLabel,
            arrayListOf(BankDetail(bankName = getString(R.string.select_issuer_bank))))
        binding.spIssuerType.adapter = adapter
        viewModel.selectedIssuerBankId = null
    }

    private fun setCardNetworkEmpty() {
        viewModel.cardNetworkList(emptyList())
        //val adapter = ArrayAdapter(this,R.layout.spinner_item, arrayOf(getString(R.string.select_card_network)).toList())
        val adapter = CardNetworkArrayAdapter(this,R.layout.image_text_spinner_item_layout,R.id.tvLabel,
            arrayListOf(CardNetwork(name = getString(R.string.select_card_network))))
        binding.spCardNetwork.adapter = adapter
        viewModel.selectedCardNetworkId = null
    }

    private fun setCardTypeEmpty() {
        viewModel.cardTypeList(emptyList())
        //val adapter = ArrayAdapter(this,R.layout.spinner_item, arrayOf(getString(R.string.select_card_type)).toList())
        val adapter = CardTypeArrayAdapter(this,R.layout.image_text_spinner_item_layout,R.id.tvLabel,
            arrayListOf(CardType(cardName = getString(R.string.select_card_type))))
        binding.spCardType.adapter = adapter
        viewModel.selectedCardTypeId = null
    }

    private fun setCardDetailsEmpty() {
        viewModel.cardList(emptyList())
        //val adapter = ArrayAdapter(this,R.layout.spinner_item, arrayOf(getString(R.string.select_your_credit_card)).toList())
        val adapter = IssuerCardListArrayAdapter(this,R.layout.image_text_spinner_item_layout,R.id.tvLabel,
            arrayListOf(CardDetail(issuerCardName = getString(R.string.select_your_credit_card))))
        binding.spCardSubType.adapter = adapter
        viewModel.selectedCardId = null
    }

    private fun setAllSpinner(){
        setIssuerEmpty()
        setIssuerTypeEmpty()
        setCardNetworkEmpty()
        setCardTypeEmpty()
        setCardDetailsEmpty()
    }



    private fun showDatePicker() {

        // Set up Calendar instance
        val calendar = Calendar.getInstance()

        // Get current date
        val currentDate = calendar.timeInMillis

        // Calculate the maximum date (5 years from now)
        calendar.add(Calendar.YEAR, 5)
        val maxDate = calendar.timeInMillis

        // Reset calendar to the current date
        calendar.timeInMillis = currentDate


        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "yyyy/MM/dd"
                val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                binding.etCardExp.setText("$formattedDate")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.datePicker.maxDate = maxDate
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    override fun getViewModel() = IssuerViewModel::class.java

    override fun getActivityBinding(
        inflater: LayoutInflater
    ) = ActivityAddCardBinding.inflate(inflater)

    override fun getActivityRepository() = IssuerRepository(
        remoteDataSource.buildApi(IssuerApi::class.java,pref.token))
}