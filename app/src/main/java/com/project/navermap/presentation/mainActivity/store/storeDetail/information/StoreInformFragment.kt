package com.project.navermap.presentation.mainActivity.store.storeDetail.information

import android.webkit.WebViewClient
import com.project.navermap.databinding.FragmentStoreMarketinformBinding
import com.project.navermap.presentation.base.BaseFragment
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