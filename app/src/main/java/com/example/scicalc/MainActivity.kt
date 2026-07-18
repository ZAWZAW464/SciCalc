package com.example.scicalc

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scicalc.domain.CalculatorViewModel
import com.example.scicalc.ui.screens.CalculatorScreen
import com.example.scicalc.ui.theme.SciCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent {
            SciCalcTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: CalculatorViewModel = viewModel()
                    CalculatorScreen(viewModel = viewModel)
                }
            }
        }
    }
}