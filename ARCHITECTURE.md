# eClock App Navigation Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                        MainActivity                              │
│                      (Entry Point)                               │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    EClockTheme                           │   │
│  │              (Material Design 3 Theme)                   │   │
│  │                                                           │   │
│  │  ┌─────────────────────────────────────────────────┐    │   │
│  │  │              MainScreen                          │    │   │
│  │  │         (Navigation Container)                   │    │   │
│  │  │                                                   │    │   │
│  │  │  ┌─────────────────────────────────────────┐    │    │   │
│  │  │  │        NavHost (Screen Router)          │    │    │   │
│  │  │  │                                           │    │    │   │
│  │  │  │  Routes:                                 │    │    │   │
│  │  │  │  • /clock      → ClockScreen            │    │    │   │
│  │  │  │  • /timer      → TimerScreen            │    │    │   │
│  │  │  │  • /countdown  → CountdownScreen        │    │    │   │
│  │  │  │  • /worldtime  → WorldTimeScreen        │    │    │   │
│  │  │  │  • /alarm      → AlarmScreen            │    │    │   │
│  │  │  └─────────────────────────────────────────┘    │    │   │
│  │  │                                                   │    │   │
│  │  │  ┌─────────────────────────────────────────┐    │    │   │
│  │  │  │      Bottom Navigation Bar               │    │    │   │
│  │  │  │  [Clock][Timer][Countdown][World][Alarm] │    │    │   │
│  │  │  └─────────────────────────────────────────┘    │    │   │
│  │  └─────────────────────────────────────────────────┘    │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

## Screen Details

### 1. ClockScreen
```
┌──────────────────────────┐
│      Current Time        │
│                          │
│   ┌────────────────┐    │
│   │   Analog Clock  │    │
│   │    ___          │    │
│   │   /   \         │    │
│   │  |  •  |  ←     │    │
│   │   \___/         │    │
│   └────────────────┘    │
│                          │
│      14:35:22           │
│                          │
│  Thursday, March 21     │
└──────────────────────────┘
```

### 2. TimerScreen
```
┌──────────────────────────┐
│      Stopwatch           │
│                          │
│     00:05:23.45         │
│                          │
│  [Reset]    [Start/Stop] │
│                          │
│    Lap Times:            │
│  ┌────────────────────┐ │
│  │ 00:05:23.45       │ │
│  │ 00:03:12.34       │ │
│  │ 00:01:45.67       │ │
│  └────────────────────┘ │
└──────────────────────────┘
```

### 3. CountdownScreen
```
┌──────────────────────────┐
│   Set Countdown Time     │
│                          │
│  Hours  Minutes Seconds  │
│   [+]     [+]     [+]   │
│   [00]    [05]    [00]   │
│   [-]     [-]     [-]   │
│                          │
│   [Start Countdown]      │
│                          │
│  (When running:)         │
│     00:04:55            │
│  [Reset]  [Pause/Resume]│
└──────────────────────────┘
```

### 4. WorldTimeScreen
```
┌──────────────────────────┐
│     World Time      [+]  │
│                          │
│ ┌──────────────────────┐│
│ │ New York        [🗑] ││
│ │ 09:35:22            ││
│ │ Mar 21, 2024        ││
│ │ UTC-05:00           ││
│ └──────────────────────┘│
│                          │
│ ┌──────────────────────┐│
│ │ London          [🗑] ││
│ │ 14:35:22            ││
│ │ Mar 21, 2024        ││
│ │ UTC+00:00           ││
│ └──────────────────────┘│
└──────────────────────────┘
```

### 5. AlarmScreen
```
┌──────────────────────────┐
│       Alarms        [+]  │
│                          │
│ ┌──────────────────────┐│
│ │ 07:00           [⚫] ││
│ │ Wake up         [🗑] ││
│ └──────────────────────┘│
│                          │
│ ┌──────────────────────┐│
│ │ 13:00           [⚪] ││
│ │ Lunch           [🗑] ││
│ └──────────────────────┘│
│                          │
│ (When adding:)           │
│ ┌──────────────────────┐│
│ │  🕐 Time Picker      ││
│ │  Label: [________]   ││
│ │  [Cancel]  [Save]    ││
│ └──────────────────────┘│
└──────────────────────────┘
```

## Component Hierarchy

