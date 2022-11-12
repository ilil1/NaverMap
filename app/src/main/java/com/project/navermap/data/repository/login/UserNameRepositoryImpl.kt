package com.project.navermap.data.repository.login

import com.project.navermap.data.entity.UserNameEntity
import kotlinx.coroutines.flow.Flow


interface UserNameRepositoryImpl {

    suspend fun saveUserName(userNameEntity: UserNameEntity)

    suspend fun getUserName() : Flow<UserNameEntity>


}