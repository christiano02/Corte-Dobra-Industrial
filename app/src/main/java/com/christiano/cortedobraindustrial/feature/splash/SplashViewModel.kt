package com.christiano.cortedobraindustrial.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        initializeApp()
    }

    /**
     * Gerencia o tempo de permanência da tela de carregamento.
     * Ajustado estritamente para 3000ms (3 segundos).
     * Complexidade de Tempo: O(1)
     */
    private fun initializeApp() {
        viewModelScope.launch {
            delay(3000L) // 3 segundos exatos de carregamento/apresentação da marca
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}