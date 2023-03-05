package com.project.navermap.app.config

import android.content.Context
import android.content.SharedPreferences
import com.project.navermap.util.PreferenceManager.getString
import javax.inject.Inject

class AppConfig @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val NICKNAME_KEY = "nickName"

        fun create(context: Context, fileName: String): AppConfig {
            val sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
            return AppConfig(sharedPreferences)
        }
    }

    fun getNickName(): String? {
        return sharedPreferences.getString(NICKNAME_KEY, null)
    }

    fun setNickName(nickName: String) {
        sharedPreferences.edit().putString(NICKNAME_KEY, nickName)
    }


}