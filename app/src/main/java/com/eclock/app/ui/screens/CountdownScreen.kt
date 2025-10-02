package com.eclock.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun CountdownScreen() {
    var totalSeconds by remember { mutableStateOf(0) }
    var remainingSeconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }
    
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }
    
    LaunchedEffect(isRunning) {
        if (isRunning && remainingSeconds > 0) {
            while (isRunning && remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
                if (remainingSeconds == 0) {
                    isFinished = true
                    isRunning = false
                }
            }
        }
    }
    
    val textColor by animateColorAsState(
        targetValue = when {
            isFinished -> Color.Red
            remainingSeconds <= 10 && isRunning -> Color(0xFFFF6B35)
            else -> MaterialTheme.colorScheme.primary
        },
        label = "countdown_color"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isRunning) 1.05f else 1f,
        label = "countdown_scale"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!isRunning && remainingSeconds == 0) {
            // Time Picker
            Text(
                text = "Set Countdown Time",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Hours
                TimePickerColumn(
                    label = "Hours",
                    value = hours,
                    onIncrement = { if (hours < 23) hours++ },
                    onDecrement = { if (hours > 0) hours-- }
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Minutes
                TimePickerColumn(
                    label = "Minutes",
                    value = minutes,
                    onIncrement = { if (minutes < 59) minutes++ },
                    onDecrement = { if (minutes > 0) minutes-- }
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Seconds
                TimePickerColumn(
                    label = "Seconds",
                    value = seconds,
                    onIncrement = { if (seconds < 59) seconds++ },
                    onDecrement = { if (seconds > 0) seconds-- }
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = {
                    totalSeconds = hours * 3600 + minutes * 60 + seconds
                    remainingSeconds = totalSeconds
                    if (totalSeconds > 0) {
                        isRunning = true
                        isFinished = false
                    }
                },
                enabled = hours > 0 || minutes > 0 || seconds > 0
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                Text("Start Countdown", modifier = Modifier.padding(start = 8.dp))
            }
        } else {
            // Countdown Display
            if (isFinished) {
                Text(
                    text = "Time's Up!",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
            
            AnimatedContent(
                targetState = formatCountdownTime(remainingSeconds),
                label = "countdown_display"
            ) { time ->
                Text(
                    text = time,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Control Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        isRunning = false
                        isFinished = false
                        remainingSeconds = 0
                        hours = 0
                        minutes = 0
                        seconds = 0
                    }
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset")
                    Text("Reset", modifier = Modifier.padding(start = 8.dp))
                }
                
                if (!isFinished) {
                    Button(
                        onClick = { isRunning = !isRunning }
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isRunning) "Pause" else "Resume"
                        )
                        Text(
                            text = if (isRunning) "Pause" else "Resume",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimePickerColumn(
    label: String,
    value: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        IconButton(onClick = onIncrement) {
            Icon(Icons.Default.Add, contentDescription = "Increase $label")
        }
        
        Card(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = String.format("%02d", value),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        IconButton(onClick = onDecrement) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease $label")
        }
    }
}

private fun formatCountdownTime(seconds: Int): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return String.format("%02d:%02d:%02d", h, m, s)
}
