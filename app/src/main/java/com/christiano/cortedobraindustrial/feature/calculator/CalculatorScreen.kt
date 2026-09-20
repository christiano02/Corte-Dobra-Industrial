package com.christiano.cortedobraindustrial.feature.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christiano.cortedobraindustrial.core.presentation.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message)
            viewModel.errorConsumed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = AppStrings.TOP_BAR_TITLE) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Texto de instrução agora deixa claro o uso do Milímetro
            Text(
                text = AppStrings.INSTRUCTION_TEXT,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error, // Destaque em cor para chamar atenção
                modifier = Modifier.align(Alignment.Start)
            )

            OutlinedTextField(
                value = uiState.cutFaceSize,
                onValueChange = { viewModel.onFaceSizeChange(it) },
                label = { Text(text = AppStrings.LABEL_FACE_SIZE) },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.hasError && uiState.cutFaceSize.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.angle,
                onValueChange = { viewModel.onAngleChange(it) },
                label = { Text(text = AppStrings.LABEL_ANGLE) },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.hasError && uiState.angle.isEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.offsetHeight,
                onValueChange = { viewModel.onOffsetChange(it) },
                label = { Text(text = AppStrings.LABEL_OFFSET) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.wallDistance,
                onValueChange = { viewModel.onWallDistanceChange(it) },
                label = { Text(text = AppStrings.LABEL_WALL_DIST) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.calculate()
                    }
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.calculate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = AppStrings.BTN_CALCULATE, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onClearFields()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = AppStrings.BTN_CLEAR, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            val hasValidResults = uiState.traceDistance != null

            AnimatedVisibility(
                visible = hasValidResults,
                enter = fadeIn() + expandVertically()
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = AppStrings.RESULT_TITLE,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        HorizontalDivider()

                        // Envia a medida já formatada para o componente visual
                        val traceStr = String.format("%.1f", uiState.traceDistance ?: 0.0)
                        TraceVisualizer(traceMeasure = traceStr)

                        HorizontalDivider()

                        // Passagem explícita do sufixo "mm" em todas as linhas de resultado
                        ResultRow(
                            label = AppStrings.RESULT_TRACE_DISTANCE,
                            value = uiState.traceDistance,
                            suffix = " mm"
                        )
                        ResultRow(
                            label = AppStrings.RESULT_TOTAL_OPENING,
                            value = uiState.totalNotch,
                            suffix = " mm"
                        )

                        if (uiState.obstacleDistance != null) {
                            ResultRow(
                                label = AppStrings.RESULT_OBSTACLE_DISTANCE,
                                value = uiState.obstacleDistance,
                                suffix = " mm"
                            )
                        }

                        if (uiState.firstCutMark != null) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            ResultRow(
                                label = AppStrings.RESULT_FIRST_CUT_MARK,
                                value = uiState.firstCutMark,
                                suffix = " mm"
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Gabarito visual com injeção estática da unidade "mm".
 * Complexidade Temporal: O(1)
 */
@Composable
fun TraceVisualizer(traceMeasure: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.height(40.dp).width(2.dp).background(MaterialTheme.colorScheme.onPrimaryContainer))

            Text(
                // Inserimos "mm" diretamente no desenho do traço
                text = " $traceMeasure mm ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Box(modifier = Modifier.height(56.dp).width(4.dp).background(MaterialTheme.colorScheme.error))

            Text(
                text = " $traceMeasure mm ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Box(modifier = Modifier.height(40.dp).width(2.dp).background(MaterialTheme.colorScheme.onPrimaryContainer))
        }
    }
}

/**
 * Componente modular para exibição segura de chave-valor.
 */
@Composable
fun ResultRow(label: String, value: Double?, suffix: String = "") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Reduzimos o peso do rótulo para dar foco absoluto no número (Valor)
        Text(text = label, style = MaterialTheme.typography.bodyMedium)

        val formattedValue = value?.let { String.format("%.1f", it) } ?: "---"
        Text(
            text = "$formattedValue$suffix",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.ExtraBold, // Peso máximo para destaque visual
            color = MaterialTheme.colorScheme.primary // Cor primária para os resultados numéricos
        )
    }
}