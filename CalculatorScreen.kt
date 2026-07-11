package com.example.scicalc.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.scicalc.domain.CalcAction
import com.example.scicalc.domain.CalculatorViewModel
import com.example.scicalc.ui.components.ButtonType
import com.example.scicalc.ui.components.CalcButton
import com.example.scicalc.ui.components.CalculatorDisplay

/**
 * Main calculator screen. Adapts between portrait and landscape layouts.
 */
@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    val state by viewModel.state.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    val isDark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Display
        CalculatorDisplay(
            state = state,
            modifier = Modifier.weight(if (isLandscape) 0.35f else 0.3f)
        )

        // Keypad
        if (isLandscape) {
            LandscapeKeypad(
                onAction = viewModel::dispatch,
                modifier = Modifier.weight(0.65f)
            )
        } else {
            PortraitKeypad(
                onAction = viewModel::dispatch,
                modifier = Modifier.weight(0.7f)
            )
        }
    }
}

// ═══════════════════════════════════════
//  PORTRAIT KEYPAD
// ═══════════════════════════════════════

@Composable
private fun PortraitKeypad(
    onAction: (CalcAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Row 1 — Scientific functions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CalcButton("sin", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("sin"))
            }
            CalcButton("cos", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("cos"))
            }
            CalcButton("tan", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("tan"))
            }
            CalcButton("sin⁻¹", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("sin⁻¹"))
            }
            CalcButton("cos⁻¹", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("cos⁻¹"))
            }
        }

        // Row 2 — More scientific functions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CalcButton("ln", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("ln"))
            }
            CalcButton("log", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("log"))
            }
            CalcButton("xʸ", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("xʸ"))
            }
            CalcButton("√", "x²", ButtonType.FUNCTION, Modifier.weight(1f)) {
                onAction(CalcAction.Function("x²"))
            }
            CalcButton("ⁿ√", "∛", ButtonType.FUNCTION, Modifier.weight(1f)) {
                onAction(CalcAction.Function("ⁿ√"))
            }
        }

        // Row 3 — Constants and more
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CalcButton("|x|", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("|x|"))
            }
            CalcButton("exp", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("exp"))
            }
            CalcButton("n!", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("n!"))
            }
            CalcButton("(", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.OpenParen)
            }
            CalcButton(")", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.CloseParen)
            }
        }

        // Row 4 — C, DEL, %
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CalcButton("C", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Clear)
            }
            CalcButton("⌫", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Delete)
            }
            CalcButton("%", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Percentage)
            }
            CalcButton("π", "e", ButtonType.FUNCTION, Modifier.weight(1f)) {
                onAction(CalcAction.ConstantPi)
            }
            CalcButton("10ˣ", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("10ˣ"))
            }
        }

        // Row 5 — Main number pad row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CalcButton("7", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Number(7))
            }
            CalcButton("8", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Number(8))
            }
            CalcButton("9", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Number(9))
            }
            CalcButton("÷", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Operator("÷"))
            }
            CalcButton("√", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("√"))
            }
        }

        // Row 6 — Main number pad row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CalcButton("4", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Number(4))
            }
            CalcButton("5", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Number(5))
            }
            CalcButton("6", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Number(6))
            }
            CalcButton("×", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Operator("×"))
            }
            CalcButton("∛", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("∛"))
            }
        }

        // Row 7 — Main number pad row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CalcButton("1", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Number(1))
            }
            CalcButton("2", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Number(2))
            }
            CalcButton("3", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Number(3))
            }
            CalcButton("−", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Operator("-"))
            }
            CalcButton("x²", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Function("x²"))
            }
        }

        // Row 8 — Main number pad row 4
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CalcButton("±", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.SignToggle)
            }
            CalcButton("0", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Number(0))
            }
            CalcButton(".", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Decimal('.'))
            }
            CalcButton("+", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Operator("+"))
            }
            CalcButton("=", type = ButtonType.EQUALS, modifier = Modifier.weight(1f)) {
                onAction(CalcAction.Equals)
            }
        }
    }
}

// ═══════════════════════════════════════
//  LANDSCAPE KEYPAD
// ═══════════════════════════════════════

@Composable
private fun LandscapeKeypad(
    onAction: (CalcAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Left side: Scientific panel
        Column(
            modifier = Modifier.weight(0.62f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Row 1 — Trig functions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("sin" to "sin", "cos" to "cos", "tan" to "tan",
                       "sin⁻¹" to "sin⁻¹", "cos⁻¹" to "cos⁻¹", "tan⁻¹" to "tan⁻¹").forEach { (label, func) ->
                    CalcButton(label, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                        onAction(CalcAction.Function(func))
                    }
                }
            }

            // Row 2 — Log & power
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("ln" to "ln", "log" to "log", "xʸ" to "xʸ",
                       "x²" to "x²", "√" to "√", "∛" to "∛").forEach { (label, func) ->
                    CalcButton(label, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                        onAction(CalcAction.Function(func))
                    }
                }
            }

            // Row 3 — More functions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CalcButton("|x|", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Function("|x|"))
                }
                CalcButton("exp", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Function("exp"))
                }
                CalcButton("n!", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Function("n!"))
                }
                CalcButton("10ˣ", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Function("10ˣ"))
                }
                CalcButton("ⁿ√", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Function("ⁿ√"))
                }
                CalcButton("π", "e", ButtonType.FUNCTION, Modifier.weight(1f)) {
                    onAction(CalcAction.ConstantPi)
                }
            }

            // Row 4 — Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CalcButton("C", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Clear)
                }
                CalcButton("⌫", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Delete)
                }
                CalcButton("(", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.OpenParen)
                }
                CalcButton(")", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.CloseParen)
                }
                CalcButton("%", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Percentage)
                }
                CalcButton("±", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.SignToggle)
                }
            }
        }

        // Right side: Number pad
        Column(
            modifier = Modifier.weight(0.38f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Row 1 — 7 8 9 ÷
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(7, 8, 9).forEach { n ->
                    CalcButton("$n", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                        onAction(CalcAction.Number(n))
                    }
                }
                CalcButton("÷", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Operator("÷"))
                }
            }

            // Row 2 — 4 5 6 ×
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(4, 5, 6).forEach { n ->
                    CalcButton("$n", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                        onAction(CalcAction.Number(n))
                    }
                }
                CalcButton("×", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Operator("×"))
                }
            }

            // Row 3 — 1 2 3 −
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(1, 2, 3).forEach { n ->
                    CalcButton("$n", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                        onAction(CalcAction.Number(n))
                    }
                }
                CalcButton("−", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Operator("-"))
                }
            }

            // Row 4 — 0 . = +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CalcButton("0", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Number(0))
                }
                CalcButton(".", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Decimal('.'))
                }
                CalcButton("=", type = ButtonType.EQUALS, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Equals)
                }
                CalcButton("+", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) {
                    onAction(CalcAction.Operator("+"))
                }
            }
        }
    }
}
