package com.librarys.ferreira.core.domain.model.model

import java.util.Date
import java.util.UUID

/**
 * Classe para registros de trades efetuados
 * @property id identificador único do trade, gerado automaticamente
 * @property date Data de realização do trade
 * @property accountInfoId Id referente a conta do plano operado
 * @property accountNumber Número da conta do plano operado
 * @property symbolAtivo simbolo do ativo operado
 * @property contratos número de contratos realizados
 * @property profit Resultado da operação
 */
data class Trades(
    val id: String = UUID.randomUUID().toString(),
    val date: Date = Date(),
    val accountInfoId: String,
    val accountNumber: String,
    val symbolAtivo: SymbolAtivo,
    val contratos: Int,
    val profit: Double,
)
