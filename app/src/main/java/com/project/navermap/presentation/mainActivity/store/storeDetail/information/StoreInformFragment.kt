package com.project.navermap.presentation.mainActivity.store.storeDetail.information

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.databinding.FragmentStoreMarketMenuBinding
import com.project.navermap.databinding.FragmentStoreMarketinformBinding
import com.project.navermap.presentation.base.BaseFragment
import com.project.navermap.presentation.mainActivity.store.storeDetail.menu.StoreMenuFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreInformFragment : BaseFragment<FragmentStoreMarketinformBinding>() {

    override fun getViewBinding() = FragmentStoreMarketinformBinding.inflate(layoutInflater)

    override fun initState() {
        init()
        super.initState()
    }

    companion object {
        fun newInstance() : StoreInformFragment {
            return StoreInformFragment()
        }
    }

    private fun init() {
        binding.webViewAddress.setWebViewClient(WebViewClient()) //새창 열기 없이 웹뷰내에서 다시 열기
        binding.webViewAddress.loadUrl("https://www.naver.com")
    }
}