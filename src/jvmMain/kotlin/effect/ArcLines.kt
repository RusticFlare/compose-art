package com.github.rusticflare.compose.effect

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.github.rusticflare.compose.DoubleSlider
import com.github.rusticflare.compose.IntSlider
import kotlin.math.PI

private const val MIN_RADIUS = Float.MIN_VALUE.toDouble()
private const val MAX_RADIUS = 900.0
private const val MIN_SWEEP = Float.MIN_VALUE.toDouble()
private const val MAX_SWEEP = 360.0
private const val MIN_ARCS = 2
private const val MAX_ARCS = 10
private const val MIN_LINES = 1
private const val MAX_LINES = 100
private const val MIN_STROKE_WIDTH = 1
private const val MAX_STROKE_WIDTH = 10

data class ArcLines(
    override val show: Boolean = true,
    override val seed: Int = 0,
    val minSweep: Double = 30.0,
    val maxSweep: Double = 330.0,
    val minRadius: Double = 50.0,
    val maxRadius: Double = 300.0,
    val arcs: Int = 3,
    val lines: Int = 3,
    val centerRange: Double = 400.0,
    val strokeWidth: Int = 3,
) : Effect<ArcLines>(name = "Arc Lines") {

    context(DrawScope)
    override fun drawEffect() {
        val possibleCenters = Circle(
            radius = centerRange.toFloat(),
            center = center,
        )
        val arcPoints = (0 until arcs).map {
            val random = getRandom(it)
            Arc(
                center = possibleCenters.randomPoint(random),
                radius = random.nextDouble(from = minRadius, until = maxRadius).toFloat(),
                startingAngle = random.nextDouble(until = 2 * PI).toFloat(),
                sweepingAngle = Math.toRadians(random.nextDouble(from = minSweep, until = maxSweep)).toFloat(),
            ).points(steps = lines)
        }
        arcPoints.runningFold(initial = buildList { repeat(lines + 1) { add(emptyList<Offset>()) } }) { acc, offsets ->
            acc.zip(offsets) { list, offset ->
                list.forEach {
                    drawLine(
                        color = Color.White,
                        start = offset,
                        end = it,
                        strokeWidth = strokeWidth.toFloat(),
                    )
                }
                list + offset
            }
        }
    }

    override fun copyAndSetShow(show: Boolean) = copy(show = show)

    context(LazyListScope)
    override fun settingsPanel(onValueChange: (ArcLines) -> Unit) {
        item {
            IntSlider(
                lines,
                MIN_LINES,
                MAX_LINES,
                "Lines"
            ) { newValue -> onValueChange(copy(lines = newValue)) }
        }
        item {
            IntSlider(
                arcs,
                MIN_ARCS,
                MAX_ARCS,
                "Arcs"
            ) { newValue -> onValueChange(copy(arcs = newValue)) }
        }
        item {
            DoubleSlider(
                minSweep,
                MIN_SWEEP,
                maxSweep - 1,
                "Min sweep"
            ) { newValue -> onValueChange(copy(minSweep = newValue)) }
        }
        item {
            DoubleSlider(
                maxSweep,
                minSweep + 1,
                MAX_SWEEP,
                "Max sweep"
            ) { newValue -> onValueChange(copy(maxSweep = newValue)) }
        }
        item {
            DoubleSlider(
                minRadius,
                MIN_RADIUS,
                maxRadius - 1,
                "Min radius"
            ) { newValue -> onValueChange(copy(minRadius = newValue)) }
        }
        item {
            DoubleSlider(
                maxRadius,
                minRadius + 1,
                MAX_RADIUS,
                "Max radius"
            ) { newValue -> onValueChange(copy(maxRadius = newValue)) }
        }
        item {
            DoubleSlider(
                centerRange,
                Float.MIN_VALUE.toDouble(),
                1000.0,
                "Center Range"
            ) { newValue -> onValueChange(copy(centerRange = newValue)) }
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
            IntSlider(
                seed,
                Int.MIN_VALUE,
                Int.MAX_VALUE,
                "Random seed"
            ) { newValue -> onValueChange(copy(seed = newValue)) }
        }
    }
}

private data class Arc(
    val center: Offset,
    val radius: Float,
    val startingAngle: Float,
    val sweepingAngle: Float,
)

private fun Arc.points(steps: Int): List<Offset> {
    val increment = sweepingAngle / steps
    return generateSequence(startingAngle) { it + increment }
        .take(steps + 1)
        .map { center + PolarCoordinate(distance = radius, angle = it) }
        .toList()
}
