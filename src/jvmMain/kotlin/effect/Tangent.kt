package com.github.rusticflare.compose.effect

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.github.rusticflare.compose.DoubleSlider
import com.github.rusticflare.compose.IntSlider
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val MIN_HEIGHT = Float.MIN_VALUE.toDouble()
private const val MAX_HEIGHT = 2000.0
private const val MIN_TANGENTS = 1
private const val MAX_TANGENTS = 1000
private const val MIN_RADIUS = Float.MIN_VALUE.toDouble()
private const val MAX_RADIUS = 1000.0

data class Tangent(
    override val show: Boolean = true,
    override val seed: Int = 0,
    val tangents: Int = 15,
    val maxHeight: Double = 500.0,
    val radius: Double = 500.0,
) : Effect<Tangent>(name = "Tangent") {

    context(DrawScope)
    override fun drawEffect() {
        repeat(tangents) { tangent ->
            val random = getRandom(tangent)
            val angle = random.nextDouble(from = 0.0, until = 2 * PI)
            val point = center + Offset(
                x = (radius * cos(angle)).toFloat(),
                y = (radius * sin(angle)).toFloat(),
            )
            val height = random.nextDouble(from = 0.0, until = maxHeight)
            val tangentAngle = angle + (PI / 2)
            val offset = Offset(
                x = (height * cos(tangentAngle)).toFloat(),
                y = (height * sin(tangentAngle)).toFloat(),
            )
            val start = point + offset
            val end = point - offset
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, Color.White, Color.Transparent),
                    start = start,
                    end = end,
                ),
                start = start,
                end = end,
                strokeWidth = 3f,
            )
        }
    }

    override fun copyAndSetShow(show: Boolean) = copy(show = show)

    context(LazyListScope)
    override fun settingsPanel(onValueChange: (Tangent) -> Unit) {
        item {
            DoubleSlider(
                maxHeight,
                MIN_HEIGHT,
                MAX_HEIGHT,
                "Max height"
            ) { newValue -> onValueChange(copy(maxHeight = newValue)) }
        }
        item {
            DoubleSlider(
                radius,
                MIN_RADIUS,
                MAX_RADIUS,
                "Radius"
            ) { newValue -> onValueChange(copy(radius = newValue)) }
        }
        item {
            IntSlider(
                tangents,
                MIN_TANGENTS,
                MAX_TANGENTS,
                "Tangents"
            ) { newValue -> onValueChange(copy(tangents = newValue)) }
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