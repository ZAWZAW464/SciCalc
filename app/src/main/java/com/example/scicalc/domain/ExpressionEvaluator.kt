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

    val fns = arrayOf(negate, factorial)

    fun preprocess(raw: String): String {
        var expr = raw.trim()

        // Unicode symbols
        expr = expr.replace("×", "*")
        expr = expr.replace("÷", "/")
        expr = expr.replace("π", "pi")
        expr = expr.replace("−", "-")

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
        expr = expr.replace(Regex("(\\d)([a-zA-Z])")) { match ->
            val fn = match.groupValues[2]
            if (fn.length == 1) { "$*$fn" } else match.value
        }
        expr = expr.replace(Regex("(pi)(\\d)"), "pi*$1")

        return expr
    }

    fun evaluate(raw: String): Double {
        if (raw.isBlank()) return 0.0

        val processed = preprocess(raw)

        return try {
            val builder = ExpressionBuilder(processed)
                .functions(fns)
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
