package com.project.navermap

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.LocalTime

@Parcelize
data class ShopInfoEntity(
    val shop_id: String,
    val shop_name: String,
    val is_open: Boolean,
    val open_time : LocalTime,
    val close_time : LocalTime,
    val lot_number_address: String,
    val road_name_address: String,
    val latitude: Double,
    val longitude: Double,
    val created_at : LocalDateTime,
    val updated_at : LocalDateTime?,
    val average_score: Int,
    val review_number: Int,
    val main_image : String,
    val representative_image_list : String,
    val shop_description: String,
    val shop_category: String,
    val shop_detail_category : String,
    val detail_category: String,
    val is_branch : Boolean,
    val branch_name : String?
) : Parcelable