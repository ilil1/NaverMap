package com.project.navermap.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.navermap.data.response.search.Poi

@Entity(tableName = "map_search_cache")
data class MapSearchCacheEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long? = null,
    val poi: Poi
)
