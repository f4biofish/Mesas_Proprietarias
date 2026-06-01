package com.librarys.ferreira.core

import com.librarys.ferreira.core.domain.model.enums.AccountStage
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.model.SymbolAtivo
import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.domain.usecase.ProcessTradeUseCase
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.Date

class TradeTest {


    @Test
    fun `launch trade in account`() {

        val account = AccountInfo(
            id = "1",
            numberAccount = "12",
            propFirm = PropFirm.YLOS_TRADING,
            accountStage = AccountStage.CHALLENGE,
            accountName = "Standard 25K",
            dayStarting = Date(),
            dayBroken = null,
            initialBalance = 25000.0,
            currentBalance = 25000.0,
            typeDrawdown = DrawnDownTypes.EOD,
            maxDrawdownAmmount = 2000.0,
            dailyLossLimit = null,
            rulesPropFirm = emptyList()
        )

        val trade = Trades(
            id = "1",
            date = Date(),
            accountInfoId = "1",
            symbolAtivo = SymbolAtivo.NQ,
            contratos = 2,
            profit = 360.0,
        )

        val accountUpdated = ProcessTradeUseCase().invoke(account, trade)

        assertEquals(25351.28, accountUpdated.currentBalance)

    }


}