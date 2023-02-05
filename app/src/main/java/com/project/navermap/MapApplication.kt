package com.project.navermap

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MapApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this,getString(R.string.kakao_app_key))

    }

}