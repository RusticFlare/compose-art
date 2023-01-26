package com.github.rusticflare.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill

fun DrawScope.drawCircleSegment(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    radius: Float = size.minDimension / 2f,
    center: Offset = this.center,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    alpha: Float = 1f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode
) {
    val diameter = radius * 2
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = center - Offset(x = radius, y = radius),
        size = Size(width = diameter, height = diameter),
        alpha = alpha,
        style = style,
        colorFilter = colorFilter,
        blendMode = blendMode,
    )
}

fun DrawScope.drawCircleSegment(
    brush: Brush,
    startAngle: Float,
    sweepAngle: Float,
    radius: Float = size.minDimension / 2.0f,
    center: Offset = this.center,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    alpha: Float = 1f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode
) {
    val diameter = radius * 2
    drawArc(
        brush = brush,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = center - Offset(x = radius, y = radius),
        size = Size(width = diameter, height = diameter),
        alpha = alpha,
        style = style,
        colorFilter = colorFilter,
        blendMode = blendMode,
    )
}

fun DrawScope.drawCircleSector(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    radius: Float = size.minDimension / 2f,
    center: Offset = this.center,
    /*@FloatRange(from = 0.0, to = 1.0)*/
    alpha: Float = 1f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode
) {
    val diameter = radius * 2
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        topLeft = center - Offset(x = radius, y = radius),
        size = Size(width = diameter, height = diameter),
        alpha = alpha,
        style = style,
        colorFilter = colorFilter,
        blendMode = blendMode,
    )
}