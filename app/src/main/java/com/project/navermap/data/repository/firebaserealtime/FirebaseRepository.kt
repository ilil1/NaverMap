package com.project.navermap.data.repository.firebaserealtime

import com.project.navermap.data.entity.firebase.FirebaseEntity
import com.project.navermap.domain.model.FirebaseModel
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    fun getMarkets() :  Flow<List<FirebaseEntity>>

}