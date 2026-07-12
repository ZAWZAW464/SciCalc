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
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("C", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Clear) }
                CalcButton("⌫", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Delete) }
                CalcButton("(", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.OpenParen) }
                CalcButton(")", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.CloseParen) }
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("sin", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("sin")) }
                CalcButton("cos", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("cos")) }
                CalcButton("tan", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("tan")) }
                CalcButton("ln", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("ln")) }
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("log", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("log")) }
                CalcButton("√", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("sqrt")) }
                CalcButton("xʸ", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Power(1.0)) }
                CalcButton("!", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Function("fact")) }
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("7", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(7)) }
                CalcButton("8", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(8)) }
                CalcButton("9", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(9)) }
                CalcButton("×", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Operator("×")) }
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("4", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(4)) }
                CalcButton("5", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(5)) }
                CalcButton("6", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(6)) }
                CalcButton("÷", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Operator("÷")) }
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("1", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(1)) }
                CalcButton("2", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(2)) }
                CalcButton("3", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(3)) }
                CalcButton("−", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Operator("−")) }
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton("±", type = ButtonType.ACTION, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.SignToggle) }
                CalcButton("0", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Number(0)) }
                CalcButton(".", type = ButtonType.NUMBER, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Decimal('.')) }
                CalcButton("=", type = ButtonType.EQUALS, modifier = Modifier.weight(1f)) { viewModel.onAction(CalcAction.Equals) }
            }
        }
    }
}
