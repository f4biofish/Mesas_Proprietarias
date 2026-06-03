package com.librarys.ferreira.core.ui.account_plan.rule_consistency_info

/**
 * Rule consistency info ui state
 *
 * @property consistencyRule Regra de consistência atual da mesa
 * @property maxDailyProfit Lucro máximo efetuado em um único dia
 * @property actualConsistency Consistência atual da conta
 * @property profitNeed Lucro necessário para atingir a consistência necessária
 * @property isLoading true sinaliza que o carregamento esta sendo efetuado
 * @property errorMessage mensagem de erro ocorrido
 */
data class RuleConsistencyInfoUiState(
    val consistencyRule: Double = 0.0,
    val maxDailyProfit: Double = 0.0,
    val actualConsistency: Double = 0.0,
    val profitNeed: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
