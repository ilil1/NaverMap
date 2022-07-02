package com.project.navermap

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopInfoEntity(
    val shop_id: String,
    val shop_name: String,
    val is_open: Boolean,
    val lot_number_address: String,
    val road_name_address: String,
    val latitude: Double,
    val longitude: Double,
    val average_score: Int,
    val review_number: Int,
    val main_image: String,
    val description: String,
    val category: String,
    val detail_category: String,
    val is_branch : Boolean,
    val branch_name : String?
) : Parcelable