package com.example.scicalc.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scicalc.domain.CalculatorState
import com.example.scicalc.ui.theme.*

@Composable
fun CalculatorDisplay(state: CalculatorState, modifier: Modifier = Modifier) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) DisplayBgDark else DisplayBgLight

    Column(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)).background(bgColor)
            .padding(horizontal = 24.dp, vertical = 20.dp).heightIn(min = 140.dp),
        horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom
    ) {
        Text(text = state.expression.ifEmpty { "" }, fontSize = if (state.expression.length > 25) 18.sp else 22.sp,
            color = if (isDark) ExpressionTextDark else ExpressionTextLight, fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.End, maxLines = 3, overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp).animateContentSize())

        AnimatedVisibility(visible = state.errorMessage != null, enter = fadeIn() + slideInVertically(), exit = fadeOut() + slideOutVertically()) {
            state.errorMessage?.let { error ->
                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.errorContainer, modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(text = error, color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), maxLines = 2)
                }
            }
        }

        Text(text = state.displayResult,
            fontSize = if (state.displayResult.length > 12) 34.sp else if (state.displayResult.length > 8) 42.sp else 48.sp,
            fontWeight = FontWeight.Bold, color = if (isDark) DisplayTextDark else DisplayTextLight,
            fontFamily = FontFamily.Default, textAlign = TextAlign.End, maxLines = 1, overflow = TextOverflow.Ellipsis, softWrap = false,
            modifier = Modifier.fillMaxWidth())
    }
}