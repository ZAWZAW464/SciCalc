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
            is CalcAction.Number -> handleNumber(action.value)
            is CalcAction.Decimal -> handleDecimal()
            is CalcAction.Operator -> handleOperator(action.symbol)
            is CalcAction.Function -> handleFunction(action.func)
            is CalcAction.Clear -> clear()
            is CalcAction.Delete -> handleDelete()
            is CalcAction.Equals -> handleEquals()
            is CalcAction.SignToggle -> handleSignToggle()
            is CalcAction.Percentage -> handlePercentage()
            is CalcAction.ConstantPi -> handleConstant("π")
            is CalcAction.ConstantE -> handleConstant("e")
            is CalcAction.OpenParen -> handleOpenParen()
            is CalcAction.CloseParen -> handleCloseParen()
            is CalcAction.Power -> handlePower()
        }
    }

    private fun clear() { _state.value = CalculatorState() }

    private fun handleNumber(num: Int) {
        val current = _state.value
        if (current.isResultShown) {
            _state.value = CalculatorState(expression = num.toString(), displayResult = num.toString())
        } else {
            val newExpr = if (current.expression == "0") num.toString() else current.expression + num
            _state.value = current.copy(expression = newExpr, displayResult = newExpr, errorMessage = null)
        }
    }

    private fun handleDecimal() {
        val current = _state.value
        val lastNum = current.expression.takeLastWhile { it.isDigit() || it == '.' }
        if ("." in lastNum) return
        val newExpr = current.expression + "."
        _state.value = current.copy(expression = newExpr, displayResult = newExpr)
    }

    private fun handleOperator(op: String) {
        val current = _state.value
        val opSuf = " $op "
        if (current.isResultShown) {
            _state.value = CalculatorState(expression = current.displayResult + opSuf, displayResult = current.displayResult)
        } else {
            val newExpr = current.expression + opSuf
            _state.value = current.copy(expression = newExpr, displayResult = "0", errorMessage = null)
        }
    }

    private fun handleFunction(func: String) {
        val current = _state.value
        val newExpr = if (current.isResultShown) {
            func + "("
        } else {
            current.expression + func + "("
        }
        _state.value = CalculatorState(expression = newExpr, displayResult = "0")
    }

    private fun handleEquals() {
        val current = _state.value
        try {
            val result = ExpressionEvaluator.evaluate(current.expression)
            val resultStr = if (result == result.toLong().toDouble()) result.toLong().toString() else result.toString()
            val history = current.history + HistoryEntry(current.expression, resultStr)
            _state.value = CalculatorState(expression = resultStr, displayResult = resultStr, isResultShown = true, history = history)
        } catch (e: CalculationException) {
            _state.value = current.copy(errorMessage = e.message)
        } catch (e: Exception) {
            _state.value = current.copy(errorMessage = "Error: ${e.message ?: "Unknown"}")
        }
    }

    private fun handleDelete() {
        val current = _state.value
        if (current.expression.length <= 1) {
            _state.value = CalculatorState()
        } else {
            val newExpr = current.expression.dropLast(1)
            _state.value = current.copy(expression = newExpr, displayResult = "0")
        }
    }

    private fun handleSignToggle() {
        val current = _state.value
        val newExpr = "negate(${current.expression})"
        _state.value = current.copy(expression = newExpr)
    }

    private fun handlePercentage() {
        val current = _state.value
        val newExpr = current.expression + "%"
        _state.value = current.copy(expression = newExpr, displayResult = newExpr)
    }

    private fun handleConstant(c: String) {
        val current = _state.value
        val newExpr = if (current.isResultShown) c else current.expression + c
        _state.value = CalculatorState(expression = newExpr, displayResult = c)
    }

    private fun handleOpenParen() {
        val current = _state.value
        val newExpr = current.expression + "("
        _state.value = current.copy(expression = newExpr, displayResult = "0")
    }

    private fun handleCloseParen() {
        val current = _state.value
        val openCount = current.expression.count { it == '(' }
        val closeCount = current.expression.count { it == ')' }
        if (openCount > closeCount) {
            val newExpr = current.expression + ")"
            _state.value = current.copy(expression = newExpr, displayResult = "0")
        }
    }

    private fun handlePower() {
        val current = _state.value
        val newExpr = current.expression + "^"
        _state.value = current.copy(expression = newExpr, displayResult = "0")
    }
}
