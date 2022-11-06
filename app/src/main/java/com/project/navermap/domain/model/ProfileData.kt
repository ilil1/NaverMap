package com.project.navermap.domain.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileData(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var image : Bitmap? = null
)
