package com.project.navermap.screen

import android.location.Location
import androidx.lifecycle.ViewModel
import com.project.navermap.data.entity.LocationEntity

class MainViewModel() : ViewModel() {

    private lateinit var destLocation: LocationEntity
    lateinit var curLocation: Location

    fun setCurrentLocation(loc: Location) { curLocation = loc }

    fun getCurrentLocation() : Location { return curLocation }

    fun setDestinationLocation(loc: LocationEntity) { destLocation = loc }

    fun getDestinationLocation(): LocationEntity { return destLocation }
}