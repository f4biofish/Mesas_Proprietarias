package com.librarys.ferreira.core.ui.account_plan.list

import com.librarys.ferreira.core.domain.model.model.AccountInfo

data class ListAccountUiState(
    val accounts: List<AccountInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
