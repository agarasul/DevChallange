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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.components.ConfigureTimer
import com.example.androiddevchallenge.components.CountDownTimer
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.bgColor

class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp() {

    var isConfiguring by remember { mutableStateOf(false) }

    var timerTime by remember { mutableStateOf(70L) }
    Surface(color = bgColor, modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isConfiguring,
            enter = slideInVertically({
                it - 50
            }),
            exit = slideOutVertically({
                it + 50
            })
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Configure Timer", style = TextStyle(color = Color.White)) },
                        backgroundColor = bgColor,
                        navigationIcon = {
                            Image(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clickable {
                                        isConfiguring = false
                                    }
                                    .padding(16.dp),
                                painter = painterResource(
                                    id =
                                    R.drawable.ic_arrow_back
                                ),
                                contentDescription = null
                            )
                        }
                    )
                },
            ) {
                ConfigureTimer {
                    isConfiguring = false
                    timerTime = it
                }
            }
        }

        AnimatedVisibility(
            visible = !isConfiguring,
            enter = slideInVertically({
                it + 50
            }),
            exit = slideOutVertically({
                it - 50
            })
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("CountDown Timer", style = TextStyle(color = Color.White)) },
                        backgroundColor = bgColor,
                    )
                },
            ) {
                CountDownTimer(timerTime) {
                    isConfiguring = true
                }
            }
        }
    }
}
