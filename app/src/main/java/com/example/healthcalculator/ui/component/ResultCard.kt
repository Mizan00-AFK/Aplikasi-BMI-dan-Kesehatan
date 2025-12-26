package com.example.healthcalculator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthcalculator.model.CalculationResult
import com.example.healthcalculator.ui.theme.*
import java.util.Locale

@Composable
fun ResultCard(data: CalculationResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Hasil Analisa", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("BMI Anda", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = String.format(Locale.US, "%.2f", data.bmi),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = getCategoryColor(data.bmiCategory)
                    )
                ) {
                    Text(
                        text = data.bmiCategory,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Warnings
            Text("Kondisi Darah:", style = MaterialTheme.typography.labelLarge)
            data.healthWarnings.forEach { warning ->
                val isNormal = warning.contains("Normal")
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    if (!isNormal) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = warning,
                        color = if (isNormal) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Diet
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
                Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
                    Text("🥗 Rekomendasi Diet", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Target Kalori: ${data.calorieRecommendation}", fontWeight = FontWeight.SemiBold)
                    Text("Menu: ${data.menuRecommendation}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

private fun getCategoryColor(category: String): Color {
    return when (category) {
        "Kurus" -> ColorKurus
        "Normal" -> ColorNormal
        "Overweight" -> ColorOverweight
        "Obesitas" -> ColorObesitas
        else -> Color.Gray
    }
}