package com.eclock.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClockScreen() {
    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }
    val rotation = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance()
            rotation.animateTo(
                targetValue = rotation.value + 6f,
                animationSpec = tween(1000, easing = LinearEasing)
            )
            delay(1000)
        }
    }
    
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Analog Clock
        AnalogClock(currentTime)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Digital Time
        Text(
            text = timeFormat.format(currentTime.time),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Date
        Text(
            text = dateFormat.format(currentTime.time),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AnalogClock(calendar: Calendar) {
    val hourRotation = remember { Animatable(0f) }
    val minuteRotation = remember { Animatable(0f) }
    val secondRotation = remember { Animatable(0f) }
    
    LaunchedEffect(calendar) {
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        
        hourRotation.snapTo((hour * 30f + minute * 0.5f))
        minuteRotation.snapTo(minute * 6f)
        secondRotation.animateTo(
            targetValue = second * 6f,
            animationSpec = tween(200)
        )
    }
    
    Canvas(modifier = Modifier.size(280.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2 * 0.9f
        
        // Clock face
        drawCircle(
            color = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.2f),
            radius = radius,
            center = center
        )
        
        // Hour marks
        for (i in 0..11) {
            val angle = i * 30.0 - 90.0
            val startRadius = radius * 0.85f
            val endRadius = radius * 0.95f
            val start = Offset(
                center.x + (cos(Math.toRadians(angle)) * startRadius).toFloat(),
                center.y + (sin(Math.toRadians(angle)) * startRadius).toFloat()
            )
            val end = Offset(
                center.x + (cos(Math.toRadians(angle)) * endRadius).toFloat(),
                center.y + (sin(Math.toRadians(angle)) * endRadius).toFloat()
            )
            drawLine(
                color = androidx.compose.ui.graphics.Color.Black,
                start = start,
                end = end,
                strokeWidth = 4.dp.toPx()
            )
        }
        
        // Hour hand
        rotate(hourRotation.value, center) {
            drawLine(
                color = androidx.compose.ui.graphics.Color.Black,
                start = center,
                end = Offset(center.x, center.y - radius * 0.5f),
                strokeWidth = 8.dp.toPx()
            )
        }
        
        // Minute hand
        rotate(minuteRotation.value, center) {
            drawLine(
                color = androidx.compose.ui.graphics.Color.Black,
                start = center,
                end = Offset(center.x, center.y - radius * 0.7f),
                strokeWidth = 6.dp.toPx()
            )
        }
        
        // Second hand
        rotate(secondRotation.value, center) {
            drawLine(
                color = androidx.compose.ui.graphics.Color.Red,
                start = center,
                end = Offset(center.x, center.y - radius * 0.8f),
                strokeWidth = 3.dp.toPx()
            )
        }
        
        // Center dot
        drawCircle(
            color = androidx.compose.ui.graphics.Color.Black,
            radius = 8.dp.toPx(),
            center = center
        )
    }
}
