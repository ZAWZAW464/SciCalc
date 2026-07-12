package com.example.scicalc.domain

sealed class CalcAction {
    data class Number(val value: Int) : CalcAction()
    data class Operator(val symbol: String) : CalcAction()
    data class Function(val func: String) : CalcAction()
    data class Decimal(val sep: Char = '.') : CalcAction()
    data object Clear : CalcAction()
    data object Delete : CalcAction()
    data object Equals : CalcAction()
    data object SignToggle : CalcAction()
    data object Percentage : CalcAction()
    data object ConstantPi : CalcAction()
    data object ConstantE : CalcAction()
    data object OpenParen : CalcAction()
    data object CloseParen : CalcAction()
    data class Power(val exponent: Double) : CalcAction()
}
