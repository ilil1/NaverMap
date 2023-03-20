package com.project.navermap.presentation.mainActivity.myinfo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.myInfoNavGraph(
    navController: NavController,
    viewModel: MyInfoViewModel,
    onClickBackPress: () -> Unit,
    onClickProfileImage: () -> Unit,
    onBackActivity: () -> Unit
){
    navigation(
        startDestination = MyInfoScreenNavs.MyInfoHome.route,
        route = MYINFO_HOME
    ){
        composable(route = MyInfoScreenNavs.MyInfoHome.route){
            MyInfoHomeScreen(
                navController = navController,
                viewModel = viewModel,
                onClickBackPress = onClickBackPress,
                onClickProfileImage = onClickProfileImage,
                onClickClose = onBackActivity
            )
        }
        composable(route = MyInfoScreenNavs.OrderList.route){

        }
        composable(route = MyInfoScreenNavs.ReviewList.route){

        }

        composable(route = MyInfoScreenNavs.FavoriteList.route) {
        }

        composable(route = MyInfoScreenNavs.InterestingList.route){

        }
    }
}