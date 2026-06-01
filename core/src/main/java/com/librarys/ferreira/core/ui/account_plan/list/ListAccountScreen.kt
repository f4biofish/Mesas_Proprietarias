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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        onAccountClick = onAccountClick
    )
}

@Composable
fun ListAccountContent(
    uiState: ListAccountUiState,
    onAddAccountClick: () -> Unit,
    onAccountClick: (AccountInfo) -> Unit
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.accounts.isEmpty()) {
                EmptyAccountsView(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(marginDefault),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.accounts) { account ->
                        AccountItem(
                            account = account,
                            onClick = { onAccountClick(account) }
                        )
                    }
                }
            }
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
            containerColor = MaterialTheme.colorScheme.surface,
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

@Preview(showBackground = true)
@Composable
private fun ListAccountPreview() {
    AppTheme {
        ListAccountContent(
            uiState = ListAccountUiState(
                accounts = listOf(
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
            ),
            onAddAccountClick = {},
            onAccountClick = {}
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
            onAccountClick = {}
        )
    }
}
