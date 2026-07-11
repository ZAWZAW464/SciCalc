package com.example.scicalc.domain

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    // Track whether we need to start a fresh expression after equals
    private var shouldReset = false

    fun dispatch(action: CalcAction) {
        val current = _state.value

        // Clear error on any new action (except clear itself)
        if (action !is CalcAction.Clear) {
            _state.update { it.copy(errorMessage = null) }
        }

        when (action) {
            is CalcAction.Number       -> handleNumber(action.value)
            is CalcAction.Decimal      -> handleDecimal()
            is CalcAction.Operator     -> handleOperator(action.symbol)
            is CalcAction.Function     -> handleFunction(action.func)
            is CalcAction.OpenParen    -> handleOpenParen()
            is CalcAction.CloseParen   -> handleCloseParen()
            is CalcAction.Clear        -> handleClear()
            is CalcAction.Delete       -> handleDelete()
            is CalcAction.Equals       -> handleEquals()
            is CalcAction.SignToggle   -> handleSignToggle()
            is CalcAction.Percentage   -> handlePercentage()
            is CalcAction.ConstantPi   -> handleConstant("π")
            is CalcAction.ConstantE    -> handleConstant("e")
            is CalcAction.Power        -> handlePower(current)
        }
    }

    // ─── Number handling ───

    private fun handleNumber(num: Int) {
        val expr = getTargetExpression()
        val newExpr = if (shouldReset) num.toString() else expr + num
        _state.update {
            it.copy(
                expression = newExpr,
                displayResult = formatForDisplay(newExpr),
                isResultShown = false
            )
        }
        shouldReset = false
    }

    // ─── Decimal ───

    private fun handleDecimal() {
        val expr = getTargetExpression()
        // Find the last number token and check if it already has a decimal
        val lastNumber = expr.split(Regex("""[+\-×÷()a-zA-Zπe\s]"""))
            .lastOrNull { it.isNotEmpty() } ?: ""
        if (lastNumber.contains(".")) return   // already has decimal
        val newExpr = if (shouldReset) "0." else expr + "."
        _state.update {
            it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr), isResultShown = false)
        }
        shouldReset = false
    }

    // ─── Operators ───

    private fun handleOperator(symbol: String) {
        var expr = getTargetExpression()
        shouldReset = false

        // Replace trailing operator
        if (expr.isNotEmpty()) {
            val last = expr.last()
            if (last in "+-×÷") {
                expr = expr.dropLast(1)
            }
        }
        // Prevent leading × or ÷
        if (expr.isEmpty() && symbol in listOf("×", "÷")) return

        expr += symbol
        _state.update {
            it.copy(expression = expr, displayResult = formatForDisplay(expr), isResultShown = false)
        }
    }

    // ─── Scientific functions ───

    private fun handleFunction(func: String) {
        val expr = getTargetExpression()
        shouldReset = false

        when (func) {
            "x²" -> {
                // Insert square: wrap preceding number or expression in (...)²
                val newExpr = wrapLastToken(expr) { "$it²" }
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "xʸ" -> {
                // Insert ^(
                val newExpr = wrapLastToken(expr) { "$it^(" }
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "√" -> {
                val newExpr = "${if (shouldReset) "" else expr}√("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "∛" -> {
                val newExpr = "${if (shouldReset) "" else expr}∛("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "ⁿ√" -> {
                // nth root: root(x, n) — we'll use root(  notation
                val newExpr = "${if (shouldReset) "" else expr}ⁿ√("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "log" -> {
                val newExpr = "${if (shouldReset) "" else expr}log("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "ln" -> {
                val newExpr = "${if (shouldReset) "" else expr}ln("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "sin" -> {
                val newExpr = "${if (shouldReset) "" else expr}sin("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "cos" -> {
                val newExpr = "${if (shouldReset) "" else expr}cos("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "tan" -> {
                val newExpr = "${if (shouldReset) "" else expr}tan("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "sin⁻¹" -> {
                val newExpr = "${if (shouldReset) "" else expr}asin("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "cos⁻¹" -> {
                val newExpr = "${if (shouldReset) "" else expr}acos("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "tan⁻¹" -> {
                val newExpr = "${if (shouldReset) "" else expr}atan("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "|x|" -> {
                val newExpr = "${if (shouldReset) "" else expr}abs("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "exp" -> {
                val newExpr = "${if (shouldReset) "" else expr}exp("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "10ˣ" -> {
                val newExpr = "${if (shouldReset) "" else expr}10^("
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
            "n!" -> {
                val newExpr = wrapLastToken(expr) { "fact($it)" }
                _state.update { it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr)) }
                return
            }
        }
    }

    private fun wrapLastToken(expr: String, transform: (String) -> String): String {
        if (expr.isEmpty()) return expr
        // If last char is ')', wrap the whole trailing parenthesized group
        if (expr.last() == ')') {
            val openIdx = findMatchingOpenParen(expr)
            if (openIdx >= 0) {
                val before = expr.substring(0, openIdx)
                val inside = expr.substring(openIdx + 1, expr.length - 1)
                return before + transform(inside)
            }
        }
        // Otherwise wrap the trailing number
        val numMatch = Regex("""(-?\d+\.?\d*)$""").find(expr)
        if (numMatch != null) {
            return expr.removeRange(numMatch.range) + transform(numMatch.value)
        }
        return transform(expr)
    }

    private fun findMatchingOpenParen(expr: String): Int {
        var depth = 0
        for (i in expr.indices.reversed()) {
            when (expr[i]) {
                ')' -> depth++
                '(' -> {
                    depth--
                    if (depth == 0) return i
                }
            }
        }
        return -1
    }

    // ─── Parentheses ───

    private fun handleOpenParen() {
        val expr = getTargetExpression()
        val newExpr = if (shouldReset) "(" else expr + "("
        _state.update {
            it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr), isResultShown = false)
        }
        shouldReset = false
    }

    private fun handleCloseParen() {
        val expr = getTargetExpression()
        val openCount = expr.count { it == '(' }
        val closeCount = expr.count { it == ')' }
        if (closeCount >= openCount) return  // no unmatched open paren
        if (expr.isNotEmpty() && expr.last() == '(' && openCount == closeCount + 1) return // avoid empty ()
        val newExpr = expr + ")"
        _state.update {
            it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr), isResultShown = false)
        }
        shouldReset = false
    }

    // ─── Clear & Delete ───

    private fun handleClear() {
        _state.update { CalculatorState() }
        shouldReset = false
    }

    private fun handleDelete() {
        val expr = _state.value.expression
        val newExpr = if (expr.length <= 1) "" else {
            // Delete the last function name or single char
            val fnMatch = Regex("""(sin|cos|tan|asin|acos|atan|log|ln|sqrt|abs|exp|cbrt|fact|root)\($""")
                .find(expr)
            if (fnMatch != null) {
                expr.removeRange(fnMatch.range)
            } else {
                expr.dropLast(1)
            }
        }
        _state.update {
            it.copy(
                expression = newExpr,
                displayResult = if (newExpr.isEmpty()) "0" else formatForDisplay(newExpr),
                isResultShown = false
            )
        }
        shouldReset = false
    }

    // ─── Equals ───

    private fun handleEquals() {
        val current = _state.value
        val expr = current.expression
        if (expr.isBlank()) return

        try {
            val result = ExpressionEvaluator.evaluate(expr)
            val resultStr = formatResult(result)
            val newHistory = current.history + HistoryEntry(expr, resultStr)
            _state.update {
                it.copy(
                    expression = resultStr,
                    displayResult = resultStr,
                    isResultShown = true,
                    history = newHistory.takeLast(50),  // keep last 50 entries
                    errorMessage = null
                )
            }
            shouldReset = true
        } catch (e: CalculationException) {
            _state.update {
                it.copy(
                    errorMessage = e.message,
                    isResultShown = false
                )
            }
        }
    }

    // ─── Sign Toggle ───

    private fun handleSignToggle() {
        val expr = getTargetExpression()
        // Try to find and toggle the sign of the last number
        val parts = expr.split(Regex("""(?<=[+\-×÷()])|(?=[+\-×÷()])"""))
        if (expr.isEmpty()) {
            _state.update { it.copy(expression = "-", displayResult = "-") }
            return
        }
        // Simplistic: toggle sign on the entire expression if it's just a number
        val isPureNumber = expr.toDoubleOrNull() != null || (expr.startsWith("-") && expr.drop(1).toDoubleOrNull() != null)
        if (isPureNumber) {
            val toggled = if (expr.startsWith("-")) expr.drop(1) else "-$expr"
            _state.update { it.copy(expression = toggled, displayResult = formatForDisplay(toggled)) }
        }
    }

    // ─── Percentage ───

    private fun handlePercentage() {
        val expr = getTargetExpression()
        if (expr.isEmpty()) return
        val newExpr = expr + "%"
        _state.update {
            it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr), isResultShown = false)
        }
        shouldReset = false
    }

    // ─── Constants ───

    private fun handleConstant(const: String) {
        val expr = getTargetExpression()
        val newExpr = if (shouldReset) const else expr + const
        _state.update {
            it.copy(expression = newExpr, displayResult = formatForDisplay(newExpr), isResultShown = false)
        }
        shouldReset = false
    }

    // ─── Power ───

    private fun handlePower(current: CalculatorState) {
        // x² is already handled in handleFunction
        // For arbitrary power, the button sends xʸ which is also handled there
    }

    // ─── Helpers ───

    private fun getTargetExpression(): String {
        return if (shouldReset) {
            shouldReset = false
            ""
        } else {
            _state.value.expression
        }
    }

    private fun formatForDisplay(expr: String): String {
        if (expr.isEmpty()) return "0"
        return expr
    }

    private fun formatResult(value: Double): String {
        return when {
            value.isNaN() -> "NaN"
            value.isInfinite() -> if (value > 0) "∞" else "-∞"
            value == value.toLong().toDouble() && value in Long.MIN_VALUE.toDouble()..Long.MAX_VALUE.toDouble() -> {
                value.toLong().toString()
            }
            value == 0.0 -> "0"
            else -> {
                // Format up to 12 significant digits, strip trailing zeros
                val formatted = "%.12f".format(value).trimEnd('0').trimEnd('.')
                // If still too long after cleaning, use scientific notation
                if (formatted.length > 15) "%.10e".format(value) else formatted
            }
        }
    }
}
