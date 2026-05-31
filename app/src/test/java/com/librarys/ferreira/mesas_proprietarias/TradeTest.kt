package com.librarys.ferreira.mesas_proprietarias

import com.librarys.ferreira.mesas_proprietarias.domain.model.Ativos
import com.librarys.ferreira.mesas_proprietarias.domain.model.PropFirms
import com.librarys.ferreira.mesas_proprietarias.domain.model.Trades
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals

class TradeTest {

    @Test
    fun `create trade para teste`() {

        val trade1 = Trades(
            propFirms = PropFirms.YLOS_TRADING,
            accountNumber = "001",
            date = Date(),
            ativo = Ativos.NQ,
            contratos = 2,
            profit = 350.0,
            isNewTrade = false
        )
        val lucroLiquido = trade1.getLucroLiquidoOperacao()

        assertEquals(350.0, trade1.profit)
        assertEquals(341.28, lucroLiquido)

    }

}