package uz.neopulsar.sumconvert.presentation.converter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import uz.neopulsar.sumconvert.domain.model.Currency
import uz.neopulsar.sumconvert.presentation.components.CurrencySelectionDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(
    onBackClick: () -> Unit
    ) {
    val viewModel: ConverterViewModel = koinInject()
    val state by viewModel.state.collectAsState()

    var showFromDialog by remember { mutableStateOf(false) }
    var showToDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Valyuta") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadRates() }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        }
    ) { padding ->

        Box(modifier = Modifier.padding(padding).fillMaxSize()) {

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Error Message
                    state.errorMessage?.let { error ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = error,
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    // FROM CARD
                    CurrencyInputCard(
                        label = "Sotaman (I sell)",
                        currency = state.fromCurrency,
                        amount = state.inputAmount,
                        onAmountChange = { viewModel.onAmountChanged(it) },
                        onCurrencyClick = { showFromDialog = true },
                        readOnly = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // SWAP BUTTON
                    IconButton(
                        onClick = { viewModel.swapCurrencies() },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                    ) {
                        Icon(Icons.Default.SwapVert, "Swap", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // TO CARD
                    CurrencyInputCard(
                        label = "Olaman (I buy)",
                        currency = state.toCurrency,
                        amount = state.resultAmount,
                        onAmountChange = {},
                        onCurrencyClick = { showToDialog = true },
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Info Block
                    if(state.fromCurrency != null && state.toCurrency != null) {
                        Text(
                            text = "Markaziy Bank kursi bo'yicha",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "So'nggi yangilanish: ${state.fromCurrency?.date ?: "Noma'lum"}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            // Dialogs
            if (showFromDialog) {
                CurrencySelectionDialog(
                    rates = state.rates,
                    onDismiss = { showFromDialog = false },
                    onSelect = { viewModel.onFromChanged(it) }
                )
            }
            if (showToDialog) {
                CurrencySelectionDialog(
                    rates = state.rates,
                    onDismiss = { showToDialog = false },
                    onSelect = { viewModel.onToChanged(it) }
                )
            }
        }
    }
}

@Composable
fun CurrencyInputCard(
    label: String,
    currency: Currency?,
    amount: String,
    onAmountChange: (String) -> Unit,
    onCurrencyClick: () -> Unit,
    readOnly: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Input Field
                OutlinedTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.headlineMedium,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    readOnly = readOnly,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Currency Button
                Button(
                    onClick = onCurrencyClick,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = currency?.code ?: "---")
                    Icon(Icons.Default.ArrowDownward, null, modifier = Modifier.size(16.dp))
                }
            }

            // Name of currency (e.g. AQSH Dollari)
            Text(
                text = currency?.name ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}