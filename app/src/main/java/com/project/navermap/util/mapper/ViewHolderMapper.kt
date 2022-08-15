package com.project.navermap.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.project.navermap.databinding.*
import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.CellType.*
import com.project.navermap.domain.model.Model
import com.project.navermap.presentation.base.BaseViewModel
import com.project.navermap.util.provider.ResourcesProvider
import com.project.navermap.widget.adapter.viewholder.*


object ViewHolderMapper {
    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: ViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {

        val inflater = LayoutInflater.from(parent.context)

        val viewHolder = when (type) {
            RESTAURANT_CELL -> {
                RestaurantViewHolder(
                    ViewholderRestaurantBinding.inflate(inflater, parent, false),
                    viewModel,
                    resourcesProvider
                )
            }

            SUGGEST_CELL -> {
                SuggestViewHolder(
                    ViewholderSuggestSeasonBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }

            HOME_TOWN_MARKET_CELL -> {
                TownMarketViewHolder(
                    ViewholderTownMarketBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }

            HOME_ITEM_CELL -> {
                HomeItemModelViewHolder(
                    ViewholderHomeItemBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }

            HOME_MAIN_MARKET_CELL -> {
                NearbyMarketViewHolder(
                    ViewholderNearbyMarketBinding.inflate(inflater),
                    viewModel,
                    resourcesProvider
                )
            }

            HOME_DETAIL_ITEM_CELL -> TODO()
            HOME_MAIN_ITEM_CELL -> TODO()
            CUSTOMER_SERVICE_CELL -> TODO()
            LIKE_MARKET_CELL -> TODO()
            LIKE_ITEM_CELL -> TODO()
            MAP_ITEM_CELL -> TODO()
            CHATTING_CELL -> TODO()
            STORE_CELL -> TODO()
            HOME_REVIEW_ITEM_CELL -> TODO()
        }
        return viewHolder as ModelViewHolder<M>
    }
}


