package com.librarys.ferreira.core.domain.model.model

import com.librarys.ferreira.core.domain.model.config.AtivosConfig
import com.librarys.ferreira.core.domain.model.enums.PropFirm

enum class SymbolAtivo{
    NQ,
    MNQ,
    ES
}

data class Ativos(
    val symbol: SymbolAtivo,
    val name: String
) {

    /**
     * Obtém o custo das taxas para operação por contrato (ida e volta)
     * @param propFirm mesa proprietária
     * @param symbolAtivo ativo
     * @return o custo de contrato (ida e volta)
     */
    fun getCostsBy(propFirm: PropFirm): Double {
        return AtivosConfig.costsAtivosMap[propFirm to symbol] ?: 0.0
    }

}
