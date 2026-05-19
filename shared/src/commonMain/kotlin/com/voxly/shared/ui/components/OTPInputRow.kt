package com.voxly.shared.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxly.shared.ui.theme.VoxlyColors

@Composable
fun OTPInputRow(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
) {
    val focusRequester = remember { FocusRequester() }
    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "alpha",
    )

    BasicTextField(
        value = code,
        onValueChange = { if (it.length <= length) onCodeChange(it.filter { c -> c.isDigit() }) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        modifier = Modifier
            .focusRequester(focusRequester)
            .size(1.dp)
            .alpha(0f),
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        repeat(length) { index ->
            val char = code.getOrNull(index)
            val isActive = index == code.length
            val isFilled = char != null
            val shape = RoundedCornerShape(14.dp)

            val (bg, borderColor, borderWidth) = when {
                isActive -> Triple(VoxlyColors.Coral15, VoxlyColors.Coral, 2)
                isFilled -> Triple(VoxlyColors.Coral15, VoxlyColors.Coral.copy(0.5f), 1)
                else     -> Triple(VoxlyColors.White06, VoxlyColors.White12, 1)
            }

            Box(
                modifier = Modifier
                    .width(46.dp)
                    .height(60.dp)
                    .clip(shape)
                    .background(bg)
                    .border(borderWidth.dp, borderColor, shape),
                contentAlignment = Alignment.Center,
            ) {
                if (isFilled) {
                    Text(
                        text = char.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VoxlyColors.TextPrimary,
                    )
                } else if (isActive) {
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(22.dp)
                            .alpha(cursorAlpha)
                            .background(VoxlyColors.Coral),
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}
