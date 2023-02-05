package com.project.navermap.data.repository.myinfo

import com.project.navermap.domain.model.ProfileData

interface ProfileRepository {

    suspend fun getProfile() : ProfileData

}