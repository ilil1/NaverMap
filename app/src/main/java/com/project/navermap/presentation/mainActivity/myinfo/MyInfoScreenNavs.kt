package com.project.navermap.presentation.mainActivity.myinfo

const val MYINFO_NAV = "myInfo_Nav"
const val MYINFO_HOME = "myInfo_Home"

sealed class MyInfoScreenNavs(val route: String){

    object MyInfoHome: MyInfoScreenNavs(route = "home")

}
