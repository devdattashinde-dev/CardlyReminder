package com.cardleyreminder.apps.data.model.home

import com.cardleyreminder.apps.data.model.base.BaseRequest

data class HomeDataRequest (
    val pagination: Int,
    val statement_closing_date : String? = null,
    val payment_due_date : String? = null,
    val valid_thru : String? = null,
    val created_time : String? = null,
    val fcm_no : String,
    val installed_version : String,
    val source : String="android",

): BaseRequest()