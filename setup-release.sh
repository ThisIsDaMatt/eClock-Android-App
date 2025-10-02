#!/bin/bash

# Setup Script for eClock Release Configuration
# This script helps you generate the signing key and prepare the GitHub secrets

set -e

echo "============================================"
echo "eClock APK Release Setup Helper"
echo "============================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if keytool is available
if ! command -v keytool &> /dev/null; then
    echo -e "${RED}ERROR: keytool not found!${NC}"
    echo "Please install Java JDK to use keytool"
    exit 1
fi

# Check if base64 is available
if ! command -v base64 &> /dev/null; then
    echo -e "${RED}ERROR: base64 not found!${NC}"
    echo "Please install base64 utility"
    exit 1
fi

echo "This script will help you:"
echo "1. Generate a keystore for signing the APK"
echo "2. Create the base64 encoded key for GitHub Secrets"
echo "3. Show you the values to add as GitHub Secrets"
echo ""

# Ask for confirmation
read -p "Do you want to continue? (y/n) " -n 1 -r
echo ""
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Setup cancelled."
    exit 0
fi

echo ""
echo "============================================"
echo "Step 1: Generate Keystore"
echo "============================================"
echo ""

# Get keystore details
read -p "Enter keystore filename [eClock-release-key.jks]: " KEYSTORE_FILE
KEYSTORE_FILE=${KEYSTORE_FILE:-eClock-release-key.jks}

read -p "Enter key alias [eclock-key]: " KEY_ALIAS
KEY_ALIAS=${KEY_ALIAS:-eclock-key}

echo ""
echo "You'll be asked for passwords and personal information."
echo "IMPORTANT: Remember these passwords! You'll need them for updates!"
echo ""

# Generate keystore
keytool -genkey -v -keystore "$KEYSTORE_FILE" \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias "$KEY_ALIAS"

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Keystore generated successfully!${NC}"
else
    echo -e "${RED}✗ Failed to generate keystore${NC}"
    exit 1
fi

echo ""
echo "============================================"
echo "Step 2: Convert to Base64"
echo "============================================"
echo ""

# Convert to base64
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    BASE64_KEY=$(base64 -i "$KEYSTORE_FILE")
else
    # Linux
    BASE64_KEY=$(base64 -w 0 "$KEYSTORE_FILE")
fi

if [ -z "$BASE64_KEY" ]; then
    echo -e "${RED}✗ Failed to convert keystore to base64${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Keystore converted to base64${NC}"

echo ""
echo "============================================"
echo "Step 3: GitHub Secrets Configuration"
echo "============================================"
echo ""

echo "Now you need to add these secrets to your GitHub repository:"
echo ""
echo "1. Go to: https://github.com/YOUR_USERNAME/eClock-Android-App/settings/secrets/actions"
echo "2. Click 'New repository secret' for each of the following:"
echo ""

# Read passwords from user for display
echo -e "${YELLOW}Please enter the passwords you just created:${NC}"
read -sp "Keystore password: " KEYSTORE_PASSWORD
echo ""
read -sp "Key password (press Enter if same as keystore): " KEY_PASSWORD
echo ""
KEY_PASSWORD=${KEY_PASSWORD:-$KEYSTORE_PASSWORD}

echo ""
echo "============================================"
echo -e "${GREEN}GitHub Secrets to Add:${NC}"
echo "============================================"
echo ""

echo -e "${YELLOW}Secret 1: SIGNING_KEY${NC}"
echo "$BASE64_KEY" | fold -w 80
echo ""

echo -e "${YELLOW}Secret 2: KEY_ALIAS${NC}"
echo "$KEY_ALIAS"
echo ""

echo -e "${YELLOW}Secret 3: KEY_STORE_PASSWORD${NC}"
echo "$KEYSTORE_PASSWORD"
echo ""

echo -e "${YELLOW}Secret 4: KEY_PASSWORD${NC}"
echo "$KEY_PASSWORD"
echo ""

# Save to file
SECRETS_FILE="github-secrets.txt"
cat > "$SECRETS_FILE" << EOF
GitHub Secrets for eClock Release
==================================

Add these secrets at: https://github.com/YOUR_USERNAME/eClock-Android-App/settings/secrets/actions

1. SIGNING_KEY:
$BASE64_KEY

2. KEY_ALIAS:
$KEY_ALIAS

3. KEY_STORE_PASSWORD:
$KEYSTORE_PASSWORD

4. KEY_PASSWORD:
$KEY_PASSWORD

IMPORTANT NOTES:
- Keep this file secure and delete it after adding secrets to GitHub!
- Keep $KEYSTORE_FILE safe - you'll need it to update your app!
- Never commit $KEYSTORE_FILE to git!
EOF

echo "============================================"
echo -e "${GREEN}Setup Complete!${NC}"
echo "============================================"
echo ""
echo "✓ Keystore created: $KEYSTORE_FILE"
echo "✓ Secrets saved to: $SECRETS_FILE"
echo ""
echo -e "${YELLOW}IMPORTANT:${NC}"
echo "1. Add the secrets to GitHub (see $SECRETS_FILE)"
echo "2. Keep $KEYSTORE_FILE in a safe place (backup recommended)"
echo "3. Delete $SECRETS_FILE after adding secrets to GitHub"
echo "4. Never commit keystore files to git!"
echo ""
echo -e "${GREEN}Next steps:${NC}"
echo "1. Add the secrets to GitHub"
echo "2. Create a release tag: git tag v1.0.0 && git push origin v1.0.0"
echo "3. Watch the Actions tab for the build to complete!"
echo ""
echo "For more information, see QUICK_START.md or RELEASE_GUIDE.md"
echo ""
