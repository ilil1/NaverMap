package com.project.navermap.domain.usecase.mapViewmodel

sealed class MarkerResult {
    object Success : MarkerResult()
    object Failure : MarkerResult()
}
