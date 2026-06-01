package com.librarys.ferreira.core.ui.account_plan

import com.librarys.ferreira.core.domain.model.enums.AccountStage
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.rules.AccountRules
import com.librarys.ferreira.core.domain.model.template.AccountPlan
import java.util.Date

/**
 * Estado da tela de inserção de plano de conta
 */
data class InsertAccountPlanUiState(
    val accountNumber: String = "",
    val selectedPropFirm: PropFirm? = null,
    val selectedAccountStage: AccountStage? = null,
    val availableAccountPlans: List<AccountPlan> = emptyList(),
    val accountName: String = "",
    val dayStarting: Date = Date(),
    val dayBroken: Date? = null,
    val initialBalance: String = "",
    val currentBalance: String = "",
    val metaProfit: String = "",
    val drawdownType: DrawnDownTypes? = null,
    val maxDrawdown: String = "",
    val dailyLossLimit: String = "",
    val rules: List<AccountRules> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)
