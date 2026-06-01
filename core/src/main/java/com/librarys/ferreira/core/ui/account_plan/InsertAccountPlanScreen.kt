package com.librarys.ferreira.core.ui.account_plan

import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
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

    InsertAccountPlanContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAccountNumberChange = viewModel::onAccountNumberChange,
        onPropFirmSelected = viewModel::onPropFirmSelected,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InsertAccountPlanContent(
    uiState: InsertAccountPlanUiState,
    onBackClick: () -> Unit,
    onAccountNumberChange: (String) -> Unit,
    onPropFirmSelected: (PropFirm) -> Unit,
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
    Log.d("Teste", "InsertAccountPlanContent: Iniciando")
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var showStartingDatePicker by remember { mutableStateOf(false) }
    var showBrokenDatePicker by remember { mutableStateOf(false) }

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

    Scaffold(
        topBar = {
            Surface(
                color = Color(0xFF0D1B3E),
                contentColor = Color.White
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
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
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D4ED8))
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
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
                        ItemTextFieldAccountPlan(label = "Saldo inicial", isRequired = true, placeholder = "Ex.: 10000.00", value = uiState.initialBalance, onValueChange = onInitialBalanceChange)
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ItemTextFieldAccountPlan(label = "Saldo atual", isRequired = true, placeholder = "Ex.: 10000.00", value = uiState.currentBalance, onValueChange = onCurrentBalanceChange)
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
                        .border(1.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(vertical = 4.dp)
                ) {
                    RuleItem("Meta de Lucro", "Defina a meta de lucro do plano", Icons.AutoMirrored.Filled.TrendingUp, Color(0xFFE0E7FF), Color(0xFF4338CA))
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.4f))
                    RuleItem("Perda Máxima", "Defina o limite de perda máxima", Icons.Default.Shield, Color(0xFFDCFCE7), Color(0xFF15803D))
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.4f))
                    RuleItem("Perda Diária Máxima", "Defina o limite de perda diária", Icons.Default.CalendarToday, Color(0xFFF3E8FF), Color(0xFF7E22CE))
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.4f))
                    RuleItem("Regras de Permanência", "Defina o tempo mínimo de operação", Icons.Default.AccessTime, Color(0xFFFFEDD5), Color(0xFFC2410C))
                }
            }

            item {
                TextButton(onClick = { /* Adicionar regra */ }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Adicionar regra", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1D4ED8)))
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
    trailingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            if (isRequired) Text(text = "*", color = Color.Red, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
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
                placeholder = { if (placeholder != null) Text(text = placeholder, color = Color.Gray.copy(alpha = 0.7f), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                trailingIcon = if (trailingIcon != null) { { Icon(trailingIcon, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Gray) } } else null,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.6f),
                    disabledBorderColor = Color.LightGray.copy(alpha = 0.6f),
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledPlaceholderColor = Color.Gray.copy(alpha = 0.7f),
                    disabledTrailingIconColor = Color.Gray,
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
            if (isRequired) Text(text = "*", color = Color.Red, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
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
                placeholder = { Text(text = placeholder, color = Color.Gray.copy(alpha = 0.7f)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.6f),
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
private fun RuleItem(title: String, subtitle: String, icon: ImageVector, backgroundColor: Color, iconColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .size(40.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
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
