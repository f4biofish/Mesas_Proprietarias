package com.librarys.ferreira.core.domain.usecase.trades

import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.domain.repository.TradesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTradesByAccountIdUseCase @Inject constructor(
    private val repository: TradesRepository
) {
    operator fun invoke(accountId: String): Flow<List<Trades>> {
        return repository.getTradesByAccountId(accountId)
    }
}
