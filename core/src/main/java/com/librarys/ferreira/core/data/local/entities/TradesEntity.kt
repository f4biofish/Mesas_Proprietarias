package com.librarys.ferreira.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "trades")
data class TradesEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val date: Long = System.currentTimeMillis(),
    val accountInfoId: String,
    val accountNumber: String,
    val symbolAtivo: String,
    val contratos: Int,
    val profit: Double,
)
