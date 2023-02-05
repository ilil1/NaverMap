package com.project.navermap.domain.model.category

import androidx.annotation.StringRes
import com.project.navermap.R

enum class HomeMarketDetailCategory(
    @StringRes val categoryNameId: Int
) {
    MENU(R.string.menu)
    ,INFORM(R.string.shopinform)
    ,REVIEW(R.string.review)
}