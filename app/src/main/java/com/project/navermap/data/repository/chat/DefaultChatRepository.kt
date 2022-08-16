package com.project.navermap.data.repository.chat


import com.project.navermap.domain.model.CellType
import com.project.navermap.domain.model.ChatModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DefaultChatRepository @Inject constructor(
) : ChatRepository {

    override fun findChatList(): List<ChatModel> {
        val mockList = listOf(
            ChatModel(
                0,
                CellType.CHATTING_CELL,
                0,
                "https://velog.velcdn.com/images/heetaeheo/post/aa67a287-54e0-40a0-bfe4-82ebf9095871/image.png",
                "싱싱상회",
                "네 바로 배송 가능합니다",
                "6.19"
            ),

            ChatModel(
                0,
                CellType.CHATTING_CELL,
                0,
                "https://velog.velcdn.com/images/heetaeheo/post/33c8b2da-8701-4bc1-80f1-8a0223ccab26/image.png",
                "이보크 가구",
                "배송은 몇일로 해드릴까요?",
                "6.17"
            ),
            ChatModel(
                0,
                CellType.CHATTING_CELL,
                0,
                "https://velog.velcdn.com/images/heetaeheo/post/99fbe3d6-2a2f-41e6-87b5-d2084c5f7943/image.png",
                "일로 홈케어",
                "정확한 날짜를 알려주실 수 있나요?",
                "6.16"
            ),
            ChatModel(
                0,
                CellType.CHATTING_CELL,
                0,
                "https://velog.velcdn.com/images/heetaeheo/post/f586693e-6c35-4ed8-a940-4b6eb63b5b9d/image.png",
                "포옹꽃",
                "꽃다발 예약은 하루 전에 부탁드려요",
                "6.14"
            )
        )
        return mockList
    }


}

