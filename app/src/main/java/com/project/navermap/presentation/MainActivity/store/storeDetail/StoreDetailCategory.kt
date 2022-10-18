package com.project.navermap.presentation.mainActivity.store.storeDetail

import androidx.annotation.StringRes
import com.project.navermap.R

enum class StoreDetailCategory(
    @StringRes
    val categoryNameId: Int,
) {
    Items(R.string.itemList),
    INFO(R.string.storeInfo),
    REVIEW(R.string.review)

}



