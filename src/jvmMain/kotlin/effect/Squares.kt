package com.github.rusticflare.compose.effect

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.github.rusticflare.compose.DoubleSlider
import com.github.rusticflare.compose.IntSlider
import java.util.*

private const val MIN_SQUARES = 1
private const val MAX_SQUARES = 100
private const val MIN_FILL_CHANCE = 0.0
private const val MAX_FILL_CHANCE = 1.0

data class Squares(
    override val show: Boolean = true,
    override val seed: Int = 0,
    val squares: Int = 25,
    val fillChance: Double = 0.0,
) : Effect<Squares>(name = "Squares") {

    context(DrawScope)
    override fun drawEffect() {
        val gaps = squares + 1
        val steps = squares * gaps * 2
        val stepWidth = size.width / steps
        val stepHeight = size.height / steps
        val gapWidth = squares * stepWidth
        val gapHeight = squares * stepHeight
        val squareWidth = gaps * stepWidth
        val squareHeight = gaps * stepHeight
        val iteratorWidth = gapWidth + squareWidth
        val iteratorHeight = gapHeight + squareHeight
        val squareSize = Size(width = squareWidth, height = squareHeight)
        for (xCenter in generateSequence(gapWidth + (squareWidth / 2)) { it + iteratorWidth }.takeWhile { it < size.width }) {
            for (yCenter in generateSequence(gapHeight + (squareHeight / 2)) { it + iteratorHeight }.takeWhile { it < size.height }) {
                val random = getRandom(Objects.hash(xCenter, yCenter))
                val sizeModifier = random.nextDouble(from = 0.5, until = 1.5).toFloat()
                val modifiedSquareSize = squareSize * sizeModifier
                val fill = random.nextDouble()
                drawRect(
                    color = Color.White,
                    topLeft = Offset(
                        x = xCenter - (modifiedSquareSize.width / 2),
                        y = yCenter - (modifiedSquareSize.height / 2),
                    ),
                    size = modifiedSquareSize,
                    style = if (fill > fillChance) Stroke(width = 2f) else Fill
                )
            }
        }
    }

    override fun copyAndSetShow(show: Boolean) = copy(show = show)

    context(LazyListScope)
    override fun settingsPanel(onValueChange: (Squares) -> Unit) {
        item {
            IntSlider(
                squares,
                MIN_SQUARES,
                MAX_SQUARES,
                "Squares"
            ) { newValue -> onValueChange(copy(squares = newValue)) }
        }
        item {
            DoubleSlider(
                fillChance,
                MIN_FILL_CHANCE,
                MAX_FILL_CHANCE,
                "Fill chance"
            ) { newValue -> onValueChange(copy(fillChance = newValue)) }
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