# Quick Start: Creating Your First Release

This is a quick guide to help you create your first APK release for the eClock app.

## Step 1: Generate Signing Key

Run this command on your local machine (you'll only do this once):

```bash
keytool -genkey -v -keystore eClock-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias eclock-key
```

You'll be asked to:
1. Enter a keystore password (create a strong password and save it!)
2. Re-enter the password
3. Enter your first and last name
4. Enter organizational unit (optional, can just press Enter)
5. Enter organization name (optional, can just press Enter)
6. Enter city/locality (optional, can just press Enter)
7. Enter state/province (optional, can just press Enter)  
8. Enter country code (optional, can just press Enter)
9. Confirm with 'yes'
10. Enter key password (you can press Enter to use the same as keystore password)

**IMPORTANT**: Save this file and passwords in a safe place! If you lose them, you won't be able to update your app!

## Step 2: Convert Keystore to Base64

### On Linux/Mac:
```bash
base64 -w 0 eClock-release-key.jks > keystore-base64.txt
cat keystore-base64.txt
```

### On Windows (PowerShell):
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("eClock-release-key.jks")) | Out-File -Encoding ASCII keystore-base64.txt
Get-Content keystore-base64.txt
```

Copy the output (the long base64 string).

## Step 3: Configure GitHub Secrets

1. Go to your repository on GitHub: https://github.com/ThisIsDaMatt/eClock-Android-App
2. Click **Settings** (top right)
3. In the left sidebar, click **Secrets and variables** → **Actions**
4. Click **New repository secret**
5. Add these four secrets:

   **Secret 1:**
   - Name: `SIGNING_KEY`
   - Value: (paste the base64 string from Step 2)

   **Secret 2:**
   - Name: `KEY_ALIAS`
   - Value: `eclock-key` (or whatever alias you used)

   **Secret 3:**
   - Name: `KEY_STORE_PASSWORD`
   - Value: (the keystore password you created)

   **Secret 4:**
   - Name: `KEY_PASSWORD`
   - Value: (the key password you created)

## Step 4: Create a Release

Now you can create a release! You have two options:

### Option A: From Command Line (Git)

```bash
# Make sure you're on the main branch and up to date
git checkout main
git pull

# Create and push a version tag
git tag v1.0.0
git push origin v1.0.0
```

### Option B: From GitHub Web Interface

1. Go to your repository on GitHub
2. Click on **Releases** (right sidebar)
3. Click **Create a new release**
4. Click **Choose a tag**
5. Type a new tag (e.g., `v1.0.0`) and click **Create new tag: v1.0.0 on publish**
6. Fill in the release title (e.g., "eClock v1.0.0 - Initial Release")
7. Add release notes (describe what's new/changed)
8. Click **Publish release**

## Step 5: Wait for Build

1. Go to the **Actions** tab in your repository
2. You'll see a workflow run for "Build and Release APK"
3. Wait for it to complete (usually 2-5 minutes)
4. When it's green/complete, go back to **Releases**
5. You'll see your APK file attached to the release!

## Step 6: Download and Test

1. Download the APK from the release page
2. Transfer it to your Android device
3. Install and test it
4. Share the release link with others!

## Troubleshooting

### Build fails with "secret not found"
→ Make sure you added all 4 secrets correctly in GitHub Settings

### Build fails with "Gradle error"
→ Check the Actions log for details, might need to update dependencies

### Can't install APK on device
→ Enable "Install from Unknown Sources" in Android settings

## Next Release

For subsequent releases, just update the version number in `app/build.gradle.kts` and create a new tag:

```bash
# Update versionCode and versionName in app/build.gradle.kts
# Then:
git add app/build.gradle.kts
git commit -m "Bump version to 1.1.0"
git tag v1.1.0
git push origin main
git push origin v1.1.0
```

## Need More Help?

See the full [RELEASE_GUIDE.md](RELEASE_GUIDE.md) for detailed documentation.

---

**That's it!** Your app will automatically build, sign, and publish whenever you create a new version tag! 🚀
