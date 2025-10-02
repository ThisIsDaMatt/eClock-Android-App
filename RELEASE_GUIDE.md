# eClock Release Guide

This guide explains how to build and release the eClock Android app using the automated GitHub Actions workflow.

## Overview

The eClock app uses GitHub Actions to automatically build, sign, and release APK files. When you create a version tag, the workflow will:

1. Build a signed release APK
2. Create a GitHub Release
3. Attach the APK to the release
4. Generate release notes automatically

## Prerequisites

### 1. Generate a Signing Key (One-time Setup)

Before creating your first release, you need to generate a keystore for signing the APK:

```bash
keytool -genkey -v -keystore eClock-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias eclock-key
```

You'll be prompted to enter:
- Keystore password (remember this!)
- Key password (remember this!)
- Your name and organization details

**IMPORTANT**: Keep this keystore file secure and never commit it to the repository!

### 2. Configure GitHub Secrets

The release workflow requires the following secrets to be configured in your GitHub repository:

1. Go to your GitHub repository
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret** and add the following:

| Secret Name | Description | How to Get |
|-------------|-------------|------------|
| `SIGNING_KEY` | Base64 encoded keystore file | `base64 -w 0 eClock-release-key.jks` |
| `KEY_ALIAS` | Keystore alias | The alias you used (e.g., "eclock-key") |
| `KEY_STORE_PASSWORD` | Keystore password | The password you set |
| `KEY_PASSWORD` | Key password | The key password you set |

#### Converting Keystore to Base64

On Linux/macOS:
```bash
base64 -w 0 eClock-release-key.jks > keystore-base64.txt
```

