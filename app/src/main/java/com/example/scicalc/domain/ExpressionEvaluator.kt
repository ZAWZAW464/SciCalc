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
            require(n >= 0 && n <= 170) { "Factorial requires 0 <= n <= 170" }
            var result = 1.0
            for (i in 2..n) result *= i
            return result
        }
    }

    private val lnFn = object : Function("LN", 1) {
        override fun apply(vararg args: Double): Double = ln(args[0])
    }

    private val logFn = object : Function("LOG", 1) {
        override fun apply(vararg args: Double): Double = log10(args[0])
    }

    private val sqrtFn = object : Function("SQRT", 1) {
        override fun apply(vararg args: Double): Double = sqrt(args[0])
    }

    private val sinFn = object : Function("SIN", 1) {
        override fun apply(vararg args: Double): Double = sin(Math.toRadians(args[0]))
    }

    private val cosFn = object : Function("COS", 1) {
        override fun apply(vararg args: Double): Double = cos(Math.toRadians(args[0]))
    }

    private val tanFn = object : Function("TAN", 1) {
        override fun apply(vararg args: Double): Double = tan(Math.toRadians(args[0]))
    }

    fun preprocess(raw: String): String {
        var expr = raw.trim()

        expr = expr.replace("\u00D7", "*")
        expr = expr.replace("\u00F7", "/")
        expr = expr.replace("\u03C0", "pi")
        expr = expr.replace("\u2212", "-")

        expr = expr.replace("sin", "SIN")
        expr = expr.replace("cos", "COS")
        expr = expr.replace("tan", "TAN")
        expr = expr.replace("ln", "LN")
        expr = expr.replace("log", "LOG")
        expr = expr.replace("sqrt", "SQRT")

        val open = expr.count { it == '(' }
        val close = expr.count { it == ')' }
        repeat(open - close) { expr += ")" }

        expr = Regex("(\\d+\\.?\\d*)%").replace(expr) { "(${it.groupValues[1]}/100)" }

        expr = expr.replace(Regex("(\\d)\\("), "$1*(")
        expr = expr.replace(Regex("\\)(\\d)"), ")*$1")
        expr = expr.replace(Regex("\\)\\("), ")*(")
        expr = expr.replace(Regex("\\)([A-Z]+)"), ")*$1")
        expr = expr.replace(Regex("(\\d)([A-Z]+)")) { match ->
            "*${match.groupValues[2]}"
        }
        expr = expr.replace(Regex("(pi)(\\d)"), "pi*$1")

        return expr
    }

    fun evaluate(raw: String): Double {
        if (raw.isBlank()) return 0.0

        val processed = preprocess(raw)

        return try {
            val builder = ExpressionBuilder(processed)
                .function(negate)
                .function(factorial)
                .function(lnFn)
                .function(logFn)
                .function(sqrtFn)
                .function(sinFn)
                .function(cosFn)
                .function(tanFn)
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
