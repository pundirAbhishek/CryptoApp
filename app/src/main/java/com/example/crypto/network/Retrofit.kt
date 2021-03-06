package com.example.crypto.network

import android.content.Context
import com.example.crypto.data.api.CryptoApi
import com.example.crypto.data.api.CryptoApiMapper
import com.example.crypto.data.repository.CryptoRepository
import com.example.crypto.data.repository.CryptoRepositoryImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Retrofit {

    private const val timeOut = 20L

    private val commonHeadersAppenderInterceptor: CommonHeadersAppenderInterceptor by lazy {
        CommonHeadersAppenderInterceptor()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder().apply {
            readTimeout(timeOut, TimeUnit.SECONDS)
            connectTimeout(timeOut, TimeUnit.SECONDS)
            addInterceptor(commonHeadersAppenderInterceptor)
        }.build()
    }


    //TODO : Add Common Convertor Factory
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(UrlConfig.cryptoBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createRepository(context: Context): CryptoRepository {
        return CryptoRepositoryImpl(
            cryptoApi = retrofit.create(CryptoApi::class.java),
            cryptoApiMapper = CryptoApiMapper()
        )
    }

}