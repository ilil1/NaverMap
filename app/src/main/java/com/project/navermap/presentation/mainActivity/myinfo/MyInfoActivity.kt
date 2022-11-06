package com.project.navermap.presentation.mainActivity.myinfo

import android.os.Bundle
import com.project.navermap.databinding.ActivityMyinfoBinding
import com.project.navermap.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.prefs.Preferences

@AndroidEntryPoint
class MyInfoActivity: BaseActivity<ActivityMyinfoBinding>() {



    override fun getViewBinding(): ActivityMyinfoBinding =
        ActivityMyinfoBinding.inflate(layoutInflater)

    override fun observeData() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }





    override fun initViews() = with(binding) {
        super.initViews()


    }

}