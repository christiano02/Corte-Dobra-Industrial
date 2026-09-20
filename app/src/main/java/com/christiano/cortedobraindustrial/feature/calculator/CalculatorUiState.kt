package com.christiano.cortedobraindustrial.feature.calculator

data class CalculatorUiState(
    val cutFaceSize: String = "",
    val angle: String = "",
    val offsetHeight: String = "",
    val wallDistance: String = "", // Nova entrada do usuário

    val traceDistance: Double? = null,
    val totalNotch: Double? = null,
    val obstacleDistance: Double? = null,
    val firstCutMark: Double? = null, // Novo resultado calculado

    val hasError: Boolean = false,
    val errorMessage: String? = null
)