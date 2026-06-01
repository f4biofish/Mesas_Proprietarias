package com.librarys.ferreira.core.data.repository

import com.librarys.ferreira.core.data.local.dao.TradesDao
import com.librarys.ferreira.core.data.mapper.toEntity
import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.domain.repository.TradesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradesRepositoryImpl @Inject constructor(
    private val tradesDao: TradesDao
) : TradesRepository {

    override suspend fun saveTradeInDb(trade: Trades): Boolean {
        return try {
            tradesDao.insertTrades(trade.toEntity()) > 0
        } catch (e: Exception) {
            false
        }
    }

}