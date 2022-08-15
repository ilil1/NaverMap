package com.project.navermap.data.entity

import android.os.Parcelable
import com.project.navermap.domain.model.CellType
import kotlinx.android.parcel.Parcelize


@Parcelize
class TownMarketEntity(
    val id: Long,
    val marketName: String,
    val isMarketOpen: Boolean,
    val locationLatLngEntity: LocationEntity,
    val marketImageUrl: String,
    val distance: Float,
    val type: CellType = CellType.HOME_TOWN_MARKET_CELL
) : Parcelable