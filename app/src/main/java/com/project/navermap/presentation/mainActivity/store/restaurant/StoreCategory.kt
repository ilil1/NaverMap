package com.project.navermap.presentation.mainActivity.store.restaurant

import androidx.annotation.StringRes
import com.project.navermap.R

enum class StoreCategory(
    @StringRes val storeCategoryNameId: Int,
    @StringRes val storeCategoryTypeId : Int
) {

    MARKET(R.string.market, R.string.market_type),
    FOODANDDRINKE(R.string.foodAndDrink, R.string.foodAndDrink_type),
    STORE(R.string.convenienceStore, R.string.convenienceStore_type),
    CLOTHINGSTORE(R.string.clothingStore, R.string.clothingStore_type),
    FLOWERSTORE(R.string.flower, R.string.flower_type),
    CLEANINGSTORE(R.string.cleaningStore, R.string.cleaningStore_type),
    EDUCATION(R.string.educationalInstitute, R.string.educationalInstitute_type),
    EXERCISE(R.string.exercise, R.string.exercise_type),
    MOVING(R.string.moving,R.string.moving_type),
    TOYSTORE(R.string.toyStore,R.string.toyStore_type)


}