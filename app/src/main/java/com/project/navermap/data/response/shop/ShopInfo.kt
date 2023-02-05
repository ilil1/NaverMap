package com.project.navermap.data.response.shop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.project.navermap.domain.model.FoodModel
import com.project.navermap.domain.model.StoreModel

data class ShopInfo(
    @Expose
    @SerializedName("success")
    val success: Boolean,
    @Expose
    @SerializedName("data")
    val shopList: List<ShopData>
)
{
}

