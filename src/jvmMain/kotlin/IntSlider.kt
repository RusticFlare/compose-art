package com.github.rusticflare.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IntSlider(
    initialValue: Int,
    minValue: Int,
    maxValue: Int,
    label: String,
    onChange: (Int) -> Unit,
) {
    var value by remember { mutableStateOf(initialValue.toFloat()) }
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Row {
            Text(text = "$label: ", fontSize = 14.sp)
            Text(text = "%d".format(value.toInt()), fontSize = 14.sp)
        }
        Slider(
            value = value,
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            onValueChange = {
                value = it
                onChange(value.toInt())
            },
        )
    }
}