package com.project.navermap.domain.model

data class ReviewModel(
    override val id: Long = 0L,
    override val type: CellType = CellType.REVIEW_CELL,
    val reviewid: Long,
    val writerId: Long,
    val writtenAt: Long,
    val marketId: Long,
    val orderId: Long,
    val title: String?,
    val content: String,
    val rating: Int,
    val reviewImages: List<String>
) : Model(id, type){

//    override fun isTheSame(item: Model): Boolean = if (item is ReviewModel) {
//        super.isTheSame(item) && item.reviewid == this.reviewid
//    } else {
//        false
//    }
}