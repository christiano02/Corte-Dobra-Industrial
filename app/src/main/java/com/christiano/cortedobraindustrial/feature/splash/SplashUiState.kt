package com.christiano.cortedobraindustrial.feature.splash

/**
 * Representa o estado imutável da tela de carregamento inicial.
 * Complexidade de Espaço: O(1)
 */
data class SplashUiState(
    val isLoading: Boolean = true
)