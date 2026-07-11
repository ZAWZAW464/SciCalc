package com.example.scicalc.domain

import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.ValidationResult
import net.objecthunter.exp4j.function.Function
import kotlin.math.*

/**
 * Evaluates mathematical expressions, handling both standard and scientific functions.
 * Uses exp4j under the hood with custom function registrations for scientific ops.
 */
object ExpressionEvaluator {

    // Custom unary function: sign change  (-x)
    private val negate = object : Function("negate", 1) {
        override fun apply(vararg args: Double): Double = -args[0]
    }

    // Custom unary function: factorial
    private val factorial = object : Function("fact", 1) {
        override fun apply(vararg args: Double): Double {
            val n = args[0].toInt()
            require(n >= 0 && n <= 170) { "Factorial requires 0 ≤ n ≤ 170" }
            var result = 1.0
            for (i in 2..n) result *= i
            return result
        }
    }

    // Custom unary function: nth root  ∛(x) → root(x, n)
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

    // Custom unary function: cbrt (cube root)
    private val cbrtFn = object : Function("cbrt", 1) {
        override fun apply(vararg args: Double): Double = args[0].pow(1.0 / 3.0)
    }

    // Custom binary: log base
    private val logBase = object : Function("logb", 2) {
        override fun apply(vararg args: Double): Double = ln(args[0]) / ln(args[1])
    }

    /**
     * Pre-process the raw display expression into an exp4j-compatible string.
     * Handles: implicit multiplication, unary minus, percentage, etc.
     */
    fun preprocess(raw: String): String {
        var expr = raw.trim()

        // Remove leading plus
        if (expr.startsWith("+")) expr = expr.drop(1)

        // Replace display symbols with evaluation symbols
        expr = expr.replace("×", "*")
        expr = expr.replace("÷", "/")
        expr = expr.replace("π", "pi")
        expr = expr.replace("−", "-")   // minus sign

        // Handle percentage:  X%  →  X/100
        // We handle this with regex: replace number% with (number/100)
        val percentRegex = Regex("""(\d+\.?\d*)%""")
        expr = percentRegex.replace(expr) { "(${it.groupValues[1]}/100)" }

        // Insert implicit multiplication:
        //  "2sin" → "2*sin", "2(" → "2*(", ")(" → ")*(", ")3" → ")*3",
        //  ")(sin" → ")*(sin", "π(" → "pi*(", "e(" → "e*("
        //  "2π" → "2*pi", "π2" → "pi*2", "e2" → "e*2"
        expr = expr.replace(Regex("""(\d)(\()"""), "$1*(")
        expr = expr.replace(Regex("""\)(\d)"""), ")*$1")
        expr = expr.replace(Regex("""\)\((?=\w)"""), ")*(")
        expr = expr.replace(Regex("""\)(\w)"""), ")*$1")
        expr = expr.replace(Regex("""(\d)([a-zA-Z])""")) { match ->
            val fn = match.groupValues[2]
            // Only add * if it's not an operator suffix like 'e' in scientific notation
            if (fn.length == 1 && fn[0] in 'a'..'z') "*$fn" else match.value
        }
        expr = expr.replace(Regex("""(pi)(\d)"""), "pi*$1")
        expr = expr.replace(")(", ")*(")

        return expr
    }

    /**
     * Evaluate the preprocessed expression and return the result.
     * Throws [CalculationException] on errors.
     */
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
                // Built-in functions supported by exp4j:
                // sin, cos, tan, asin, acos, atan, sinh, cosh, tanh,
                // log (base 10), ln, sqrt, abs, pow, exp
                .build()

            // Validate
            val result = builder.validate(false)
            if (!result.isValid) {
                val msg = result.errors.joinToString("; ") { it.message ?: "Unknown error" }
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
