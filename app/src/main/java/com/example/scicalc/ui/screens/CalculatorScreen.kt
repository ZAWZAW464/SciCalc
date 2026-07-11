package com.example.scicalc.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

    Column(modifier = Modifier.fillMaxSize().padding(bottom = 8.dp)) {
        CalculatorDisplay(state = state, modifier = Modifier.weight(0.35f))

        Column(modifier = Modifier.weight(0.65f).padding(horizontal = 8.dp, vertical = 4.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton(label = "C", type = ButtonType.ACTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Clear) })
                CalcButton(label = "⌫", type = ButtonType.ACTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Delete) })
                CalcButton(label = "(", type = ButtonType.ACTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.OpenParen) })
                CalcButton(label = ")", type = ButtonType.ACTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.CloseParen) })
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton(label = "sin", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Function("sin")) })
                CalcButton(label = "cos", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Function("cos")) })
                CalcButton(label = "tan", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Function("tan")) })
                CalcButton(label = "ln", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Function("ln")) })
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton(label = "log", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Function("log")) })
                CalcButton(label = "√", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Function("sqrt")) })
                CalcButton(label = "Xʸ", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Power(1.0)) })
                CalcButton(label = "!", type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Function("fact")) })
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton(label = "7", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Number(7)) })
                CalcButton(label = "8", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Number(8)) })
                CalcButton(label = "9", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Number(9)) })
                CalcButton(label = "×", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Operator("×")) })
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton(label = "4", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Number(4)) })
                CalcButton(label = "5", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Number(5)) })
                CalcButton(label = "6", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Number(6)) })
                CalcButton(label = "÷", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Operator("÷")) })
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton(label = "1", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Number(1)) })
                CalcButton(label = "2", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Number(2)) })
                CalcButton(label = "3", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Number(3)) })
                CalcButton(label = "−", type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Operator("−")) })
            }
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                CalcButton(label = "±", type = ButtonType.ACTION, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.SignToggle) })
                CalcButton(label = "0", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Number(0)) })
                CalcButton(label = ".", modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Decimal('.')) })
                CalcButton(label = "=", type = ButtonType.EQUALS, modifier = Modifier.weight(1f), onClick = { viewModel.onAction(CalcAction.Equals) })
            }
        }
    }
}
