package uz.neopulsar.sumconvert.presentation.generic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import uz.neopulsar.sumconvert.data.local.GenericUnit

data class GenericScreen(val categoryId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<GenericViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(categoryId) { viewModel.initCategory(categoryId) }

        GenericScreenContent(
            state = state,
            onEvent = viewModel::onAmountChanged,
            onBackClick = { navigator.pop() },
            onSwap = viewModel::swap,
            onFromChanged = viewModel::onFromChanged,
            onToChanged = viewModel::onToChanged
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenericScreenContent(
    state: GenericUiState,
    onEvent: (String) -> Unit,
    onBackClick: () -> Unit,
    onSwap: () -> Unit,
    onFromChanged: (GenericUnit) -> Unit,
    onToChanged: (GenericUnit) -> Unit
) {
    var showFromDialog by remember { mutableStateOf(false) }
    var showToDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(state.category.nameUz) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // FROM
            GenericInputCard(
                label = "Dan",
                unit = state.fromUnit,
                amount = state.input,
                readOnly = false,
                onValueChange = onEvent,
                onClick = { showFromDialog = true }
            )

            Spacer(modifier = Modifier.height(8.dp))
            IconButton(
                onClick = onSwap,
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
            ) {
                Icon(Icons.Default.SwapVert, null)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // TO
            GenericInputCard(
                label = "Ga",
                unit = state.toUnit,
                amount = state.output,
                readOnly = true,
                onValueChange = {},
                onClick = { showToDialog = true }
            )
        }

        if (showFromDialog) {
            GenericSelectionDialog(state.units, { showFromDialog = false }, onFromChanged)
        }
        if (showToDialog) {
            GenericSelectionDialog(state.units, { showToDialog = false }, onToChanged)
        }
    }
}

@Composable
fun GenericInputCard(
    label: String,
    unit: GenericUnit?,
    amount: String,
    readOnly: Boolean,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    readOnly = readOnly,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    textStyle = MaterialTheme.typography.headlineMedium,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    )
                )
                Button(onClick = onClick) {
                    Text(unit?.id ?: "-")
                    Icon(Icons.Default.ArrowDownward, null, modifier = Modifier.size(16.dp))
                }
            }
            Text(unit?.name ?: "", style = MaterialTheme.typography.bodySmall)
        }
    }
}