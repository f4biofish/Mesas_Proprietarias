package com.librarys.ferreira.core.domain.repository

import com.librarys.ferreira.core.domain.model.model.Trades

interface TradesRepository {
    /**
     * Salva um registro de trade no banco de dados
     * @param trade Dados do trade
     * @return true se o trade foi salvo com sucesso, false caso contrário
     */
    suspend fun saveTradeInDb(trade: Trades): Boolean
}