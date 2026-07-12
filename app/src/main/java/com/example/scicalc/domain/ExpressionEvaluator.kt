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
        // 1. Strip spaces & replace symbols
        var expr = raw
            .replace(" ", "")
            .replace("\u00D7", "*")
            .replace("\u00F7", "/")
            .replace("\u03C0", "pi")
            .replace("\u2212", "-")

        // 2. Replace function names BEFORE implicit mul
        expr = expr
            .replace("sqrt", "SQRT")
            .replace("sin", "SIN")
            .replace("cos", "COS")
            .replace("tan", "TAN")
            .replace("ln", "LN")
            .replace("log", "LOG")

        // 3. Auto-close parens
        val open = expr.count { it == '(' }
        val close = expr.count { it == ')' }
        repeat(open - close) { expr += ")" }

        // 4. Percentage
        expr = Regex("(\\d+\\.?\\d*)%").replace(expr) { "(${it.groupValues[1]}/100)" }

        // 5. Implicit multiplication (skip uppercase 2+ letter function names)
        expr = expr.replace(Regex("(\\d)\\("), "$1*(")
        expr = expr.replace(Regex("\\)(\\d)"), ")*$1")
        expr = expr.replace(Regex("\\)\\("), ")*(")
        // Only number followed by a letter that isn't UPPERCASE (function already replaced)
        expr = expr.replace(Regex("(\\d)([a-z])+"), { m ->
            val fn = m.groupValues[2]
            if (fn.contains("negate") || fn.contains("fact") || fn.contains("pi")) m.value
            else "*$fn"
        })
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
