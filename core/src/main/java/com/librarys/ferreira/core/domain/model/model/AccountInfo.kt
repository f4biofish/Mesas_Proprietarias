package com.librarys.ferreira.core.domain.model.model

import com.librarys.ferreira.core.domain.model.enums.AccountStage
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.rules.AccountRules
import java.util.Date
import java.util.UUID

/**
 * Informações de contas de mesas proprietárias
 * @property id identificador único da conta, gerado automaticamente
 * @property numberAccount Número da conta na mesa proprietária
 * @property propFirm Mesa proprietária
 * @property accountName Plano da conta. Ex: Standard 25K
 * @property dayStarting Dia de início da conta
 * @property dayBroken Dia de quebra da conta
 * @property initialBalance Saldo inicial
 * @property currentBalance Saldo atual
 * @property typeDrawdown Tipo de drawdown
 * @property maxDrawdownAmmount Máximo de drawdown da conta
 * @property dailyLossLimit Limite diário de perda
 * @property rulesPropFirm Regras da mesa proprietária
 */
data class AccountInfo(
    val id: String = UUID.randomUUID().toString(),
    val numberAccount: String,
    val propFirm: PropFirm,
    val accountStage: AccountStage,
    val accountName: String,
    val dayStarting: Date = Date(),
    val dayBroken: Date? = null,
    val initialBalance: Double,
    val currentBalance: Double,
    val typeDrawdown: DrawnDownTypes,
    val maxDrawdownAmmount: Double,
    val dailyLossLimit: Double? = null,
    val rulesPropFirm: List<AccountRules>
)
