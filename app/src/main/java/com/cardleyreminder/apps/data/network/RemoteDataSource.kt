package com.cardleyreminder.apps.data.network

import com.cardleyreminder.apps.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RemoteDataSource {
    companion object{
        private const val BASE_URL ="https://cardlyreminder.com/"
        //private const val BASE_URL ="https://dev.cardlyreminder.com/"
    }

    fun<Api> buildApi(
        api : Class<Api>,
        token : String? =null
    ): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        chain.proceed(chain.request().newBuilder().also {
                            it.addHeader("Token","Bearer $token")
                            it.addHeader("Content-Type","application/json")
                        }.build())
                    }.also {client ->
                    if(BuildConfig.DEBUG){
                        val logging = HttpLoggingInterceptor()
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                        client.addInterceptor(logging)
                    }
                }
                    .readTimeout(1,TimeUnit.MINUTES)
                    .writeTimeout(1,TimeUnit.MINUTES)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}