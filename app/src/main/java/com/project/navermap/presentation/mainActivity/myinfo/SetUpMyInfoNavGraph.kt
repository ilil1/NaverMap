package com.project.navermap.presentation.mainActivity.myinfo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun SetUpMyInfoNavGraph(
    navController: NavHostController,
    viewModel: MyInfoViewModel,
    onClickBackPress: () -> Unit,
    onClickProfileImage: () -> Unit,
    onBackActivity: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MYINFO_HOME,
        route = MYINFO_NAV,
    ) {
        myInfoNavGraph(
            navController = navController,
            viewModel = viewModel,
            onClickBackPress = onClickBackPress,
            onClickProfileImage = onClickProfileImage,
            onBackActivity = onBackActivity
        )
    }
}