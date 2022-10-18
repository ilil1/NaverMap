package com.project.navermap.domain.usecase.mapViewmodel.legacyShop

//class MarkerListenerUseCaseImpl @Inject constructor(
//    private val context: Context
//) {
//
//    private var infoWindow: InfoWindow? = null
//
//    fun setMarkerListener(markers : List<Marker>) {
//        for (marker in markers) {
//            val tempInfoWindow = InfoWindow()
//            tempInfoWindow.adapter = object : InfoWindow.DefaultTextAdapter(context) {
//                override fun getText(infoWindow: InfoWindow): CharSequence {
//                    return infoWindow.marker?.tag as CharSequence
//                }
//            }
//            infoWindow = tempInfoWindow
//            marker.setOnClickListener {
//
//                if (tempInfoWindow.marker != null) {
//                    tempInfoWindow.close()
//                } else {
//                    tempInfoWindow.open(marker)
//                }
//
//                // 여기서 오픈한 말풍선은 fbtnViewPager2를 클릭하면 제거
////                viewPagerAdapter.registerStore(markets[marker.zIndex])
////                binding.viewPager2.adapter = viewPagerAdapter
////                binding.viewPager2.visibility = View.VISIBLE
////                binding.fbtnCloseViewPager.visibility = View.VISIBLE
//                true
//            }
//        }
//    }
//}