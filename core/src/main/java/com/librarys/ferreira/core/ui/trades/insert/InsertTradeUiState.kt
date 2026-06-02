package com.librarys.ferreira.core.ui.trades.insert

import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.model.SymbolAtivo
import java.util.Date

data class InsertTradeUiState(
    val tradeId: String? = null,
    val selectedAccount: AccountInfo? = null,
    val date: Date = Date(),
    val symbolAtivo: SymbolAtivo? = null,
    val contratos: String = "",
    val profit: String = "",
    val availableAccounts: List<AccountInfo> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
) {
    val isEditMode: Boolean get() = tradeId != null
}
