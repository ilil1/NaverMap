package com.project.navermap

import android.app.Activity
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.project.navermap.MainActivity.Companion.MY_LOCATION_KEY
import com.project.navermap.databinding.ActivitySearchAddressBinding


class SearchAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchAddressBinding
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        binding.webViewAddress.settings.apply {
            javaScriptEnabled = true//javaScript 허용
            javaScriptCanOpenWindowsAutomatically = true //javaScript window.open 허용
            setSupportMultipleWindows(true)
        }

        binding.webViewAddress.apply {
            webViewClient = client
            webChromeClient = chromeClient
            addJavascriptInterface(AndroidBridge(), "TestApp")
            loadUrl("http://3.36.51.15/search.php")
        }
    }

    // TODO : search.php에서 arg1 매개변수 안넣게 하기
    private inner class AndroidBridge {
        // 웹에서 JavaScript로 android 함수를 호출할 수 있도록 도와줌
        @JavascriptInterface
        fun setAddress(arg1: String?, arg2: String?, arg3: String?) {
            // search.php에서 호출되는 함수
            handler.post {
                setResult(RESULT_OK,
                Intent().apply {
                        putExtra(MY_LOCATION_KEY, String.format("%s %s", arg2, arg3))
                    },
                )
                finish()
            }
        }
    }

    // TODO : 제거?
    private val client: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }

        // TODO : 제거?
        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?
        ) {
            handler?.proceed()
        }
    }

    private val chromeClient = object : WebChromeClient() {

        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {

            val newWebView = WebView(this@SearchAddressActivity).apply {
                settings.javaScriptEnabled = true
            }

            setContentView(newWebView)
            newWebView.webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(
                    view: WebView,
                    url: String,
                    message: String,
                    result: JsResult
                ): Boolean {
                    super.onJsAlert(view, url, message, result)

                    // 선택한 주소 출력
//                    Toast.makeText(
//                        this@SearchAddressActivity,
//                        "결과 : " + result.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()

                    return true
                }
            }

            (resultMsg!!.obj as WebView.WebViewTransport).webView = newWebView
            resultMsg.sendToTarget()

            return true
        }
    }
}