package com.example.elevationgainteller

import android.os.Bundle
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Image
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
//import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // Assuming you might want to display the counter
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
                    BackGround(
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
fun RoundIncrementButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = CircleShape, // Makes the button circular
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray, // This sets the button's background
            contentColor = Color.White    // This sets the color of the text inside the button
        ),
        modifier = modifier
            .size(250.dp) // Adjust the size as needed
    ) {
        Text(
            text = "Round Trip",
            fontSize = 30.sp
            ) // Or any text you prefer
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
    var counter by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
    ) {
        /*Image(
            painter = bgImage,
            contentDescription = null
        )*/
        Column( // Use a Column to arrange Description, Counter Text, and Button
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Add some padding around the content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround // Distribute space
        ) {
            Description(
                title = title,
                instructions = instructions
            )

            // Display the counter
            Text(
                text = "Round Trips: $counter",
                fontSize = 24.sp, // Make the counter text prominent
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            RoundIncrementButton(
                onClick = { counter++ } // Increment the counter on click
            )
        }
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