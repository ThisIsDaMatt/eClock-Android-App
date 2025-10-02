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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
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
import java.util.Calendar

data class Alarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val label: String,
    var isEnabled: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen() {
    val alarms = remember { mutableStateListOf<Alarm>() }
    var showDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Alarm")
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
                text = "Alarms",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            if (alarms.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No alarms set",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(alarms) { alarm ->
                        AlarmItem(
                            alarm = alarm,
                            onToggle = { alarm.isEnabled = !alarm.isEnabled },
                            onDelete = { alarms.remove(alarm) }
                        )
                    }
                }
            }
        }
        
        if (showDialog) {
            AddAlarmDialog(
                onDismiss = { showDialog = false },
                onConfirm = { hour, minute, label ->
                    alarms.add(
                        Alarm(
                            id = alarms.size,
                            hour = hour,
                            minute = minute,
                            label = label,
                            isEnabled = true
                        )
                    )
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AlarmItem(
    alarm: Alarm,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
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
                    text = String.format("%02d:%02d", alarm.hour, alarm.minute),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (alarm.isEnabled) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (alarm.label.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = alarm.label,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = alarm.isEnabled,
                    onCheckedChange = { onToggle() }
                )
                
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlarmDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, String) -> Unit
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )
    var label by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Alarm") },
        text = {
            Column {
                TimePicker(state = timePickerState)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Label (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(timePickerState.hour, timePickerState.minute, label)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
