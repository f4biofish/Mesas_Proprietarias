package com.librarys.ferreira.core

import com.librarys.ferreira.core.domain.model.config.AtivosConfig
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.model.Ativos
import com.librarys.ferreira.core.domain.model.model.SymbolAtivo
import org.junit.Assert.assertEquals
import org.junit.Test

class CostsAtivosTest {

    @Test
    fun `recupera o custo de taxas por mesa proprietaria e ativo`() {
        val listaAtivos = AtivosConfig.listAtivos
        val ativoNq = listaAtivos.find { it.symbol == SymbolAtivo.NQ }
        val ativoMnq = listaAtivos.find { it.symbol == SymbolAtivo.MNQ }
        val ativoEs = listaAtivos.find { it.symbol == SymbolAtivo.ES }

        assertEquals(4.36, ativoNq?.getCostsBy(PropFirm.YLOS_TRADING,))
        assertEquals(4.36, ativoEs?.getCostsBy(PropFirm.YLOS_TRADING))
        assertEquals(1.3, ativoMnq?.getCostsBy(PropFirm.YLOS_TRADING))


        assertEquals(5.76, ativoNq?.getCostsBy(PropFirm.TRADEIFY,))
        assertEquals(5.76, ativoEs?.getCostsBy(PropFirm.TRADEIFY))
        assertEquals(1.82, ativoMnq?.getCostsBy(PropFirm.TRADEIFY))

        assertEquals(3.50, ativoNq?.getCostsBy(PropFirm.LUCID_TRADING))
        assertEquals(3.50, ativoEs?.getCostsBy(PropFirm.LUCID_TRADING))
        assertEquals(1.0, ativoMnq?.getCostsBy(PropFirm.LUCID_TRADING))
    }

}