package com.github.rusticflare.compose.effect

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.github.rusticflare.compose.DoubleSlider
import com.github.rusticflare.compose.IntSlider
import kotlin.math.*

private const val MIN_RADIUS = Float.MIN_VALUE.toDouble()
private const val MAX_RADIUS = 1000.0
private const val MIN_RING_RADIUS = Float.MIN_VALUE.toDouble()
private const val MAX_RING_RADIUS = 100.0
private const val MIN_RINGS = 100
private const val MAX_RINGS = 1000
private const val MIN_ALPHA = 1
private const val MAX_ALPHA = 2

data class Sunflower(
    override val show: Boolean = true,
    override val seed: Int = 0,
    val rings: Int = 500,
    val alpha: Int = 1,
    val radius: Double = 550.0,
    val ringRadius: Double = 5.0,
) : Effect<Sunflower>(name = "Sunflower") {

    context(DrawScope)
    override fun drawEffect() {
        val b = round(alpha * sqrt(rings.toFloat()))
        val phi = (sqrt(5f) + 1) / 2
        for (k in 1..rings) {
            val r = when {
                k > rings - b -> 1f
                else -> sqrt(k - 0.5f) / sqrt(rings - (b + 1) / 2)
            } * radius
            val theta = 2 * PI * k / phi.pow(2)
            drawCircle(
                color = Color.White,
                radius = ringRadius.toFloat(),
                center = center + Offset(
                    x = (r * cos(theta)).toFloat(),
                    y = (r * sin(theta)).toFloat(),
                ),
                style = Stroke(width = 1f)
            )
        }
    }

    override fun copyAndSetShow(show: Boolean) = copy(show = show)

    context(LazyListScope)
    override fun settingsPanel(onValueChange: (Sunflower) -> Unit) {
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
                rings,
                MIN_RINGS,
                MAX_RINGS,
                "Rings"
            ) { newValue -> onValueChange(copy(rings = newValue)) }
        }
        item {
            IntSlider(
                alpha,
                MIN_ALPHA,
                MAX_ALPHA,
                "Alpha"
            ) { newValue -> onValueChange(copy(alpha = newValue)) }
        }
        item {
            DoubleSlider(
                ringRadius,
                MIN_RING_RADIUS,
                MAX_RING_RADIUS,
                "Ring radius"
            ) { newValue -> onValueChange(copy(ringRadius = newValue)) }
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
