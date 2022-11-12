package com.project.navermap.data.repository.myinfo

import com.project.navermap.domain.model.CSModel
import com.project.navermap.domain.model.category.CSCategory


interface CSRepository {
    suspend fun findCsByCategory(csCategory: CSCategory) : List<CSModel>
}