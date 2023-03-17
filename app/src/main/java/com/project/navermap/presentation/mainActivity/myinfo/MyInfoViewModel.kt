package com.project.navermap.presentation.mainActivity.myinfo

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.project.navermap.MapApplication.Companion.appConfig
import com.project.navermap.app.config.AppConfig
import com.project.navermap.data.repository.myinfo.ProfileRepositoryImpl
import com.project.navermap.domain.model.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(

) : ViewModel() {

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    private val _showSaveBtn = MutableStateFlow(false)
    val showSaveBtn = _showSaveBtn.asStateFlow()

    fun onNicknameChanged(nickname: String) {
        _nickname.value = nickname
        _showSaveBtn.value = true
    }

    fun saveNickname(context: Context, appConfig: AppConfig) {
        if (_nickname.value.isBlank()) {
            Toast.makeText(context, "닉네임을 작성해주세요", Toast.LENGTH_SHORT).show()
        } else {
            appConfig.setNickName(_nickname.value)
            _showSaveBtn.value = false
            Toast.makeText(context, "닉네임을 저장하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }


}