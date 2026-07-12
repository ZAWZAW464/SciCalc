package com.example.scicalc.domain

import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Function
import kotlin.math.*

object ExpressionEvaluator {

    private val negate = object : Function("negate", 1) {
        override fun apply(vararg args: Double): Double = -args[0]
    }

    private val factorial = object : Function("fact", 1) {
        override fun apply(vararg args: Double): Double {
            val n = args[0].toInt()
            require(n >= 0 && n <= 170) { "Factorial requires 0 ≤ n ≤ 170" }
            var result = 1.0
            for (i in 2..n) result *= i
            return result
        }
    }

    private val nthRoot = object : Function("root", 2) {
        override fun apply(vararg args: Double): Double {
            val base = args[0]
            val n = args[1]
            return if (base < 0 && n % 2.0 == 1.0) {
                -abs(base).pow(1.0 / n)
            } else {
                base.pow(1.0 / n)
            }
        }
    }

    private val cbrtFn = object : Function("cbrt", 1) {
        override fun apply(vararg args: Double): Double = args[0].pow(1.0 / 3.0)
    }

    private val logBase = object : Function("logb", 2) {
        override fun apply(vararg args: Double): Double = ln(args[0]) / ln(args[1])
    }

    fun preprocess(raw: String): String {
        var expr = raw.trim()

        // Remove leading +
        if (expr.startsWith("+")) expr = expr.drop(1)

        // Replace display symbols
        expr = expr.replace("×", "*")
        expr = expr.replace("÷", "/")
        expr = expr.replace("π", "pi")
        expr = expr.replace("−", "-")

        // Auto-close unmatched parentheses so exp4j doesn't complain
        expr = autoCloseParens(expr)

        // Percentage: X% -> (X/100)
        expr = Regex("(\\d+\\.?\\d*)%").replace(expr) { "(${it.groupValues[1]}/100)" }

        // Implicit multiplication
        expr = expr.replace(Regex("(\\d)\\("), "$1*(")
        expr = expr.replace(Regex("\\)(\\d)"), ")*$1")
        expr = expr.replace(Regex("\\)\\("), ")*(")
        expr = expr.replace(Regex("\\)([a-zA-Z])"), ")*$1")
        expr = Regex("(\\d)([a-zA-Z])").replace(expr) { match ->
            val fn = match.groupValues[2]
            if (fn.length == 1 && fn[0] in 'a'..'z') "*$fn" else match.value
        }
        expr = expr.replace(Regex("(pi)(\\d)"), "pi*$1")

        return expr
    }

    /** Auto-close any unmatched '(' with corresponding ')' */
    private fun autoCloseParens(expr: String): String {
        val openCount = expr.count { it == '(' }
        val closeCount = expr.count { it == ')' }
        val missing = openCount - closeCount
        if (missing <= 0) return expr
        return expr + ")".repeat(missing)
    }

    fun evaluate(rawExpression: String): Double {
        if (rawExpression.isBlank()) return 0.0

        val processed = preprocess(rawExpression)

        return try {
            val builder = ExpressionBuilder(processed)
                .function(negate)
                .function(factorial)
                .function(nthRoot)
                .function(cbrtFn)
                .function(logBase)
                .build()

            val validation = builder.validate(false)
            if (!validation.isValid) {
                val msg = validation.errors.joinToString("; ")
                throw CalculationException(msg)
            }

            val value = builder.evaluate()

            if (value.isNaN()) throw CalculationException("Result is undefined (NaN)")
            if (value.isInfinite()) throw CalculationException("Result is infinite — division by zero?")

            value
        } catch (e: CalculationException) {
            throw e
        } catch (e: ArithmeticException) {
            throw CalculationException("Arithmetic error: ${e.message}")
        } catch (e: IllegalArgumentException) {
            throw CalculationException("Invalid expression: ${e.message}")
        } catch (e: Exception) {
            throw CalculationException("Error: ${e.message ?: "Unable to evaluate"}")
        }
    }
}

class CalculationException(message: String) : Exception(message)
