package com.github.rusticflare.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val padding = Modifier.padding(10.dp)

@Composable
fun DoubleSlider(
    initialValue: Double,
    minValue: Double,
    maxValue: Double,
    label: String,
    onChange: (Double) -> Unit,
) {
    var value by remember { mutableStateOf(initialValue.toFloat()) }
    Column(
        modifier = padding
    ) {
        Row {
            Text(text = "$label: ", fontSize = 14.sp)
            Text(text = "%.2f".format(value), fontSize = 14.sp)
        }
        Slider(
            value = value,
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            onValueChange = {
                value = it
                onChange(value.toDouble())
            },
        )
    }
}

@Composable
fun BooleanSwitch(
    initialValue: Boolean,
    label: String,
    onChange: (Boolean) -> Unit
) {
    var value by remember { mutableStateOf(initialValue) }
    Text(modifier = padding, text = label)
    Switch(
        modifier = padding,
        checked = value,
        onCheckedChange = {
            value = it
            onChange(value)
        },
    )
}
