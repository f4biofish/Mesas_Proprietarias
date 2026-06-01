package com.librarys.ferreira.core.ui.common

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.librarys.ferreira.core.ui.theme.AppTheme

@Composable
fun TextFieldDefault(
    modifier: Modifier = Modifier,
    initialValue: String = "",
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    prefix: String? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    maxLines: Int = if(singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {

    val leadingIconComposable: (@Composable () -> Unit)? =
        if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else null

    val trailingIconComposable: (@Composable () -> Unit)? =
        if (trailingIcon != null) {
            {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else null

    OutlinedTextField(
        modifier = modifier,
        value = initialValue,
        onValueChange = {},
        enabled = enabled,
        textStyle = textStyle,
        label = {
            if(!label.isNullOrEmpty()){
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        placeholder = {
            if(!placeholder.isNullOrEmpty()){
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        },
        leadingIcon = leadingIconComposable,
        trailingIcon = trailingIconComposable,
        prefix = {
            if(!prefix.isNullOrEmpty()) {
                Text(
                    text = prefix,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        },
        isError = isError,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        shape = shape,
        colors = colors
    )

}


@Preview
@Composable
private fun TextFieldDefaultPreview() {
    AppTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            TextFieldDefault(
                initialValue = "Teste",
                placeholder = "Valor",
                prefix = "21"
            )
        }
    }
}