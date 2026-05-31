package com.librarys.ferreira.core.domain.model.config

import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.rules.AccountRules
import com.librarys.ferreira.core.domain.model.template.AccountPlan

/**
 * Objeto para preenchimento dos dados das contas de mesas proprietárias que serão suportados pelo App
 */
object PropFirmConfig {


    /**
     * Busca a lista de tipos de contas disponíveis na mesa proprietária
     * @param propFirm mesa proprietária para a busca
     * @return lista de contas disponíveis
     */
    fun getAccountsFor(propFirm: PropFirm) : List<AccountPlan> {

        return when(propFirm) {
            PropFirm.YLOS_TRADING -> listOf(

                //Standard 25K
                AccountPlan(
                    propFirm = PropFirm.YLOS_TRADING,
                    name = "Standard 25K",
                    initialBalance = 25000.0,
                    metaProfit = 1500.0,
                    maxDrawdown = 1500.0,
                    dailyLossLimit = null,
                    maxContratos = 4,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.TRAILING,
                    rulesChallenge = emptyList(),
                    rulesFunded = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 40%", 40.0),
                        AccountRules.NewsRestrictionRule(minutesBefore = 2, minutesAfter = 2),
                        AccountRules.MinimumTradingDaysRule(daysTrading = 10, daysWin = 7, profitWin = 50.0)
                    )
                ),

                //Standard 50K
                AccountPlan(
                    propFirm = PropFirm.YLOS_TRADING,
                    name = "Standard 50K",
                    initialBalance = 50000.0,
                    metaProfit = 3000.0,
                    maxDrawdown = 2500.0,
                    dailyLossLimit = null,
                    maxContratos = 10,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.TRAILING,
                    rulesChallenge = emptyList(),
                    rulesFunded = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 40%", 40.0),
                        AccountRules.NewsRestrictionRule(minutesBefore = 2, minutesAfter = 2),
                        AccountRules.MinimumTradingDaysRule(daysTrading = 10, daysWin = 7, profitWin = 50.0)
                    )
                ),

                //Standard 100K
                AccountPlan(
                    propFirm = PropFirm.YLOS_TRADING,
                    name = "Standard 100K",
                    initialBalance = 100000.0,
                    metaProfit = 6000.0,
                    maxDrawdown = 3000.0,
                    dailyLossLimit = null,
                    maxContratos = 17,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.TRAILING,
                    rulesChallenge = emptyList(),
                    rulesFunded = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 40%", 40.0),
                        AccountRules.NewsRestrictionRule(minutesBefore = 2, minutesAfter = 2),
                        AccountRules.MinimumTradingDaysRule(daysTrading = 10, daysWin = 7, profitWin = 50.0)
                    )
                ),

