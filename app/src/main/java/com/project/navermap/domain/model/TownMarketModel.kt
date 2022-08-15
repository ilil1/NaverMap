package com.project.navermap.domain.model

import com.project.navermap.data.entity.LocationEntity
import com.project.navermap.data.entity.TownMarketEntity


data class TownMarketModel(
    override val id: Long,
    val marketName: String,
    val isMarketOpen: Boolean,
    val locationLatLngEntity: LocationEntity,
    val marketImageUrl: String,
    val distance: Float,
    override val type: CellType = CellType.HOME_TOWN_MARKET_CELL
) : Model(id, type) {

    fun toEntity() = TownMarketEntity(
        id,
        marketName,
        isMarketOpen,
        locationLatLngEntity,
        marketImageUrl,
        distance,
        type
    )
}