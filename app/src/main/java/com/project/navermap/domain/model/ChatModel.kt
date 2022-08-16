package com.project.navermap.domain.model

data class ChatModel(
    override val id:Long,
    override val type: CellType = CellType.CHATTING_CELL,
    val InfoId: Long,
    val ImageUrl: String,
    val StoreName: String,
    val RecentlyText:String,
    val Data:String
): Model(id,type)
