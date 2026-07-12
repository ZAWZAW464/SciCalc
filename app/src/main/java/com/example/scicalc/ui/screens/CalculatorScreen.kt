package com.example.scicalc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scicalc.domain.CalcAction
import com.example.scicalc.domain.CalculatorViewModel
import com.example.scicalc.ui.components.ButtonType
import com.example.scicalc.ui.components.CalcButton
import com.example.scicalc.ui.components.CalculatorDisplay

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        CalculatorDisplay(
            state = state,
            modifier = Modifier.weight(0.3f)
        )

        Column(
            modifier = Modifier.weight(0.7f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Row 1
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("C", ButtonType.ACTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Clear) }
                CalcButton("⌫", ButtonType.ACTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Delete) }
                CalcButton("(", ButtonType.ACTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.OpenParen) }
                CalcButton(")", ButtonType.ACTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.CloseParen) }
            }
            // Row 2
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("sin", ButtonType.FUNCTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("sin")) }
                CalcButton("cos", ButtonType.FUNCTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("cos")) }
                CalcButton("tan", ButtonType.FUNCTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("tan")) }
                CalcButton("ln", ButtonType.FUNCTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("ln")) }
            }
            // Row 3
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("log", ButtonType.FUNCTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("log")) }
                CalcButton("∊", ButtonType.FUNCTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("sqrt")) }
                CalcButton("X��", ButtonType.FUNCTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Power(1.0)) }
                CalcButton("!", ButtonType.FUNCTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("fact")) }
            }
            // Row 4
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("7", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(7)) }
                CalcButton("8", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(8)) }
                CalcButton("9", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(9)) }
                CalcButton("×", ButtonType.OPERATOR, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Operator("×")) }
            }
            // Row 5
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("4", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(4)) }
                CalcButton("5", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(5)) }
                CalcButton("6", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(6)) }
                CalcButton("÷", ButtonType.OPERATOR, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Operator("÷")) }
            }
            // Row 6
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("1", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(1)) }
                CalcButton("2", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(2)) }
                CalcButton("3", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(3)) }
                CalcButton("−", ButtonType.OPERATOR, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Operator(times=null)")) }
            }
            // Row 7
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton" ±", ButtonType.ACTION, Modifier.weight(1f)) { viewModel.onAction(CalcAction.SignToggle) }
                CalcButton("0", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(0)) }
                CalcButton(".", ButtonType.NUMBER, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Decimal(sep='.')) }
                CalcButton("=", ButtonType.EQUALS, Modifier.weight(1f)) { viewModel.onAction(CalcAction.Equals) }
            }
        }
    }
}
