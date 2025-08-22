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
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay // Import delay

// --- State Definition ---
enum class AppState {
    Start,
    Running,
    Stopped
}

// --- Helper Function for Time Formatting ---
fun formatTime(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
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
            fontSize = 20.sp,
            lineHeight = 30.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = instructions,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
    }
}

@Composable
fun Skeleton(initialTitle: String, initialInstructions: String, modifier: Modifier = Modifier) {
    var appState by rememberSaveable { mutableStateOf(AppState.Start) }
    var counter by rememberSaveable { mutableStateOf(0) }
    val elevationGain = 5.35 * counter

    var elapsedTimeInSeconds by rememberSaveable { mutableStateOf(0L) }
    var isTimerRunning by rememberSaveable { mutableStateOf(false) }

    // Dynamic title and instructions based on state
    val currentTitle = when (appState) {
        AppState.Start -> "Welcome to ElevationGainTeller !"
        AppState.Running -> "Don't give up !"
        AppState.Stopped -> "Well done !"
    }
    val currentInstructions = when (appState) {
        AppState.Start -> "Press on the Start button to begin your activity."
        AppState.Running -> "Press 'Lap' for each lap. Press 'Stop' to end."
        AppState.Stopped -> "You completed $counter round trips in ${formatTime(elapsedTimeInSeconds)}, gaining ${String.format("%.1f", elevationGain)} meters. Here's more detailed stats." // Added time to summary
    }

    // Timer Logic
    if (isTimerRunning) {
        LaunchedEffect(Unit) {
            while (isTimerRunning && appState == AppState.Running) {
                delay(1000L)
                elapsedTimeInSeconds++
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                        onClick = {
                            appState = AppState.Running
                            isTimerRunning = true
                            elapsedTimeInSeconds = 0L
                            counter = 0
                        },
                        backgroundColor = Color(0xFF4CAF50)
                    )
                }
                AppState.Running -> {
                    ActionButton(
                        text = "Stop",
                        onClick = {
                            appState = AppState.Stopped
                            isTimerRunning = false
                        },
                        backgroundColor = Color(0xFFF44336)
                    )
                }
                AppState.Stopped -> {
                    ActionButton(
                        text = "Reset",
                        onClick = {
                            appState = AppState.Start
                            isTimerRunning = false
                            elapsedTimeInSeconds = 0L
                            counter = 0
                        },
                        backgroundColor = Color(0xFF2196F3)
                    )
                }
            }
            if (appState == AppState.Start) {
                SimpleInfos(
                    counter = counter,
                    elevationGain = elevationGain,
                    currentTime = formatTime(elapsedTimeInSeconds)
                )
                RoundIncrementButton(
                    onClick = {}
                )
            } else if (appState == AppState.Running) {
                SimpleInfos(
                    counter = counter,
                    elevationGain = elevationGain,
                    currentTime = formatTime(elapsedTimeInSeconds)
                )
                RoundIncrementButton(
                    onClick = { counter++ }
                )
            } else if (appState == AppState.Stopped) {
                DetailedInfos(
                    counter = counter,
                    elevationGain = elevationGain,
                    totalTime = formatTime(elapsedTimeInSeconds)
                )
            }
        }
    }
}

@Composable
fun OneInfo(title: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SimpleInfos(counter: Int, elevationGain: Double, currentTime: String) {
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
            value = currentTime
        )
        OneInfo(
            title = "Elevation Gain:",
            value = "${String.format("%.1f", elevationGain)} m"
        )
        OneInfo(
            title = "Last lap:",
            value = "00:00"
        )
    }
}

@Composable
fun DetailedInfos(counter: Int, elevationGain: Double, totalTime: String) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Detailed Statistics", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        Text("Total Laps: $counter")
        Text("Total Time: $totalTime")
        Text("Total Elevation Gain: ${String.format("%.1f", elevationGain)} m")
        Text("Average Lap Time: could be interesting if enough time")
        Text("Fastest Lap: could be interesting if enough time")
    }
}



@Preview(showBackground = true, name = "Start State Skeleton")
@Composable
fun SkeletonPreview_Start() {
    ElevationGainTellerTheme {
        Skeleton(
            initialTitle = "Welcome!",
            initialInstructions = "Press Start to begin."
        )
    }
}

@Preview(showBackground = true, name = "Running State Skeleton")
@Composable
fun SkeletonPreview_Running() {
    ElevationGainTellerTheme {
        // Simulate Running State more accurately for preview
        var appState by remember { mutableStateOf(AppState.Running) }
        var counter by remember { mutableStateOf(5) }
        val elevationGain = 5.35 * counter
        var elapsedTimeInSeconds by remember { mutableStateOf(305L) } // e.g., 05:05
        var isTimerRunning by remember { mutableStateOf(true) }


        // This LaunchedEffect is for the preview only to see time ticking
        if (isTimerRunning) {
            LaunchedEffect(Unit) {
                while (isTimerRunning) {
                    delay(1000L)
                    elapsedTimeInSeconds++
                }
            }
        }


        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Description(
                    "Don't give up !",
                    "Press 'Lap' for each lap. Press 'Stop' to end."
                )
                ActionButton(
                    text = "Stop",
                    onClick = { isTimerRunning = false /* appState = AppState.Stopped */ },
                    backgroundColor = Color(0xFFF44336)
                )
                SimpleInfos(
                    counter = counter,
                    elevationGain = elevationGain,
                    currentTime = formatTime(elapsedTimeInSeconds)
                )
                RoundIncrementButton(
                    onClick = { counter++ }
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "Stopped State Skeleton")
@Composable
fun SkeletonPreview_Stopped() {
    ElevationGainTellerTheme {
        val counter = 10
        val elevationGain = 5.35 * counter
        val finalTimeInSeconds = 1250L // e.g., 20:50

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Description(
                    "Well done !",
                    "You completed $counter round trips in ${formatTime(finalTimeInSeconds)}, gaining ${String.format("%.1f", elevationGain)} meters. Here's more detailed stats."
                )
                ActionButton(
                    text = "Reset",
                    onClick = { /* appState = AppState.Start */ },
                    backgroundColor = Color(0xFF2196F3)
                )
                DetailedInfos(
                    counter = counter,
                    elevationGain = elevationGain,
                    totalTime = formatTime(finalTimeInSeconds)
                )
            }
        }
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

@Preview(showBackground = true, name = "SimpleInfos Preview")
@Composable
fun SimpleInfosPreview() {
    ElevationGainTellerTheme {
        Surface {
            SimpleInfos(counter = 3, elevationGain = 16.05, currentTime = "00:25")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionButtonPreview() {
    ElevationGainTellerTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionButton(text = "Start", onClick = {}, backgroundColor = Color(0xFF4CAF50))
            ActionButton(text = "Stop", onClick = {}, backgroundColor = Color(0xFFF44336))
            ActionButton(text = "Reset", onClick = {}, backgroundColor = Color(0xFF2196F3))
        }
    }
}

@Preview(showBackground = true, name = "OneInfo Preview")
@Composable
fun OneInfoPreview() {
    ElevationGainTellerTheme {
        Surface {
            Column(Modifier.padding(16.dp)) {
                OneInfo(title = "Laps:", value = "10")
                OneInfo(title = "Time:", value = "05:32")
                OneInfo(title = "Elevation Gain:", value = "107.0 m")
                OneInfo(title = "A much longer title to test wrapping:", value = "Short Value")
            }
        }
    }
}
