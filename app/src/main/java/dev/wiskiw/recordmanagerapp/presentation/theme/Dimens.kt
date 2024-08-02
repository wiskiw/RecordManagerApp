package dev.wiskiw.recordmanagerapp.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
    val zero: Dp = 0.dp,
    val oneEight: Dp = 1.dp,
    val quarter: Dp = 2.dp,
    val half: Dp = 4.dp,
    val threeQuarter: Dp = 6.dp,
    val threeQuarterOneEight: Dp = 7.dp,
    val one: Dp = 8.dp,
    val oneQuarter: Dp = 10.dp,
    val oneAndHalf: Dp = 12.dp,
    val two: Dp = 16.dp,
    val twoAndHalf: Dp = 20.dp,
    val three: Dp = 24.dp,
    val threeAndHalf: Dp = 28.dp,
    val four: Dp = 32.dp,
    val fourAndHalf: Dp = 36.dp,
    val five: Dp = 40.dp,
    val fiveAndHalf: Dp = 44.dp,
    val six: Dp = 48.dp,
    val sixAndHalf: Dp = 52.dp,
    val seven: Dp = 56.dp,
    val sevenAndHalf: Dp = 60.dp,
    val eighth: Dp = 64.dp,
    val ten: Dp = 80.dp,
    val twentyOne: Dp = 168.dp,
)

val LocalSize = compositionLocalOf { Size() }

val MaterialTheme.size: Size
    @Composable
    @ReadOnlyComposable
    get() = LocalSize.current
