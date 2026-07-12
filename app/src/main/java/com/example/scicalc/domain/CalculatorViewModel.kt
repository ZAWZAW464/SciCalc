package com.example.scicalc.domain

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    fun onAction(action: CalcAction) {
        when (action) {
            is CalcAction.Number      -> handleNumber(action.value)
            is CalcAction.Decimal     -> addDecimal()
            is CalcAction.Operator    -> handleOperator(action.symbol)
            is CalcAction.Function    -> addFunction(action.func)
            is CalcAction.Clear       -> clear()
            is CalcAction.Delete      -> backspace()
            is CalcAction.Equals      -> evaluate()
            is CalcAction.SignToggle  -> toggleSign()
            is CalcAction.Percentage  -> addPercent()
            is CalcAction.ConstantPi  -> appendText("π")
            is CalcAction.ConstantE   -> appendText("e")
            is CalcAction.OpenParen   -> appendText("(")
            is CalcAction.CloseParen  -> appendText(")")
            is CalcAction.Power       -> appendText("^")
        }
    }

    private fun clear() { _state.value = CalculatorState() }

    private fun handleNumber(num: Int) {
        val c = _state.value
        val newExpr = if (c.isResultShown || c.expression == "0") num.toString() else c.expression + num
        _state.value = CalculatorState(expression = newExpr, displayResult = newExpr, errorMessage = null)
    }

    private fun addDecimal() {
        val c = _state.value
        val newExpr = if (c.isResultShown) "0." else c.expression + "."
        _state.value = CalculatorState(expression = newExpr, displayResult = newExpr)
    }

    private fun appendText(t: String) {
        val c = _state.value
        val newExpr = if (c.isResultShown || c.expression == "0") t else c.expression + t
        _state.value = CalculatorState(expression = newExpr, displayResult = newExpr, errorMessage = null)
    }

    private fun handleOperator(op: String) {
        val c = _state.value
        val newExpr = if (c.isResultShown) c.displayResult + "$op" else c.expression + "$op"
        _state.value = CalculatorState(expression = newExpr, displayResult = op)
    }

    private fun addFunction(func: String) {
        val c = _state.value
        val newExpr = if (c.isResultShown || c.expression == "0") "$func(" else c.expression + "$func("
        _state.value = CalculatorState(expression = newExpr, displayResult = "0")
    }

    private fun addPercent() {
        val c = _state.value
        val newExpr = c.expression + "%"
        _state.value = c.copy(expression = newExpr, displayResult = newExpr)
    }

    private fun backspace() {
        val c = _state.value
        val expr = c.expression
        if (expr.length <= 1 || c.isResultShown || c.errorMessage != null) {
            _state.value = CalculatorState()
            return
        }
        val trimmed = expr.trimEnd()
        val newExpr = when {
            trimmed.endsWith("sqrt(") || trimmed.endsWith("sin(") || trimmed.endsWith("cos(") || trimmed.endsWith("tan(") || trimmed.endsWith("ln(") || trimmed.endsWith("log(") -> trimmed.removeSuffix("(")
            trimmed.endsWith("fact(") -> trimmed.removeSuffix("fact(")
            trimmed.endsWith("negate(") -> trimmed.removeSuffix("negate(")
            else -> trimmed.dropLast(1)
        }
        _state.value = CalculatorState(expression = newExpr, displayResult = "0")
    }

    private fun toggleSign() {
        val c = _state.value
        val newExpr = "negate(${c.expression})"
        _state.value = CalculatorState(expression = newExpr, displayResult = "0")
    }

    private fun evaluate() {
        val c = _state.value
        if (c.expression.isBlank() || c.expression == "0") return
        try {
            val result = ExpressionEvaluator.evaluate(c.expression)
            val resultStr = if (result == result.toLong().toDouble()) result.toLong().toString() else result.toString()
            _state.value = CalculatorState(expression = resultStr, displayResult = resultStr, isResultShown = true)
        } catch (e: CalculationException) {
            _state.value = c.copy(errorMessage = e.message)
        } catch (e: Exception) {
            _state.value = c.copy(errorMessage = "Error: ${e.message ?: "Unknown"}")
        }
    }
}
