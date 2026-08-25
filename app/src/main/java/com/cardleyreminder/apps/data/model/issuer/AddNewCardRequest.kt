package com.cardleyreminder.apps.data.model.issuer

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class AddNewCardRequest (
    val card_number: String,
    val issuer_id: String,
    val bank_id: String,
    val card_network_id: String,

    val card_type_id: String,
    val issuer_card_id: String,
    val statement_closing_date: String,
    val payment_due_date: String,
    val card_expire: String,
    /*val card_expire_month: String,
    val card_expire_year: String,*/
    val comment: String,
    val card_color_id: String,
    val issuer_card_name: String,
): BaseRequest()