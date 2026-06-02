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

    /**
     * Retorna um trade específico através do seu ID
     * @param tradeId ID do trade
     * @return O trade ou null se não encontrado
     */
    suspend fun getTradeById(tradeId: String): Trades?

    /**
     * Exclui um trade do banco de dados
     * @param trade Dados do trade a ser excluído
     * @return true se o trade foi excluído com sucesso, false caso contrário
     */
    suspend fun deleteTrade(trade: Trades): Boolean
}