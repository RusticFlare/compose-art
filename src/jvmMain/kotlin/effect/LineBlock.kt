package com.github.rusticflare.compose.effect

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.github.rusticflare.compose.DoubleSlider
import com.github.rusticflare.compose.IntSlider

private const val MIN_POS = 0.0
private const val MAX_POS = 1800.0
private const val MIN_LINES = 1
private const val MAX_LINES = 1000

data class LineBlock(
    override val show: Boolean = true,
    override val seed: Int = 0,
    val startXA: Double = 100.0,
    val startYA: Double = 100.0,
    val startXB: Double = 100.0,
    val startYB: Double = 900.0,
    val endXA: Double = 900.0,
    val endYA: Double = 900.0,
    val endXB: Double = 900.0,
    val endYB: Double = 100.0,
    val lines: Int = 15,
) : Effect<LineBlock>(name = "Line Block") {

    context(DrawScope)
    override fun drawEffect() {
        val startA = Offset(
            x = startXA.toFloat(),
            y = startYA.toFloat(),
        )
        val startB = Offset(
            x = startXB.toFloat(),
            y = startYB.toFloat(),
        )
        val startLine = startB - startA
        val endA = Offset(
            x = endXA.toFloat(),
            y = endYA.toFloat(),
        )
        val endB = Offset(
            x = endXB.toFloat(),
            y = endYB.toFloat(),
        )
        val endLine = endB - endA
        for (line in 0..lines) {
            drawLine(
                color = Color.White,
                start = startA + (startLine * line.toFloat() / lines.toFloat()),
                end = endA + (endLine * line.toFloat() / lines.toFloat()),
            )
        }
    }

    override fun copyAndSetShow(show: Boolean) = copy(show = show)

    context(LazyListScope)
    override fun settingsPanel(onValueChange: (LineBlock) -> Unit) {
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
                startXA,
                MIN_POS,
                MAX_POS,
                "Start X - A"
            ) { newValue -> onValueChange(copy(startXA = newValue)) }
        }
        item {
            DoubleSlider(
                startYA,
                MIN_POS,
                MAX_POS,
                "Start Y - A"
            ) { newValue -> onValueChange(copy(startYA = newValue)) }
        }
        item {
            DoubleSlider(
                startXB,
                MIN_POS,
                MAX_POS,
                "Start X - B"
            ) { newValue -> onValueChange(copy(startXB = newValue)) }
        }
        item {
            DoubleSlider(
                startYB,
                MIN_POS,
                MAX_POS,
                "Start Y - B"
            ) { newValue -> onValueChange(copy(startYB = newValue)) }
        }
        item {
            DoubleSlider(
                endXA,
                MIN_POS,
                MAX_POS,
                "End X - A"
            ) { newValue -> onValueChange(copy(endXA = newValue)) }
        }
        item {
            DoubleSlider(
                endYA,
                MIN_POS,
                MAX_POS,
                "End Y - A"
            ) { newValue -> onValueChange(copy(endYA = newValue)) }
        }
        item {
            DoubleSlider(
                endXB,
                MIN_POS,
                MAX_POS,
                "End X - B"
            ) { newValue -> onValueChange(copy(endXB = newValue)) }
        }
        item {
            DoubleSlider(
                endYB,
                MIN_POS,
                MAX_POS,
                "End Y - B"
            ) { newValue -> onValueChange(copy(endYB = newValue)) }
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
