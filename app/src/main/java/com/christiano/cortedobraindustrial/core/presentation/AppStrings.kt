package com.christiano.cortedobraindustrial.core.presentation

/**
 * Repositório central de Strings.
 * Domínio rigorosamente travado na unidade de Milímetros (mm) para garantir
 * precisão industrial e mitigar erros de conversão em campo.
 * Complexidade Espacial: O(1)
 */
object AppStrings {
    const val APP_NAME = "Corte & Dobra Industrial"
    const val TOP_BAR_TITLE = "Traçado de Eletrocalhas"

    // Instrução focada na precisão
    const val INSTRUCTION_TEXT = "Todas as medidas devem ser em milímetros (mm)"

    // Rótulos reforçando a unidade esperada
    const val LABEL_FACE_SIZE = "Tamanho da Face Cortada (mm)"
    const val LABEL_ANGLE = "Ângulo da Curva (Graus, ex: 45, 90)"
    const val LABEL_OFFSET = "Desvio / Obstáculo (mm)"
    const val LABEL_WALL_DIST = "Distância até a Parede (mm)"

    const val BTN_CALCULATE = "Calcular Traçado"

    const val RESULT_TITLE = "Gabarito de Marcação:"
    const val RESULT_TRACE_DISTANCE = "Medida do Traçado (Centro à Lateral):"
    const val RESULT_TOTAL_OPENING = "Abertura Total do Triângulo:"
    const val RESULT_OBSTACLE_DISTANCE = "Desvio (Centro 1 à 1ª Linha 2):"
    const val RESULT_FIRST_CUT_MARK = "Centro do Grau (Próximo à Parede):"

    const val ERROR_INVALID_INPUT = "Insira valores numéricos válidos (> 0)."
}