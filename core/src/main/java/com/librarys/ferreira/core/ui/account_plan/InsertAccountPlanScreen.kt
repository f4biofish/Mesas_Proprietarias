package com.librarys.ferreira.core.ui.account_plan

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.librarys.ferreira.core.domain.model.enums.DrawnDownTypes
import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.ui.theme.AppTheme
import com.librarys.ferreira.core.ui.theme.marginDefault
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertAccountPlanScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    // Estados para os campos da AccountInfo
    var accountNumber by remember { mutableStateOf("") }
    var selectedPropFirm by remember { mutableStateOf<PropFirm?>(null) }
    var accountName by remember { mutableStateOf("") }
    var dayStarting by remember { mutableStateOf(Date()) }
    var dayBroken by remember { mutableStateOf<Date?>(null) }
    var initialBalance by remember { mutableStateOf("") }
    var currentBalance by remember { mutableStateOf("") }
    var drawdownType by remember { mutableStateOf<DrawnDownTypes?>(null) }
    var maxDrawdown by remember { mutableStateOf("") }
    var dailyLossLimit by remember { mutableStateOf("") }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            Surface(
                color = Color(0xFF0D1B3E), // Azul escuro do cabeçalho
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D4ED8))
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Salvar Conta",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
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
                    value = accountNumber,
                    onValueChange = { accountNumber = it }
                )
            }

            item {
                ItemDropdownAccountPlan(
                    label = "Mesa Proprietária (Prop Firm)",
                    isRequired = true,
                    placeholder = "Selecione a mesa proprietária",
                    selectedOption = selectedPropFirm?.name,
                    options = PropFirm.entries.map { it.name },
                    onOptionSelected = { selectedPropFirm = PropFirm.valueOf(it) }
                )
            }

            item {
                ItemTextFieldAccountPlan(
                    label = "Nome da conta",
                    isRequired = true,
                    placeholder = "Ex.: My Evaluation 1",
                    value = accountName,
                    onValueChange = { accountName = it }
                )
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(marginDefault)) {
                    Box(modifier = Modifier.weight(1f)) {
                        ItemTextFieldAccountPlan(
                            label = "Data de início (Day Starting)",
                            isRequired = true,
                            value = dateFormat.format(dayStarting),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = Icons.Default.CalendarToday
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ItemTextFieldAccountPlan(
                            label = "Data de quebra (Day Broken)",
                            placeholder = "Selecione (opcional)",
                            value = dayBroken?.let { dateFormat.format(it) } ?: "",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = Icons.Default.CalendarToday
                        )
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(marginDefault)) {
                    Box(modifier = Modifier.weight(1f)) {
                        ItemTextFieldAccountPlan(label = "Saldo inicial", isRequired = true, placeholder = "Ex.: 10000.00", value = initialBalance, onValueChange = { initialBalance = it })
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ItemTextFieldAccountPlan(label = "Saldo atual", isRequired = true, placeholder = "Ex.: 10000.00", value = currentBalance, onValueChange = { currentBalance = it })
                    }
                }
            }

            item {
                ItemDropdownAccountPlan(
                    label = "Tipo de Drawdown",
                    isRequired = true,
                    placeholder = "Selecione o tipo",
                    selectedOption = drawdownType?.name,
                    options = DrawnDownTypes.entries.map { it.name },
                    onOptionSelected = { drawdownType = DrawnDownTypes.valueOf(it) }
                )
            }

            item {
                ItemTextFieldAccountPlan(
                    label = "Drawdown máximo permitido",
                    isRequired = true,
                    placeholder = "Ex.: 1000.00",
                    value = maxDrawdown,
                    onValueChange = { maxDrawdown = it }
                )
            }

            item {
                ItemTextFieldAccountPlan(
                    label = "Limite de perda diária (Daily Loss Limit)",
                    placeholder = "Ex.: 500.00 (opcional)",
                    value = dailyLossLimit,
                    onValueChange = { dailyLossLimit = it }
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
    trailingIcon: ImageVector? = null
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            if (isRequired) Text(text = "*", color = Color.Red, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { if (placeholder != null) Text(text = placeholder, color = Color.Gray.copy(alpha = 0.7f)) },
            trailingIcon = if (trailingIcon != null) { { Icon(trailingIcon, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Gray) } } else null,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray.copy(alpha = 0.6f)),
            singleLine = true
        )
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
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedOption ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(text = placeholder, color = Color.Gray.copy(alpha = 0.7f)) },
                trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray.copy(alpha = 0.6f))
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = { onOptionSelected(option); expanded = false })
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
private fun InsertAccountPlanScreen() {
    AppTheme() {
        Surface(color = MaterialTheme.colorScheme.surface) {
            InsertAccountPlanScreen {  }
        }
    }
}