package com.project.navermap.data.mapper

import com.project.navermap.data.entity.MapSearchCacheEntity
import com.project.navermap.data.entity.restaurant.RestaurantEntity
import com.project.navermap.data.response.search.Poi
import com.project.navermap.domain.model.RestaurantModel
import com.project.navermap.presentation.mainActivity.store.restaurant.RestaurantCategory

/**
 * TMAP을 이용하여 가져온 결과를 다른 type으로 mapping하는 mapper
 */


fun Poi.toMapSearchCacheEntity() = MapSearchCacheEntity(
    id = id?.toLong(),
    poi = this
)

/*
    TODO: 2022-09-19 월 21:36
     - random 함수로 구현한 것 삭제
     - isMarketOpen 가게 상태에 따라 알맞은 값 할당
     - restaurantImageUrl를 가게 이미지 url로
*/
fun MapSearchCacheEntity.toRestaurantEntity(
    restaurantCategory: RestaurantCategory
) = RestaurantEntity(
    id = hashCode().toLong(),
    restaurantInfoId = (1..10).random().toLong(),
    restaurantCategory = restaurantCategory,
    restaurantTitle = poi.name ?: "제목 없음",
    restaurantImageUrl = "https://picsum.photos/200",
    grade = (1 until 5).random() + ((0..10).random() / 10f),
    reviewCount = (0 until 200).random(),
    deliveryTimeRange = Pair((0..20).random(), (40..60).random()),
    deliveryTipRange = Pair((0..1000).random(), (2000..4000).random()),
    restaurantTelNumber = poi.telNo,
    latitude = poi.frontLat,
    longitude = poi.frontLon,
    isMarketOpen = true,
    distance = (0 until 3).random() + ((0..10).random() / 10f)
)

fun RestaurantEntity.toRestaurantModel() = RestaurantModel(
    id = id,
    restaurantInfoId = restaurantInfoId,
    restaurantCategory = restaurantCategory,
    restaurantTitle = restaurantTitle,
    restaurantImageUrl = restaurantImageUrl,
    grade = grade,
    reviewCount = reviewCount,
    deliveryTimeRange = deliveryTimeRange,
    deliveryTipRange = deliveryTipRange,
    restaurantTelNumber = restaurantTelNumber,
    latitude = latitude,
    longitude = longitude,
    isMarketOpen = isMarketOpen,
    distance = distance
)