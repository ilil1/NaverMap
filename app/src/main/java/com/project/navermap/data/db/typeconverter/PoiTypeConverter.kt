package com.project.navermap.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.project.navermap.data.response.search.Poi

class PoiTypeConverter(
    private val gson: Gson = Gson()
) {

    @TypeConverter
    fun poiToString(poi: Poi): String {
        return gson.toJson(poi)
    }

    @TypeConverter
    fun stringToPoi(jsonString: String): Poi {
        return gson.fromJson(jsonString, Poi::class.java)
    }
}