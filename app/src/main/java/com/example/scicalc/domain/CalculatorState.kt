package com.example.scicalc.domain

data class CalculatorState(
    val expression: String = "0",
    val displayResult: String = "0",
    val isResultShown: Boolean = false,
    val errorMessage: String? = null)