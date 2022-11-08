package com.project.navermap.data.repository.myinfo

import com.project.navermap.R
import com.project.navermap.domain.model.ProfileData
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(

) : ProfileRepository {
//
//    fun getProfile() = flow {
//        val post = profileRepository.getProfile()
//        emit(Result.success(post))
//    }.catch { e -> emit(Result.failure(e)) }

    //    suspend fun insertProfile(profileData: ProfileData) = imageDao.insertData(profileData)
//
//    suspend fun getAllProfile() = imageDao.getAllAddresses()
//
    override suspend fun getProfile(): ProfileData {
        return ProfileData(
            0,
            R.drawable.image
        )
    }

}