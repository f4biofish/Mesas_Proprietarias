package com.librarys.ferreira.core.ui.account_plan.rule_consistency_info

import androidx.lifecycle.ViewModel
import com.librarys.ferreira.core.domain.model.config.AtivosConfig
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.domain.model.rules.AccountRules
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class RuleConsistencyInfoViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(RuleConsistencyInfoUiState())
    val uiState = _uiState.asStateFlow()


    /**
     * Função inicial para validação de dados de consistência
     * @param accountInfo Dados da conta
     * @param trades Trades realizados
     */
    fun validateConsistency(accountInfo: AccountInfo, trades: List<Trades>) {

        //1. Buscando regra de consistência para a conta
        _uiState.update { it.copy(isLoading = true) }
        val rules = accountInfo.rulesPropFirm.find { it is AccountRules.ConsistencyRule } as? AccountRules.ConsistencyRule
        if (rules == null) {
            Timber.d("Este plano não foi encontrado regra de consistência")
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        //2. Atualiza o percentual de regra de consistência para o plano
        _uiState.update { it.copy(consistencyRule = rules.maxPercentage) }

        //3. Obtém o maior lucro líquido em um único dia
        val maxDailyProfit = getMaxDailyProfit(accountInfo.propFirm, trades)
        _uiState.update { it.copy(maxDailyProfit = maxDailyProfit) }

        //4. Obtém o lucro líquido total do período
        val totalNetProfit = getTotalNetProfit(accountInfo.propFirm, trades)

        //5. Calcula a consistência atual
        val actualConsistency = maxDailyProfit / totalNetProfit * 100
        Timber.d("Consistência atual: $actualConsistency")
        _uiState.update { it.copy(actualConsistency = actualConsistency) }

        //6. Verifica se ainda precisa de mais lucro para alcançar a consistência necessária
        if(actualConsistency > rules.maxPercentage){
            val profitNeed = (maxDailyProfit / rules.maxPercentage * 100 - totalNetProfit).coerceAtLeast(0.0)
            Timber.d("Lucro necessário: $profitNeed")
            _uiState.update { it.copy(profitNeed = profitNeed) }
        }

    }

    /**
     * Obtém o lucro máximo efetuado em um único dia, descontado as taxas
     * @param trades Trades realizados no período
     * @return Maior lucro líquido efetuado em um único dia
     */
    private fun getMaxDailyProfit(propFirm: PropFirm, trades: List<Trades>) : Double {

        //1. Captura o melhor dia de operação
        val bestDay = trades
            .groupBy {
                it.date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
            .maxByOrNull { (_, tradesDoDia) ->
                tradesDoDia.sumOf { it.profit }
            } ?: return 0.0

        Timber.d("Melhor dia de operação: $bestDay")

        //2. Percorre a lista de trades do melhor dia, abatendo os custos de taxas
        var lucroLiquido = 0.0

        for (trade in bestDay.value){
            //Obtém o custo de taxas por trade
            val fee = trade.contratos * AtivosConfig.getCost(propFirm, trade.symbolAtivo)
            val netProfit = trade.profit - fee
            lucroLiquido += netProfit
        }

        return lucroLiquido
    }

    /**
     * Obtém o lucro liquido total
     * @param propFirm mesa proprietária
     * @param trades Trades realizados
     * @return o lucro liquido total das operações
     */
    private fun getTotalNetProfit(propFirm: PropFirm, trades: List<Trades>) : Double {

        //1. Percorre os trades, calculando o total de lucro líquido
        var totalNetProfit : Double = 0.0
        trades.forEach { trade ->
            val fee = trade.contratos * AtivosConfig.getCost(propFirm = propFirm, symbol = trade.symbolAtivo)
            val netProfit = trade.profit - fee
            totalNetProfit += netProfit
        }

        return totalNetProfit
    }


}