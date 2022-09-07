package com.project.navermap.di.moduleComponent.singletonComponent

import com.project.navermap.BuildConfig
import com.project.navermap.data.network.FoodApiService
import com.project.navermap.data.network.KakaoAddressApiService
import com.project.navermap.data.network.MapApiService
import com.project.navermap.data.network.ShopController
import com.project.navermap.data.url.Url
import com.project.navermap.di.annotation.networkmodule.FoodRetrofitInstance
import com.project.navermap.di.annotation.networkmodule.KakaoRetrofitInstance
import com.project.navermap.di.annotation.networkmodule.ShopRetrofitInstance
import com.project.navermap.di.annotation.networkmodule.TmapRetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()

        interceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @TmapRetrofitInstance
    @Provides
    @Singleton
    fun provideTmapRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.TMAP_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @ShopRetrofitInstance
    @Provides
    @Singleton
    fun provideShopRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Url.SHOP_URL)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
    }

    @FoodRetrofitInstance
    @Provides
    @Singleton
    fun provideFoodRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.FOOD_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @KakaoRetrofitInstance
    @Provides
    @Singleton
    fun provideKakaoRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.Kakao_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideMapApiService(@TmapRetrofitInstance retrofit: Retrofit): MapApiService {
        return retrofit.create(MapApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideShopController(@ShopRetrofitInstance retrofit: Retrofit): ShopController {
        return retrofit.create(ShopController::class.java)
    }

    @Provides
    @Singleton
    fun provideFoodController(@FoodRetrofitInstance retrofit: Retrofit): FoodApiService {
        return retrofit.create(FoodApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoController(@KakaoRetrofitInstance retrofit: Retrofit): KakaoAddressApiService {
        return retrofit.create(KakaoAddressApiService::class.java)
    }
}