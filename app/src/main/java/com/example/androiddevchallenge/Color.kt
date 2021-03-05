package com.example.androiddevchallenge

import androidx.compose.ui.graphics.Color


fun Color.Companion.fromHex(hexColor : String) : Color{
    return Color(android.graphics.Color.parseColor(hexColor))
}