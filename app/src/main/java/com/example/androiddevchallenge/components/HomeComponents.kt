/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.states.TimerState
import com.example.androiddevchallenge.ui.theme.bgColor
import com.example.androiddevchallenge.ui.theme.progressColor
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@ExperimentalAnimationApi
@Composable
fun CountDownTimer(totalTime: Long, onClick: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    var remainedTime by remember { mutableStateOf(totalTime) }
    var timerState by remember { mutableStateOf(TimerState.Initial) }

    var offset by remember { mutableStateOf(0f) }
    val scaleFraction by animateFloatAsState(targetValue = if (timerState == TimerState.Initial) 0f else 1f)
    val cornerRadius by animateDpAsState(targetValue = if (timerState == TimerState.Initial) 56.dp else 0.dp)

    var job: Job? by remember { mutableStateOf(null) }
    fun startTimer(shouldContinue: Boolean) {
        timerState = TimerState.Playing
        val initialTimerValue = if (shouldContinue) remainedTime else totalTime
        val initialOffsetValue = if (shouldContinue) offset else 0f

        job = coroutineScope.launch {
            val startTime = withFrameNanos { it }

            if (timerState == TimerState.Initial) {
                remainedTime = totalTime
                offset = 0f
            }
            do {
                val playTime = withFrameNanos { it } - startTime

                remainedTime = initialTimerValue - playTime

                offset = initialOffsetValue - (
                    (
                        playTime.toFloat() / totalTime
                            .toFloat()
                        )
                    )
                if (TimeUnit.NANOSECONDS.toMillis(remainedTime) == 0L) {
                    timerState = TimerState.Initial
                    remainedTime = totalTime
                }
            } while (timerState == TimerState.Playing)
        }
    }

    fun pauseTimer() {
        timerState = TimerState.Paused
    }

    val minutes = getMinutes(remainedTime)
    val seconds = getSeconds(remainedTime)

    BoxWithConstraints(
        modifier = Modifier
            .background(bgColor)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        val boxHeight = with(LocalDensity.current) { constraints.maxHeight.toDp() }

        Box(
            modifier = Modifier
                .offset(y = -(boxHeight * (1 + offset)))
                .background(
                    color = progressColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .animateContentSize()
                .fillMaxSize(fraction = scaleFraction)

        )
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CountDownTimerText(
                currentValue = remainedTime,
                shouldAnimateHours = false,
                shouldAnimateMinutes = seconds.toInt() == 0 && timerState == TimerState.Playing,
                shouldAnimateSeconds = timerState == TimerState.Playing,
                isLastSeconds = minutes.toInt() <= 0 && seconds.toInt() <= 5 && timerState == TimerState.Playing,
                state = timerState
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row(Modifier) {

                Button(
                    enabled = totalTime != 0L,
                    onClick = {
                        when (timerState) {
                            TimerState.Paused -> {
                                startTimer(shouldContinue = true)
                            }
                            TimerState.Playing -> {
                                pauseTimer()
                            }
                            else -> {
                                startTimer(shouldContinue = false)
                            }
                        }
                    }
                ) {
                    val text = when (timerState) {
                        TimerState.Playing -> {
                            "Pause"
                        }
                        else -> {
                            "Play"
                        }
                    }
                    Text(text = text)
                }
                Spacer(Modifier.width(16.dp))
                Button(
                    enabled = timerState != TimerState.Initial,
                    onClick = {
                        remainedTime = totalTime
                        offset = 0f
                        timerState = TimerState.Initial
                        job?.cancel()
                    }
                ) {
                    Text(text = "Reset")
                }
                Spacer(Modifier.width(16.dp))
                Button(
                    enabled = timerState == TimerState.Initial,
                    onClick = onClick
                ) {
                    Text(text = "Configure")
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun CountDownTimerText(
    currentValue: Long,
    shouldAnimateHours: Boolean,
    shouldAnimateMinutes: Boolean,
    shouldAnimateSeconds: Boolean,
    isLastSeconds: Boolean,
    state: TimerState
) {
    val hours = getHours(currentValue)
    val minutes = getMinutes(currentValue)
    val seconds = getSeconds(currentValue)

    val infiniteTransition = rememberInfiniteTransition()
    val scaleAnimation by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isLastSeconds) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearOutSlowInEasing)

        )
    )

    val secondsSlideAnimation by infiniteTransition.animateFloat(
        initialValue = if (shouldAnimateSeconds) -1f else 0f,
        targetValue = if (shouldAnimateSeconds) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        )
    )

    val minutesInitialValue = getInitialMinitesValue(minutes.toInt(), seconds.toInt(), state)

    val minutesTargetValue = getTargetMinitesValue(minutes.toInt(), seconds.toInt(), state)

    val minutesSlideAnimation by infiniteTransition.animateFloat(
        initialValue = minutesInitialValue,
        targetValue = minutesTargetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        )
    )

    Row {
        Box(
            Modifier
                .shadow(3.dp)
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier,
                text = hours,
                textAlign = TextAlign.Center,
                fontSize = 56.sp,
                style = TextStyle(color = Color.White)
            )
        }
        Box(
            Modifier
                .padding(top = 16.dp, bottom = 16.dp, start = 4.dp, end = 4.dp)
        ) {
            Text(
                modifier = Modifier,
                text = ":",
                textAlign = TextAlign.Center,
                fontSize = 56.sp,
                style = TextStyle(color = Color.White)
            )
        }
        Box(
            Modifier
                .shadow(3.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .offset(y = (-72).dp * minutesSlideAnimation),
                text = minutes,
                textAlign = TextAlign.Center,
                fontSize = 56.sp,
                style = TextStyle(color = Color.White)
            )
        }
        Box(
            Modifier
                .padding(top = 16.dp, bottom = 16.dp, start = 4.dp, end = 4.dp)
        ) {
            Text(
                modifier = Modifier,
                text = ":",
                textAlign = TextAlign.Center,
                fontSize = 56.sp,
                style = TextStyle(color = Color.White)
            )
        }

        Box(
            Modifier
                .scale(scaleAnimation)
                .shadow(3.dp)

        ) {

            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .offset(y = (-80).dp * secondsSlideAnimation),
                text = seconds,
                textAlign = TextAlign.Center,
                fontSize = 56.sp,
                style = TextStyle(color = Color.White)
            )
        }
    }
}

