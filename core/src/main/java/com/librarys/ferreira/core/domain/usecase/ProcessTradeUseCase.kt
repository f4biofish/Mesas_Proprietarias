package com.librarys.ferreira.core.domain.usecase

import com.librarys.ferreira.core.domain.model.config.AtivosConfig
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.model.Trades

class ProcessTradeUseCase {

    /**
     * Realiza a operação de lançamento do trade na conta do plano do usuário
     * @param account dados da conta do plano
     * @param trade trade realizado
     * @return os dados de conta atualizados
     */
    operator fun invoke(account: AccountInfo, trade: Trades) : AccountInfo {
        val custos = (AtivosConfig.costsAtivosMap[account.propFirm to trade.symbolAtivo] ?: 0.0) * trade.contratos
        val netProfit = trade.profit - custos
        val newBalance = account.currentBalance + netProfit
        return account.copy(currentBalance = newBalance)
    }

}