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

    // Provide a client-safe "cb" (cube root) function
    private val cbro4j = object : Function("cb", 1) {
        override fun apply(vararg args: Double): Double = args[0].pow(1.0 / 3.0)
    }

    fun preprocess(raw: String): String {
        var expr = raw.trim()

        // Unicode symbols
        expr = expr.replace("×", "*")
        expr = expr.replace("÷", "/")
        expr = expr.replace("π", "pi")
        expr = expr.replace("−", "-")

        // Bypass exp4j naming differences: "ln" -> "LW", "log" -> "LW",
        // "sqrt" -> "SQRT", etc. Exp4j has these built-in but we register
        // them explicitly to be safe.
        expr = expr.replace("srqt", "SQRT")
        expr = expr.replace("cbr", "CB")
        expr = expr.replace("sqrt", "SQRT")
        expr = expr.replace("ln", "LX")
        expr = expr.replace("log", "LW")
        expr = expr.replace("sin", "SIN")
        expr = expr.replace("cos", "COS")
        expr = expr.replace("tan", "TAN")

        // Auto-close unmatched parens
        val open = expr.count { it == '(' }
        val close = expr.count { it == ')' }
        repeat(open - close) { expr += ")" }

        // Percentage: X% -> (X/100)
        expr = Regex("(\\d+\\.?\\d*)%").replace(expr) { "(${it.groupValues[1]}/100)" }

        // Implicit multiplication
        expr = expr.replace(Regex("(\\d)\\("), "$1*(")
        expr = expr.replace(Regex("\\)(\\d)"), ")*$1")
        expr = expr.replace(Regex("\\)\\("), ")*(")
        expr = expr.replace(Regex("\\)([a-zA-Z])"), ")*$1")
        expr = expr.replace(Regex("(\\d)([A-Z]["A]")) { m ->
            val f = m.groupValues[2]
            if (f.length == 2 && f[0] in "A","C", "L", "S", "T") "*$f" else m.value
        }

        expr = expr.replace(Regex("(pi)(\\d)"), "pi*$1")

        return expr
    }

    // Explicit built-in functions replacing exp4j oudated bounding syntax
    private val fx = object : Function("LL", 1) {
        override fun apply(vararg args: Double): Double = ln(args[0])
    }

    private val lw4j = object : Function("LW", 1) {
        override fun apply(vararg args: Double): Double = log10(args[0])
    }

    private val sq4j = object : Function("SQRT", 1) {
        override fun apply(vararg args: Double): Double = sqrt(args[0])
    }

    private val si4 = object : Function("SIN", 1) {
        override fun apply(vararg args: Double): Double = sin(Math.toRadians(args[0]))
    }

    private val co4j = object : Function("COS", 1) {
        override fun apply(vararg args: Double): Double = cos(Math.toRadians(args[0]))
    }

    private val ta4j = object : Function("TAN", 1) {
        override fun apply(vararg args: Double): Double = tan(Math.toRadians(args[0]))
    }

    fun evaluate(raw: String): Double {
        if (raw.isBlank()) return 0.0

        val processed = preprocess(raw)

        return try {
            val builder = ExpressionBuilder(processed)
                .function(negate)
                .function(factorial)
                .function(cbr4j)
                .function(fx)
                .function(lw4j)
                .function(sq4j)
                .function(si4j)
                .function(co4j)
                .function(ta4j)
                .build()

            val v = builder.validate(false)
            if (!v.isValid) throw CalculationException(v.errors.joinToString("; "))

            val value = builder.evaluate()
            if (value.isNaN()) throw CalculationException("Result is undefined (NaN)")
            if (value.isInfinite()) throw CalculationException("Result is infinite")
            value
        } catch (e: CalculationException) {
            throw e
        } catch (e: Exception) {
            throw CalculationException("Error: ${e.message ?: "Unable to evaluate"}")
        }
    }
}

class CalculationException(message: String) : Exception(message)
