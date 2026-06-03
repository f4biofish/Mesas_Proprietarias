package com.librarys.ferreira.core.ui.account_plan.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.librarys.ferreira.core.R
import com.librarys.ferreira.core.domain.model.enums.AccountStage
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.model.AccountInfo
import com.librarys.ferreira.core.ui.theme.AppTheme
import com.librarys.ferreira.core.ui.theme.marginDefault
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ListAccountScreen(
    viewModel: ListAccountViewModel = hiltViewModel(),
    onAddAccountClick: () -> Unit = {},
    onAccountClick: (AccountInfo) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ListAccountContent(
        uiState = uiState,
        onAddAccountClick = onAddAccountClick,
        onAccountClick = onAccountClick,
        onFilterSelected = viewModel::onFilterSelected,
        onDeleteAccount = viewModel::onDeleteAccountClick,
        onDeleteConfirm = viewModel::onDeleteConfirm,
        onDeleteDismiss = viewModel::onDeleteDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAccountContent(
    uiState: ListAccountUiState,
    onAddAccountClick: () -> Unit,
    onAccountClick: (AccountInfo) -> Unit,
    onFilterSelected: (AccountFilter) -> Unit,
    onDeleteAccount: (AccountInfo) -> Unit,
    onDeleteConfirm: () -> Unit,
    onDeleteDismiss: () -> Unit
) {
    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(marginDefault)
                ) {
                    Text(
                        text = stringResource(R.string.minhas_contas),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                    Text(
                        text = stringResource(R.string.gerencie_seus_planos),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAccountClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.adicionar_conta))
            }
        },
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Text(
                    text = stringResource(R.string.total_contas, uiState.filteredAccounts.size),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterRow(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = onFilterSelected,
                modifier = Modifier.padding(horizontal = marginDefault, vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.filteredAccounts.isEmpty()) {
                    EmptyAccountsView(modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = marginDefault,
                            end = marginDefault,
                            top = 4.dp,
                            bottom = 80.dp // Extra padding for FAB
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.filteredAccounts,
                            key = { it.id }
                        ) { account ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    if (value == SwipeToDismissBoxValue.StartToEnd) {
                                        onDeleteAccount(account)
                                        false // Return false to not dismiss immediately, let the dialog handle it
                                    } else {
                                        false
                                    }
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = {
                                    SwipeBackground(dismissState)
                                },
                                enableDismissFromEndToStart = false,
                                content = {
                                    AccountItem(
                                        account = account,
                                        onClick = { onAccountClick(account) }
                                    )
                                }
                            )
                        }
                    }
                }

                uiState.accountToDelete?.let { account ->
                    DeleteAccountConfirmationDialog(
                        accountName = account.accountName,
                        onConfirm = onDeleteConfirm,
                        onDismiss = onDeleteDismiss
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteAccountConfirmationDialog(
    accountName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.titulo_excluir_conta))
        },
        text = {
            Text(text = stringResource(R.string.mensagem_excluir_conta, accountName))
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(text = stringResource(R.string.confirmar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancelar))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterRow(
    selectedFilter: AccountFilter,
    onFilterSelected: (AccountFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AccountFilter.entries.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = when (filter) {
                            AccountFilter.ALL -> stringResource(R.string.filtro_todas)
                            AccountFilter.ACTIVE -> stringResource(R.string.filtro_ativas)
                            AccountFilter.BROKEN -> stringResource(R.string.filtro_quebradas)
                        }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountItem(
    account: AccountInfo,
    onClick: () -> Unit
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "US"))

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.accountName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "${account.propFirm.name} • ${account.accountStage.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Saldo: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currencyFormatter.format(account.currentBalance),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (account.currentBalance >= account.initialBalance) {
                                colorResource(R.color.positive_green)
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )
                    )
                }
            }

            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun EmptyAccountsView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(marginDefault),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.nenhuma_conta),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(R.string.adicione_a_primeira_conta),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.errorContainer
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp)
            .background(color, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.excluir),
                modifier = Modifier.padding(start = 16.dp),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListAccountPreview() {
    val accounts = listOf(
        AccountInfo(
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
        AccountInfo(
            numberAccount = "789012",
            propFirm = PropFirm.YLOS_TRADING,
            accountStage = AccountStage.FUNDED,
            accountName = "Expert 100K",
            initialBalance = 100000.0,
            currentBalance = 98500.0,
            typeDrawdown = DrawnDownTypes.EOD,
            maxDrawdownAmmount = 3000.0,
            rulesPropFirm = emptyList()
        )
    )
    AppTheme {
        ListAccountContent(
            uiState = ListAccountUiState(
                accounts = accounts,
                filteredAccounts = accounts
            ),
            onAddAccountClick = {},
            onAccountClick = {},
            onFilterSelected = {},
            onDeleteAccount = {},
            onDeleteConfirm = {},
            onDeleteDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListAccountEmptyPreview() {
    AppTheme {
        ListAccountContent(
            uiState = ListAccountUiState(),
            onAddAccountClick = {},
            onAccountClick = {},
            onFilterSelected = {},
            onDeleteAccount = {},
            onDeleteConfirm = {},
            onDeleteDismiss = {}
        )
    }
}
