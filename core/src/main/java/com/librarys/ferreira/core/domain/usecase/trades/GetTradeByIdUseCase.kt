package com.librarys.ferreira.core.domain.usecase.trades

import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.domain.repository.TradesRepository
import javax.inject.Inject

class GetTradeByIdUseCase @Inject constructor(
    private val repository: TradesRepository
) {
    suspend operator fun invoke(tradeId: String): Trades? {
        return repository.getTradeById(tradeId)
    }
}
