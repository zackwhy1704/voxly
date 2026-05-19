package com.voxly.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxly.shared.ui.theme.VoxlyColors

@Composable
fun VoxlyInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    singleLine: Boolean = true,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(14.dp)
    val isFocused = value.isNotEmpty()

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = VoxlyColors.TextSecondary,
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(VoxlyColors.SurfaceElevated, shape),
            placeholder = {
                Text(placeholder, color = VoxlyColors.TextTertiary, fontSize = 14.sp)
            },
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor         = VoxlyColors.TextPrimary,
                unfocusedTextColor       = VoxlyColors.TextPrimary,
                focusedBorderColor       = VoxlyColors.Coral,
                unfocusedBorderColor     = VoxlyColors.White12,
                errorBorderColor         = VoxlyColors.Error,
                cursorColor              = VoxlyColors.Coral,
                focusedContainerColor    = VoxlyColors.SurfaceElevated,
                unfocusedContainerColor  = VoxlyColors.SurfaceElevated,
            ),
            shape = shape,
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            text = if (passwordVisible) "👁" else "🙈",
                            fontSize = 16.sp,
                        )
                    }
                }
            } else trailingIcon,
            textStyle = LocalTextStyle.current.copy(
                color = VoxlyColors.TextPrimary,
                fontSize = 14.sp,
            ),
        )
    }
}
