package com.project.navermap.data.entity

import android.os.Parcelable
import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.category.SuggestCategory
import kotlinx.parcelize.Parcelize

@Parcelize
class SuggestItemEntity(
    val id: Long,
    val type: CellType = CellType.SUGGEST_CELL,
    val marketImageUrl : String,
    val marketName: String,
    val distance:Float,
    val category: SuggestCategory
): Parcelable