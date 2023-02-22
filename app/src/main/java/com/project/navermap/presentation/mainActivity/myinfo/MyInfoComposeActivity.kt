package com.project.navermap.presentation.mainActivity.myinfo

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.project.navermap.presentation.ui.theme.NaverMapTheme

class MyInfoComposeActivity : ComponentActivity() {

    private val viewModel: MyInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            NaverMapTheme{
                SetUpMyInfoNavGraph(
                    navController = navController,
                    viewModel = viewModel,
                    onClickBackPress = {},
                    onClickProfileImage = {},
                    onBackActivity = { finish() }
                )
            }
        }
    }
}