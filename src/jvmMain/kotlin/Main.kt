package com.github.rusticflare.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.rusticflare.compose.effect.*

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(size = DpSize(width = 1500.dp, height = 1000.dp)),
        resizable = false,
        title = "rustic.flare",
    ) {
        MaterialTheme(colors = darkColors()) {
            var arcLines by remember { mutableStateOf(ArcLines(show = true)) }
            var circlePathLines by remember { mutableStateOf(CirclePathLines(show = false)) }
            var building by remember { mutableStateOf(Building(show = false)) }
            var coffee by remember { mutableStateOf(Coffee(show = false)) }
            var lineBlock by remember { mutableStateOf(LineBlock(show = false)) }
            var tangent by remember { mutableStateOf(Tangent(show = false)) }
            var towers by remember { mutableStateOf(Towers(show = false)) }
            var sunflower by remember { mutableStateOf(Sunflower(show = false)) }
            var squares by remember { mutableStateOf(Squares(show = false)) }

            Row(modifier = Modifier.padding(5.dp)) {
                Column(
                    modifier = Modifier.width(1000.dp).fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Canvas(
                        modifier = Modifier
                            .height(900.dp)
                            .width(900.dp)
                            .background(Color.Black)
                    ) {
                        arcLines.draw()
                        circlePathLines.draw()
                        building.draw()
                        coffee.draw()
                        lineBlock.draw()
                        tangent.draw()
                        towers.draw()
                        sunflower.draw()
                        squares.draw()
                    }
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LazyColumn(
                        modifier = Modifier.width(400.dp)
                    ) {
                        item {
                            Text(
                                text = "Settings",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )
                        }
                        arcLines.settings { arcLines = it }
                        circlePathLines.settings { circlePathLines = it }
                        building.settings { building = it }
                        coffee.settings { coffee = it }
                        lineBlock.settings { lineBlock = it }
                        tangent.settings { tangent = it }
                        towers.settings { towers = it }
                        sunflower.settings { sunflower = it }
                        squares.settings { squares = it }
                    }
                }
            }
        }
    }
}

