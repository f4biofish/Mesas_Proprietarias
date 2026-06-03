package com.librarys.ferreira.core.ui.account_plan.rule_consistency_info

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.librarys.ferreira.core.R
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.ui.common.InfoCard
import com.librarys.ferreira.core.ui.common.InfoRow
import com.librarys.ferreira.core.ui.theme.AppTheme
import timber.log.Timber
import java.text.NumberFormat
import java.util.Locale


@Composable
fun RuleConsistencyInfoViewCard(
    viewmodel: RuleConsistencyInfoViewModel = hiltViewModel(),
    accountInfo: AccountInfo,
    trades: List<Trades>
) {

    val state by viewmodel.uiState.collectAsState()

    Timber.d("Iniciando Rule Consistency")
    LaunchedEffect(accountInfo, trades) {
        viewmodel.validateConsistency(accountInfo, trades)
    }

    RuleConsistencyInfoContent(state = state)
}

@Composable
private fun RuleConsistencyInfoContent(
    state: RuleConsistencyInfoUiState
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    InfoCard(title = stringResource(R.string.regra_consistencia)) {
        InfoRow(label = stringResource(R.string.consistencia_necessaria), value = "${state.consistencyRule}%")
        InfoRow(label = stringResource(R.string.maior_lucro_diario), value = currencyFormatter.format(state.maxDailyProfit))
        InfoRow(label = stringResource(R.string.consistencia_atual), value = "%.2f%%".format(state.actualConsistency), valueColor = if(state.actualConsistency > state.consistencyRule) Color.Red else Color.Green)
        InfoRow(label = stringResource(R.string.lucro_necessario), value = currencyFormatter.format(state.profitNeed))
    }

}


@Preview
@Composable
private fun RuleConsistencyInfoViewCardPreview() {
    AppTheme {
        RuleConsistencyInfoContent(RuleConsistencyInfoUiState())
    }
}