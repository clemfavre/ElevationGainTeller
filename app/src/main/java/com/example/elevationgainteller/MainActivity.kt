package com.example.elevationgainteller

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.elevationgainteller.ui.theme.ElevationGainTellerTheme
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
// Other imports remain the same
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// --- State Definition ---
enum class AppState { Start, Running, Stopped }

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
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
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
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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

    var elapsedTimeInSeconds by rememberSaveable { mutableStateOf(0L) }
    var isTimerRunning by rememberSaveable { mutableStateOf(false) }

    var lapTimes by rememberSaveable { mutableStateOf(listOf<Long>()) }
    var currentLapStartTimeSeconds by rememberSaveable { mutableStateOf(0L) }

    val elevationGain = 5.20 * lapTimes.size

    val currentTitle = when (appState) {
        AppState.Start -> "Welcome to ElevationGainTeller !"
        AppState.Running -> "Don't give up !"
        AppState.Stopped -> "Well done !"
    }
    val currentInstructions = when (appState) {
        AppState.Start -> "Press on the Start button to begin your activity."
        AppState.Running -> "Press 'Lap' for each lap. Press 'Stop' to end."
        AppState.Stopped -> "You completed ${lapTimes.size} round trips in ${formatTime(elapsedTimeInSeconds)}, gaining ${String.format("%.1f", elevationGain)} meters. Here's more detailed stats." // Added time to summary
    }

    if (isTimerRunning) {
        LaunchedEffect(Unit) {
            while (isTimerRunning && appState == AppState.Running) {
                delay(1000L)
                elapsedTimeInSeconds++
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Description and Start/Stop/Reset button
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
                            currentLapStartTimeSeconds = 0L
                            lapTimes = emptyList()
                        },
                        backgroundColor = Color(0xFF4CAF50) // Green
                    )
                }
                AppState.Running -> {
                    ActionButton(
                        text = "Stop",
                        onClick = {
                            if (isTimerRunning && elapsedTimeInSeconds > currentLapStartTimeSeconds) {
                                val finalLapDuration = elapsedTimeInSeconds - currentLapStartTimeSeconds
                                lapTimes = lapTimes + finalLapDuration
                            }
                            appState = AppState.Stopped
                            isTimerRunning = false
                        },
                        backgroundColor = Color(0xFFF44336) // Red
                    )
                }
                AppState.Stopped -> {
                    ActionButton(
                        text = "Reset",
                        onClick = {
                            appState = AppState.Start
                            isTimerRunning = false
                            elapsedTimeInSeconds = 0L
                            lapTimes = emptyList()
                            currentLapStartTimeSeconds = 0L
                            // manualPointCounter = 0 // Reset if using
                        },
                        backgroundColor = Color(0xFF2196F3) // Blue
                    )
                }
            }

            // SimpleInfos and LapTimesDisplay
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                if (appState == AppState.Running || appState == AppState.Start) {
                    SimpleInfos(
                        totalLaps = lapTimes.size,
                        elevationGain = elevationGain,
                        currentTime = formatTime(elapsedTimeInSeconds),
                        currentLapTime = formatTime(if (isTimerRunning && appState == AppState.Running) elapsedTimeInSeconds - currentLapStartTimeSeconds else 0L)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LapTimesDisplay(lapTimes = lapTimes)
                } else if (appState == AppState.Stopped) {
                    DetailedInfos(
                        totalLaps = lapTimes.size,
                        elevationGain = elevationGain,
                        totalTime = formatTime(elapsedTimeInSeconds),
                        allLapTimes = lapTimes
                    )
                }
            }

            // Lap button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (appState == AppState.Running) {
                    RoundIncrementButton(
                        onClick = {
                            if (isTimerRunning) {
                                val lapDuration = elapsedTimeInSeconds - currentLapStartTimeSeconds
                                if (lapDuration > 0) {
                                    lapTimes = lapTimes + lapDuration
                                }
                                currentLapStartTimeSeconds = elapsedTimeInSeconds
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                } else if (appState == AppState.Start) {
                    RoundIncrementButton(onClick = {})
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}


@Composable
fun OneInfo(title: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
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
fun SimpleInfos(totalLaps: Int, elevationGain: Double, currentTime: String, currentLapTime: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth() // Ensure SimpleInfos takes full width
    ) {
        OneInfo(
            title = "Laps:",
            value = "$totalLaps"
        )
        OneInfo(
            title = "Total Time:",
            value = currentTime
        )
        OneInfo(
            title = "Elevation Gain:",
            value = "${String.format("%.1f", elevationGain)} m"
        )
        OneInfo(
            title = "Current Lap:",
            value = currentLapTime
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LapTimesDisplay(lapTimes: List<Long>, modifier: Modifier = Modifier) {
    val displayedLaps = lapTimes.takeLast(3).asReversed()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = displayedLaps.isNotEmpty(),
            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(animationSpec = tween(300), initialOffsetY = { it / 2 }),
            exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(animationSpec = tween(300), targetOffsetY = { it / 2 })
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Recent Laps:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                displayedLaps.forEachIndexed { index, lapTime ->
                    Text(
                        text = "Lap ${lapTimes.size - index}: ${formatTime(lapTime)}",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    if (index < displayedLaps.size - 1) {
                        Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 48.dp, vertical = 2.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DetailedInfos(totalLaps: Int, elevationGain: Double, totalTime: String, allLapTimes: List<Long>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Detailed Statistics", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
        OneInfo(title = "Total Laps:", value = "$totalLaps")
        OneInfo(title = "Total Time:", value = totalTime)
        OneInfo(title = "Total Elevation:", value = "${String.format("%.1f", elevationGain)} m")

        if (allLapTimes.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("All Lap Times:", fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 4.dp))
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .height(120.dp) // Constrain height
                    .fillMaxWidth()
                    .border(0.5.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                itemsIndexed(allLapTimes) { index, lapTime ->
                    Text("Lap ${index + 1}: ${formatTime(lapTime)}", modifier = Modifier.padding(vertical = 2.dp))
                    if (index < allLapTimes.size - 1) {
                        Divider(color = Color.LightGray, thickness = 0.5.dp)
                    }
                }
            }
        }
        // Add more detailed stats like average, fastest etc. later if needed
    }
}

// --- Previews --- (Ensure they are updated or simplified)

@Preview(showBackground = true, name = "Skeleton - Start")
@Composable
fun SkeletonPreview_Start() {
    ElevationGainTellerTheme {
        Skeleton(initialTitle = "Welcome", initialInstructions = "Press Start")
    }
}

@Preview(showBackground = true, name = "Skeleton - Running")
@Composable
fun SkeletonPreview_Running() {
    // This preview will be static, showing what it looks like.
    // For interactive previews, state needs to be hoisted or more complex setup.
    ElevationGainTellerTheme {
        val appState = AppState.Running
        val counter = 2 // Represents lapTimes.size
        val elevationGain = 5.35 * counter
        val elapsedTimeInSeconds = 125L
        val isTimerRunning = true
        val lapTimes = listOf(60L, 62L)
        val currentLapStartTimeSeconds = 122L

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Description("Activity in Progress!", "Press 'Lap' or 'Stop'.")
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                    SimpleInfos(
                        totalLaps = lapTimes.size,
                        elevationGain = elevationGain,
                        currentTime = formatTime(elapsedTimeInSeconds),
                        currentLapTime = formatTime(elapsedTimeInSeconds - currentLapStartTimeSeconds)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LapTimesDisplay(lapTimes = lapTimes)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    RoundIncrementButton(onClick = {})
                    Spacer(modifier = Modifier.height(16.dp))
                    ActionButton(text = "Stop", onClick = {}, backgroundColor = Color(0xFFF44336))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Skeleton - Stopped")
@Composable
fun SkeletonPreview_Stopped() {
    ElevationGainTellerTheme {
        // Static preview for stopped state
        val lapTimes = listOf(60L, 62L, 58L)
        val totalTime = 180L
        val elevation = 5.35 * lapTimes.size

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Description("Activity Summary", "Completed ${lapTimes.size} laps in ${formatTime(totalTime)}, gaining ${String.format("%.1f", elevation)}m.")
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                    DetailedInfos(
                        totalLaps = lapTimes.size,
                        elevationGain = elevation,
                        totalTime = formatTime(totalTime),
                        allLapTimes = lapTimes
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    ActionButton(text = "Reset", onClick = {}, backgroundColor = Color(0xFF2196F3))
                }
            }
        }
    }
}
