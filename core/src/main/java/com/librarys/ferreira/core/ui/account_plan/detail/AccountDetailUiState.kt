package com.librarys.ferreira.core.ui.account_plan.detail

import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.model.Trades

data class AccountDetailUiState(
    val isLoading: Boolean = false,
    val account: AccountInfo? = null,
    val trades: List<Trades> = emptyList(),
    val errorMessage: String? = null,
    val selectedTab: Int = 0
)
