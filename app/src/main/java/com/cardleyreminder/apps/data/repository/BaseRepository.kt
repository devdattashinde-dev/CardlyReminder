package com.cardleyreminder.apps.data.repository

import com.cardleyreminder.apps.data.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.IllegalArgumentException

abstract class BaseRepository {
    suspend fun<T> apiCall(
        apiCall : suspend () -> T
    ) : Resource<T>{
        return withContext(Dispatchers.IO){
            try {
                Resource.Success(apiCall.invoke())
            }catch (throwable : Throwable){
                when(throwable){
                    is HttpException ->{
                        Resource.Failure(false,throwable.code(),throwable.response()?.errorBody(),null)
                    }
                    is IllegalArgumentException ->{
                        Resource.Failure(false,null,null,throwable.message)
                    }
                    else ->{
                        Resource.Failure(true,null,null,null)
                    }
                }
            }
        }
    }


}