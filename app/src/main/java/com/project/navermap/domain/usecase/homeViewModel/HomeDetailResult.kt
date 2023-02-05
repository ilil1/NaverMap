package com.project.navermap.domain.usecase.homeViewModel

sealed class HomeDetailResult {
    object Loading : HomeDetailResult()
}