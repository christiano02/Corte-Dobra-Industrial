package com.christiano.cortedobraindustrial.feature.calculator

import androidx.lifecycle.ViewModel
import com.christiano.cortedobraindustrial.core.presentation.AppStrings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.tan

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onFaceSizeChange(newValue: String) {
        _uiState.update { it.copy(cutFaceSize = newValue, hasError = false) }
    }
    fun onAngleChange(newValue: String) {
        _uiState.update { it.copy(angle = newValue, hasError = false) }
    }
    fun onOffsetChange(newValue: String) {
        _uiState.update { it.copy(offsetHeight = newValue, hasError = false) }
    }
    fun onWallDistanceChange(newValue: String) {
        _uiState.update { it.copy(wallDistance = newValue, hasError = false) }
    }
    fun errorConsumed() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun calculate() {
        val currentState = _uiState.value

        val faceVal = currentState.cutFaceSize.replace(",", ".").toDoubleOrNull()
        val anguloVal = currentState.angle.replace(",", ".").toDoubleOrNull()
        val desvioVal = currentState.offsetHeight.replace(",", ".").toDoubleOrNull()
        val paredeVal = currentState.wallDistance.replace(",", ".").toDoubleOrNull()

        if (faceVal == null || anguloVal == null || faceVal <= 0.0 || anguloVal <= 0.0) {
            _uiState.update {
                it.copy(
                    hasError = true, errorMessage = AppStrings.ERROR_INVALID_INPUT,
                    traceDistance = null, totalNotch = null,
                    obstacleDistance = null, firstCutMark = null
                )
            }
            return
        }

        // 1. Cálculo do Traçado
        val angleRadHalf = (anguloVal / 2.0) * (PI / 180.0)
        val trace = faceVal * tan(angleRadHalf)
        val totalOpening = trace * 2.0

        // 2. Cálculo do Desvio (Hipotenusa pura)
        val obstacleDist = desvioVal?.let { desvio ->
            if (desvio > 0) desvio / sin(anguloVal * (PI / 180.0)) else null
        }

        // 3. NOVO: Desconto da Parede (Onde marcar o primeiro centro)
        val firstMark = paredeVal?.let { parede ->
            if (parede > 0) parede - trace else null
        }

        _uiState.update {
            it.copy(
                hasError = false, errorMessage = null,
                traceDistance = trace, totalNotch = totalOpening,
                obstacleDistance = obstacleDist, firstCutMark = firstMark
            )
        }
    }
}