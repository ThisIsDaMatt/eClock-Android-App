# Building and Running the eClock App

## Quick Start Guide

### Prerequisites

Before building the app, ensure you have:

1. **Android Studio** (Hedgehog 2023.1.1 or later)
   - Download from: https://developer.android.com/studio

2. **Java Development Kit (JDK)** 8 or later
   - Check version: `java -version`
   - Android Studio includes JDK

3. **Android SDK** with the following components:
   - Android SDK Platform 34
   - Android SDK Build-Tools 34.0.0
   - Android SDK Platform-Tools

4. **Minimum Device Requirements**:
   - Android 7.0 (API level 24) or higher
   - Recommended: Android 12 (API level 31) or higher for dynamic colors

---

## Installation Steps

### Method 1: Using Android Studio (Recommended)

1. **Clone the Repository**
   ```bash
   git clone https://github.com/ThisIsDaMatt/eClock-Android-App.git
   cd eClock-Android-App
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Click "Open" or "Open an Existing Project"
   - Navigate to the cloned directory
   - Select the project folder
   - Click "OK"

3. **Wait for Gradle Sync**
   - Android Studio will automatically sync Gradle
   - This may take a few minutes on first run
   - All dependencies will be downloaded

4. **Run the App**
   - Connect an Android device via USB (with USB debugging enabled)
   - OR start an Android Virtual Device (AVD) emulator
   - Click the "Run" button (green triangle) in Android Studio
   - Select your target device
   - Wait for the app to build and install

---

### Method 2: Using Command Line

1. **Clone the Repository**
   ```bash
   git clone https://github.com/ThisIsDaMatt/eClock-Android-App.git
   cd eClock-Android-App
   ```

2. **Build Debug APK**
   ```bash
   ./gradlew assembleDebug
   ```
   
   The APK will be generated at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Install on Connected Device**
   ```bash
   ./gradlew installDebug
   ```

4. **Build and Run**
   ```bash
   ./gradlew build
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

---

## Setting Up an Emulator (If No Physical Device)

### Creating a Virtual Device

1. **Open AVD Manager** in Android Studio
   - Tools → Device Manager

2. **Create New Virtual Device**
   - Click "Create Device"
   - Select a device definition (e.g., Pixel 5)
   - Click "Next"

3. **Select System Image**
   - Choose Android 12 (API 31) or higher recommended
   - Click "Download" if not already installed
   - Click "Next"

4. **Configure AVD**
   - Name your AVD (e.g., "Pixel_5_API_31")
   - Click "Finish"

5. **Start Emulator**
   - Click the play button next to your AVD
   - Wait for emulator to boot

---

## Enabling USB Debugging (For Physical Devices)

### Android Device Setup

1. **Enable Developer Options**
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   - You'll see "You are now a developer!"

2. **Enable USB Debugging**
   - Go to Settings → System → Developer Options
   - Enable "USB debugging"
   - Connect device to computer via USB

3. **Authorize Computer**
   - When connected, a dialog will appear on device
   - Check "Always allow from this computer"
   - Tap "OK"

4. **Verify Connection**
   ```bash
   adb devices
   ```
   You should see your device listed

---

## Building Release APK

### For Production Release

1. **Generate Signing Key** (first time only)
   ```bash
   keytool -genkey -v -keystore my-release-key.jks \
     -keyalg RSA -keysize 2048 -validity 10000 \
     -alias my-key-alias
   ```

2. **Configure Signing in build.gradle.kts**
   ```kotlin
   android {
       signingConfigs {
           create("release") {
               storeFile = file("my-release-key.jks")
               storePassword = "your-password"
               keyAlias = "my-key-alias"
               keyPassword = "your-password"
           }
       }
       
       buildTypes {
           release {
               signingConfig = signingConfigs.getByName("release")
               isMinifyEnabled = true
               proguardFiles(...)
           }
       }
   }
   ```

3. **Build Release APK**
   ```bash
   ./gradlew assembleRelease
   ```
   
   APK location:
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

---

## Troubleshooting

### Common Issues and Solutions

#### Issue: Gradle Sync Failed
**Solution:**
- Check internet connection
- Update Gradle: `./gradlew wrapper --gradle-version 8.2`
- Invalidate caches: File → Invalidate Caches / Restart

#### Issue: SDK Not Found
**Solution:**
- Open SDK Manager: Tools → SDK Manager
- Install Android SDK Platform 34
- Install Android SDK Build-Tools 34.0.0

#### Issue: Device Not Detected
**Solution:**
- Check USB cable
- Enable USB debugging on device
- Run: `adb kill-server && adb start-server`
- Try different USB port

#### Issue: Build Fails with "Out of Memory"
**Solution:**
- Increase Gradle memory in `gradle.properties`:
  ```
  org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
  ```

