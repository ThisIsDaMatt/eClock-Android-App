package com.eclock.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import java.util.Locale

data class WorldClock(
    val cityName: String,
    val timeZoneId: String
)

@Composable
fun WorldTimeScreen() {
    val worldClocks = remember {
        mutableStateListOf(
            WorldClock("New York", "America/New_York"),
            WorldClock("London", "Europe/London"),
            WorldClock("Tokyo", "Asia/Tokyo"),
            WorldClock("Sydney", "Australia/Sydney")
        )
    }
    
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = System.currentTimeMillis()
            delay(1000)
        }
    }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Add more cities (simple implementation)
                    val cities = listOf(
                        WorldClock("Paris", "Europe/Paris"),
                        WorldClock("Dubai", "Asia/Dubai"),
                        WorldClock("Los Angeles", "America/Los_Angeles"),
                        WorldClock("Singapore", "Asia/Singapore"),
                        WorldClock("Moscow", "Europe/Moscow")
                    )
                    val newCity = cities.random()
                    if (!worldClocks.any { it.timeZoneId == newCity.timeZoneId }) {
                        worldClocks.add(newCity)
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Time Zone")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "World Time",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(worldClocks) { clock ->
                    WorldClockItem(
                        worldClock = clock,
                        currentTime = currentTime,
                        onDelete = { worldClocks.remove(clock) }
                    )
                }
            }
        }
    }
}

@Composable
fun WorldClockItem(
    worldClock: WorldClock,
    currentTime: Long,
    onDelete: () -> Unit
) {
    val timeZone = TimeZone.getTimeZone(worldClock.timeZoneId)
    val calendar = Calendar.getInstance(timeZone)
    calendar.timeInMillis = currentTime
    
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    timeFormat.timeZone = timeZone
    
    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    dateFormat.timeZone = timeZone
    
    val offsetFormat = SimpleDateFormat("Z", Locale.getDefault())
    offsetFormat.timeZone = timeZone
    val offset = offsetFormat.format(calendar.time)
    val formattedOffset = "UTC${offset.substring(0, 3)}:${offset.substring(3)}"
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = worldClock.cityName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = timeFormat.format(calendar.time),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = dateFormat.format(calendar.time),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = formattedOffset,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
