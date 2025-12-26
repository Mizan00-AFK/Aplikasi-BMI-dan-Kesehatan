package com.example.healthcalculator.model

data class CalculationResult(
    val bmi: Double,
    val bmiCategory: String,
    val healthWarnings: List<String>,
    val calorieRecommendation: String,
    val menuRecommendation: String
)