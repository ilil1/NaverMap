package com.project.navermap

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.project.navermap.app.config.AppConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MapApplication : Application(){

    var context: Context? = this

    companion object{

        lateinit var appConfig: AppConfig
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this,getString(R.string.kakao_app_key))

        appConfig = context?.let { AppConfig(it) }!!
    }

    override fun onTerminate() {
        super.onTerminate()

        context = null
    }

}