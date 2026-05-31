package com.librarys.ferreira.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val numberAccount: String,
    val propFirm: String,
    val accountName: String,
    val initialBalance: Double,
    val currentBalance: Double,
    val maxDrawdownAmmount: Double,
    val dailyLossLimit: Double? = null,
    val dayStarting: Long = System.currentTimeMillis(),
    val dayBroken: Long? = null
)
