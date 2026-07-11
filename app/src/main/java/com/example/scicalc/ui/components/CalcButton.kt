package com.example.scicalc.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scicalc.ui.theme.*

enum class ButtonType { NUMBER, OPERATOR, FUNCTION, EQUALS, ACTION }

@Composable
fun CalcButton(
    label: String,
    subLabel: String? = null,
    type: ButtonType = ButtonType.NUMBER,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh, dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    val interactionSource = remember { MutableInteractionSource() }
    val pressed = interactionSource.collectIsPressedAsState().value
    LaunchedEffect(pressed) { isPressed = pressed }

    val bgColor = when (type) {
        ButtonType.NUMBER   -> if (isDark) NumberButtonDark else NumberButtonLight
        ButtonType.OPERATOR -> if (isDark) OperatorButtonDark else OperatorButtonLight
        ButtonType.FUNCTION -> if (isDark) FunctionButtonDark else FunctionButtonLight
        ButtonType.EQUALS   -> if (isDark) EqualsButtonDark else EqualsButtonLight
        ButtonType.ACTION   -> if (isDark) FunctionButtonDark.copy(alpha = 0.7f) else FunctionButtonLight.copy(alpha = 0.8f)
    }

    val contentColor = when (type) {
        ButtonType.NUMBER   -> if (isDark) DarkOnSurface else LightOnSurface
        ButtonType.OPERATOR -> if (isDark) DarkPrimary else LightPrimary
        ButtonType.FUNCTION -> if (isDark) DarkPrimary else LightPrimary
        ButtonType.EQUALS   -> if (isDark) Color(0xFF002114) else Color.White
        ButtonType.ACTION   -> if (isDark) DarkPrimary else LightPrimary
    }

    Button(
        onClick = onClick,
        modifier = modifier.scale(scale).height(if (type == ButtonType.EQUALS) 56.dp else 52.dp),
        shape = RoundedCornerShape(if (type == ButtonType.EQUALS) 20.dp else 14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor, contentColor = contentColor),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp, pressedElevation = 0.dp),
        interactionSource = interactionSource
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = label, fontSize = if (type == ButtonType.NUMBER) 22.sp else 16.sp, fontWeight = if (type == ButtonType.NUMBER) FontWeight.Medium else FontWeight.SemiBold, textAlign = TextAlign.Center, maxLines = 1)
            if (subLabel != null) Text(text = subLabel, fontSize = 9.sp, color = contentColor.copy(alpha = 0.6f), maxLines = 1)
        }
    }
}
