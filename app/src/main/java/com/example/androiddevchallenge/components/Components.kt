package com.example.androiddevchallenge.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R


@Composable
fun PuppyItem(modifier: Modifier) {
    Card(modifier.shadow(8.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier
                    .size(56.dp)
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.dog),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Akita"
            )
        }
    }
}


@Preview
@Composable
fun PuppyItemPreview() {
    PuppyItem(modifier = Modifier)
}