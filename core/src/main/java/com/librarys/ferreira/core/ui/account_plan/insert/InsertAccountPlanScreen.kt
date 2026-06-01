package com.librarys.ferreira.core.ui.account_plan.insert

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.librarys.ferreira.core.R
import com.librarys.ferreira.core.domain.model.enums.AccountStage
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.rules.AccountRules
import com.librarys.ferreira.core.domain.model.template.AccountPlan
import com.librarys.ferreira.core.ui.theme.AppTheme
import com.librarys.ferreira.core.ui.theme.marginDefault
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InsertAccountPlanScreen(
    viewModel: InsertAccountPlanViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(uiState.isSaved) {
        if(uiState.isSaved){
            Toast.makeText(context, "Conta cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        if (!uiState.errorMessage.isNullOrEmpty())
            Toast.makeText(context, uiState.errorMessage ?: "", Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if(uiState.isLoading){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center).size(80.dp),
            )
        }

        InsertAccountPlanContent(
            uiState = uiState,
            onBackClick = onBackClick,
            onAccountNumberChange = viewModel::onAccountNumberChange,
            onPropFirmSelected = viewModel::onPropFirmSelected,
            onAccountStageSelected = viewModel::onAccountStageSelected,
            onAccountPlanSelected = viewModel::onAccountPlanSelected,
            onInitialBalanceChange = viewModel::onInitialBalanceChange,
            onCurrentBalanceChange = viewModel::onCurrentBalanceChange,
            onDrawdownTypeSelected = viewModel::onDrawdownTypeSelected,
            onMaxDrawdownChange = viewModel::onMaxDrawdownChange,
            onDailyLossLimitChange = viewModel::onDailyLossLimitChange,
            onDayStartingChange = viewModel::onDayStartingChange,
            onDayBrokenChange = viewModel::onDayBrokenChange,
            onSaveClick = viewModel::onSaveClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InsertAccountPlanContent(
    uiState: InsertAccountPlanUiState,
    onBackClick: () -> Unit,
    onAccountNumberChange: (String) -> Unit,
    onPropFirmSelected: (PropFirm) -> Unit,
    onAccountStageSelected: (AccountStage) -> Unit,
    onAccountPlanSelected: (AccountPlan) -> Unit,
    onInitialBalanceChange: (String) -> Unit,
    onCurrentBalanceChange: (String) -> Unit,
    onDrawdownTypeSelected: (DrawnDownTypes) -> Unit,
    onMaxDrawdownChange: (String) -> Unit,
    onDailyLossLimitChange: (String) -> Unit,
    onDayStartingChange: (Date) -> Unit,
    onDayBrokenChange: (Date?) -> Unit,
    onSaveClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var showStartingDatePicker by remember { mutableStateOf(false) }
    var showBrokenDatePicker by remember { mutableStateOf(false) }
    var selectedRule by remember { mutableStateOf<AccountRules?>(null) }

    if (showStartingDatePicker) {
        AccountPlanDatePicker(
            initialDate = uiState.dayStarting,
            onDateSelected = {
                onDayStartingChange(it)
                showStartingDatePicker = false
            },
            onDismiss = { showStartingDatePicker = false }
        )
    }

    if (showBrokenDatePicker) {
        AccountPlanDatePicker(
            initialDate = uiState.dayBroken ?: Date(),
            onDateSelected = {
                onDayBrokenChange(it)
                showBrokenDatePicker = false
            },
            onDismiss = { showBrokenDatePicker = false }
        )
    }

    if (selectedRule != null) {
        RuleDetailDialog(
            rule = selectedRule!!,
            onDismiss = { selectedRule = null }
        )
    }

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
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                        Text(
                            text = "Nova Conta",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                    }
                    Text(
                        text = "Preencha os dados do plano da mesa proprietária",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier.padding(start = 52.dp)
                    )
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(marginDefault)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = onSaveClick,
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Salvar Conta",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = marginDefault),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                ItemTextFieldAccountPlan(
                    label = "Número da conta",
                    isRequired = true,
                    placeholder = "Ex.: 1234567",
                    value = uiState.accountNumber,
                    onValueChange = onAccountNumberChange
                )
            }

            item {
                ItemDropdownAccountPlan(
                    label = "Mesa Proprietária (Prop Firm)",
                    isRequired = true,
                    placeholder = "Selecione a mesa proprietária",
                    selectedOption = uiState.selectedPropFirm?.name,
                    options = PropFirm.entries.map { it.name },
                    onOptionSelected = { onPropFirmSelected(PropFirm.valueOf(it)) }
                )
            }

            item {
                ItemDropdownAccountPlan(
                    label = "Estágio da conta",
                    isRequired = true,
                    placeholder = "Selecione o estágio",
                    selectedOption = uiState.selectedAccountStage?.name,
                    options = AccountStage.entries.map { it.name },
                    onOptionSelected = { onAccountStageSelected(AccountStage.valueOf(it)) }
                )
            }

            item {
                ItemDropdownAccountPlan(
                    label = "Nome do plano da conta",
                    isRequired = true,
                    placeholder = if (uiState.selectedPropFirm == null) "Selecione primeiro a mesa" else "Selecione o plano",
                    selectedOption = uiState.accountName,
                    options = uiState.availableAccountPlans.map { it.name },
                    onOptionSelected = { name ->
                        uiState.availableAccountPlans.find { it.name == name }?.let {
                            onAccountPlanSelected(it)
                        }
                    }
                )
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(marginDefault)) {
                    Box(modifier = Modifier.weight(1f)) {
                        ItemTextFieldAccountPlan(
                            label = "Data de início",
                            isRequired = true,
                            value = dateFormat.format(uiState.dayStarting),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = Icons.Default.CalendarToday,
                            onClick = { showStartingDatePicker = true }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ItemTextFieldAccountPlan(
                            label = "Data de quebra",
                            placeholder = "Selecione (opcional)",
                            value = uiState.dayBroken?.let { dateFormat.format(it) } ?: "",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = Icons.Default.CalendarToday,
                            onClick = { showBrokenDatePicker = true }
                        )
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(marginDefault)) {
                    Box(modifier = Modifier.weight(1f)) {
                        ItemTextFieldAccountPlan(label = "Saldo inicial", isRequired = true, readOnly = true, placeholder = "Ex.: 10000.00", value = uiState.initialBalance, onValueChange = onInitialBalanceChange, prefix = "$")
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ItemTextFieldAccountPlan(label = "Saldo atual", isRequired = true, placeholder = "Ex.: 10000.00", value = uiState.currentBalance, onValueChange = onCurrentBalanceChange, prefix = "$")
                    }
                }
            }

            item {
                ItemDropdownAccountPlan(
                    label = "Tipo de Drawdown",
                    isRequired = true,
                    placeholder = "Selecione o tipo",
                    selectedOption = uiState.drawdownType?.name,
                    options = DrawnDownTypes.entries.map { it.name },
                    onOptionSelected = { onDrawdownTypeSelected(DrawnDownTypes.valueOf(it)) }
                )
            }

            item {
                ItemTextFieldAccountPlan(
                    label = "Drawdown máximo permitido",
                    isRequired = true,
                    placeholder = "Ex.: 1000.00",
                    prefix = "$",
                    readOnly = true,
                    value = uiState.maxDrawdown,
                    onValueChange = onMaxDrawdownChange
                )
            }

            item {
                ItemTextFieldAccountPlan(
                    label = "Limite de perda diária",
                    placeholder = "Ex.: 500.00 (opcional)",
                    value = uiState.dailyLossLimit,
                    onValueChange = onDailyLossLimitChange
                )
            }

            item {
                Text(
                    text = "Regras da mesa proprietária",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                        .padding(vertical = 4.dp)
                ) {
                    val rulesToDisplay = mutableListOf<@Composable () -> Unit>()

                    if (uiState.metaProfit.isNotEmpty()) {
                        rulesToDisplay.add {
                            RuleItem(
                                title = "Meta de Lucro",
                                subtitle = "Alcance o lucro de $${uiState.metaProfit}",
                                icon = Icons.AutoMirrored.Filled.TrendingUp,
                                backgroundColor = colorResource(R.color.rule_consistency_bg),
                                iconColor = colorResource(R.color.rule_consistency_icon),
                                onClick = { }
                            )
                        }
                    }

                    if (uiState.maxDrawdown.isNotEmpty()) {
                        rulesToDisplay.add {
                            RuleItem(
                                title = "Perda Máxima",
                                subtitle = "Limite total de perda de $${uiState.maxDrawdown}",
                                icon = Icons.Default.Shield,
                                backgroundColor = colorResource(R.color.rule_news_bg),
                                iconColor = colorResource(R.color.rule_news_icon),
                                onClick = { }
                            )
                        }
                    }

                    if (uiState.dailyLossLimit.isNotEmpty()) {
                        rulesToDisplay.add {
                            RuleItem(
                                title = "Perda Diária Máxima",
                                subtitle = "Limite diário de $${uiState.dailyLossLimit}",
                                icon = Icons.Default.CalendarToday,
                                backgroundColor = colorResource(R.color.rule_days_bg),
                                iconColor = colorResource(R.color.rule_days_icon),
                                onClick = { }
                            )
                        }
                    }

                    uiState.rules.forEach { rule ->
                        val info = getRuleInfo(rule)
                        rulesToDisplay.add {
                            RuleItem(
                                title = info.title,
                                subtitle = info.subtitle,
                                icon = info.icon,
                                backgroundColor = colorResource(info.backgroundColorRes),
                                iconColor = colorResource(info.iconColorRes),
                                onClick = { selectedRule = rule }
                            )
                        }
                    }

                    if (rulesToDisplay.isEmpty()) {
                        Text(
                            text = "Nenhuma regra adicional configurada",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        rulesToDisplay.forEachIndexed { index, ruleComposable ->
                            ruleComposable()
                            if (index < rulesToDisplay.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun ItemTextFieldAccountPlan(
    label: String,
    isRequired: Boolean = false,
    placeholder: String? = null,
    value: String = "",
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
    prefix: String? = null,
    trailingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            if (isRequired) Text(text = "*", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (onClick != null) Modifier.clickable { onClick() } else Modifier
                )
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = readOnly,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { if (placeholder != null) Text(text = placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                trailingIcon = if (trailingIcon != null) { { Icon(trailingIcon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant) } } else null,
                shape = RoundedCornerShape(10.dp),
                prefix = {
                    if(!prefix.isNullOrEmpty()) {
                        Text(
                            text = prefix,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                singleLine = true,
                enabled = onClick == null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemDropdownAccountPlan(
    label: String,
    isRequired: Boolean = false,
    placeholder: String,
    selectedOption: String?,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            if (isRequired) Text(text = "*", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedOption ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(text = placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Composable
private fun RuleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(backgroundColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
    }
}

@Preview
@Composable
private fun InsertAccountPlanScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            InsertAccountPlanContent(
                uiState = InsertAccountPlanUiState(),
                onBackClick = {},
                onAccountNumberChange = {},
                onPropFirmSelected = {},
                onAccountStageSelected = {},
                onAccountPlanSelected = {},
                onInitialBalanceChange = {},
                onCurrentBalanceChange = {},
                onDrawdownTypeSelected = {},
                onMaxDrawdownChange = {},
                onDailyLossLimitChange = {},
                onDayStartingChange = {},
                onDayBrokenChange = {},
                onSaveClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountPlanDatePicker(
    initialDate: Date,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.time
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(Date(it))
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun RuleDetailDialog(
    rule: AccountRules,
    onDismiss: () -> Unit
) {
    val info = getRuleInfo(rule)
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(info.icon, contentDescription = null, modifier = Modifier.size(40.dp), tint = colorResource(info.iconColorRes)) },
        title = { Text(text = info.title, fontWeight = FontWeight.Bold) },
        text = { Text(text = info.description) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}

private data class RuleUiInfo(
    val title: String,
    val description: String,
    val subtitle: String,
    val icon: ImageVector,
    val backgroundColorRes: Int,
    val iconColorRes: Int
)

private fun getRuleInfo(rule: AccountRules): RuleUiInfo {
    return when (rule) {
        is AccountRules.ConsistencyRule -> RuleUiInfo(
            title = "Regra de Consistência",
            description = "${rule.description}\n\nO percentual máximo que um único dia de operação pode ter no lucro total da conta é de ${rule.maxPercentage}%.",
            subtitle = "Máximo ${rule.maxPercentage}% por dia",
            icon = Icons.AutoMirrored.Filled.TrendingUp,
            backgroundColorRes = R.color.rule_consistency_bg,
            iconColorRes = R.color.rule_consistency_icon
        )

        is AccountRules.NewsRestrictionRule -> RuleUiInfo(
            title = "Restrição de Notícias",
            description = "É proibido operar durante notícias de alto impacto.\n\nVocê deve estar fora de operação ${rule.minutesBefore} minutos antes e aguardar ${rule.minutesAfter} minutos após o anúncio.",
            subtitle = "${rule.minutesBefore} min antes e ${rule.minutesAfter} min depois",
            icon = Icons.Default.Notifications,
            backgroundColorRes = R.color.rule_news_bg,
            iconColorRes = R.color.rule_news_icon
        )

        is AccountRules.MinimumTradingDaysRule -> RuleUiInfo(
            title = "Dias Mínimos de Operação",
            description = "Para solicitar saque, você deve cumprir:\n\n- ${rule.daysTrading} dias mínimos de operação.\n- ${rule.daysWin} dias com lucro superior a $${rule.profitWin}.",
            subtitle = "Mínimo ${rule.daysTrading} dias de trade",
            icon = Icons.Default.CalendarToday,
            backgroundColorRes = R.color.rule_days_bg,
            iconColorRes = R.color.rule_days_icon
        )
    }
}
