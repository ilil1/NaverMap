package com.project.navermap.presentation.myLocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.project.navermap.*
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.data.url.Key
import com.project.navermap.data.url.Url
import com.project.navermap.databinding.ActivityMyLocationBinding
import com.project.navermap.extensions.showToast
import com.project.navermap.presentation.mainActivity.MainActivity
import com.project.navermap.presentation.mainActivity.map.SearchAddress.SearchAddressActivity
import com.project.navermap.presentation.myLocation.mapLocationSetting.MapLocationSettingActivity
import com.project.navermap.widget.RecentAddrAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@AndroidEntryPoint
class MyLocationActivity : AppCompatActivity() {

    private val viewModel: MyLocationViewModel by viewModels()

    private lateinit var binding: ActivityMyLocationBinding
    lateinit var recentAddrAdapter: RecentAddrAdapter

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {

                val bundle = result.data?.extras
                val result = bundle?.get("result")

                intent?.putExtra(MY_LOCATION_KEY, result as MapSearchInfoEntity)
                setResult(Activity.RESULT_OK, intent)

                viewModel.saveRecentSearchItems(result as MapSearchInfoEntity) //Room에 저장
                Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()
                finish()
            }
        }

    private val startSearchActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {

                    val bundle = result.data?.extras //인텐트로 보낸 extras를 받아옵니다.
                    val str = bundle?.get(SEARCH_LOCATION_KEY).toString()
                    var asw: MapSearchInfoEntity?

                    Log.d("SearchActivityForResult", str)

                    // TODO: coroutine과 retrofit으로 바꾸기
                    Thread {

                        val obj: URL
                        val address: String = URLEncoder.encode(str, "UTF-8")

                        obj = URL(Url.GEOCODE_URL + address)

                        val con: HttpURLConnection = obj.openConnection() as HttpURLConnection

                        con.setRequestMethod("GET")
                        con.setRequestProperty("Authorization", "KakaoAK " + Key.GEOCODE_USER_INFO)
                        con.setRequestProperty("content-type", "application/json")
                        con.setDoOutput(true)
                        con.setUseCaches(false)
                        con.setDefaultUseCaches(false)

                        val data = con.inputStream.bufferedReader().readText()

                        Log.d("application/json", data)

                        val dataList = "[$data]"
                        val xy = Gson().fromJson(dataList, Array<Address>::class.java).toList()

                        asw = MapSearchInfoEntity(
                            xy[0].documents[0].addressName,
                            xy[0].documents[0].roadAddress.buildingName,
                            LocationEntity(
                                xy[0].documents[0].y.toDouble(),
                                xy[0].documents[0].x.toDouble())
                        )

                        intent?.putExtra(MY_LOCATION_KEY, asw)
                        setResult(Activity.RESULT_OK, intent)
                        viewModel.saveRecentSearchItems(asw!!)

                        finish()
                    }.start()
                }
            }
        }

    companion object {

        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"
        const val SEARCH_LOCATION_KEY = "SEARCH_LOCATION_KEY"

        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(MainActivity.MY_LOCATION_KEY, mapSearchInfoEntity)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSetLocation.setOnClickListener {
            startForResult.launch(
                MapLocationSettingActivity.newIntent
                    (this, intent.getParcelableExtra(MainActivity.MY_LOCATION_KEY)!!)
            )
        }

        binding.btnClear.setOnClickListener {
            viewModel.deleteAllAddresses()
            recentAddrAdapter.clear()
            recentAddrAdapter.notifyDataSetChanged()
        }

        binding.etSearch.setOnClickListener {
            startSearchActivityForResult.launch(
                Intent(applicationContext, SearchAddressActivity::class.java)
            )
        }
        binding.ivBack.setOnClickListener {
            finish()
        }

        recentAddrAdapter = RecentAddrAdapter { addressData ->
            intent.putExtra(
                MY_LOCATION_KEY,
                MapSearchInfoEntity(
                    fullAddress = addressData.fullAddress,
                    name = addressData.name,
                    locationLatLng = LocationEntity(addressData.lat, addressData.lng)
                )
            )
            setResult(RESULT_OK, intent)
            finish()
        }
        observeData()
    }

    private fun observeData() = with(binding) {

        viewModel.AddressesData.observe(this@MyLocationActivity) {
            when (it) {
                is MyLocationState.Uninitialized -> {
                    viewModel.getAllAddresses()
                }
                is MyLocationState.Loading -> {}
                is MyLocationState.Success -> {
                    for (allAddress in it.addressHistoryList!!) {
                        recentAddrAdapter.datas.add(allAddress)
                    }
                    binding.rvRecentAddr.adapter = recentAddrAdapter
                }
                is MyLocationState.Error -> {}
            }
        }
    }
}