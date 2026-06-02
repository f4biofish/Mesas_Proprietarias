package com.librarys.ferreira.core.ui.account_plan.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.librarys.ferreira.core.R
import com.librarys.ferreira.core.domain.model.enums.AccountStage
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.domain.model.model.SymbolAtivo
import com.librarys.ferreira.core.domain.model.model.Trades
import com.librarys.ferreira.core.ui.theme.AppTheme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AccountDetailScreen(
    viewModel: AccountDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onAddTradeClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    AccountDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAddTradeClick = onAddTradeClick,
        onTabSelected = viewModel::onTabSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailContent(
    uiState: AccountDetailUiState,
    onBackClick: () -> Unit,
    onAddTradeClick: () -> Unit,
    onTabSelected: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.account?.accountName ?: stringResource(R.string.detalhes_conta)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (uiState.selectedTab == 1 && uiState.account != null) {
                FloatingActionButton(
                    onClick = onAddTradeClick,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Icon(Icons.Default.History, contentDescription = stringResource(R.string.trades))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.errorMessage)
                }
            } else if (uiState.account != null) {
                val tabs = listOf(
                    stringResource(R.string.informacoes) to Icons.Default.Info,
                    stringResource(R.string.trades) to Icons.Default.History
                )

                Column {
                    TabRow(
                        selectedTabIndex = uiState.selectedTab,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, (title, icon) ->
                            Tab(
                                selected = uiState.selectedTab == index,
                                onClick = { onTabSelected(index) },
                                text = { Text(text = title) },
                                icon = { Icon(icon, contentDescription = null) },
                                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                        }
                    }

                    when (uiState.selectedTab) {
                        0 -> AccountInfoTab(uiState.account)
                        1 -> TradesTab(uiState.trades)
                    }
                }
            }
        }
    }
}

@Composable
fun AccountInfoTab(account: AccountInfo) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            InfoCard(title = stringResource(R.string.resumo_financeiro)) {
                InfoRow(label = stringResource(R.string.saldo_inicial), value = currencyFormatter.format(account.initialBalance))
                InfoRow(
                    label = stringResource(R.string.saldo_atual),
                    value = currencyFormatter.format(account.currentBalance),
                    valueColor = if (account.currentBalance >= account.initialBalance) colorResource(R.color.positive_green) else MaterialTheme.colorScheme.error
                )
                val profit = account.currentBalance - account.initialBalance
                InfoRow(
                    label = stringResource(R.string.lucro_prejuizo_total),
                    value = currencyFormatter.format(profit),
                    valueColor = if (profit >= 0) colorResource(R.color.positive_green) else MaterialTheme.colorScheme.error
                )
            }
        }

        item {
            InfoCard(title = stringResource(R.string.configuracoes_plano)) {
                InfoRow(label = stringResource(R.string.numero_conta), value = account.numberAccount)
                InfoRow(label = stringResource(R.string.mesa_proprietaria), value = account.propFirm.name)
                InfoRow(label = stringResource(R.string.estagio), value = account.accountStage.name)
                InfoRow(label = stringResource(R.string.tipo_drawdown), value = account.typeDrawdown.name)
                InfoRow(label = stringResource(R.string.max_drawdown), value = currencyFormatter.format(account.maxDrawdownAmmount))
                account.dailyLossLimit?.let {
                    InfoRow(label = stringResource(R.string.limite_perda_diaria), value = currencyFormatter.format(it))
                }
            }
        }

        item {
            InfoCard(title = stringResource(R.string.datas)) {
                InfoRow(label = stringResource(R.string.data_inicio), value = dateFormatter.format(account.dayStarting))
                account.dayBroken?.let {
                    InfoRow(label = stringResource(R.string.data_quebra), value = dateFormatter.format(it), valueColor = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun TradesTab(trades: List<Trades>) {
    if (trades.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.nenhum_trade_realizado),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trades.sortedByDescending { it.date }) { trade ->
                TradeItem(trade)
            }
        }
    }
}

@Composable
fun TradeItem(trade: Trades) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = trade.symbolAtivo.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = dateFormatter.format(trade.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currencyFormatter.format(trade.profit),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (trade.profit >= 0) colorResource(R.color.positive_green) else MaterialTheme.colorScheme.error
                    )
                )
                Text(
                    text = "${trade.contratos} ${if (trade.contratos > 1) "contratos" else "contrato"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = valueColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountDetailPreview() {
    AppTheme {
        AccountDetailContent(
            uiState = AccountDetailUiState(
                account = AccountInfo(
                    numberAccount = "123456",
                    propFirm = PropFirm.TRADEIFY,
                    accountStage = AccountStage.CHALLENGE,
                    accountName = "Standard 50K",
                    initialBalance = 50000.0,
                    currentBalance = 51250.0,
                    typeDrawdown = DrawnDownTypes.TRAILING,
                    maxDrawdownAmmount = 2000.0,
                    rulesPropFirm = emptyList()
                ),
                trades = listOf(
                    Trades(
                        accountInfoId = "1",
                        accountNumber = "123456",
                        symbolAtivo = SymbolAtivo.NQ,
                        contratos = 2,
                        profit = 450.0,
                        date = Date()
                    ),
                    Trades(
                        accountInfoId = "1",
                        accountNumber = "123456",
                        symbolAtivo = SymbolAtivo.ES,
                        contratos = 1,
                        profit = -120.0,
                        date = Date()
                    )
                )
            ),
            onBackClick = {},
            onAddTradeClick = {},
            onTabSelected = {}
        )
    }
}
