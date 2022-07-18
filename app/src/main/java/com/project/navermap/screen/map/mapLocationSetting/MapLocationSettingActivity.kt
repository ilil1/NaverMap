package com.project.navermap.screen.map.mapLocationSetting

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.project.navermap.Key
import com.project.navermap.LocationEntity
import com.project.navermap.MapSearchInfoEntity
import com.project.navermap.RetrofitUtil
import com.project.navermap.databinding.ActivityMapLocationSettingBinding
import com.skt.Tmap.TMapView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapLocationSettingActivity : AppCompatActivity() {

    var isCurAddressNull = true

    private lateinit var tMapView: TMapView
    private lateinit var binding: ActivityMapLocationSettingBinding
    private lateinit var uiScope: CoroutineScope // 코루틴 생명주기 관리
    private lateinit var mapSearchInfoEntity : MapSearchInfoEntity

    private var Text: MutableLiveData<String> = MutableLiveData()

    companion object {

        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"

        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MapLocationSettingActivity::class.java).apply {
                putExtra(MY_LOCATION_KEY, mapSearchInfoEntity)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapLocationSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uiScope = CoroutineScope(Dispatchers.Main) // UI와 상호작용하거나 빠른 작업을 위해 메인스레드에서 코루틴 실행
        initMap()

//        Text.observe(this, Observer {
//            // it로 넘어오는 param은 LiveData의 value
//            isCurAddressNull = false
//            binding.tvCurAddress.text = it
//        })

        binding.btnSetCurLocation.setOnClickListener {

            if (!isCurAddressNull) {
                val entity = mapSearchInfoEntity
                val intent = Intent()
                intent.putExtra("result", entity)
                setResult(Activity.RESULT_OK, intent)
                Toast.makeText(this@MapLocationSettingActivity, "설정완료!", Toast.LENGTH_SHORT).show()
                finish()
            }
            else {
                Toast.makeText(
                    this@MapLocationSettingActivity,
                    "위치를 드래그해서 갱신해주세요!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun getReverseGeoInformation(locationLatLngEntity: LocationEntity) {

        uiScope.launch {
            withContext(Dispatchers.IO) {

                val currentLocation = locationLatLngEntity
                val response = RetrofitUtil.mapApiService.getReverseGeoCode(
                    lat = locationLatLngEntity.latitude,
                    lon = locationLatLngEntity.longitude
                )//response = addressInfo

                if (response.isSuccessful) {
                    val body = response.body()
                    withContext(Dispatchers.Main) {

                       mapSearchInfoEntity = MapSearchInfoEntity(
                            fullAddress = body!!.addressInfo.fullAddress ?: "주소 정보 없음",
                            name = body!!.addressInfo.buildingName ?: "주소 정보 없음",
                            locationLatLng = currentLocation
                        )

                        binding.tvCurAddress.text = mapSearchInfoEntity.fullAddress
                        isCurAddressNull = false
                        //Text.value = mapSearchInfoEntity.fullAddress
                    }
                }
                else {
                    null
                }
            }
        }
    }

    private fun initMap() = with(binding) {

        tMapView = TMapView(this@MapLocationSettingActivity).apply {
            setSKTMapApiKey(Key.TMap_View_API) //지도 출력APIkey
            setOnDisableScrollWithZoomLevelListener { _, tMapPoint ->
                isCurAddressNull = true
                getReverseGeoInformation(
                    LocationEntity(tMapPoint.latitude, tMapPoint.longitude)
                )
            }
        }

        TMap.addView(tMapView)

        val entity =  intent.getParcelableExtra<MapSearchInfoEntity>(MY_LOCATION_KEY)

        tvCurAddress.text = entity?.fullAddress ?: "정보없음"
        tMapView.setLocationPoint(entity?.locationLatLng!!.longitude, entity.locationLatLng.latitude)
        tMapView.setCenterPoint(entity.locationLatLng.longitude, entity.locationLatLng.latitude)
    }
}