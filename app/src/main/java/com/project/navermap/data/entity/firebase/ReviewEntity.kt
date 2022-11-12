package com.project.navermap.data.entity.firebase

import android.os.Parcelable
import com.project.navermap.domain.model.ReviewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewEntity(
    var id: Long = 0L,
    var writerId: Long =0L,
    var writtenAt: Long =0L,
    var marketId: Long =0L,
    var orderId: Long =0L,
    var title: String? = null,
    var content: String = "",
    var rating: Int = 0,
    var reviewImages: List<String> = emptyList()
) : Parcelable {
    fun toModel() = ReviewModel(
        reviewid = id,
        writerId = writerId,
        writtenAt = writtenAt,
        marketId = marketId,
        orderId = orderId,
        title = title,
        content = content,
        rating = rating,
        reviewImages = reviewImages
    )
}
