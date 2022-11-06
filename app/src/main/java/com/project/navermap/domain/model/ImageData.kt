package com.project.navermap.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val csTitle : String,
    val csContentTitle : String,
    val csContentBody :String,
    val csAuthor: String,
) : Parcelable
