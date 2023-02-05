package com.project.navermap.domain.model

import android.graphics.Bitmap
import android.media.Image
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "profile")
data class ProfileData(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val image : Int? = null
)
