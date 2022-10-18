package com.project.navermap.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikeItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val itemName: String,
    val marketName: String,
    val marketDistance: Float,
    val saleRatio: Int,
    val price: Int,
    val imageUrl: String
)

