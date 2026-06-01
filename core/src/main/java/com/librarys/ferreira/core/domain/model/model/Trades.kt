package com.librarys.ferreira.core.domain.model.model

import java.util.Date
import java.util.UUID
import kotlin.require

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
) {
    init {
        //Validação para garantir que o campo date não seja maior que a data atual
        require(date <= Date()) {
            "A data do trade não pode ser maior que a data atual."
        }

        //Validação para garantir que o campo contratos não seja negativo ou 0
        require(contratos > 0){
            "O número de contratos deve ser maior que zero."
        }
    }
}
