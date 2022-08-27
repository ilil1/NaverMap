package com.project.navermap.presentation.myLocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.navermap.*
import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.MapSearchInfoEntity
import com.project.navermap.databinding.ActivityMyLocationBinding
import com.project.navermap.presentation.mainActivity.MainActivity
import com.project.navermap.presentation.mainActivity.map.SearchAddress.SearchAddressActivity
import com.project.navermap.presentation.myLocation.mapLocationSetting.MapLocationSettingActivity
import com.project.navermap.widget.RecentAddrAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

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
        { results ->
            if(results.resultCode == RESULT_OK) {
                if(results.data != null) {
                    val result = results.data!!.getStringExtra(SEARCH_LOCATION_KEY)
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
                Intent(
                    applicationContext,
                    SearchAddressActivity::class.java
                )
            )
        }
        binding.ivBack.setOnClickListener {
            finish()
        }

        recentAddrAdapter = RecentAddrAdapter { addressData ->
            intent.putExtra(MY_LOCATION_KEY,
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