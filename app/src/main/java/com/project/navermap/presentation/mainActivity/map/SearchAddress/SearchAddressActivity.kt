package com.project.navermap.presentation.mainActivity.map.SearchAddress

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.project.navermap.databinding.ActivitySearchAddressBinding
import com.project.navermap.presentation.mainActivity.MainActivity.Companion.MY_LOCATION_KEY
import com.project.navermap.presentation.myLocation.MyLocationActivity
import com.project.navermap.presentation.myLocation.MyLocationActivity.Companion.SEARCH_LOCATION_KEY


class SearchAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
//        webViewAddress = // 메인 웹뷰
//        webViewLayout = // 웹뷰가 속한 레이아웃
// 공통 설정
        binding.webViewAddress.settings.run {
                javaScriptEnabled = true// javaScript 허용으로 메인 페이지 띄움
                javaScriptCanOpenWindowsAutomatically = true//javaScript window.open 허용
                setSupportMultipleWindows(true)
        }

        binding.webViewAddress.addJavascriptInterface(AndroidBridge(), "Android")

        binding.webViewAddress.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
//page loading을 끝냈을 때 호출되는 콜백 메서드
//안드로이드에서 자바스크립트 메서드 호출
                binding.webViewAddress.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        })
//최초로 웹뷰 로딩
        binding.webViewAddress.loadUrl("https://javaaddress-1b3d9.web.app/")
        //binding.webViewAddress.webChromeClient = webChromeClient

    }

    private inner class AndroidBridge {
        // 웹에서 JavaScript로 android 함수를 호출할 수 있도록 도와줌
        @JavascriptInterface
        fun processDATA(data: String?) {
            //자바 스크립트로 부터 다음 카카오 주소 검색 API 결과를 전달 받는다.
            val extra = Bundle()
            val intent = Intent()
            extra.putString("data", data)
            intent.putExtras(extra)
            setResult(RESULT_OK, intent)
            finish()
        }
    //        fun setAddress(arg1: String?, arg2: String?, arg3: String?) {
//            // search.php에서 callback 호출되는 함수
//            Log.d("arg1.toString()", arg1.toString())
//            Log.d("arg2.toString()", arg2.toString())
//            Log.d("arg3.toString()", arg3.toString())
//
//            val extra = Bundle()
//            val intent = Intent()
//            extra.putString(SEARCH_LOCATION_KEY, String.format("%s %s", arg2, arg3))
//            intent.putExtras(extra)
//            setResult(RESULT_OK, intent)
//            finish()
//        }
    }

    private val webChromeClient = object: WebChromeClient() {

        /// ---------- 팝업 열기 ----------
        /// - 카카오 JavaScript SDK의 로그인 기능은 popup을 이용합니다.
        /// - window.open() 호출 시 별도 팝업 webview가 생성되어야 합니다.
        ///
        lateinit var dialog : Dialog

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreateWindow(view: WebView, isDialog: Boolean,
                                    isUserGesture: Boolean, resultMsg: Message): Boolean {
            // 웹뷰 만들기
            var childWebView = WebView(view.context)
            Log.d("TAG", "웹뷰 만들기")
            // 부모 웹뷰와 동일하게 웹뷰 설정
            childWebView.run {
                settings.run {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    setSupportMultipleWindows(true)
                }
                layoutParams = view.layoutParams
                webViewClient = view.webViewClient
                webChromeClient = view.webChromeClient
            }

            dialog = Dialog(this@SearchAddressActivity).apply {
                setContentView(childWebView)
                window!!.attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
                window!!.attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
                show()
            }

            //webViewLayout.addView(childWebView)
            // TODO: 화면 추가 이외에 onBackPressed() 와 같이
            //       사용자의 내비게이션 액션 처리를 위해
            //       별도 웹뷰 관리를 권장함
            //   ex) childWebViewList.add(childWebView)

            // 웹뷰 간 연동
            val transport = resultMsg.obj as WebView.WebViewTransport
            transport.webView = childWebView
            resultMsg.sendToTarget()

            return true
        }

        override fun onCloseWindow(window: WebView) {
            super.onCloseWindow(window)
            Log.d("로그 ", "onCloseWindow")
            dialog.dismiss()
            //window!!.destroy()
            // 화면에서 제거하기
            // TODO: 화면 제거 이외에 onBackPressed() 와 같이
            //       사용자의 내비게이션 액션 처리를 위해
            //       별도 웹뷰 array 관리를 권장함
            //   ex) childWebViewList.remove(childWebView)
        }
    }
}