package com.librarys.ferreira.core.ui.account_plan.insert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarys.ferreira.core.domain.model.config.PropFirmConfig
import com.librarys.ferreira.core.domain.model.enums.AccountStage
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.template.AccountPlan
import com.librarys.ferreira.core.domain.usecase.account.InsertAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.NumberFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InsertAccountPlanViewModel @Inject constructor(
    private val insertAccountUseCase: InsertAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsertAccountPlanUiState())
    val uiState = _uiState.asStateFlow()


    private val currencyFormatter = NumberFormat.getNumberInstance(Locale("pt", "BR")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    /**
     * Efetua a formatação do valor para o campo formatado de moeda
     * @param value valor a ser formatado
     * @return a string formatada
     */
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

    /**
     * Converte o campo de string para o valor Double
     * @param value valor a ser convertido
     * @return o valor convertido
     */
    private fun parseCurrency(value: String) : Double {
        val cleanString = value.replace(Regex("[^\\d]"), "")
        return if (cleanString.isEmpty()) 0.0 else cleanString.toDouble() / 100
    }


    fun onAccountNumberChange(newValue: String) {
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
        _uiState.update { it.copy(dailyLossLimit = formatToCurrency(newValue)) }
    }

    fun onSaveClick() {
        Timber.d("Iniciando cadastro de conta")
        _uiState.update { it.copy(isLoading = true) }
        val flag = validateRequiredFields()
        if(!flag) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        val state = _uiState.value

        //Mapeamento para o objeto de domínio
        val account = AccountInfo(
            numberAccount = state.accountNumber,
            propFirm = state.selectedPropFirm!!,
            accountStage = state.selectedAccountStage!!,
            accountName = state.accountName,
            dayStarting = state.dayStarting,
            dayBroken = state.dayBroken,
            initialBalance = parseCurrency(state.initialBalance),
            currentBalance = parseCurrency(state.currentBalance),
            typeDrawdown = state.drawdownType!!,
            maxDrawdownAmmount = parseCurrency(state.maxDrawdown),
            dailyLossLimit = if (state.dailyLossLimit.isNotEmpty()) parseCurrency(state.dailyLossLimit) else null,
            rulesPropFirm = state.rules
        )

        // Chamada ao UseCase via Coroutine
        viewModelScope.launch {
            insertAccountUseCase(account).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSaved = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    /**
     * Efetua a validação dos campos obrigatórios
     * @return true se todos os campos forem preenchidos, false caso contrário
     */
    private fun validateRequiredFields() : Boolean {

        val state = _uiState.value

        //Validação do número da conta
        if(state.accountNumber.isEmpty()){
            updateMessageError("Informe o número da conta para cadastro")
            return false
        } else if (state.accountNumber.length < 5) {
            updateMessageError("O número da conta deve ter no mínimo 5 dígitos")
            return false
        }

        //Validação da mesa proprietária
        if(state.selectedPropFirm == null){
            updateMessageError("Selecione a mesa proprietária para cadastro")
            return false
        }

        //Validação do Estágio da Conta
        if(state.selectedAccountStage == null) {
            updateMessageError("Selecione o estágio da conta para cadastro")
            return false
        }

        //Validação do nome do plano da conta
        if(state.accountName.isEmpty()){
            updateMessageError("Selecione o plano da conta para cadastro")
            return false
        }

        if (state.initialBalance.isEmpty()) {
            updateMessageError("Informe o saldo inicial")
            return false
        }

        if (state.drawdownType == null) {
            updateMessageError("Selecione o tipo de drawdown")
            return false
        }

        if (state.maxDrawdown.isEmpty()) {
            updateMessageError("Informe o valor do drawdown máximo")
            return false
        }

        return true
    }

    /**
     * Efetua a atualização da mensagem de erro
     * @param messageError mensagem de erro para atualização
     */
    private fun updateMessageError(messageError: String) {
        _uiState.update { it.copy(errorMessage = messageError) }
    }


}
