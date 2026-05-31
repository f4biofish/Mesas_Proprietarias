package com.librarys.ferreira.core.domain.model.model

import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.rules.AccountRules
import java.util.Date
import java.util.UUID

data class AccountInfo(
    val id: String = UUID.randomUUID().toString(),
    val propFirm: PropFirm,
    val accountName: String,
    val dayStarting: Date = Date(),
    val dayBroken: Date? = null,
    val initialBalance: Double,
    val currentBalance: Double,
    val typeDrawdown: DrawnDownTypes,
    val maxDrawdownAmmount: Double,
    val dailyLossLimit: Double,
    val rulesPropFirm: List<AccountRules>
)
