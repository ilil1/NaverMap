package com.project.navermap.presentation.mainActivity.myinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

class MyInfoComposeActivity : ComponentActivity() {

    private val viewModel: MyInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MyInfo(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel
            )
        }
    }
}