package com.github.rusticflare.compose.effect

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.github.rusticflare.compose.DoubleSlider
import com.github.rusticflare.compose.IntSlider
import java.lang.Math.PI
import kotlin.math.*

private const val MIN_BASE_RADIUS = Float.MIN_VALUE.toDouble()
private const val MAX_BASE_RADIUS = 2000.0
private const val MIN_CENTER_RANGE = Float.MIN_VALUE.toDouble()
private const val MAX_CENTER_RANGE = 2000.0
private const val MIN_RADIUS_RANGE = Float.MIN_VALUE.toDouble()
private const val MAX_RADIUS_RANGE = 2000.0
private const val MIN_RINGS = 1
private const val MAX_RINGS = 100
private const val MIN_STROKE_WIDTH = 1.0
private const val MAX_STROKE_WIDTH = 100.0

data class Coffee(
    override val show: Boolean = true,
    override val seed: Int = 0,
    val rings: Int = 15,
    val baseRadius: Double = 550.0,
    val centerRange: Double = 100.0,
    val radiusRange: Double = 250.0,
    val minimumStrokeWidth: Double = 2.0,
    val maximumStrokeWidth: Double = 7.0,
) : Effect<Coffee>(name = "Coffee") {

    context(DrawScope)
    override fun drawEffect() {
        repeat(rings) { ring ->
            val random = getRandom(seed = ring)
            val startAngle = random.nextDouble(from = 0.0, until = 360.0).toFloat()
            val sweepAngle = random.nextDouble(from = 60.0, until = 355.0).toFloat()

            val centerOffsetRadius = sqrt(random.nextFloat()) * centerRange
            val centerOffsetRadians = random.nextDouble(from = -PI, until = PI)
            val circleCenter = center + Offset(
                x = (sin(centerOffsetRadians) * centerOffsetRadius).toFloat(),
                y = (cos(centerOffsetRadians) * centerOffsetRadius).toFloat(),
            )
            val radius = (baseRadius + random.nextDouble(from = -radiusRange, until = radiusRange)).toFloat()
            val radians = Math.toRadians((startAngle + (sweepAngle / 2)).toDouble()).toFloat()
            val brushCenterOffset = Offset(
                x = radius * cos(radians),
                y = radius * sin(radians),
            )
            val brush = Brush.radialGradient(
                listOf(Color.White, Color.Transparent),
                center = circleCenter + brushCenterOffset,
                radius = radius * sin(Math.toRadians((sweepAngle / 4).toDouble())).toFloat() * 2,
            )
            drawCircle(
                brush = brush,
                center = circleCenter,
                radius = radius,
                style = Stroke(
                    width = random.nextDouble(from = minimumStrokeWidth, until = maximumStrokeWidth).toFloat(),
                    cap = StrokeCap.Round,
                )
            )
        }
    }

    override fun copyAndSetShow(show: Boolean) = copy(show = show)

    context(LazyListScope)
    override fun settingsPanel(onValueChange: (Coffee) -> Unit) {
        item {
            DoubleSlider(
                baseRadius,
                max(MIN_BASE_RADIUS, radiusRange),
                MAX_BASE_RADIUS,
                "Base radius"
            ) { newValue -> onValueChange(copy(baseRadius = newValue)) }
        }
        item {
            IntSlider(
                rings,
                MIN_RINGS,
                MAX_RINGS,
                "Rings"
            ) { newValue -> onValueChange(copy(rings = newValue)) }
        }
        item {
            DoubleSlider(
                centerRange,
                MIN_CENTER_RANGE,
                MAX_CENTER_RANGE,
                "Center range"
            ) { newValue -> onValueChange(copy(centerRange = newValue)) }
        }
        item {
            DoubleSlider(
                radiusRange,
                MIN_RADIUS_RANGE,
                min(baseRadius, MAX_RADIUS_RANGE),
                "Radius range"
            ) { newValue -> onValueChange(copy(radiusRange = newValue)) }
        }
        item {
            IntSlider(
                seed,
                Int.MIN_VALUE,
                Int.MAX_VALUE,
                "Random seed"
            ) { newValue -> onValueChange(copy(seed = newValue)) }
        }
        item {
            DoubleSlider(
                minimumStrokeWidth,
                MIN_STROKE_WIDTH,
                maximumStrokeWidth - 1,
                "Minimum stroke width"
            ) { newValue -> onValueChange(copy(minimumStrokeWidth = newValue)) }
        }
        item {
            DoubleSlider(
                maximumStrokeWidth,
                minimumStrokeWidth + 1,
                MAX_STROKE_WIDTH,
                "Maximum stroke width"
            ) { newValue -> onValueChange(copy(maximumStrokeWidth = newValue)) }
        }
    }
}
