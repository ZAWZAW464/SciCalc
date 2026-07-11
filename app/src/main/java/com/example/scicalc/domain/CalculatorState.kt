package com.example.scicalc.domain

/**
 * Represents the current state of the calculator display and expression.
 */
data class CalculatorState(
    val expression: String = "",         // The expression being built, e.g. "sin(45) + 3×2"
    val displayResult: String = "0",     // The result shown in the primary display
    val isResultShown: Boolean = false,  // True when the last action was '='
    val errorMessage: String? = null,    // Non-null when an error occurred
    val history: List<HistoryEntry> = emptyList()
)

data class HistoryEntry(
    val expression: String,
    val result: String
)
