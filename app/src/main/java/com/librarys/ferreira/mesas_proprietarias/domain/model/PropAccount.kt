package com.librarys.ferreira.mesas_proprietarias.domain.model

import java.util.UUID

/**
 * Tipos de contas das mesas
 * @property id Identificador da conta
 * @property propFirms Mesa proprietária
 * @property accountName Nome da conta. Ex: Standard 50K
 * @property initialBalance Saldo inicial
 * @property currentBalance Saldo atualizado
 * @property typeDrawdown tipo de drawdown da conta
 * @property maxDrawdownAmmount Valor máximo de drawdown da conta
 * @property dailyLossLimit Valor máximo de perda diária
 * @property rulesPropFirm lista de regras da mesa
 */
sealed class PropAccount(
    val id: String = UUID.randomUUID().toString(),
    val propFirms: PropFirms,
    val accountName: String,
    val initialBalance: Double,
    val currentBalance: Double,
    val typeDrawdown: TypeDrawdown,
    val maxDrawdownAmmount: Double,
    val dailyLossLimit: Double? = null,
    val rulesPropFirm: List<AccountRule>
) {

    data class PropChallengeAccount(
        val firms: PropFirms,
        val status: StatusAccount,
        val name: String,
        val balance: Double,
        val drawdown: TypeDrawdown,
        val maxDrawdown: Double,
        val targetProfit: Double,
        val stage: AccountStage = AccountStage.CHALLENGE,
        val dailyLoss: Double? = null,
        val rules: List<AccountRule>
    ) : PropAccount(propFirms = firms, accountName = name, initialBalance = balance, currentBalance = balance, maxDrawdownAmmount = maxDrawdown,
        typeDrawdown = drawdown, dailyLossLimit = dailyLoss, rulesPropFirm = rules)


    data class PropFundedAccount(
        val firms: PropFirms,
        val status: StatusAccount,
        val name: String,
        val balance: Double,
        val drawdown: TypeDrawdown,
        val maxDrawdown: Double,
        val totalWithDrawn: Double = 0.0,
        val stage: AccountStage = AccountStage.FUNDED,
        val dailyLoss: Double? = null,
        val rules: List<AccountRule>
    ) : PropAccount(propFirms = firms, accountName = name, initialBalance = balance, currentBalance = balance, typeDrawdown = drawdown, maxDrawdownAmmount = maxDrawdown, dailyLossLimit = dailyLoss, rulesPropFirm = rules)

}
