package com.librarys.ferreira.mesas_proprietarias.domain.data

import com.librarys.ferreira.mesas_proprietarias.domain.model.AccountRule
import com.librarys.ferreira.mesas_proprietarias.domain.model.AccountTemplate
import com.librarys.ferreira.mesas_proprietarias.domain.model.AssetFee
import com.librarys.ferreira.mesas_proprietarias.domain.model.Ativos
import com.librarys.ferreira.mesas_proprietarias.domain.model.PropFirms
import com.librarys.ferreira.mesas_proprietarias.domain.model.TypeDrawdown

object PropFirmConfig {


    //region Mapa de Taxas de Mesas
    private val feesByFirm = mapOf(
        PropFirms.YLOS_TRADING to listOf(
            AssetFee(
                nome = "Nasdaq",
                symbol = Ativos.NQ,
                feePerContract = 4.36
            ),
            AssetFee(
                nome = "Micro Nasdaq",
                symbol = Ativos.MNQ,
                feePerContract = 1.30
            )
        ),

        PropFirms.TRADEIFY to listOf(
            AssetFee(
                nome = "Nasdaq",
                symbol = Ativos.NQ,
                feePerContract = 5.76
            ),
            AssetFee(
                nome = "Micro Nasdaq",
                symbol = Ativos.MNQ,
                feePerContract = 1.82
            )
        ),

        PropFirms.LUCID_TRADING to listOf(
            AssetFee(
                nome = "Nasdaq",
                symbol = Ativos.NQ,
                feePerContract = 3.5
            ),
            AssetFee(
                nome = "Micro Nasdaq",
                symbol = Ativos.MNQ,
                feePerContract = 1.0
            )
        )
    )


    /**
     * Obtém a taxa total (entrada e saída) da operação de acordo com a mesa proprietária e o ativo
     * @param firms mesa proprietária
     * @param symbol ativo
     * @return o valor total da operação por contrato
     */
    fun getFeeFot(firms: PropFirms, symbol: Ativos) : Double {
        return feesByFirm[firms]?.find { it.symbol == symbol }?.feePerContract ?: 0.0
    }

    //endregion


    private val ylosCommonRulesStandardAccount = listOf(
        AccountRule.ConsistencyRule(description = "Consistência de 40%", 40.0),
        AccountRule.NewsRestrictionRule(minutesBefore = 2, minutesAfter = 2),
        AccountRule.MinimumTradingDaysRule(daysTrading = 10, daysWin = 7, minProfit = 50.0)
    )

    private val ylosCommonRulesInstantFundingAccount = listOf(
        AccountRule.ConsistencyRule(description = "Consistência de 30%", 30.0),
        AccountRule.NewsRestrictionRule(minutesBefore = 2, minutesAfter = 2),
        AccountRule.MinimumTradingDaysRule(daysTrading = 5, daysWin = 5, minProfit = 200.0)
    )

    //Catálogo de contas da Ylos
    val ylosTemplates = listOf(

        //Standard 25K
        AccountTemplate(
            firm = PropFirms.YLOS_TRADING,
            name = "Standard 25K",
            initialBalance = 25000.0,
            maxDrawdown = 1500.0,
            targetProfit = 1500.0,
            drawdownChallenge = TypeDrawdown.EOD,
            drawdownFunded = TypeDrawdown.TRAILING,
            dailyLossLimit = null,
            rulesChallenge = emptyList(),
            rulesFunded = ylosCommonRulesStandardAccount
        ),

        //Standard 50K
        AccountTemplate(
            firm = PropFirms.YLOS_TRADING,
            name = "Standard 50K",
            initialBalance = 50000.0,
            maxDrawdown = 2500.0,
            targetProfit = 3000.0,
            drawdownChallenge = TypeDrawdown.EOD,
            drawdownFunded = TypeDrawdown.TRAILING,
            dailyLossLimit = null,
            rulesChallenge = emptyList(),
            rulesFunded = ylosCommonRulesStandardAccount
        ),

        //Standard 100K
        AccountTemplate(
            firm = PropFirms.YLOS_TRADING,
            name = "Standard 100K",
            initialBalance = 100000.0,
            maxDrawdown = 3000.0,
            targetProfit = 6000.0,
            drawdownChallenge = TypeDrawdown.EOD,
            drawdownFunded = TypeDrawdown.TRAILING,
            dailyLossLimit = null,
            rulesChallenge = emptyList(),
            rulesFunded = ylosCommonRulesStandardAccount
        ),

        //Instant Funding 25K
        AccountTemplate(
            firm = PropFirms.YLOS_TRADING,
            name = "Instant Funding 25K",
            initialBalance = 25000.0,
            maxDrawdown = 1500.0,
            targetProfit = null,
            drawdownChallenge = null,
            drawdownFunded = TypeDrawdown.TRAILING,
            dailyLossLimit = null,
            rulesChallenge = emptyList(),
            rulesFunded = ylosCommonRulesInstantFundingAccount
        ),
    )

}