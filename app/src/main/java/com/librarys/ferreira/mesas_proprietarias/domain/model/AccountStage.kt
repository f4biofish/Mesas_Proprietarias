package com.librarys.ferreira.mesas_proprietarias.domain.model

/**
 * Possíveis estágios das contas de mesas proprietárias
 */
enum class AccountStage {
    /**
     * Conta de avaliação
     */
    CHALLENGE,
    /**
     * Conta financiada
     */
    FUNDED
}