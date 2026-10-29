package au.edu.jcu.assessment.utilityapp.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.jcu.assessment.utilityapp.viewmodel.CurrencyUiState
import au.edu.jcu.assessment.utilityapp.viewmodel.CurrencyViewModel
import au.edu.jcu.assessment.utilityapp.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityScreen(
    viewModel: CurrencyViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Header
        Text(
            text = settingsViewModel.getString("title"),
            style = MaterialTheme.typography.headlineMedium
        )

        if (isLandscape) {
            // LANDSCAPE LAYOUT: Side-by-Side (Left: Inputs, Right: Result Card)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column: Amount Input & Dropdowns
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.amountInput,
                        onValueChange = { viewModel.onAmountChanged(it) },
                        label = { Text(settingsViewModel.getString("amount")) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CurrencyDropdown(
                            selectedCurrency = uiState.fromCurrency,
                            currencies = uiState.availableCurrencies,
                            onCurrencySelected = { viewModel.onFromCurrencyChanged(it) },
                            label = settingsViewModel.getString("from"),
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = { viewModel.swapCurrencies() },
                            modifier = Modifier.padding(horizontal = 2.dp)
                        ) {
                            Text("⇄", style = MaterialTheme.typography.titleLarge)
                        }

                        CurrencyDropdown(
                            selectedCurrency = uiState.toCurrency,
                            currencies = uiState.availableCurrencies,
                            onCurrencySelected = { viewModel.onToCurrencyChanged(it) },
                            label = settingsViewModel.getString("to"),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Right Column: Result Display Card
                ResultCard(
                    uiState = uiState,
                    settingsViewModel = settingsViewModel,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            // PORTRAIT LAYOUT: Stacked Vertically
            OutlinedTextField(
                value = uiState.amountInput,
                onValueChange = { viewModel.onAmountChanged(it) },
                label = { Text(settingsViewModel.getString("amount")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurrencyDropdown(
                    selectedCurrency = uiState.fromCurrency,
                    currencies = uiState.availableCurrencies,
                    onCurrencySelected = { viewModel.onFromCurrencyChanged(it) },
                    label = settingsViewModel.getString("from"),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { viewModel.swapCurrencies() },
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text("⇄", style = MaterialTheme.typography.titleLarge)
                }

                CurrencyDropdown(
                    selectedCurrency = uiState.toCurrency,
                    currencies = uiState.availableCurrencies,
                    onCurrencySelected = { viewModel.onToCurrencyChanged(it) },
                    label = settingsViewModel.getString("to"),
                    modifier = Modifier.weight(1f)
                )
            }

            ResultCard(
                uiState = uiState,
                settingsViewModel = settingsViewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ResultCard(
    uiState: CurrencyUiState,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
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
