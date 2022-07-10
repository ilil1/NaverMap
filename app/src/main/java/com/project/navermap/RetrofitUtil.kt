package com.project.navermap

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtil {

    private var shopinstance: Retrofit? = null
    private var instance: Retrofit? = null

    val shopController: ShopController by lazy {
        provideShopRetrofit().create(ShopController::class.java)
    }

    val mapApiService: MapApiService by lazy {
        getRetrofit().create(MapApiService::class.java)
    }

    private fun getRetrofit(): Retrofit {
        if(instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(Url.TMAP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildOkHttpClient())
                .build()
        }
        return instance!!
    }

    private fun provideShopRetrofit(): Retrofit {
        if(shopinstance == null) {
            shopinstance = Retrofit.Builder()
                .baseUrl(Url.SHOP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildOkHttpClient())
                .build()
        }
        return shopinstance!!
    }

    private fun buildOkHttpClient(): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()

        interceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }
}