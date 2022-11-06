package com.project.navermap.domain.model

import com.project.navermap.domain.model.category.CSCategory

data class CSModel(
    override val id:Long,
    override val type: CellType = CellType.CUSTOMER_SERVICE_CELL,
    val csInfoId: Long,
    val csCategory: CSCategory,
    val csTitle: String,
    val csAuthor:String,
    val csContentTitle:String,
    val csContentBody:String
): Model(id,type)
