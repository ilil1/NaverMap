package com.project.navermap.presentation.mainActivity.myinfo

const val MYINFO_NAV = "myInfo_Nav"
const val MYINFO_HOME = "myInfo_Home"

sealed class MyInfoScreenNavs(val route: String){
    object MyInfoHome: MyInfoScreenNavs(route = "home")
    object CustomerServiceCenter: MyInfoScreenNavs(route = "customerServiceCenter")
    object OrderList: MyInfoScreenNavs(route = "order_list")
    object ReviewList: MyInfoScreenNavs(route = "review_list")
    object FavoriteList: MyInfoScreenNavs(route = "favorite_list")
    object InterestingList: MyInfoScreenNavs(route = "interesting_list")

}
