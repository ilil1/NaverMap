package com.project.navermap.presentation.myLocation.mapLocationSetting

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.data.url.Key
import com.project.navermap.databinding.ActivityMapLocationSettingBinding
import com.project.navermap.presentation.myLocation.MyLocationActivity
import com.skt.Tmap.TMapView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MapLocationSettingActivity : AppCompatActivity() {

    private val viewModel: MapLocationSettingViewModel by viewModels()

    var isCurAddressNull = true
    private lateinit var tMapView: TMapView
    private lateinit var binding: ActivityMapLocationSettingBinding

    companion object {
        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MapLocationSettingActivity::class.java).apply {
                putExtra(MyLocationActivity.MY_LOCATION_KEY, mapSearchInfoEntity)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapLocationSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMap()

        binding.btnSetCurLocation.setOnClickListener {

            if (!isCurAddressNull) {
                val entity = viewModel.getMapSearchInfo()
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
        observeData()
    }

    private fun observeData() = with(binding) {

        viewModel.locationData.observe(this@MapLocationSettingActivity) {
            when (it) {
                is MapLocationSettingState.Uninitialized -> {
                }

                is MapLocationSettingState.Loading -> {}

                is MapLocationSettingState.Success -> {
                    tvCurAddress.text = it.mapSearchInfoEntity.fullAddress
                    isCurAddressNull = false
                }

                is MapLocationSettingState.Error -> {
                    tvCurAddress.text = getString(it.errorMessage)
                }
            }
        }
    }

    private fun initMap() = with(binding) {

        tMapView = TMapView(this@MapLocationSettingActivity).apply {
            setSKTMapApiKey(Key.TMap_View_API) //지도 출력APIkey
            setOnDisableScrollWithZoomLevelListener { _, tMapPoint ->
                isCurAddressNull = true
                viewModel.getReverseGeoInformation(
                    LocationEntity(tMapPoint.latitude, tMapPoint.longitude)
                )
            }
        }

        TMap.addView(tMapView)

        //MainActivity로 부터 받아온 값으로 처음 위치 초기화
        val entity =  intent.getParcelableExtra<MapSearchInfoEntity>(MyLocationActivity.MY_LOCATION_KEY)

        tvCurAddress.text = entity?.fullAddress ?: "정보없음"
        tMapView.setLocationPoint(entity?.locationLatLng!!.longitude, entity.locationLatLng.latitude)
        tMapView.setCenterPoint(entity.locationLatLng.longitude, entity.locationLatLng.latitude)
    }
}