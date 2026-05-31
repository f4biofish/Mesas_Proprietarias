package com.librarys.ferreira.core

import com.librarys.ferreira.core.domain.model.config.PropFirmConfig
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.rules.AccountRules
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountTemplatesTest {


    @Test
    fun `validate account template ylos`() {

        val accountsYlos = PropFirmConfig.getAccountsFor(PropFirm.YLOS_TRADING)
        assertEquals(4, accountsYlos.size)

        val accountInstant25 = accountsYlos.find { it.name.contains("Instant") && it.initialBalance == 25000.0 }
        assertEquals(25000.0, accountInstant25?.initialBalance)

        val rulesInstant = accountInstant25?.rulesFunded ?: emptyList()

        val consistencia = rulesInstant
            .filterIsInstance<AccountRules.ConsistencyRule>()
            .firstOrNull()
            ?.maxPercentage

        assertEquals(30.0, consistencia)
    }

    @Test
    fun `validate account template tradeify`() {

        val accountsTradeify = PropFirmConfig.getAccountsFor(PropFirm.TRADEIFY)
        assertEquals(5, accountsTradeify.size)

        val select50K = accountsTradeify.find { it.name.contains("Select") && it.initialBalance == 50000.0 }
        val growth25K = accountsTradeify.find { it.name.contains("Growth") && it.initialBalance == 25000.0 }

        assertEquals(2000.0, select50K?.maxDrawdown)
        assertEquals(1000.0, growth25K?.maxDrawdown)

        val rulesGrowth = growth25K?.rulesFunded ?: emptyList()
        val consistencia = rulesGrowth
            .filterIsInstance<AccountRules.ConsistencyRule>()
            .firstOrNull()
            ?.maxPercentage

        assertEquals(35.0, consistencia)

    }

    @Test
    fun `validate account template lucid trading`() {
        val accountsLucid = PropFirmConfig.getAccountsFor(PropFirm.LUCID_TRADING)
        assertEquals(3, accountsLucid.size)

        val account25K = accountsLucid.find { it.initialBalance == 25000.0 }
        assertEquals(1000.0, account25K?.maxDrawdown)
    }


}