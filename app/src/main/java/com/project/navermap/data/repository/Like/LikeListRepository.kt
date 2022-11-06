package com.project.navermap.data.repository.Like

import com.project.navermap.domain.model.Model

interface LikeListRepository<T : Model> {
    suspend fun getAll(): List<T>
    suspend fun insert(model: T)
    suspend fun delete(model: T)
    suspend fun deleteAll()
}