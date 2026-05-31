package com.librarys.ferreira.core.domain.model.template

import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.model.AccountPropFirm
import com.librarys.ferreira.core.domain.model.rules.AccountRules

/**
 * Conta Template para criação de todas as contas de mesas proprietárias
 * @property propFirm Mesa proprietária a qual a conta pertence
 * @property name Nome da conta. Ex: Standard 25K
 * @property initialBalance Saldo inicial da conta
 * @property metaProfit Meta de lucro da fase de avaliação. Null se a conta for financiada
 * @property maxDrawdown Drawdown máximo permitido na conta
 * @property dailyLossLimit Perda máxima diária na conta. Null, se regra não ativa na mesa
 * @property maxContratos Máximo de contratos permitidos na conta
 * @property typeDrawdownChallenge Tipo de drawdown na fase de avaliação
 * @property typeDrawdownFunded Tipo de drawdown na conta financiada
 * @property rulesChallenge Regras da mesa na fase de avaliação
 * @property rulesFunded Regras da mesa na fase financiada
 */
data class AccountPlan(
    val propFirm: PropFirm,
    val name: String,
    val initialBalance: Double,
    val metaProfit: Double?,
    val maxDrawdown: Double,
    val dailyLossLimit: Double? = null,
    val maxContratos: Int,
    val typeDrawdownChallenge: DrawnDownTypes,
    val typeDrawdownFunded: DrawnDownTypes,
    val rulesChallenge: List<AccountRules>,
    val rulesFunded: List<AccountRules>
)

/**
 * Converte a classe de [AccountPlan] para a classe [AccountPropFirm.Challenge]
 */
fun AccountPlan.toAccountPropFirmChallenge() = AccountPropFirm.Challenge(
    info = AccountInfo(
        propFirm = this.propFirm,
        accountName = this.name,
        initialBalance = this.initialBalance,
        currentBalance = this.initialBalance,
        typeDrawdown = this.typeDrawdownChallenge,
        maxDrawdownAmmount = this.maxDrawdown,
        dailyLossLimit = this.dailyLossLimit,
        rulesPropFirm = this.rulesChallenge
    ),
    targetProfit = this.metaProfit ?: 0.0
)

/**
 * Converte a classe de [AccountPlan] para [AccountPropFirm.Funded]
 */
fun AccountPlan.toAccountPropFirmFunded() = AccountPropFirm.Funded(
    info = AccountInfo(
        propFirm = this.propFirm,
        accountName = this.name,
        initialBalance = this.initialBalance,
        currentBalance = this.initialBalance,
        typeDrawdown = this.typeDrawdownFunded,
        maxDrawdownAmmount = this.maxDrawdown,
        dailyLossLimit = this.dailyLossLimit,
        rulesPropFirm = this.rulesFunded
    )
)