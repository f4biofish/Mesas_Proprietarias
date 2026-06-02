package com.librarys.ferreira.core.domain.model.rules

import kotlinx.serialization.Serializable

/**
 * Classe de armazenamento de todas as regras de consistências de todas as mesas suportadas
 */
@Serializable
sealed class AccountRules {

    /**
     * Regra de consistência
     * @property description descrição da regra
     * @property maxPercentage Percentual máximo que um único dia de operação pode ter no lucro total da conta
     */
    @Serializable
    data class ConsistencyRule(val description: String, val maxPercentage: Double) : AccountRules()

    /**
     * Regra de notícias
     * @property minutesBefore A quantidade de mínutos que você deve estar fora de operação antes do anúncio de uma notícia
     * @property minutesAfter A quantidade de minutos que você deve aguardar após a divulgação de uma notícia
     */
    @Serializable
    data class NewsRestrictionRule(val minutesBefore: Int, val minutesAfter: Int) : AccountRules()

    /**
     * Regra da quantidade mínima de dias de operação para solicitação de um saque
     * @property daysTrading Dias mínimos de operação
     * @property daysWin Dias mínimos de vitória
     * @property profitWin Lucro mínimo para considerar o dia como vitória
     */
    @Serializable
    data class MinimumTradingDaysRule(val daysTrading: Int, val daysWin: Int, val profitWin: Double) : AccountRules()


}