#### Issue: App Crashes on Launch
**Solution:**
- Check Logcat in Android Studio
- Ensure device is Android 7.0 or higher
- Clear app data and reinstall

---

## Project Structure Overview

```
eClock-Android-App/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/eclock/app/     # Kotlin source files
│   │       ├── res/                      # Resources (layouts, strings, etc.)
│   │       └── AndroidManifest.xml       # App configuration
│   └── build.gradle.kts                  # Module build config
├── gradle/                               # Gradle wrapper files
├── build.gradle.kts                      # Root build config
├── settings.gradle.kts                   # Project settings
└── gradlew                               # Gradle wrapper script
```

---

## Gradle Tasks Reference

### Common Commands

```bash
# Clean build artifacts
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install debug app on connected device
./gradlew installDebug

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Check for dependency updates
./gradlew dependencyUpdates

# Generate APK and bundle
./gradlew build
```

---

## Development Workflow

### Recommended Workflow

1. **Start Development**
   ```bash
   git checkout -b feature/my-feature
   ```

2. **Make Changes**
   - Edit Kotlin files in `app/src/main/java/`
   - Update resources in `app/src/main/res/`
   - Test changes frequently

3. **Build and Test**
   ```bash
   ./gradlew assembleDebug
   ./gradlew test
   ```

4. **Run on Device/Emulator**
   - Use Android Studio's Run button
   - Or: `./gradlew installDebug`

5. **Commit Changes**
   ```bash
   git add .
   git commit -m "Description of changes"
   git push origin feature/my-feature
   ```

---

## Running Tests

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests (Requires device/emulator)
```bash
./gradlew connectedAndroidTest
```

### Test Coverage Report
```bash
./gradlew jacocoTestReport
```

---

## Debugging

### Using Android Studio Debugger

1. **Set Breakpoints**
   - Click left margin in code editor
   - Red dot appears

2. **Debug App**
   - Click "Debug" button (bug icon)
   - Or: Run → Debug 'app'

3. **Inspect Variables**
   - When breakpoint hits, check "Variables" panel
   - Step through code with controls

### Using Logcat

1. **Add Logs in Code**
   ```kotlin
   import android.util.Log
   
   Log.d("ClockScreen", "Time updated: $currentTime")
   Log.e("AlarmScreen", "Error setting alarm", exception)
   ```

2. **View Logs**
   - Open Logcat in Android Studio
   - Filter by tag or package name
   - Choose log level (Debug, Error, etc.)

---

## Performance Optimization

### Best Practices

1. **Enable R8 Shrinking** (for release builds)
   - Already configured in `build.gradle.kts`
   - Reduces APK size
   - Optimizes code

2. **Profile Your App**
   - Use Android Studio Profiler
   - Check CPU, memory, network usage
   - Identify bottlenecks

3. **Optimize Compose Performance**
   - Use `remember` for expensive calculations
   - Avoid unnecessary recompositions
   - Use `LaunchedEffect` for side effects

---

## Continuous Integration (CI)

### GitHub Actions Example

Create `.github/workflows/android.yml`:

```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    
    - name: Build with Gradle
      run: ./gradlew build
    
    - name: Run tests
      run: ./gradlew test
```

---

## App Signing and Publishing

### Publishing to Google Play Store

1. **Prepare Release Build**
   - Sign APK/AAB with release key
   - Test thoroughly on multiple devices
   - Prepare screenshots and descriptions

2. **Create Developer Account**
   - Visit: https://play.google.com/console
   - Pay one-time $25 fee

3. **Create App Listing**
   - Add app details, screenshots, description
   - Set pricing and distribution

4. **Upload Release**
   - Upload signed AAB (recommended) or APK
   - Fill out content rating questionnaire
   - Submit for review

---

## Useful Resources

### Official Documentation
- [Android Developer Guide](https://developer.android.com/guide)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)

### Tools
- [Android Studio](https://developer.android.com/studio)
- [Gradle Documentation](https://docs.gradle.org/)
- [ADB Command Reference](https://developer.android.com/studio/command-line/adb)

### Community
- [Stack Overflow - Android](https://stackoverflow.com/questions/tagged/android)
- [Reddit r/androiddev](https://www.reddit.com/r/androiddev/)
- [Android Developers Community](https://developer.android.com/community)

---

## Support

For issues, questions, or contributions:
- **GitHub Issues**: https://github.com/ThisIsDaMatt/eClock-Android-App/issues
- **Pull Requests**: https://github.com/ThisIsDaMatt/eClock-Android-App/pulls
- **Discussions**: https://github.com/ThisIsDaMatt/eClock-Android-App/discussions

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Happy coding! 🎉
