package com.librarys.ferreira.core.domain.repository

import com.librarys.ferreira.core.domain.model.model.Trades

interface TradesRepository {
    /**
     * Salva um registro de trade no banco de dados
     * @param trade Dados do trade
     * @return true se o trade foi salvo com sucesso, false caso contrário
     */
    suspend fun saveTradeInDb(trade: Trades): Boolean

    /**
     * Retorna o fluxo de todos os trades vinculados a uma conta específica
     * @param accountId ID da conta vinculada aos trades
     * @return Flow contendo a lista de trades
     */
    fun getTradesByAccountId(accountId: String): kotlinx.coroutines.flow.Flow<List<Trades>>
}