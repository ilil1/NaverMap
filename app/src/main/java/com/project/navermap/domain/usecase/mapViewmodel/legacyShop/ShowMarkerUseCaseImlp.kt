package com.project.navermap.domain.usecase.mapViewmodel.legacyShop

//class ShowMarkerUseCaseImlp @Inject constructor(
//    private val getUpdateMarkerUseCaseImpl : GetUpdateMarkerUseCaseImpl
//) {
//
//    fun showMarkersOnMap(naverMap: NaverMap?, shopList: List<ShopInfoEntity>?, markers : List<Marker>) {
//        if (markers.isNullOrEmpty())
//            return
//
//        for (marker in markers) {
//            marker.map = naverMap
//            setMarkerIconAndColor(marker, getCategoryNum(shopList?.get(marker.zIndex)!!.category))
//        }
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
//    fun setMarkerIconAndColor(marker: Marker, category: Int) = with(marker) {
//        when (category) {
//            0 -> {
//                icon = OverlayImage.fromResource(R.drawable.marker_m)
//                iconTintColor = Color.parseColor("#46F5FF")
//            }
//            1 -> {
//                icon = OverlayImage.fromResource(R.drawable.marker_r)
//                iconTintColor = Color.parseColor("#FFCB41")
//            }
//            2 -> {
//                icon = OverlayImage.fromResource(R.drawable.marker_s)
//                iconTintColor = Color.parseColor("#886AFF")
//            }
//            3 -> {
//                icon = OverlayImage.fromResource(R.drawable.marker_e)
//                iconTintColor = Color.parseColor("#04B404")
//            }
//            4 -> {
//                icon = OverlayImage.fromResource(R.drawable.marker_f)
//                iconTintColor = Color.parseColor("#8A0886")
//            }
//            5 -> {
//                icon = OverlayImage.fromResource(R.drawable.marker_f)
//                iconTintColor = Color.parseColor("#0B2F3A")
//            }
//        }
//    }
//}