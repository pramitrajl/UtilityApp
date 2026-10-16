package au.edu.jcu.assessment.utilityapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.jcu.assessment.utilityapp.viewmodel.CurrencyViewModel
import au.edu.jcu.assessment.utilityapp.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityScreen(
    viewModel: CurrencyViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val settingsState by settingsViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Translated Title Header
        Text(
            text = settingsViewModel.getString("title"),
            style = MaterialTheme.typography.headlineMedium
        )

        // Translated Amount Input Field
        OutlinedTextField(
            value = uiState.amountInput,
            onValueChange = { viewModel.onAmountChanged(it) },
            label = { Text(settingsViewModel.getString("amount")) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Currency Selectors Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // From Currency Dropdown
            CurrencyDropdown(
                selectedCurrency = uiState.fromCurrency,
                currencies = uiState.availableCurrencies,
                onCurrencySelected = { viewModel.onFromCurrencyChanged(it) },
                label = settingsViewModel.getString("from"),
                modifier = Modifier.weight(1f)
            )

            // Swap Button (⇄)
            IconButton(
                onClick = { viewModel.swapCurrencies() },
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    text = "⇄",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // To Currency Dropdown
            CurrencyDropdown(
                selectedCurrency = uiState.toCurrency,
                currencies = uiState.availableCurrencies,
                onCurrencySelected = { viewModel.onToCurrencyChanged(it) },
                label = settingsViewModel.getString("to"),
                modifier = Modifier.weight(1f)
            )
        }

        // Conversion Result Display Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator()
                        Text(settingsViewModel.getString("fetching"))
                    }

                    uiState.errorMessage != null -> {
                        Text(
                            text = uiState.errorMessage ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    else -> {
                        Text(
                            text = settingsViewModel.getString("result"),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = uiState.convertedResult,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropdown(
    selectedCurrency: String,
    currencies: List<String>,
    onCurrencySelected: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCurrency,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency) },
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}