fun getHours(nanos: Long): String {
    val currentValue = TimeUnit.NANOSECONDS.toSeconds(nanos)

    val hours = currentValue / 3600

    return if (hours < 10) {
        "0$hours"
    } else {
        "$hours"
    }
}

fun getMinutes(nanos: Long): String {
    val currentValue = TimeUnit.NANOSECONDS.toSeconds(nanos)

    val hours = currentValue / 3600

    val minutes = TimeUnit.NANOSECONDS.toMinutes(nanos) - (hours * 60)

    return if (minutes < 10) {
        "0$minutes"
    } else {
        "$minutes"
    }
}

fun getSeconds(nanos: Long): String {
    val currentValue = TimeUnit.NANOSECONDS.toSeconds(nanos)
    val hours = currentValue / 3600

    val minutes = TimeUnit.SECONDS.toMinutes(currentValue) - (hours * 60)
    val seconds = currentValue - (minutes * 60) - (hours * 3600)
    return if (seconds < 10) {
        "0$seconds"
    } else {
        "$seconds"
    }
}

@Composable
fun ConfigureTimer(onTimerSet: (Long) -> Unit) {
    var timerMinutes by remember { mutableStateOf("") }
    var timerSeconds by remember { mutableStateOf("") }
    var timerHours by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding()
    ) {

        Spacer(modifier = Modifier.width(36.dp))
        Row(Modifier.padding(16.dp)) {

            TimerTextField(
                modifier = Modifier.weight(1f),
                label = "Hour",
                value = timerHours,
                onValueChange = { timerHours = it }
            )

            Spacer(modifier = Modifier.width(8.dp))

            TimerTextField(
                modifier = Modifier.weight(1f),
                label = "Minutes",
                value = timerMinutes,
                onValueChange = { timerMinutes = it }
            )

            Spacer(modifier = Modifier.width(8.dp))

            TimerTextField(
                modifier = Modifier.weight(1f),
                label = "Seconds",
                value = timerSeconds,
                onValueChange = { timerSeconds = it }
            )
        }

        Button(
            onClick = {

                val totalTime =
                    TimeUnit.HOURS.toNanos(timerHours.toLongOrNull() ?: 0) +
                        TimeUnit.MINUTES.toNanos(timerMinutes.toLongOrNull() ?: 0) +
                        TimeUnit.SECONDS.toNanos(timerSeconds.toLongOrNull() ?: 0)
                onTimerSet.invoke(totalTime)
            }
        ) {
            Text(text = "Save")
        }
    }
}

private fun getInitialMinitesValue(minutes: Int, seconds: Int, state: TimerState): Float {
    return if (state == TimerState.Playing) {
        when {
            seconds == 0 && minutes > 0 -> {
                0f
            }
            seconds == 59 -> {
                -1f
            }
            else -> {
                0f
            }
        }
    } else {
        0f
    }
}

private fun getTargetMinitesValue(minutes: Int, seconds: Int, state: TimerState): Float {
    return if (state == TimerState.Playing) {
        when {
            seconds == 0 && minutes > 0 -> {
                1f
            }
            seconds == 59 -> {
                0f
            }
            else -> {
                0f
            }
        }
    } else {
        0f
    }
}

@Composable
fun TimerTextField(
    modifier: Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier, value = value,
        onValueChange = {
            if (it.length <= 2) {
                onValueChange.invoke(it)
            }
        },
        label = {
            Text(text = label, style = TextStyle(color = Color.White))
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            textColor = Color.White
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        )

    )
}
