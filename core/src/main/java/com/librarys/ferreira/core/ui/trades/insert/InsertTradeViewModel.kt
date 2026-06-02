package com.librarys.ferreira.core.ui.trades.insert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarys.ferreira.core.domain.model.config.AtivosConfig
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.model.SymbolAtivo
import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.domain.repository.AccountRepository
import com.librarys.ferreira.core.domain.repository.TradesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class InsertTradeViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val tradesRepository: TradesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsertTradeUiState())
    val uiState: StateFlow<InsertTradeUiState> = _uiState.asStateFlow()

    init {
        loadAccounts()
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            accountRepository.getAccounts().collect { accounts ->
                // Filtra apenas contas que não foram quebradas (dayBroken == null)
                val activeAccounts = accounts.filter { it.dayBroken == null }
                _uiState.update { 
                    it.copy(
                        availableAccounts = activeAccounts,
                        isLoading = false
                    ) 
                }
            }
        }
    }

    fun onAccountSelected(account: AccountInfo) {
        _uiState.update { it.copy(selectedAccount = account) }
    }

    fun onDateChanged(date: Date) {
        _uiState.update { it.copy(date = date) }
    }

    fun onSymbolAtivoSelected(symbol: SymbolAtivo) {
        _uiState.update { it.copy(symbolAtivo = symbol) }
    }

    fun onContratosChanged(contratos: String) {
        _uiState.update { it.copy(contratos = contratos) }
    }

    fun onProfitChanged(profit: String) {
        _uiState.update { it.copy(profit = profit) }
    }

    fun onSaveClick() {
        val state = _uiState.value
        
        if (state.selectedAccount == null) {
            _uiState.update { it.copy(errorMessage = "Selecione uma conta") }
            return
        }
        
        if (state.symbolAtivo == null) {
            _uiState.update { it.copy(errorMessage = "Selecione um ativo") }
            return
        }
        
        val contratosInt = state.contratos.toIntOrNull()
        if (contratosInt == null || contratosInt <= 0) {
            _uiState.update { it.copy(errorMessage = "Número de contratos inválido") }
            return
        }
        
        val profitDouble = state.profit.replace(",", ".").toDoubleOrNull()
        if (profitDouble == null) {
            _uiState.update { it.copy(errorMessage = "Resultado inválido") }
            return
        }

        if (state.date.after(Date())) {
            _uiState.update { it.copy(errorMessage = "A data não pode ser futura") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val account = state.selectedAccount
            val costPerContract = AtivosConfig.getCost(account.propFirm, state.symbolAtivo)
            val totalCost = costPerContract * contratosInt
            val netProfit = profitDouble - totalCost
            
            val trade = Trades(
                date = state.date,
                accountInfoId = account.id,
                accountNumber = account.numberAccount,
                symbolAtivo = state.symbolAtivo,
                contratos = contratosInt,
                profit = profitDouble
            )

            val tradeSaved = tradesRepository.saveTradeInDb(trade)
            
            if (tradeSaved) {
                val updatedAccount = account.copy(
                    currentBalance = account.currentBalance + netProfit
                )
                val accountUpdated = accountRepository.updateAccount(updatedAccount)

                if (accountUpdated) {
                    _uiState.update { it.copy(isSaved = true, isLoading = false) }
                    delay(500L.milliseconds)
                    _uiState.value = InsertTradeUiState()
                } else {
                    _uiState.update { it.copy(errorMessage = "Erro ao atualizar o saldo da conta", isLoading = false) }
                }
            } else {
                _uiState.update { it.copy(errorMessage = "Erro ao salvar o trade", isLoading = false) }
            }
        }
    }
}
