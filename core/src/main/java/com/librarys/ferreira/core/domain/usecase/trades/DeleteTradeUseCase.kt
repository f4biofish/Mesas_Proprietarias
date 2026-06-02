package com.librarys.ferreira.core.domain.usecase.trades

import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.domain.repository.TradesRepository
import javax.inject.Inject

class DeleteTradeUseCase @Inject constructor(
    private val repository: TradesRepository
) {
    suspend operator fun invoke(trade: Trades): Boolean {
        return repository.deleteTrade(trade)
    }
}
