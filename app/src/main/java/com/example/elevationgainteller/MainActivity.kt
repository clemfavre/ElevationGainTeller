package com.example.elevationgainteller

import android.os.Bundle
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
// import androidx.compose.foundation.Image // Only if you use it
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
// import androidx.compose.foundation.layout.Row // Not explicitly used in this example now
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
// import androidx.compose.material3.Scaffold // Not used
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
// import androidx.compose.ui.res.painterResource // Only if you use the image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable // For state preservation
import androidx.compose.runtime.setValue
import com.example.elevationgainteller.ui.theme.ElevationGainTellerTheme

// --- State Definition ---
enum class AppState {
    Start,
    Running,
    Stopped
}

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
                    // Pass initial values for title and instructions if they are static
                    // Or manage them within BackGround if they need to change with AppState
                    Skeleton(
                        initialTitle = "",
                        initialInstructions = ""
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButton( // A more generic button for Start, Stop, Reset
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary, // Default to primary color
    contentColor: Color = MaterialTheme.colorScheme.onPrimary  // Default content color
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(percent = 50),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .size(width = 150.dp, height = 60.dp)
    ) {
        Text(text = text, fontSize = 24.sp, textAlign = TextAlign.Center)
    }
}


@Composable
fun RoundIncrementButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.White
        ),
        modifier = modifier
            .size(250.dp)
    ) {
        Text(
            text = "Lap",
            fontSize = 50.sp
        )
    }
}

@Composable
fun Description(title: String, instructions: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp), // Increased padding for better spacing
        horizontalAlignment = Alignment.CenterHorizontally // Center title and instructions
    ) {
        Text(
            text = title,
            fontSize = 20.sp, // Slightly larger title
            lineHeight = 30.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = instructions,
            fontSize = 16.sp, // Larger instruction text for readability
            lineHeight = 20.sp,
            textAlign = TextAlign.Center, // Center instructions as well
            modifier = Modifier
        )
    }
}

@Composable
fun Skeleton(initialTitle: String, initialInstructions: String, modifier: Modifier = Modifier) {
    // val bgImage = painterResource(R.drawable.stairs) // If you add an image back

    var appState by rememberSaveable { mutableStateOf(AppState.Start) }
    var counter by rememberSaveable { mutableStateOf(0) } // Preserve counter across configuration changes
    val elevationGain = 5.35 * counter

    // Dynamic title and instructions based on state
    val currentTitle = when (appState) {
        AppState.Start -> "Welcome to ElevationGainTeller !"
        AppState.Running -> "Don't give up !"
        AppState.Stopped -> "Well done !"
    }
    val currentInstructions = when (appState) {
        AppState.Start -> "Press on the Start button to begin your activity."
        AppState.Running -> "Press 'Round Trip' for each lap. Press 'Stop' to end."
        AppState.Stopped -> "You completed $counter round trips, gaining $elevationGain meters. Here's more detailed stats."
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
        // .paint(painter = bgImage, contentScale = ContentScale.Crop) // Example if using background image
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Description(
                title = currentTitle,
                instructions = currentInstructions
            )
            when (appState) {
                AppState.Start -> {
                    ActionButton(
                        text = "Start",
                        onClick = { appState = AppState.Running },
                        backgroundColor = Color(0xFF4CAF50) // Green
                    )
                }
                AppState.Running -> {
                    ActionButton(
                        text = "Stop",
                        onClick = { appState = AppState.Stopped },
                        backgroundColor = Color(0xFFF44336) // Red
                    )
                }
                AppState.Stopped -> {
                    ActionButton(
                        text = "Reset",
                        onClick = {
                            appState = AppState.Start
                            counter = 0 // Reset counter when going back to Start
                        },
                        backgroundColor = Color(0xFF2196F3) // Blue
                    )
                }
            }
            if (appState == AppState.Start) {
                SimpleInfos(
                    counter,
                    elevationGain
                )
                RoundIncrementButton(
                    onClick = {}
                )
            } else if (appState == AppState.Running) {
                SimpleInfos(
                    counter,
                    elevationGain
                )
                RoundIncrementButton(
                    onClick = { counter++ }
                )
            } else if (appState == AppState.Stopped) {
                DetailedInfos(counter, elevationGain)
            }
        }
    }
}

@Composable
fun OneInfo(title: String, value: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            fontSize = 18.sp,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier.weight(1f),
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun SimpleInfos(counter: Int, elevationGain: Double) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    )
    {
        OneInfo(
            title = "Laps:",
            value = "$counter"
        )
        OneInfo(
            title = "Time:",
            value = "00:00"
        )
        OneInfo(
            title = "Elevation Gain:",
            value = "$elevationGain m"
        )
        OneInfo(
            title = "Last lap:",
            value = "00:00"
        )
    }
}

@Composable
fun DetailedInfos(counter: Int, elevationGain: Double) {}

@Preview(showBackground = true, name = "Start State")
@Composable
fun BackGroundPreview_Start() {
    ElevationGainTellerTheme {
        // Simulate Start state for preview
        val tempAppState = remember { mutableStateOf(AppState.Start) }
        val tempCounter = remember { mutableStateOf(0) }
        val elevation = 5 * tempCounter.value

        Skeleton(
            "Welcome!",
            "Press Start to begin."
        )
        // This preview will show the initial state.
        // For more complex previews of different states, you might need to
        // pass the state as a parameter to BackGround or create separate preview functions.
    }
}

@Preview(showBackground = true, name = "Running State")
@Composable
fun BackGroundPreview_Running() {
    ElevationGainTellerTheme {
        // To preview specific states, you'd ideally pass the state to BackGround
        // For simplicity here, just calling it. It will default to Start.
        // A better way for previews is to make BackGround accept an initial AppState
        // for previewing purposes, or create a wrapper.
        Skeleton(
            "Activity in Progress",
            "Press 'Round Trip' for each lap."
        )
        // This will still show start. See notes in the composable on how to preview states better.
    }
}


@Preview(showBackground = true)
@Composable
fun DescriptionPreview() {
    ElevationGainTellerTheme {
        Description(
            "Test Title",
            "These are some test instructions for the description component.",
            modifier = Modifier.padding(8.dp)
        )
    }
}
