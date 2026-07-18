package com.example.scicalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scicalc.domain.CalculatorViewModel
import com.example.scicalc.ui.screens.CalculatorScreen
import com.example.scicalc.ui.theme.SciCalcTheme
import com.example.scicalc.ui.theme.rememberIsDarkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme = rememberIsDarkTheme()
            SciCalcTheme(darkTheme = isDarkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: CalculatorViewModel = viewModel()
                    CalculatorScreen(
                        viewModel = viewModel,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}