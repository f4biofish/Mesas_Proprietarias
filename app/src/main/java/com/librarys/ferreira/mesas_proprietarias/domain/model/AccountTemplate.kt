package com.librarys.ferreira.mesas_proprietarias.domain.model

/**
 * Template para a criação de contas de mesa proprietária
 * @property firm Mesa proprietária
 * @property name Nome da conta
 * @property initialBalance Saldo inicial
 * @property maxDrawdown Drawdown máximo
 * @property targetProfit Meta de lucro
 * @property typeDrawdown tipo de drawdown
 * @property dailyLossLimit perda diária
 * @property rules regras da conta
 */
data class AccountTemplate(
    val firm: PropFirms,
    val name: String,
    val initialBalance: Double,
    val maxDrawdown: Double,
    val targetProfit: Double?, //null se for Instant Funding,
    val drawdownChallenge: TypeDrawdown?,
    val drawdownFunded: TypeDrawdown,
    val dailyLossLimit: Double? = null,
    val rulesChallenge: List<AccountRule> = emptyList(),
    val rulesFunded: List<AccountRule> = emptyList()
)


fun AccountTemplate.toPropAccount(accountNumber: String) : PropAccount {
    return if (this.targetProfit != null) {
        PropAccount.PropChallengeAccount(
            firms = this.firm,
            status = StatusAccount.ACTIVE,
            name = this.name,
            balance = this.initialBalance,
            drawdown = this.drawdownChallenge ?: TypeDrawdown.EOD,
            maxDrawdown = this.maxDrawdown,
            targetProfit = this.targetProfit,
            stage = AccountStage.CHALLENGE,
            dailyLoss = this.dailyLossLimit,
            rules = this.rulesChallenge
        )
    } else {
        PropAccount.PropFundedAccount(
            firms = this.firm,
            status = StatusAccount.ACTIVE,
            name = this.name,
            balance = this.initialBalance,
            drawdown = this.drawdownFunded,
            maxDrawdown = this.maxDrawdown,
            totalWithDrawn = 0.0,
            stage = AccountStage.FUNDED,
            dailyLoss = this.dailyLossLimit,
            rules = this.rulesFunded
        )
    }
}
