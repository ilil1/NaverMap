package com.project.navermap.app.config

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppConfig @Inject constructor(
    @ApplicationContext context: Context,
    appConfigName: String = "withMarket"
):BaseAppConfig(context = context, appConfigName = appConfigName){




}