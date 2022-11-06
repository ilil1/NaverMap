package com.project.navermap.util

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONException
import org.json.JSONObject

object PreferenceManager {

    val PREFERENCESUSER_NAME = "account_contain"

    private val DEFAULT_VALUE_STRING = " "
    private val DEFAULT_VALUE_BOOLEAN = false
    private val DEFAULT_VALUE_INT = -1
    private val DEFAULT_VALUE_LONG = -1L
    private val DEFAULT_VALUE_FLOAT = -1f

    private fun getPreferences(context: Context, value :String): SharedPreferences {
        return context.getSharedPreferences(value, Context.MODE_PRIVATE)
    }
    private fun getUserPreferences(context: Context, value :String): SharedPreferences {
        return context.getSharedPreferences(value, Context.MODE_PRIVATE)
    }
    private fun getTempUserPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCESUSER_NAME, Context.MODE_PRIVATE)
    }
    private fun getImgPreferences(context: Context, value: String): SharedPreferences {
        return context.getSharedPreferences(value, Context.MODE_PRIVATE)
    }
    fun getMy_ID(context: Context): String {
        val prefbs = getTempUserPreferences(context)
        val col_val: Collection<*> = prefbs.all.values
        val it_val = col_val.iterator()
        val values = it_val.next() as String
        var MY_ID : String = ""

        // value 값은 다음과 같이 저장되어있다
        // "{\"title\":\"hi title\",\"content\":\"hi content\"}"

        try {
            // String으로 된 value를 JSONObject로 변환하여 key-value로 데이터 추출
            val jsonObject = JSONObject(values)
            MY_ID = jsonObject.getString("MY_ID") as String
        } catch (e: JSONException) { }

        return MY_ID
    }
    /* String 값 저장
       param context
       param key
       param value
     */
    fun setString(context: Context, key: String?, value: String?, string: String) {
        val prefs = getPreferences(context, string)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun setUserString(context: Context, key: String?, value: String?, string: String) {
        val prefs = getUserPreferences(context, string)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun setTempUserString(context: Context, key: String?, value: String?) {
        val prefs = getTempUserPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun setImgString(context: Context, key: String?, value: String?, string: String) {
        val prefs = getImgPreferences(context, string)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
    /* String 값 로드
       param context
       param key
       return
     */
    fun getString(context: Context, key: String?, string: String): String? {
        val prefs = getPreferences(context, string)
        return prefs.getString(key, DEFAULT_VALUE_STRING)
    }
    /* 키 값 삭제
      param context
      param key
    */
    fun removeKey(context: Context, key: String?, string: String) {
        val prefs = getPreferences(context, string)
        val editor = prefs.edit()
        editor.remove(key)
        editor.commit()
    }
    /* 모든 저장 데이터 삭제
      param context
    */
    fun clear(context: Context, string: String) {
        val prefs = getPreferences(context, string)
        val editor = prefs.edit()
        editor.clear()
        editor.commit()
    }
}