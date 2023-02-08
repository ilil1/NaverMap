package com.project.navermap.presentation.mainActivity.myinfo

import android.os.Bundle
import androidx.activity.viewModels
import com.project.navermap.R
import com.project.navermap.databinding.ActivityMyinfoBinding
import com.project.navermap.presentation.base.BaseActivity
import com.project.navermap.presentation.login.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.prefs.Preferences

@AndroidEntryPoint
class MyInfoActivity: BaseActivity<ActivityMyinfoBinding>() {

    private val viewModel : SignUpViewModel by viewModels()

    override fun getViewBinding(): ActivityMyinfoBinding =
        ActivityMyinfoBinding.inflate(layoutInflater)

    override fun observeData() {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = viewModel.userName.value.toString()
        var myinfo = MyInfoFragment()
        var bundle = Bundle()
        bundle.putString("data",data)
        myinfo.arguments = bundle

    }

    override fun initViews() = with(binding) {
        super.initViews()


    }

}