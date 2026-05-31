package com.librarys.ferreira.mesas_proprietarias.domain.model

/**
 * Classe com as regras que podem ser utilizadas pelas mesas proprietárias
 */
sealed  class AccountRule {

    /**
     * Regra de consistência de saldo
     * @property description Descrição
     * @property maxPercentage Percentual de consistência de saldo
     */
    data class ConsistencyRule(val description: String, val maxPercentage: Double) : AccountRule()

    /**
     * Regra de restrição de notícias
     * @property minutesBefore Quantos minutos antes deve sair da operação
     * @property minutesAfter Quantos minutos após a notícia, pode entrar em operação
     */
    data class NewsRestrictionRule(val minutesBefore: Int, val minutesAfter: Int) : AccountRule()

    /**
     * Regra de mínimo de dias operados
     * @property daysTrading Dias mínimos de operação para contar como dia operado
     * @property daysWin Dias mínimos de vitória para solicitar saque
     * @property minProfit Valor mínimo conquistado no dia para contar como dia vencedor
     */
    data class MinimumTradingDaysRule(val daysTrading: Int, val daysWin: Int, val minProfit: Double) : AccountRule()

}