                //Instant Funding 25K
                AccountPlan(
                    propFirm = PropFirm.YLOS_TRADING,
                    name = "Instant Funding 25K",
                    initialBalance = 25000.0,
                    metaProfit = 1500.0,
                    maxDrawdown = 1500.0,
                    dailyLossLimit = null,
                    maxContratos = 4,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.TRAILING,
                    rulesChallenge = emptyList(),
                    rulesFunded = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 30%", 30.0),
                        AccountRules.NewsRestrictionRule(minutesBefore = 2, minutesAfter = 2),
                        AccountRules.MinimumTradingDaysRule(daysTrading = 5, daysWin = 5, profitWin = 200.0)
                    )
                )

            )
            PropFirm.TRADEIFY -> listOf(

                //Select 25K
                AccountPlan(
                    propFirm = PropFirm.TRADEIFY,
                    name = "Select 25K",
                    initialBalance = 25000.0,
                    metaProfit = 1500.0,
                    maxDrawdown = 1000.0,
                    dailyLossLimit = null,
                    maxContratos = 1,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.EOD,
                    rulesChallenge = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 40%", maxPercentage = 40.0)
                    ),
                    rulesFunded = listOf(AccountRules.MinimumTradingDaysRule(daysTrading = 5, daysWin = 5, profitWin = 100.0))
                ),

                //Select 50K
                AccountPlan(
                    propFirm = PropFirm.TRADEIFY,
                    name = "Select 50K",
                    initialBalance = 50000.0,
                    metaProfit = 3000.0,
                    maxDrawdown = 2000.0,
                    dailyLossLimit = null,
                    maxContratos = 4,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.EOD,
                    rulesChallenge = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 40%", maxPercentage = 40.0)
                    ),
                    rulesFunded = listOf(AccountRules.MinimumTradingDaysRule(daysTrading = 5, daysWin = 5, profitWin = 150.0))
                ),

                //Select 100K
                AccountPlan(
                    propFirm = PropFirm.TRADEIFY,
                    name = "Select 100K",
                    initialBalance = 100000.0,
                    metaProfit = 6000.0,
                    maxDrawdown = 3000.0,
                    dailyLossLimit = null,
                    maxContratos = 8,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.EOD,
                    rulesChallenge = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 40%", maxPercentage = 40.0)
                    ),
                    rulesFunded = listOf(AccountRules.MinimumTradingDaysRule(daysTrading = 5, daysWin = 5, profitWin = 200.0))
                ),

                //Growth 25K
                AccountPlan(
                    propFirm = PropFirm.TRADEIFY,
                    name = "Growth 25K",
                    initialBalance = 25000.0,
                    metaProfit = 1500.0,
                    maxDrawdown = 1000.0,
                    dailyLossLimit = 600.0,
                    maxContratos = 1,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.EOD,
                    rulesChallenge = emptyList(),
                    rulesFunded = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 35%", maxPercentage = 35.0),
                        AccountRules.MinimumTradingDaysRule(daysTrading = 5, daysWin = 5, profitWin = 100.0)
                    )
                ),

                //Growth 50K
                AccountPlan(
                    propFirm = PropFirm.TRADEIFY,
                    name = "Growth 50K",
                    initialBalance = 50000.0,
                    metaProfit = 3000.0,
                    maxDrawdown = 2000.0,
                    dailyLossLimit = 1200.0,
                    maxContratos = 4,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.EOD,
                    rulesChallenge = emptyList(),
                    rulesFunded = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 35%", maxPercentage = 35.0),
                        AccountRules.MinimumTradingDaysRule(daysTrading = 5, daysWin = 5, profitWin = 150.0)
                    )
                ),

            )
            PropFirm.LUCID_TRADING -> listOf(

                //Lucid Flex 25K
                AccountPlan(
                    propFirm = PropFirm.LUCID_TRADING,
                    name = "Lucid Flex 25K",
                    initialBalance = 25000.0,
                    metaProfit = 1250.0,
                    maxDrawdown = 1000.0,
                    dailyLossLimit = null,
                    maxContratos = 2,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.EOD,
                    rulesChallenge = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 50%", 50.0)
                    ),
                    rulesFunded = listOf(
                        AccountRules.MinimumTradingDaysRule(daysTrading = 5, daysWin = 5, profitWin = 100.0)
                    )
                ),

                //Lucid Flex 50K
                AccountPlan(
                    propFirm = PropFirm.LUCID_TRADING,
                    name = "Lucid Flex 50K",
                    initialBalance = 50000.0,
                    metaProfit = 3000.0,
                    maxDrawdown = 2000.0,
                    dailyLossLimit = null,
                    maxContratos = 4,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.EOD,
                    rulesChallenge = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 50%", 50.0)
                    ),
                    rulesFunded = listOf(
                        AccountRules.MinimumTradingDaysRule(daysTrading = 5, daysWin = 5, profitWin = 150.0)
                    )
                ),

                //Lucid Flex 100K
                AccountPlan(
                    propFirm = PropFirm.LUCID_TRADING,
                    name = "Lucid Flex 100K",
                    initialBalance = 100000.0,
                    metaProfit = 6000.0,
                    maxDrawdown = 3000.0,
                    dailyLossLimit = null,
                    maxContratos = 6,
                    typeDrawdownChallenge = DrawnDownTypes.EOD,
                    typeDrawdownFunded = DrawnDownTypes.EOD,
                    rulesChallenge = listOf(
                        AccountRules.ConsistencyRule(description = "Regra de consistência de 50%", 50.0)
                    ),
                    rulesFunded = listOf(
                        AccountRules.MinimumTradingDaysRule(daysTrading = 5, daysWin = 5, profitWin = 200.0)
                    )
                )
            )
        }
    }



}