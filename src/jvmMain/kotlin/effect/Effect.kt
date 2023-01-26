package com.github.rusticflare.compose.effect

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.rusticflare.compose.BooleanSwitch
import kotlin.random.Random

abstract class Effect<SELF : Effect<SELF>>(val name: String) {

    abstract val show: Boolean
    abstract val seed: Int

    context(DrawScope)
    fun draw() {
        if (show) drawEffect()
    }

    context(DrawScope)
    protected abstract fun drawEffect()

    context(LazyListScope)
    fun settings(onValueChange: (SELF) -> Unit) {
        item {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
            )
        }
        item {
            BooleanSwitch(
                initialValue = show,
                label = "Draw",
                onChange = { onValueChange(copyAndSetShow(show = it)) }
            )
        }
        if (show) {
            settingsPanel(onValueChange)
        }
    }

    protected abstract fun copyAndSetShow(show: Boolean): SELF

    context(LazyListScope)
    protected abstract fun settingsPanel(onValueChange: (SELF) -> Unit)
}

fun <SELF : Effect<SELF>> SELF.getRandom(seed: Int) =
    Random(seed = Random(this.seed).nextLong() * Random(seed).nextLong())
