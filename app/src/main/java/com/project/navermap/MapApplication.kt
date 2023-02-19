package com.project.navermap

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.project.navermap.app.config.AppConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MapApplication : Application(){

    companion object{
        lateinit var context: Context
        lateinit var appConfig: AppConfig
    }

    init {
        context = this
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this,getString(R.string.kakao_app_key))

        appConfig = AppConfig(context)
    }

}