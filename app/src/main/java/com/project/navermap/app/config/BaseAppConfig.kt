package com.project.navermap.app.config

import android.content.Context

open class BaseAppConfig(
    val context: Context,
    val appConfigName: String
) {
    protected val sharedPreferences = context.getSharedPreferences(appConfigName,Context.MODE_PRIVATE)
}