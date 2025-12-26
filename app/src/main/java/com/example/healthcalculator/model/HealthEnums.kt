package com.example.healthcalculator.model

enum class Gender(val label: String) {
    MALE("Laki-laki"),
    FEMALE("Perempuan")
}

enum class SugarTestType(val label: String) {
    FASTING("Puasa (8-9 Jam)"),
    BEFORE_MEAL("Sebelum Makan"),
    AFTER_MEAL("2 Jam Setelah Makan / Sewaktu")
}

enum class AgeCategory(val label: String) {
    CHILD("Anak-anak (0-12 th)"),
    TEEN("Remaja (13-17 th)"),
    ADULT("Dewasa (18-59 th)"),
    SENIOR("Lansia (≥ 60 th)")
}