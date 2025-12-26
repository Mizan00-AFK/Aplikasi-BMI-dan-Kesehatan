package com.example.healthcalculator.model

object HealthCalculator {

    fun getAgeCategory(age: Int): AgeCategory {
        return when (age) {
            in 0..12 -> AgeCategory.CHILD
            in 13..17 -> AgeCategory.TEEN
            in 18..59 -> AgeCategory.ADULT
            else -> AgeCategory.SENIOR
        }
    }

    fun calculate(
        age: Int,
        gender: Gender,
        heightCm: Double,
        weightKg: Double,
        cholesterol: Double,
        uricAcid: Double,
        bloodSugar: Double,
        sugarType: SugarTestType
    ): CalculationResult {

        val ageCategory = getAgeCategory(age)

        // 1. Hitung BMI
        val heightM = heightCm / 100.0
        val bmi = weightKg / (heightM * heightM)
        val bmiCategory = when {
            bmi < 18.5 -> "Kurus"
            bmi < 25.0 -> "Normal"
            bmi < 30.0 -> "Overweight"
            else -> "Obesitas"
        }

        // 2. Evaluasi Gula Darah
        val sugarStatus = evaluateBloodSugar(ageCategory, sugarType, bloodSugar)

        // 3. Evaluasi Kolesterol Total
        val cholesterolStatus = evaluateCholesterol(ageCategory, cholesterol)

        // 4. Evaluasi Asam Urat
        val uricStatus = evaluateUricAcid(ageCategory, gender, uricAcid)

        // Hasil Evaluasi
        val warnings = mutableListOf<String>()
        if (sugarStatus != "Normal") warnings.add("Gula Darah: $sugarStatus")
        if (cholesterolStatus != "Normal") warnings.add("Kolesterol: $cholesterolStatus")
        if (uricStatus != "Normal") warnings.add("Asam Urat: $uricStatus")
        if (warnings.isEmpty()) warnings.add("Semua Parameter Normal ✅")

        // Rekomendasi Diet
        val (calories, menu) = when (bmiCategory) {
            "Kurus" -> "±2500 kkal/hari" to "Nasi, ayam, telur, susu, buah"
            "Normal" -> "±2200 kkal/hari" to "Nasi merah, sayur, ikan, buah"
            "Overweight" -> "±1800 kkal/hari" to "Sayur, dada ayam, oatmeal"
            else -> "±1500 kkal/hari" to "Sayur rebus, buah, protein rendah lemak"
        }

        return CalculationResult(bmi, bmiCategory, warnings, calories, menu)
    }

    // --- LOGIKA RINCI TIAP KATEGORI ---

    private fun evaluateBloodSugar(category: AgeCategory, type: SugarTestType, value: Double): String {
        // Batas bawah
        if (value < 60) return "Bahaya: Hipoglikemia (< 60)"
        val range = when (type) {
            SugarTestType.FASTING -> when (category) {
                AgeCategory.CHILD, AgeCategory.TEEN -> 70.0..100.0
                AgeCategory.ADULT -> 70.0..99.0
                AgeCategory.SENIOR -> 70.0..110.0
            }
            SugarTestType.BEFORE_MEAL -> when (category) {
                AgeCategory.CHILD, AgeCategory.TEEN, AgeCategory.ADULT -> 70.0..130.0
                AgeCategory.SENIOR -> 80.0..140.0
            }
            SugarTestType.AFTER_MEAL -> when (category) {
                AgeCategory.CHILD, AgeCategory.TEEN -> 0.0..139.9 // < 140
                AgeCategory.ADULT, AgeCategory.SENIOR -> 0.0..199.9 // < 200
            }
        }
        return when {
            value < range.start -> "Rendah (< ${range.start})"
            value > range.endInclusive -> "Tinggi (> ${range.endInclusive.toInt()})"
            else -> "Normal"
        }
    }

    private fun evaluateCholesterol(category: AgeCategory, value: Double): String {
        // Range Kadar Kolesterol
        val maxLimit = when (category) {
            AgeCategory.CHILD, AgeCategory.TEEN -> 170.0
            AgeCategory.ADULT, AgeCategory.SENIOR -> 200.0
        }
        return if (value > maxLimit) "Tinggi (> ${maxLimit.toInt()})" else "Normal"
    }

    private fun evaluateUricAcid(category: AgeCategory, gender: Gender, value: Double): String {
        val range = when (category) {
            AgeCategory.CHILD -> 2.0..5.5
            AgeCategory.TEEN -> 2.5..6.5
            AgeCategory.ADULT, AgeCategory.SENIOR -> if (gender == Gender.FEMALE)
                2.4..6.0 else 3.4..7.0
        }
        return when {
            value < range.start -> "Rendah (< ${range.start})"
            value > range.endInclusive -> "Tinggi (> ${range.endInclusive})"
            else -> "Normal"
        }
    }
}