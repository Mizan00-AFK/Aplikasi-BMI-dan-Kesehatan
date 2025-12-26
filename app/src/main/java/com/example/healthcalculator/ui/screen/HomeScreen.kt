package com.example.healthcalculator.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcalculator.model.CalculationResult
import com.example.healthcalculator.model.Gender
import com.example.healthcalculator.model.HealthCalculator
import com.example.healthcalculator.model.SugarTestType
import com.example.healthcalculator.ui.components.GenderSelection
import com.example.healthcalculator.ui.components.HealthInputRow
import com.example.healthcalculator.ui.components.ResultCard
import com.example.healthcalculator.ui.components.SugarTypeDropdown
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // State Input
    var ageInput by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(Gender.MALE) }
    var heightInput by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("") }

    var cholesterolInput by remember { mutableStateOf("") }
    var uricInput by remember { mutableStateOf("") }

    var selectedSugarType by remember { mutableStateOf(SugarTestType.FASTING) }
    var sugarInput by remember { mutableStateOf("") }

    // State Output
    var result by remember { mutableStateOf<CalculationResult?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Health & Diet Planner") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Data Pribadi",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }

            // --- Input Data Diri ---
            item {
                HealthInputRow("Umur (Tahun)", ageInput, { ageInput = it })
                GenderSelection(selectedGender) { selectedGender = it }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        HealthInputRow("Tinggi (cm)", heightInput, { heightInput = it })
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        HealthInputRow("Berat (kg)", weightInput, { weightInput = it })
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Hasil Lab",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }

            // --- Input Data Lab/Kesehatan ---
            item {
                SugarTypeDropdown(selectedSugarType) { selectedSugarType = it }
                HealthInputRow("Gula Darah (mg/dL)", sugarInput, { sugarInput = it })
                HealthInputRow("Kolesterol Total (mg/dL)", cholesterolInput, { cholesterolInput = it })
                HealthInputRow("Asam Urat (mg/dL)", uricInput, { uricInput = it }, ImeAction.Done)
            }

            // --- Tombol Hitung ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        // Validasi
                        val age = ageInput.toIntOrNull()
                        val h = heightInput.toDoubleOrNull()
                        val w = weightInput.toDoubleOrNull()
                        val chol = cholesterolInput.toDoubleOrNull()
                        val uric = uricInput.toDoubleOrNull()
                        val sugar = sugarInput.toDoubleOrNull()

                        if (age != null && h != null && w != null && chol != null && uric != null && sugar != null) {
                            result = HealthCalculator.calculate(
                                age, selectedGender, h, w, chol, uric, sugar, selectedSugarType
                            )
                        } else {
                            scope.launch { snackbarHostState.showSnackbar("Mohon lengkapi semua data dengan benar.") }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Analisa Kesehatan", fontSize = 16.sp)
                }
            }

            // --- Hasil ---
            item {
                result?.let { res ->
                    Spacer(modifier = Modifier.height(24.dp))
                    ResultCard(res)
                }
            }
        }
    }
}