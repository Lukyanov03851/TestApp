package com.lukyanov.vyacheslav.testapp.api

import com.lukyanov.vyacheslav.testapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val ENDPOINT = "https://jsonplaceholder.typicode.com/"

class APIClient {

    private var retrofit: Retrofit? = null

    private val logLevel = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    /**
     * Returns Retrofit builder to create
     * @return retrofit
     */
    private fun getClient(): Retrofit {

        val logging = HttpLoggingInterceptor()
        logging.level = logLevel

        val client = OkHttpClient.Builder().connectTimeout(3,
                TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3,
                        TimeUnit.MINUTES).addInterceptor(logging).build()

        if(null == retrofit) {
            retrofit = Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build()
        }

        return retrofit!!
    }

    fun getAPIService(): APIService = getClient().create(APIService::class.java)

}