```
MainActivity
 └─ EClockTheme
     └─ Surface
         └─ MainScreen
             ├─ Scaffold
             │   ├─ NavHost
             │   │   ├─ ClockScreen (Composable)
             │   │   │   ├─ Column
             │   │   │   │   ├─ AnalogClock (Canvas)
             │   │   │   │   ├─ Text (Digital Time)
             │   │   │   │   └─ Text (Date)
             │   │   │
             │   │   ├─ TimerScreen (Composable)
             │   │   │   ├─ Column
             │   │   │   │   ├─ AnimatedContent (Time Display)
             │   │   │   │   ├─ Row (Buttons)
             │   │   │   │   └─ LazyColumn (Lap Times)
             │   │   │
             │   │   ├─ CountdownScreen (Composable)
             │   │   │   ├─ Column
             │   │   │   │   ├─ Row (Time Pickers)
             │   │   │   │   │   ├─ TimePickerColumn x3
             │   │   │   │   ├─ Button (Start)
             │   │   │   │   └─ Row (Controls)
             │   │   │
             │   │   ├─ WorldTimeScreen (Composable)
             │   │   │   ├─ Scaffold
             │   │   │   │   ├─ LazyColumn
             │   │   │   │   │   └─ WorldClockItem (Cards) x N
             │   │   │   │   └─ FloatingActionButton
             │   │   │
             │   │   └─ AlarmScreen (Composable)
             │   │       ├─ Scaffold
             │   │       │   ├─ LazyColumn
             │   │       │   │   └─ AlarmItem (Cards) x N
             │   │       │   └─ FloatingActionButton
             │   │       └─ AddAlarmDialog (Optional)
             │   │           ├─ TimePicker
             │   │           └─ TextField (Label)
             │   │
             │   └─ NavigationBar (Bottom Bar)
             │       └─ NavigationBarItem x5
             │           ├─ Icon
             │           └─ Text (Label)
```

## State Management

### ClockScreen State:
- `currentTime: Calendar` - Updated every second
- `rotation: Animatable<Float>` - For smooth clock hand rotation

### TimerScreen State:
- `elapsedTime: Long` - Milliseconds elapsed
- `isRunning: Boolean` - Timer running state
- `lapTimes: SnapshotStateList<Long>` - List of recorded laps

### CountdownScreen State:
- `hours: Int`, `minutes: Int`, `seconds: Int` - Time input
- `totalSeconds: Int` - Total countdown duration
- `remainingSeconds: Int` - Current countdown value
- `isRunning: Boolean` - Countdown running state
- `isFinished: Boolean` - Completion flag

### WorldTimeScreen State:
- `worldClocks: SnapshotStateList<WorldClock>` - List of time zones
- `currentTime: Long` - Current timestamp (updated every second)

### AlarmScreen State:
- `alarms: SnapshotStateList<Alarm>` - List of alarms
- `showDialog: Boolean` - Dialog visibility

## Animation Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    Animation Types                           │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  1. Continuous Animations (LaunchedEffect)                   │
│     • Clock hands rotation (1 second interval)               │
│     • Timer updates (10ms interval)                          │
│     • Countdown updates (1 second interval)                  │
│     • World time updates (1 second interval)                 │
│                                                               │
│  2. Transition Animations (AnimatedContent)                  │
│     • Timer display value changes                            │
│     • Countdown display value changes                        │
│                                                               │
│  3. State Animations (animateXAsState)                       │
│     • Timer scale when running                               │
│     • Countdown color when time is low                       │
│                                                               │
│  4. Visibility Animations (AnimatedVisibility)               │
│     • Bottom navigation slide in/out                         │
│     • List item expansions                                   │
│                                                               │
│  5. Canvas Animations (Animatable)                           │
│     • Clock hand smooth rotations                            │
│     • Custom drawing animations                              │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

## Data Models

```kotlin
// AlarmScreen
data class Alarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val label: String,
    var isEnabled: Boolean
)

// WorldTimeScreen
data class WorldClock(
    val cityName: String,
    val timeZoneId: String
)

// Navigation
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Clock : Screen("clock", "Clock", Icons.Default.AccessTime)
    object Timer : Screen("timer", "Timer", Icons.Default.Timer)
    object Countdown : Screen("countdown", "Countdown", Icons.Default.HourglassEmpty)
    object WorldTime : Screen("worldtime", "World Time", Icons.Default.Public)
    object Alarm : Screen("alarm", "Alarm", Icons.Default.Alarm)
}
```

## Material Design 3 Theme

```
Color Palette:
┌─────────────────────────────────────┐
│  Light Theme:                        │
│  • Primary: Purple40 (#6650a4)      │
│  • Secondary: PurpleGrey40 (#625b71) │
│  • Tertiary: Pink40 (#7D5260)        │
│                                      │
│  Dark Theme:                         │
│  • Primary: Purple80 (#D0BCFF)      │
│  • Secondary: PurpleGrey80 (#CCC2DC) │
│  • Tertiary: Pink80 (#EFB8C8)        │
└─────────────────────────────────────┘

Typography:
┌─────────────────────────────────────┐
│  • bodyLarge: 16sp, normal          │
│  • titleLarge: 22sp, normal         │
│  • labelSmall: 11sp, medium         │
└─────────────────────────────────────┘

Dynamic Colors:
┌─────────────────────────────────────┐
│  Android 12+ (API 31+):             │
│  • Adapts to system wallpaper       │
│  • Material You color extraction    │
│                                      │
│  Android 11 and below:               │
│  • Uses predefined color schemes    │
└─────────────────────────────────────┘
```
