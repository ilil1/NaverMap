package com.project.navermap.domain.model.category

import androidx.annotation.StringRes
import com.project.navermap.R

enum class LikeCategory(
    @StringRes val likeCategoryId: Int
) {
    MARKET(R.string.like_market),
    ITEM(R.string.like_item)
}