# Windows Build Configuration

This file contains instructions for building and signing the Windows executable.

## Prerequisites

1. **Install Java 11 or higher**
2. **Install Gradle** (or use the wrapper)
3. **Install WiX Toolset** (for MSI installer)
4. **Get a Code Signing Certificate** (for Windows SmartScreen bypass)

## Building the Application

### 1. Build the JAR
```bash
./gradlew :desktop:build
```

### 2. Create Windows Executable
```bash
./gradlew :desktop:packageDistributionForCurrentOS
```

### 3. Create MSI Installer
```bash
./gradlew :desktop:packageMsi
```

## Code Signing

To avoid Windows SmartScreen warnings, you need to sign the executable with a valid certificate.

### Using signtool (Windows SDK)
```bash
signtool sign /f "certificate.p12" /p "password" /t http://timestamp.sectigo.com CloudStream.exe
```

### Using jarsigner for JAR files
```bash
jarsigner -keystore certificate.jks -storetype PKCS12 CloudStream.jar "alias"
```

## Certificate Options

1. **Self-signed certificate** (will still show warnings)
2. **Code signing certificate from CA** (like DigiCert, Sectigo)
3. **Extended Validation (EV) certificate** (best for SmartScreen)

## Automated Build Script

See `build-windows.bat` for automated building and signing.

## Distribution

The final executable will be located in:
- `desktop/build/compose/binaries/main/exe/`
- `desktop/build/compose/binaries/main/msi/`