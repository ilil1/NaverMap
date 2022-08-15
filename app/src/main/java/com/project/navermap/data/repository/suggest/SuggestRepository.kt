package com.project.navermap.data.repository.suggest

import com.project.navermap.domain.model.SuggestItemModel

interface SuggestRepository {

    fun seasonMarket() : List<SuggestItemModel>

    fun suggestAnniversary() : List<SuggestItemModel>

    fun fixMarket() : List<SuggestItemModel>

    fun suggestHobby() : List<SuggestItemModel>
}