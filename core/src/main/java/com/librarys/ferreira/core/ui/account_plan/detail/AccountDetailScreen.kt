package com.librarys.ferreira.core.ui.account_plan.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onAddTradeClick: () -> Unit = {},
    onEditTradeClick: (Trades) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    AccountDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAddTradeClick = onAddTradeClick,
        onTabSelected = viewModel::onTabSelected,
        onEditTradeClick = onEditTradeClick,
        onDeleteTradeClick = viewModel::deleteTrade
    )
}

//region Conteúdo da tela de detalhes da conta
/**
 * Screen principal da tela de detalhes da conta
 * @param uiState Tela de detalhes da conta
 * @param onBackClick Voltar para a tela anterior
 * @param onAddTradeClick Adicionar novo trade
 * @param onTabSelected Alterar a aba selecionada
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountDetailContent(
    uiState: AccountDetailUiState,
    onBackClick: () -> Unit,
    onAddTradeClick: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onEditTradeClick: (Trades) -> Unit,
    onDeleteTradeClick: (Trades) -> Unit
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
                        1 -> TradesTab(
                            trades = uiState.trades,
                            onEditTradeClick = onEditTradeClick,
                            onDeleteTradeClick = onDeleteTradeClick
                        )
                    }
                }
            }
        }
    }
}
//endregion

//region Conteúdo da tela de detalhes da conta
/**
 * Tela de informações da conta
 * @param account Dados da conta
 */
@Composable
private fun AccountInfoTab(account: AccountInfo) {
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
//endregion

//region Conteúdo da tela de Trades
/**
 * Tela de Trades realizados
 * @param trades Lista de Trades
 * @param onEditTradeClick Editar trade
 * @param onDeleteTradeClick Excluir trade
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TradesTab(
    trades: List<Trades>,
    onEditTradeClick: (Trades) -> Unit,
    onDeleteTradeClick: (Trades) -> Unit
) {
    //Sem trades realizados
    if (trades.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.nenhum_trade_realizado),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {

        var acumularFlag by remember { mutableStateOf(false) }

        // Processamento dos dados para exibição
        val sortedTrades = remember(trades) { trades.sortedByDescending { it.date } }

        val dailySummaries = remember(sortedTrades, acumularFlag) {
            if (acumularFlag) {
                sortedTrades.groupBy {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.date)
                }.map { (_, group) ->
                    DailyTradeSummary(
                        date = group.first().date,
                        totalProfit = group.sumOf { it.profit },
                        tradeCount = group.size
                    )
                }
            } else emptyList()
        }

        //Lista de Trades Realizados
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            //Acumular operações por dia
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = acumularFlag,
                        onCheckedChange = {
                            acumularFlag = it
                        }
                    )

                    Text(
                        text = stringResource(R.string.acumular_por_dia),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (acumularFlag) {
                items(dailySummaries) { summary ->
                    TradeDailyItem(summary.date, summary.totalProfit, summary.tradeCount)
                }
            } else {
                items(
                    items = sortedTrades,
                    key = { it.id }
                ) { trade ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            when (value) {
                                SwipeToDismissBoxValue.StartToEnd -> {
                                    onDeleteTradeClick(trade)
                                    true
                                }
                                SwipeToDismissBoxValue.EndToStart -> {
                                    onEditTradeClick(trade)
                                    false // Não some da tela, apenas abre a edição
                                }
                                SwipeToDismissBoxValue.Settled -> false
                            }
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            SwipeBackground(dismissState)
                        },
                        content = {
                            TradeItem(trade)
                        }
                    )
                }
            }

        }
    }
}

/**
 * Background para o swipe de exclusão e edição
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.errorContainer
        SwipeToDismissBoxValue.EndToStart -> colorResource(R.color.positive_green).copy(alpha = 0.2f)
        else -> Color.Transparent
    }

    val direction = dismissState.dismissDirection

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp)
            .background(color, RoundedCornerShape(8.dp)),
        contentAlignment = when (direction) {
            SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
            SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
            else -> Alignment.Center
        }
    ) {
        if (direction == SwipeToDismissBoxValue.StartToEnd) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.excluir),
                modifier = Modifier.padding(start = 16.dp),
                tint = MaterialTheme.colorScheme.error
            )
        } else if (direction == SwipeToDismissBoxValue.EndToStart) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.editar),
                modifier = Modifier.padding(end = 16.dp),
                tint = colorResource(R.color.positive_green)
            )
        }
    }
}

/**
 * Data class para resumo diário de trades
 */
private data class DailyTradeSummary(
    val date: Date,
    val totalProfit: Double,
    val tradeCount: Int
)
//endregion

//region Item da lista de trades
/**
 * Item da lista de trades
 * @param trade Dados do trade realizado
 */
@Composable
private fun TradeItem(trade: Trades) {
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

/**
 * Item de resumo diário de trades
 * @param date Data do dia
 * @param totalProfit Lucro total do dia
 * @param tradeCount Quantidade de operações no dia
 */
@Composable
private fun TradeDailyItem(date: Date, totalProfit: Double, tradeCount: Int) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dayOfWeekFormatter = SimpleDateFormat("EEEE", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
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
                    text = dateFormatter.format(date),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = dayOfWeekFormatter.format(date).replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currencyFormatter.format(totalProfit),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (totalProfit >= 0) colorResource(R.color.positive_green) else MaterialTheme.colorScheme.error
                    )
                )
                Text(
                    text = "$tradeCount ${if (tradeCount > 1) "operações" else "operação"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
//endregion

//region Card de informações
/**
 * Card de informações
 * @param title Titulo do card
 * @param content Conteudo do card
 */
@Composable
private fun InfoCard(title: String, content: @Composable ColumnScope.() -> Unit) {
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
//endregion

//region Linha de informações
/**
 * Linha de informações
 * @param label Label da linha
 * @param value Valor da linha
 * @param valueColor Cor do valor
 */
@Composable
private fun InfoRow(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
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
//endregion

//region preview aba trades
/**
 * Preview da aba de detalhes do trade
 */
@Preview
@Composable
private fun AccountDetailTabTradesPreview() {
    AppTheme {
        TradesTab(
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
            ),
            onEditTradeClick = {},
            onDeleteTradeClick = {}
        )
    }
}
//endregion

//region Preview Account Details
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
            onTabSelected = {},
            onEditTradeClick = {},
            onDeleteTradeClick = {}
        )
    }
}
//endregion
