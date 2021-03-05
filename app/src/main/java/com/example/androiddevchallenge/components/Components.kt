package com.example.androiddevchallenge.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Composable
fun CountDownTimer(count: Int, isPlaying: Boolean = false, onFinish: (Float) -> Unit) {

    val countDown  = if (isPlaying) {
      animateFloatAsState(
            targetValue =  count.toFloat(),
            animationSpec = keyframes {

                durationMillis = TimeUnit.SECONDS.toMillis(count.toLong()).toInt()
                count.toFloat() at 0
                0f at TimeUnit.SECONDS.toMillis(count.toLong()).toInt()

            },
            finishedListener = onFinish
        )
    } else {
       mutableStateOf(0f)
    }

    Text(
        modifier = Modifier.padding(16.dp),
        text = countDown.value.roundToInt().toString(),
        textAlign = TextAlign.Center,
        fontSize = 56.sp
    )
}

@Composable
@Preview
fun CountDownTimerPreview() {
    CountDownTimer(20, false) { }
}
