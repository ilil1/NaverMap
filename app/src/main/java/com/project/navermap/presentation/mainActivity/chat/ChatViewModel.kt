package com.project.navermap.presentation.mainActivity.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.navermap.data.repository.chat.ChatRepository
import com.project.navermap.domain.model.ChatModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    //List<ChatModel>을 가져오면
    private val _chatListData = MutableLiveData<List<ChatModel>>()
    val chatListData : LiveData<List<ChatModel>> = _chatListData

    fun fetchData(): Job = viewModelScope.launch {
        _chatListData.value = chatRepository.findChatList()
    }
}