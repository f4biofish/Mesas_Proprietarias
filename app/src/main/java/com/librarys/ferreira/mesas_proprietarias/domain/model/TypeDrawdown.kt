package com.librarys.ferreira.mesas_proprietarias.domain.model

/**
 * Tipos de Drawdown
 */
enum class TypeDrawdown {
    /**
     * Drawdown ao final do dia de operações
     */
    EOD,

    /**
     * Drawdown em tempo real
     */
    TRAILING
}