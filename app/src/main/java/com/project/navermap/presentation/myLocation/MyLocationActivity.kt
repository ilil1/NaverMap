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

                viewModel.saveRecentSearchItems(result as MapSearchInfoEntity)

                Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()
                finish() //반환시에 MyLocationActivity finish()
            }
        }

    companion object {

        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"

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

        binding.etSearch.setOnClickListener {}

        binding.ivBack.setOnClickListener {
            finish()
        }

        recentAddrAdapter = RecentAddrAdapter { item ->
            intent.putExtra(MY_LOCATION_KEY, MapSearchInfoEntity(item.name, item.name, LocationEntity(item.lat, item.lng)))
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.rvRecentAddr.layoutManager = LinearLayoutManager(
            this@MyLocationActivity, LinearLayoutManager.VERTICAL, false)

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