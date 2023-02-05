package com.project.navermap.domain.usecase.mapViewmodel.legacyShop

//일단은 이렇게 UseCase를 구성하지만 서버로 비즈니스 로직을 옮겨야함.
//class GetUpdateMarkerUseCaseImpl @Inject constructor(
//    private val ioDispatcher: CoroutineDispatcher
//) {
//
//    private var markers = mutableListOf<Marker>()
//
//
//    fun setMarkers(list: List<Marker>) {
//        markers.clear()
//        markers = list as MutableList
//    }
//
//    fun getMarkers() : List<Marker> {
//        return markers
//    }
//
//    private fun getCategoryNum(category: String): Int =
//        when (category) {
//            "FOOD_BEVERAGE" -> 0
//            "SERVICE" -> 1
//            "ACCESSORY" -> 2
//            "MART" -> 3
//            "FASHION" -> 4
//            else -> 5
//        }
//
//    suspend fun updateMarker(
//        filterCategoryChecked : MutableList<Boolean>,
//        shopList: List<ShopInfoEntity>?) = withContext(ioDispatcher) {
//
//        var temp = arrayListOf<Marker>()
//        var i = 0
//
//        shopList?.let {
//            repeat(shopList.size) {
//                if (filterCategoryChecked[getCategoryNum(shopList[i].category)]) {
//                    temp += Marker().apply {
//                        position = LatLng(shopList[i].latitude, shopList[i].longitude)
//                        icon = MarkerIcons.BLACK
//                        tag = shopList[i].shop_name
//                        zIndex = i
//                    }
//                }
//                i++
//            }
//            setMarkers(temp)
//        }
//        MarkerResult.Success
//    }
//}