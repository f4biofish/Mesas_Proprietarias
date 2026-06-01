package com.librarys.ferreira.core.ui.account_plan

import android.util.Log
import androidx.lifecycle.ViewModel
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class InsertAccountPlanViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(InsertAccountPlanUiState())
    val uiState = _uiState.asStateFlow()

    fun onAccountNumberChange(newValue: String) {
        Log.d("Teste", "onAccountNumberChange: Recebido o novo valor: $newValue")
        _uiState.update { it.copy(accountNumber = newValue) }
    }

    fun onPropFirmSelected(propFirm: PropFirm) {
        _uiState.update { it.copy(selectedPropFirm = propFirm) }
    }

    fun onAccountNameChange(newValue: String) {
        _uiState.update { it.copy(accountName = newValue) }
    }

    fun onDayStartingChange(newValue: Date) {
        _uiState.update { it.copy(dayStarting = newValue) }
    }

    fun onDayBrokenChange(newValue: Date?) {
        _uiState.update { it.copy(dayBroken = newValue) }
    }

    fun onInitialBalanceChange(newValue: String) {
        _uiState.update { it.copy(initialBalance = newValue) }
    }

    fun onCurrentBalanceChange(newValue: String) {
        _uiState.update { it.copy(currentBalance = newValue) }
    }

    fun onDrawdownTypeSelected(type: DrawnDownTypes) {
        _uiState.update { it.copy(drawdownType = type) }
    }

    fun onMaxDrawdownChange(newValue: String) {
        _uiState.update { it.copy(maxDrawdown = newValue) }
    }

    fun onDailyLossLimitChange(newValue: String) {
        _uiState.update { it.copy(dailyLossLimit = newValue) }
    }

    fun onSaveClick() {
        // TODO: Pendente a gravação do registro do plano
        // Aqui você adicionará a lógica de salvamento futuramente (ex: chamar um Repository)
        _uiState.update { it.copy(isLoading = true) }
        
        // Simulação de salvamento
        println("Salvando conta: ${_uiState.value.accountName}")
    }
}
