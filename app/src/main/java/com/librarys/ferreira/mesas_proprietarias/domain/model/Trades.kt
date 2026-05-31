package com.librarys.ferreira.mesas_proprietarias.domain.model

import com.librarys.ferreira.mesas_proprietarias.domain.data.PropFirmConfig
import java.util.Date
import java.util.UUID

data class Trades(
    val id: String = UUID.randomUUID().toString(),
    val propFirms: PropFirms,
    val accountNumber: String,
    val date : Date = Date(),
    val ativo: Ativos,
    val contratos: Int,
    val profit: Double,
    val isNewTrade: Boolean = false
) {

    fun getLucroLiquidoOperacao() : Double {
        val feePerContratct = PropFirmConfig.getFeeFot(propFirms, ativo)
        val totalFees = feePerContratct * contratos
        return profit - totalFees
    }

}
