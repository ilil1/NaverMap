package com.project.navermap.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MapStudy")
data class AddressHistoryEntity(
    @PrimaryKey(autoGenerate = true) //키가 1,2,3,4 ... 순서대로 자동으로 할당
    val id: Long?,
    val name: String,
    val lat: Double,
    val lng: Double
)
