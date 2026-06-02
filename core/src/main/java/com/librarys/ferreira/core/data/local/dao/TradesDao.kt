package com.librarys.ferreira.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.librarys.ferreira.core.data.local.entities.TradesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradesDao {

    @Query("SELECT * FROM trades")
    fun getAllTrades() : Flow<List<TradesEntity>>

    @Query("SELECT * FROM trades WHERE accountInfoId = :accountId")
    fun getAllTradesByAccountId(accountId: String) : Flow<List<TradesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrades(trades: TradesEntity) : Long

    @Delete
    suspend fun deleteTrade(trade: TradesEntity) : Int

    @Query("DELETE FROM trades WHERE accountInfoId = :accountId")
    suspend fun deleteAllTradesByAccount(accountId: String) : Int

}