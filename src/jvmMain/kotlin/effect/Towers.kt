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
import kotlin.math.sqrt

private const val MIN_HEIGHT = Float.MIN_VALUE.toDouble()
private const val MAX_HEIGHT = 2000.0
private const val MIN_ANGLE = -PI / 2
private const val MAX_ANGLE = PI / 2
private const val MIN_TOWERS = 1
private const val MAX_TOWERS = 1000

data class Towers(
    override val show: Boolean = true,
    override val seed: Int = 0,
    val towers: Int = 15,
    val maxHeight: Double = 550.0,
    val angle: Double = 0.0,
) : Effect<Towers>(name = "Towers") {
    context(DrawScope)
    override fun drawEffect() {
        val towerWidth = size.width / towers
        (generateSequence(0f) { it + towerWidth }.takeWhile { it < size.width } + size.width)
            .zipWithNext()
            .forEach { (l, r) ->
                val random = getRandom(l.toInt())
                val height = ((1 - sqrt(random.nextFloat())) * maxHeight).toFloat()
                val start = Offset(
                    x = (l + r) / 2,
                    y = size.height,
                )
                val angle = random.nextDouble(from = -PI / 3, until = PI / 3)
                val end = start - Offset(
                    x = (height * sin(angle)).toFloat(),
                    y = (height * cos(angle)).toFloat(),
                )
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.White, Color.Transparent),
                        start = start,
                        end = end,
                    ),
                    start = start,
                    end = end,
                    strokeWidth = r - l,
                )
            }
    }

    override fun copyAndSetShow(show: Boolean) = copy(show = show)

    context(LazyListScope)
    override fun settingsPanel(onValueChange: (Towers) -> Unit) {
        item {
            DoubleSlider(
                maxHeight,
                MIN_HEIGHT,
                MAX_HEIGHT,
                "Max height"
            ) { newValue -> onValueChange(copy(maxHeight = newValue)) }
        }
        item {
            IntSlider(
                towers,
                MIN_TOWERS,
                MAX_TOWERS,
                "Towers"
            ) { newValue -> onValueChange(copy(towers = newValue)) }
        }
        item {
            DoubleSlider(
                angle,
                MIN_ANGLE,
                MAX_ANGLE,
                "Angle"
            ) { newValue -> onValueChange(copy(angle = newValue)) }
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
