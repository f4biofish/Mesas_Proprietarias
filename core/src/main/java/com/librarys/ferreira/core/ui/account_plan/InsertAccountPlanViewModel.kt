package com.librarys.ferreira.core.ui.account_plan

import android.util.Log
import androidx.lifecycle.ViewModel
import com.librarys.ferreira.core.domain.model.config.PropFirmConfig
import com.librarys.ferreira.core.domain.model.enums.AccountStage
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.template.AccountPlan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InsertAccountPlanViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(InsertAccountPlanUiState())
    val uiState = _uiState.asStateFlow()


    private val currencyFormatter = NumberFormat.getNumberInstance(Locale("pt", "BR")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    private fun formatToCurrency(value: String) : String {
        //Remove tudo que não for dpigito
        val cleanString = value.replace(Regex("[^\\d]"), "")
        if(cleanString.isEmpty()) return ""

        return try {
            val parsed = cleanString.toDouble() / 100
            currencyFormatter.format(parsed)
        } catch (e: Exception){
            ""
        }
    }


    fun onAccountNumberChange(newValue: String) {
        Log.d("Teste", "onAccountNumberChange: Recebido o novo valor: $newValue")
        _uiState.update { it.copy(accountNumber = newValue) }
    }

    fun onPropFirmSelected(propFirm: PropFirm) {
        val accounts = PropFirmConfig.getAccountsFor(propFirm)
        _uiState.update { it.copy(
            selectedPropFirm = propFirm,
            availableAccountPlans = accounts,
            accountName = "", // Reset account name when prop firm changes
            initialBalance = "",
            maxDrawdown = "",
            dailyLossLimit = "",
            drawdownType = null
        ) }
    }

    fun onAccountPlanSelected(accountPlan: AccountPlan) {
        _uiState.update { it.copy(
            accountName = accountPlan.name,
            selectedAccountStage = AccountStage.CHALLENGE,
            initialBalance = currencyFormatter.format(accountPlan.initialBalance),
            currentBalance = currencyFormatter.format(accountPlan.initialBalance), // Usually current balance starts as initial balance
            metaProfit = accountPlan.metaProfit?.toString() ?: "",
            maxDrawdown = currencyFormatter.format(accountPlan.maxDrawdown),
            dailyLossLimit = accountPlan.dailyLossLimit?.toString() ?: "",
            drawdownType = accountPlan.typeDrawdownChallenge, // Assuming we start with Challenge type
            rules = accountPlan.rulesChallenge
        ) }
    }

    fun onAccountStageSelected(stage: AccountStage) {
        val currentPlanName = _uiState.value.accountName
        val currentPropFirm = _uiState.value.selectedPropFirm
        
        val plan = if (currentPropFirm != null) {
            PropFirmConfig.getAccountsFor(currentPropFirm).find { it.name == currentPlanName }
        } else null

        _uiState.update { it.copy(
            selectedAccountStage = stage,
            drawdownType = if (stage == AccountStage.CHALLENGE) plan?.typeDrawdownChallenge else plan?.typeDrawdownFunded,
            rules = if (stage == AccountStage.CHALLENGE) plan?.rulesChallenge ?: emptyList() else plan?.rulesFunded ?: emptyList()
        ) }
    }

    fun onDayStartingChange(newValue: Date) {
        _uiState.update { it.copy(dayStarting = newValue) }
    }

    fun onDayBrokenChange(newValue: Date?) {
        _uiState.update { it.copy(dayBroken = newValue) }
    }

    fun onInitialBalanceChange(newValue: String) {
        _uiState.update { it.copy(initialBalance = formatToCurrency(newValue)) }
    }

    fun onCurrentBalanceChange(newValue: String) {
        _uiState.update { it.copy(currentBalance = formatToCurrency(newValue)) }
    }

    fun onDrawdownTypeSelected(type: DrawnDownTypes) {
        _uiState.update { it.copy(drawdownType = type) }
    }

    fun onMaxDrawdownChange(newValue: String) {
        _uiState.update { it.copy(maxDrawdown = formatToCurrency(newValue)) }
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
