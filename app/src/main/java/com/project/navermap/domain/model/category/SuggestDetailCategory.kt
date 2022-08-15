package com.example.YUmarket.model.homelist.category

import androidx.annotation.StringRes
import com.project.navermap.R
import com.project.navermap.domain.model.category.SuggestCategory


enum class SuggestDetailCategory(
    @StringRes detailCategory: Int,
    @StringRes detailCategoryTypeId: Int,
    private val suggestCategory: SuggestCategory
) {

    SUGGEST_HOBBY(R.string.hobby, R.string.hobby_type, SuggestCategory.HOBBY),
    SUGGEST_FIX(R.string.fix, R.string.fix_type, SuggestCategory.FIX),
    SUGGEST_BIRTH(R.string.birth, R.string.birth_type, SuggestCategory.BIRTH),
    SUGGEST_SEASON(R.string.season_spring, R.string.season_spring_type, SuggestCategory.SEASON_SPRING),
    SUGGEST_ANNIVERSARY(R.string.anniversary,R.string.anniversary_type, SuggestCategory.ANNIVERSARY),

    // 계절
    SPRING(
        R.string.suggest_detail_season_spring_filter,
        R.string.suggest_detail_season_spring_filter_type,
        SuggestCategory.SEASON_SPRING
    ),

    SUMMER(
        R.string.suggest_detail_season_spring_airconditioner,
        R.string.suggest_detail_season_spring_airconditioner_type,
        SuggestCategory.SEASON_SPRING
    ),

    FALL(
        R.string.suggest_detail_season_spring_carwash,
        R.string.suggest_detail_season_spring_carwash_type,
        SuggestCategory.SEASON_SPRING
    ),

    WINTER(
        R.string.suggest_detail_season_spring_cloth,
        R.string.suggest_detail_season_spring_cloth_type,
        SuggestCategory.SEASON_SPRING
    ),

    // 수리
    DOOR(
        R.string.suggest_detail_fix_door,
        R.string.suggest_detail_fix_door_type,
        SuggestCategory.FIX
    ),

    ELECTROWIRE(
        R.string.suggest_detail_fix_electrowire,
        R.string.suggest_detail_fix_electrowire_type,
        SuggestCategory.FIX
    ),

    HOMEAPPLIANCES(
        R.string.suggest_detail_fix_homeappliances,
        R.string.suggest_detail_fix_homeappliances_type,
        SuggestCategory.FIX
    ),
    FURNITURE(
        R.string.suggest_detail_fix_furniture,
        R.string.suggest_detail_fix_furniture_type,
        SuggestCategory.FIX
    ),
    TOILET(
        R.string.suggest_detail_fix_toilet,
        R.string.suggest_detail_fix_toilet_type,
        SuggestCategory.FIX
    ),
    HOUSE(
        R.string.suggest_detail_fix_house,
        R.string.suggest_detail_fix_house_type,
        SuggestCategory.FIX
    ),


    //기념일
    FLOWER(
        R.string.suggest_detail_anniversary_flower,
        R.string.suggest_detail_anniversary_flower_type,
        SuggestCategory.ANNIVERSARY
    ),
    CAKE(
        R.string.suggest_detail_anniversary_cake,
        R.string.suggest_detail_anniversary_cake_type,
        SuggestCategory.ANNIVERSARY
    ),
    ROOM(
        R.string.suggest_detail_anniversary_room,
        R.string.suggest_detail_anniversary_room_type,
        SuggestCategory.ANNIVERSARY
    ),
    TOY(
        R.string.suggest_detail_anniversary_toy,
        R.string.suggest_detail_anniversary_toy_type,
        SuggestCategory.ANNIVERSARY
    ),
    FOOD(
        R.string.suggest_detail_anniversary_food,
        R.string.suggest_detail_anniversary_food_type,
        SuggestCategory.ANNIVERSARY,
    ),
    JEWELY(
        R.string.suggest_detail_anniversary_jewely,
        R.string.suggest_detail_anniversary_jewely_type,
        SuggestCategory.ANNIVERSARY
    ),


    // 생일

    PERFUME(
        R.string.suggest_detail_birth_perfume,
        R.string.suggest_detail_birth_perfume_type,
        SuggestCategory.BIRTH
    ),

    RECSELL(
        R.string.suggest_detail_birth_resell,
        R.string.suggest_detail_birth_resell_type,
        SuggestCategory.BIRTH
    ),
    IT(
        R.string.suggest_detail_birth_it,
        R.string.suggest_detail_birth_it_type,
        SuggestCategory.BIRTH
    ),

    HEALTH(
        R.string.suggest_detail_birth_health,
        R.string.suggest_detail_birth_health_type,
        SuggestCategory.BIRTH
    )
}