package com.example.elevationgainteller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                    Description("How to use the app", "Press this to do that")
                }
            }
        }
    }
}

@Composable
fun Description(message: String, secondText: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        Text(
            text = message,
            fontSize = 40.sp,
            lineHeight = 100.sp
        )
        Text(
            text = secondText,
            fontSize = 20.sp,
            lineHeight = 100.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DescriptionPreview() {
    ElevationGainTellerTheme {
        Description("How to use the app", "Press this to do that")
    }
}