# eClock - Modern Clock App for Android

A modern, intuitive and responsive Clock app with all the core features of a clock app. Built with Kotlin and Jetpack Compose, it provides a smooth experience with Material Design 3 theming and responsive animations.

## Features

### 🕐 Clock
- Beautiful analog clock with smooth animations
- Digital time display
- Current date display
- Real-time updates every second

### ⏱️ Timer
- Count-up timer with millisecond precision
- Start, pause, and reset functionality
- Lap time recording
- Animated display with smooth transitions

### ⏳ Countdown
- Customizable countdown timer
- Set hours, minutes, and seconds
- Visual feedback when time is running low
- Completion notification

### 🌍 World Time
- View time in multiple time zones
- Pre-configured major cities (New York, London, Tokyo, Sydney)
- Add/remove time zones
- Shows local date and UTC offset
- Real-time updates

### ⏰ Alarm
- Create multiple alarms
- Set custom labels for alarms
- Enable/disable alarms with toggle
- 24-hour time picker
- Delete alarms easily

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Modern Android app architecture
- **Design**: Material Design 3
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Building the App

### Prerequisites
- Android Studio Hedgehog or later
- JDK 8 or later
- Android SDK with API level 34

### Build Instructions

1. Clone the repository:
```bash
git clone https://github.com/ThisIsDaMatt/eClock-Android-App.git
cd eClock-Android-App
```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Run the app on an emulator or physical device

Alternatively, build from command line:
```bash
./gradlew assembleDebug
```

## Releases

### Download APK

The latest release APK can be downloaded from the [Releases](https://github.com/ThisIsDaMatt/eClock-Android-App/releases) page.

### Creating a Release

To create a new release, follow the [RELEASE_GUIDE.md](RELEASE_GUIDE.md) which covers:
- Setting up signing keys
- Configuring GitHub secrets
- Creating release tags
- Automated APK building and publishing via GitHub Actions

## App Structure

```
app/
├── src/main/
│   ├── java/com/eclock/app/
│   │   ├── MainActivity.kt          # Main entry point
│   │   ├── MainScreen.kt            # Navigation setup
│   │   ├── ui/
│   │   │   ├── screens/
│   │   │   │   ├── ClockScreen.kt   # Clock feature
│   │   │   │   ├── TimerScreen.kt   # Timer feature
│   │   │   │   ├── CountdownScreen.kt # Countdown feature
│   │   │   │   ├── WorldTimeScreen.kt # World time feature
│   │   │   │   └── AlarmScreen.kt   # Alarm feature
│   │   │   └── theme/               # Material 3 theming
│   │   └── ...
│   └── res/                         # Resources
└── build.gradle.kts
```

## UI/UX Features

- **Smooth Animations**: Transitions and animated content changes
- **Material Design 3**: Modern, clean interface following Google's design guidelines
- **Bottom Navigation**: Easy navigation between features
- **Responsive Design**: Adapts to different screen sizes
- **Dark/Light Theme**: System-based theme switching
- **Intuitive Controls**: Simple and easy-to-use interface

## License

MIT License - see [LICENSE](LICENSE) file for details

## Author

Matías Múnera

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
