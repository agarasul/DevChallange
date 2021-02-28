package com.example.androiddevchallenge.data.datasource

import android.content.Context
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.data.entity.Puppy
import com.example.androiddevchallenge.helper.jsonToClass
import com.google.gson.Gson
import com.google.gson.JsonElement

class PuppyDataSource(private val context: Context) {
    val puppies = context.jsonToClass<JsonElement>(R.raw.mockdata).asJsonArray.map {
        Gson().fromJson(
            it,
            Puppy::class.java
        )
    }
}