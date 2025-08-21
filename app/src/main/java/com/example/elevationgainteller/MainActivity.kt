package com.example.elevationgainteller

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Image
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.elevationgainteller.ui.theme.ElevationGainTellerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElevationGainTellerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Description(
                        "Welcome to ElevationGainTeller !",
                        "Press on the Start button and start running ! Then press on the Round trip button every time you made one. Enjoy !",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Description(title: String, instructions: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = instructions,
            fontSize = 8.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
        )
    }
}

@Composable
fun BackGround(title: String, instructions: String, modifier: Modifier = Modifier) {
    val bgImage = painterResource(R.drawable.stairs)
    Box(modifier) {
        Image(
            painter = bgImage,
            contentDescription = null
        )
        Description(
            title = title,
            instructions = instructions,
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BackGroundPreview() {
    ElevationGainTellerTheme {
        BackGround(
            "Welcome to ElevationGainTeller !",
            "Press on the Start button and start running ! Then press on the Round trip button every time you made one. Enjoy !"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DescriptionPreview() {
    ElevationGainTellerTheme {
        Description(
            "Welcome to ElevationGainTeller !",
            "Press on the Start button and start running ! Then press on the Round trip button every time you made one. Enjoy !",
            modifier = Modifier.padding(8.dp)
        )
    }
}