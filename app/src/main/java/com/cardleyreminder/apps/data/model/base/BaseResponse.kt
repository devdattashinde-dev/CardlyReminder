package com.cardleyreminder.apps.data.model.base

abstract class BaseResponse{
    abstract var message: String?
    abstract var status: Boolean
    abstract var ws: String?
}