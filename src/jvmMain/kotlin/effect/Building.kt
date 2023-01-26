package com.github.rusticflare.compose.effect

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.github.rusticflare.compose.DoubleSlider
import com.github.rusticflare.compose.IntSlider
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val MIN_WIDTH = 0.0
private const val MAX_WIDTH = 1800.0
private const val MIN_LINES = 1
private const val MAX_LINES = 100

data class Building(
    override val show: Boolean = true,
    override val seed: Int = 0,
    val height: Double = 1000.0,
    val startHeight: Double = 1400.0,
    val minWidth: Double = 50.0,
    val maxWidth: Double = 300.0,
    val lines: Int = 25,
) : Effect<Building>(name = "Building") {

    context(DrawScope)
    override fun drawEffect() {
        val initialPoints = (0..lines)
            .map { startHeight - (height * it / lines) }
            .map { y -> Offset(x = 0f, y = y.toFloat()) }
        var end = false
        generateSequence(initialPoints) { points ->
            val random = getRandom(points.first().x.toInt())
            val angle = random.nextDouble(from = -PI / 3, until = PI / 3)
            val distance = random.nextDouble(from = minWidth, until = maxWidth)
            val offset = Offset(
                x = (distance * cos(angle)).toFloat(),
                y = (distance * sin(angle)).toFloat(),
            )
            points.takeUnless { end }?.map {
                (it + offset).let { off ->
                    if (off.x > size.width) {
                        end = true
                        off.copy(x = size.width)
                    } else {
                        off
                    }
                }
            }
        }.zipWithNext(List<Offset>::zip).flatten().forEach { (start, end) ->
            drawLine(
                color = Color.White,
                start = start,
                end = end,
                strokeWidth = 2f,
            )
        }
    }

    override fun copyAndSetShow(show: Boolean) = copy(show = show)

    context(LazyListScope)
    override fun settingsPanel(onValueChange: (Building) -> Unit) {
        item {
            IntSlider(
                lines,
                MIN_LINES,
                MAX_LINES,
                "Lines"
            ) { newValue -> onValueChange(copy(lines = newValue)) }
        }
        item {
            DoubleSlider(
                height,
                MIN_WIDTH,
                MAX_WIDTH,
                "Height"
            ) { newValue -> onValueChange(copy(height = newValue)) }
        }
        item {
            DoubleSlider(
                startHeight,
                MIN_WIDTH,
                MAX_WIDTH,
                "Start height"
            ) { newValue -> onValueChange(copy(startHeight = newValue)) }
        }
        item {
            DoubleSlider(
                minWidth,
                MIN_WIDTH,
                maxWidth,
                "Minimum width"
            ) { newValue -> onValueChange(copy(minWidth = newValue)) }
        }
        item {
            DoubleSlider(
                maxWidth,
                minWidth,
                MAX_WIDTH,
                "Maximum width"
            ) { newValue -> onValueChange(copy(maxWidth = newValue)) }
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
