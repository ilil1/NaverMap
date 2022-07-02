package com.project.navermap

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopInfo(
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    val shopList: List<ShopData>
)

