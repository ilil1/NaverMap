package com.project.navermap.domain.model.category

import androidx.annotation.StringRes
import com.project.navermap.R

enum class CSCategory(
    @StringRes val categoryNameId: Int,
    @StringRes val categoryTypeId: Int
) {
    LOGIN(R.string.cs_login, R.string.cs_login_type),
    USE(R.string.cs_use, R.string.cs_use_type),
    ORDER(R.string.cs_order, R.string.cs_order_type),
    REVIEW(R.string.cs_review, R.string.cs_review_type),
    ETC(R.string.cs_etc, R.string.cs_etc_type)


}