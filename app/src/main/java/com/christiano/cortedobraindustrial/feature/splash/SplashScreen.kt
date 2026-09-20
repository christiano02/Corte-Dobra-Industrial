package com.christiano.cortedobraindustrial.feature.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christiano.cortedobraindustrial.core.presentation.AppStrings
import com.christiano.cortedobraindustrial.feature.calculator.CalculatorScreen
import com.christiano.cortedobraindustrial.feature.calculator.CalculatorViewModel

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    calculatorViewModel: CalculatorViewModel
) {
    val splashState by splashViewModel.uiState.collectAsState()

    // Container principal que sobrepõe a tela de carregamento e a calculadora
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // 1. A Tela Principal (Calculadora) fica rodando por baixo, pronta
        CalculatorScreen(viewModel = calculatorViewModel)

        // 2. A Camada da Splash Screen que desaparece com animação assim que isLoading = false
        AnimatedVisibility(
            visible = splashState.isLoading,
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary), // Cor de destaque industrial
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Título do Aplicativo com alto contraste
                    Text(
                        text = AppStrings.APP_NAME,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    // Indicador de progresso circular otimizado em Material 3
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 4.dp
                    )
                }
            }
        }
    }
}