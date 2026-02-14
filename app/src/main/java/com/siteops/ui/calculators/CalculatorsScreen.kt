package com.siteops.ui.calculators

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorsScreen(viewModel: CalculatorsViewModel = viewModel()) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Engineering Calculators") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Duct Sizer Card
            item {
                CalculatorCard(title = "Duct Sizer") {
                    CalculatorInput(label = "CFM", value = viewModel.ductCfm, onValueChange = { viewModel.ductCfm = it })
                    CalculatorInput(label = "Friction Loss (in/100ft)", value = viewModel.ductFriction, onValueChange = { viewModel.ductFriction = it })
                    Button(onClick = { viewModel.calculateDuct() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Calculate Diameter")
                    }
                    if (viewModel.ductResult > 0) {
                        Text("Required Diameter: ${"%.2f".format(viewModel.ductResult)} in", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            // DOAS Sizer Card
            item {
                CalculatorCard(title = "DOAS Sizer (ASHRAE 62.1)") {
                    CalculatorInput(label = "Floor Area (sq ft)", value = viewModel.doasArea, onValueChange = { viewModel.doasArea = it })
                    CalculatorInput(label = "Occupancy", value = viewModel.doasOcc, onValueChange = { viewModel.doasOcc = it })
                    Button(onClick = { viewModel.calculateDOAS() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Calculate OA")
                    }
                    if (viewModel.doasResult > 0) {
                        Text("Required OA: ${"%.1f".format(viewModel.doasResult)} CFM", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            // Psychrometric Card
            item {
                CalculatorCard(title = "Psychrometric Tool") {
                    CalculatorInput(label = "Dry Bulb (F)", value = viewModel.psychDb, onValueChange = { viewModel.psychDb = it })
                    CalculatorInput(label = "Relative Humidity (%)", value = viewModel.psychRh, onValueChange = { viewModel.psychRh = it })
                    Button(onClick = { viewModel.calculatePsych() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Calculate Properties")
                    }
                    if (viewModel.enthalpyResult != 0.0) {
                        Column {
                            Text("Enthalpy (h): ${"%.2f".format(viewModel.enthalpyResult)} BTU/lb")
                            Text("Dew Point: ${"%.1f".format(viewModel.dewPointResult)} °F")
                        }
                    }
                }
            }

            // Cooling Load Card
            item {
                CalculatorCard(title = "Cooling Load Quick-Check") {
                    CalculatorInput(label = "Area (sq ft)", value = viewModel.coolingArea, onValueChange = { viewModel.coolingArea = it })
                    Button(onClick = { viewModel.calculateCooling() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Calculate Tons")
                    }
                    if (viewModel.coolingResult > 0) {
                        Text("Estimated Load: ${"%.1f".format(viewModel.coolingResult)} Tons", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
fun CalculatorInput(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true
    )
}
