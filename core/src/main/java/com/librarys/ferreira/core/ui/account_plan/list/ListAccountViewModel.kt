package com.librarys.ferreira.core.ui.account_plan.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarys.ferreira.core.domain.usecase.account.GetAccountsUseCase
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListAccountViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListAccountUiState())
    val uiState: StateFlow<ListAccountUiState> = _uiState.asStateFlow()

    init {
        getAccounts()
    }

    private fun getAccounts() {
        Timber.d("Iniciando busca de contas")
        viewModelScope.launch {
            getAccountsUseCase()
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
                .collect { accounts ->
                    Timber.d("Lista de contas cadastradas: ${accounts.size}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            accounts = accounts,
                            filteredAccounts = filterAccounts(accounts, it.selectedFilter)
                        )
                    }
                }
        }
    }

    fun onFilterSelected(filter: AccountFilter) {
        _uiState.update {
            it.copy(
                selectedFilter = filter,
                filteredAccounts = filterAccounts(it.accounts, filter)
            )
        }
    }

    private fun filterAccounts(accounts: List<AccountInfo>, filter: AccountFilter): List<AccountInfo> {
        return when (filter) {
            AccountFilter.ALL -> accounts
            AccountFilter.ACTIVE -> accounts.filter { it.dayBroken == null }
            AccountFilter.BROKEN -> accounts.filter { it.dayBroken != null }
        }
    }
}
