package com.example.scicalc.domain

/**
 * Sealed class representing all calculator input actions.
 */
sealed class CalcAction {
    data class Number(val value: Int) : CalcAction()
    data class Decimal(val char: Char) : CalcAction()      // '.'
    data class Operator(val symbol: String) : CalcAction()  // + - × ÷
    data class Function(val func: String) : CalcAction()     // sin, cos, tan, log, ln, sqrt, etc.
    data object OpenParen : CalcAction()
    data object CloseParen : CalcAction()
    data object Clear : CalcAction()
    data object Delete : CalcAction()
    data object Equals : CalcAction()
    data object SignToggle : CalcAction()
    data object Percentage : CalcAction()
    data object ConstantPi : CalcAction()
    data object ConstantE : CalcAction()
    data class Power(val exponent: Double) : CalcAction()
}
