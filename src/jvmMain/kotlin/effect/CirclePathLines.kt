package com.github.rusticflare.compose.effect

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.github.rusticflare.compose.BooleanSwitch
import com.github.rusticflare.compose.DoubleSlider
import com.github.rusticflare.compose.IntSlider
import kotlin.math.*
import kotlin.random.Random

private const val MIN_RADIUS = Float.MIN_VALUE.toDouble()
private const val MAX_RADIUS = 900.0
private const val MIN_WIDTH = Float.MIN_VALUE.toDouble()
private const val MAX_WIDTH = 900.0
private const val MIN_STROKE_WIDTH = 1
private const val MAX_STROKE_WIDTH = 10

data class CirclePathLines(
    override val show: Boolean = true,
    override val seed: Int = 0,
    val radius: Double = 500.0,
    val minWidth: Double = 50.0,
    val maxWidth: Double = 300.0,
    val strokeWidth: Int = 3,
    val drawDownwardLines: Boolean = true,
) : Effect<CirclePathLines>(name = "Circle Path Lines") {

    context(DrawScope)
    override fun drawEffect() {
        val circle = Circle(radius = radius.toFloat(), center = center)
        val startAngle = getRandom(seed = 13).nextDouble(from = PI * 2 / 3, until = PI * 4 / 3).toFloat()
        val start = circle.offsetAt(angle = startAngle)
        val points = generateSequence(seed = startAngle + PI.toFloat()) {
            val random = getRandom(seed = (it * 2f.pow(16)).toInt())
            random.nextDouble(from = -PI / 3, until = PI / 3).toFloat()
        }.map { angle ->
            val random = getRandom(seed = (angle * 2f.pow(16)).toInt())
            PolarCoordinate(
                angle = angle,
                distance = random.nextDouble(from = minWidth, until = maxWidth).toFloat(),
            )
        }.runningFold(initial = start, operation = Offset::plus)
            .takeWhile { it in circle }
            .toList()
            .takeUnless { it.isEmpty() }
            ?.let { it.dropLast(1) + circle.offsetAt(angle = circle.center.angleTo(it.last())) }
            ?: listOf(start)
        val path = Path().apply {
            moveTo(points.first())
            points.drop(1).dropLast(1).forEach {
                lineTo(it)
                if (drawDownwardLines) {
                    lineTo(Offset(x = it.x, y = size.height))
                    moveTo(it)
                }
            }
            val angleToFirst = circle.center.angleTo(points.first())
            val angleToLast = circle.center.angleTo(points.last())
            arcToRad(
                rect = circle.rect,
                startAngleRadians = angleToLast,
                sweepAngleRadians = when {
                    angleToLast > angleToFirst -> -(angleToLast - angleToFirst)
                    else -> -angleToLast - (2 * PI.toFloat() - angleToFirst)
                },
                forceMoveTo = false,
            )
        }
        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(width = strokeWidth.toFloat()),
        )
    }

    override fun copyAndSetShow(show: Boolean) = copy(show = show)

    context(LazyListScope)
    override fun settingsPanel(onValueChange: (CirclePathLines) -> Unit) {
        item {
            DoubleSlider(
                radius,
                MIN_RADIUS,
                MAX_RADIUS,
                "Radius"
            ) { newValue -> onValueChange(copy(radius = newValue)) }
        }
        item {
            DoubleSlider(
                minWidth,
                MIN_WIDTH,
                maxWidth - 1,
                "Minimum width"
            ) { newValue -> onValueChange(copy(minWidth = newValue)) }
        }
        item {
            DoubleSlider(
                maxWidth,
                minWidth + 1,
                MAX_WIDTH,
                "Maximum width"
            ) { newValue -> onValueChange(copy(maxWidth = newValue)) }
        }
        item {
            IntSlider(
                strokeWidth,
                MIN_STROKE_WIDTH,
                MAX_STROKE_WIDTH,
                "Stroke width"
            ) { newValue -> onValueChange(copy(strokeWidth = newValue)) }
        }
        item {
            BooleanSwitch(
                drawDownwardLines,
                "Draw downward lines"
            ) { newValue -> onValueChange(copy(drawDownwardLines = newValue)) }
        }
        item {
            IntSlider(
                seed,
                Int.MIN_VALUE,
                Int.MAX_VALUE,
                "Random seed"
            ) { newValue -> onValueChange(copy(seed = newValue)) }
        }
    }
}

data class Circle(
    val radius: Float,
    val center: Offset,
)

fun Circle.randomPoint(random: Random) = center + PolarCoordinate(
    distance = sqrt(random.nextFloat()) * radius,
    angle = random.nextFloat() * 2 * PI.toFloat(),
)

val Circle.rect
    get() = Rect(center = center, radius = radius)

private fun Circle.offsetAt(angle: Float) = center + PolarCoordinate(distance = radius, angle = angle)

private operator fun Circle.contains(offset: Offset) =
    (offset - center).getDistanceSquared() <= radius * radius


data class PolarCoordinate(
    val distance: Float,
    val angle: Float,
)

fun PolarCoordinate.toOffset() = Offset(
    x = (distance * cos(angle)),
    y = (distance * sin(angle)),
)

private fun Offset.angleTo(offset: Offset) = (offset - this).let { atan2(y = it.y, x = it.x) }

operator fun Offset.plus(polarCoordinate: PolarCoordinate) = plus(polarCoordinate.toOffset())

private fun Path.moveTo(offset: Offset) = moveTo(x = offset.x, y = offset.y)

private fun Path.lineTo(offset: Offset) = lineTo(x = offset.x, y = offset.y)

private fun Path.relativeLineTo(dOffset: Offset) = relativeLineTo(dx = dOffset.x, dy = dOffset.y)
