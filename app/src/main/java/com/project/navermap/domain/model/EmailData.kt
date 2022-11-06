package com.project.navermap.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmailData(
    val emailAddress : String,
    val title: String,
    val content: String
): Parcelable
