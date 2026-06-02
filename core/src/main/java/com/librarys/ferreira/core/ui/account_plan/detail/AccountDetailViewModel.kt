package com.librarys.ferreira.core.ui.account_plan.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarys.ferreira.core.domain.model.config.AtivosConfig
import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.domain.repository.AccountRepository
import com.librarys.ferreira.core.domain.usecase.account.GetAccountByIdUseCase
import com.librarys.ferreira.core.domain.usecase.trades.DeleteTradeUseCase
import com.librarys.ferreira.core.domain.usecase.trades.GetTradesByAccountIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val getTradesByAccountIdUseCase: GetTradesByAccountIdUseCase,
    private val deleteTradeUseCase: DeleteTradeUseCase,
    private val accountRepository: AccountRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val accountId: String = checkNotNull(savedStateHandle["accountId"])

    private val _uiState = MutableStateFlow(AccountDetailUiState())
    val uiState: StateFlow<AccountDetailUiState> = _uiState.asStateFlow()

    init {
        loadAccountData()
    }

    private fun loadAccountData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                getAccountByIdUseCase(accountId),
                getTradesByAccountIdUseCase(accountId)
            ) { account, trades ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        account = account,
                        trades = trades
                    )
                }
            }.catch { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }.collect {}
        }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    /**
     * Exclui um trade selecionado e atualiza o saldo da conta
     * @param trade Dados do trade a ser excluído
     */
    fun deleteTrade(trade: Trades) {
        viewModelScope.launch {
            val account = _uiState.value.account ?: return@launch

            // Calcula o lucro líquido do trade (lucro bruto - custos) para estornar do saldo
            val costPerContract = AtivosConfig.getCost(account.propFirm, trade.symbolAtivo)
            val totalCost = costPerContract * trade.contratos
            val netProfit = trade.profit - totalCost

            val deleted = deleteTradeUseCase(trade)
            if (deleted) {
                // Atualiza o saldo da conta estornando o lucro líquido do trade deletado
                val updatedAccount = account.copy(
                    currentBalance = account.currentBalance - netProfit
                )
                accountRepository.updateAccount(updatedAccount)
            } else {
                _uiState.update { it.copy(errorMessage = "Erro ao excluir o trade") }
            }
        }
    }
}
