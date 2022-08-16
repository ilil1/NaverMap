package com.project.navermap.data.repository.chat

import com.project.navermap.domain.model.ChatModel

interface ChatRepository {
    fun findChatList() : List<ChatModel>
}