package com.project.navermap.screen.map.myLocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.navermap.*
import com.project.navermap.data.db.MapDB
import com.project.navermap.data.entity.AddressHistoryEntity
import com.project.navermap.databinding.ActivityMyLocationBinding
import com.project.navermap.screen.map.mapLocationSetting.MapLocationSettingActivity
import kotlinx.coroutines.runBlocking

class MyLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyLocationBinding
    lateinit var database: MapDB
    lateinit var recentAddrAdapter: RecentAddrAdapter

    var dataList : MutableList<AddressHistoryEntity> = mutableListOf()

    companion object {

        const val MY_LOCATION_KEY = "MY_LOCATION_KEY"

        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(MY_LOCATION_KEY, mapSearchInfoEntity)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = MapDB.getInstance(this)!!

        binding.btnSetLocation.setOnClickListener {
            startForResult.launch(
                MapLocationSettingActivity.newIntent
                (this, intent.getParcelableExtra(MY_LOCATION_KEY)!!)
            )
        }

        binding.btnClear.setOnClickListener {
            runBlocking {
                database.addressHistoryDao().deleteAllAddresses()
                recentAddrAdapter.clear()
                recentAddrAdapter.notifyDataSetChanged()
            }
        }

        binding.etSearch.setOnClickListener {
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        recentAddrAdapter = RecentAddrAdapter { item ->
            intent?.putExtra(MY_LOCATION_KEY, MapSearchInfoEntity(item.name, item.name, LocationEntity(item.lat, item.lng)))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.rvRecentAddr.layoutManager = LinearLayoutManager(
            this@MyLocationActivity,
            LinearLayoutManager.VERTICAL,
            false)

        binding.rvRecentAddr.adapter = recentAddrAdapter

        runBlocking {
            //배열로 하나씩 받아서 넣어준다.
            for (allAddress in database.addressHistoryDao().getAllAddresses()) {
                recentAddrAdapter.datas.add(allAddress)
            }
        }
    }

    private fun saveRecentSearchItems(entity: MapSearchInfoEntity) = runBlocking {
        val data = AddressHistoryEntity(
            id = null,
            name = entity.fullAddress,
            lat = entity.locationLatLng.latitude,
            lng = entity.locationLatLng.longitude
        )
        database.addressHistoryDao().insertAddress(data)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {

                val bundle = result.data?.extras
                val result = bundle?.get("result")

                intent?.putExtra(MY_LOCATION_KEY, result as MapSearchInfoEntity)
                setResult(Activity.RESULT_OK, intent)

                saveRecentSearchItems(result as MapSearchInfoEntity)

                Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()
                //finish()//반환시에 MyLocationActivity finish()
            }
        }
}