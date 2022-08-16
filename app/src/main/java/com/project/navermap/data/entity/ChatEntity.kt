package com.project.navermap.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatEntity(
    val storeName : String,
) : Parcelable