On Windows (PowerShell):
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("eClock-release-key.jks")) | Out-File -Encoding ASCII keystore-base64.txt
```

Then copy the contents of `keystore-base64.txt` and paste it as the `SIGNING_KEY` secret value.

## Creating a Release

### Method 1: Using Git Tags (Recommended)

1. **Ensure your code is ready for release**
   ```bash
   git status
   git add .
   git commit -m "Prepare for v1.0.0 release"
   ```

2. **Update version in app/build.gradle.kts** (if needed)
   ```kotlin
   versionCode = 1
   versionName = "1.0.0"
   ```

3. **Create and push a version tag**
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

4. **The workflow will automatically:**
   - Build the release APK
   - Sign it with your keystore
   - Create a GitHub Release
   - Upload the signed APK

5. **Check the release**
   - Go to your GitHub repository
   - Click on **Releases** (on the right sidebar)
   - You should see your new release with the APK attached

### Method 2: Manual Workflow Trigger

1. Go to your GitHub repository
2. Click on **Actions** tab
3. Select **Build and Release APK** workflow
4. Click **Run workflow**
5. Select the branch
6. Click **Run workflow**

This will build and create an artifact, but won't create a release (releases require a tag).

## Release Workflow Details

### Workflow File: `.github/workflows/release.yml`

The release workflow:
- **Triggers on**: Version tags matching `v*.*.*` (e.g., v1.0.0, v2.1.3)
- **Runs on**: Ubuntu latest
- **Java Version**: JDK 17
- **Build Tool**: Gradle 8.2
- **Output**: Signed APK named `eClock-v*.apk`

### Build Workflow: `.github/workflows/build.yml`

For regular builds (non-releases):
- **Triggers on**: Push/PR to main branch
- **Builds**: Debug APK
- **Runs**: Unit tests
- **Uploads**: Debug APK as artifact

## Version Numbering

Follow semantic versioning: `vMAJOR.MINOR.PATCH`

- **MAJOR**: Incompatible API changes
- **MINOR**: New functionality (backwards compatible)
- **PATCH**: Bug fixes (backwards compatible)

Examples:
- `v1.0.0` - Initial release
- `v1.1.0` - Added new feature
- `v1.1.1` - Bug fix
- `v2.0.0` - Breaking changes

## Troubleshooting

### Release workflow fails with "signingKeyBase64: not found"

**Solution**: Make sure you've configured the GitHub secrets as described in the Prerequisites section.

### APK is not signed

**Solution**: Verify that all four secrets (SIGNING_KEY, KEY_ALIAS, KEY_STORE_PASSWORD, KEY_PASSWORD) are correctly set in GitHub repository secrets.

### Build fails with Gradle error

**Solution**: 
1. Check the Actions logs for specific error
2. Ensure the code builds locally: `./gradlew assembleRelease`
3. Check that all dependencies are properly configured

### Release is created but APK is missing

**Solution**: Check the workflow logs to see if the signing step failed. Ensure your keystore secrets are correct.

### Release workflow fails with "Resource not accessible by integration"

**Solution**: This error occurs when the GitHub Actions workflow lacks the necessary permissions to create releases. The workflow has been updated to include the required `permissions: contents: write` setting. If you're still experiencing this issue, ensure you're using the latest version of the workflow file from the repository.

## Manual Release Build (Local)

If you need to build a release APK locally:

### Without Signing (Debug Release)

```bash
./gradlew assembleRelease
```

APK location: `app/build/outputs/apk/release/app-release-unsigned.apk`

### With Signing (Production Release)

1. Create `keystore.properties` in the project root:
   ```properties
   storeFile=path/to/eClock-release-key.jks
   storePassword=your_keystore_password
   keyAlias=eclock-key
   keyPassword=your_key_password
   ```

2. Update `app/build.gradle.kts`:
   ```kotlin
   val keystorePropertiesFile = rootProject.file("keystore.properties")
   val keystoreProperties = Properties()
   if (keystorePropertiesFile.exists()) {
       keystoreProperties.load(FileInputStream(keystorePropertiesFile))
   }

   android {
       signingConfigs {
           create("release") {
               storeFile = file(keystoreProperties["storeFile"] ?: "")
               storePassword = keystoreProperties["storePassword"] as String?
               keyAlias = keystoreProperties["keyAlias"] as String?
               keyPassword = keystoreProperties["keyPassword"] as String?
           }
       }
       
       buildTypes {
           release {
               signingConfig = signingConfigs.getByName("release")
               // ... other config
           }
       }
   }
   ```

3. Build:
   ```bash
   ./gradlew assembleRelease
   ```

APK location: `app/build/outputs/apk/release/app-release.apk`

**Note**: Never commit `keystore.properties` or the keystore file!

## Publishing to Google Play Store

Once you have a signed release APK:

1. **Create a Developer Account**
   - Visit: https://play.google.com/console
   - Pay one-time $25 registration fee

2. **Create App Listing**
   - Fill in app details
   - Add screenshots (use images from VISUAL_PREVIEW.md)
   - Set pricing (free)
   - Choose distribution countries

3. **Upload Release**
   - Use Android App Bundle (AAB) for better optimization:
     ```bash
     ./gradlew bundleRelease
     ```
   - Or upload the signed APK
   - Fill out content rating questionnaire
   - Submit for review

4. **Wait for Approval**
   - Usually takes a few hours to a few days
   - You'll receive email when app is published

## Distribution Options

### 1. GitHub Releases (Current Setup)
- ✅ Free
- ✅ Automatic via workflow
- ✅ Direct APK download
- ❌ Users need to enable "Unknown sources"

### 2. Google Play Store
- ✅ Official store
- ✅ Automatic updates
- ✅ Better discoverability
- ❌ $25 one-time fee
- ❌ Review process

### 3. F-Droid
- ✅ Free and open-source store
- ✅ No registration fee
- ❌ Requires app to be fully FOSS

## Security Best Practices

1. **Never commit sensitive files:**
   - ✅ Add to `.gitignore`: `*.jks`, `keystore.properties`
   - ✅ Use GitHub Secrets for CI/CD
   
2. **Keystore backup:**
   - ✅ Keep secure backup of keystore file
   - ✅ Store passwords in password manager
   - ⚠️ If lost, you can't update the app!

3. **Code security:**
   - ✅ Enable ProGuard/R8 for release builds
   - ✅ Remove debug logs in production
   - ✅ Validate user inputs

## Next Steps

1. ✅ Set up GitHub secrets (one-time)
2. ✅ Create your first release tag
3. ✅ Download and test the APK
4. ✅ Share the release link with users
5. ✅ (Optional) Publish to Google Play Store

## Support

For issues with:
- **App functionality**: Open an issue on GitHub
- **Build/release process**: Check workflow logs in Actions tab
- **Play Store**: Consult Google Play Console help

---

**Congratulations!** You now have an automated release pipeline for the eClock Android app! 🎉
