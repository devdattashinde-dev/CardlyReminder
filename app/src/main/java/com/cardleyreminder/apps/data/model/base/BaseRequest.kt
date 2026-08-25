package com.cardleyreminder.apps.data.model.base

import java.util.*

open class BaseRequest{
    var language: String = if(Locale.getDefault().getLanguage().equals("en",true) )"en" else "ar"
}