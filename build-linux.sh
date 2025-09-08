#!/bin/bash

# CloudStream Desktop Linux Build Script

echo "Building CloudStream Desktop for Linux..."

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    exit 1
fi

# Clean previous builds
echo "Cleaning previous builds..."
./gradlew clean

# Build the application
echo "Building application..."
./gradlew :desktop:build
if [ $? -ne 0 ]; then
    echo "Error: Build failed"
    exit 1
fi

# Create Linux packages
echo "Creating Linux packages..."
./gradlew :desktop:packageDistributionForCurrentOS
if [ $? -ne 0 ]; then
    echo "Error: Failed to create packages"
    exit 1
fi

# Create DEB package
echo "Creating DEB package..."
./gradlew :desktop:packageDeb
if [ $? -ne 0 ]; then
    echo "Warning: Failed to create DEB package"
fi

echo "Build complete!"
echo "Executable location: desktop/build/compose/binaries/main/"
echo "DEB package location: desktop/build/compose/binaries/main/deb/"