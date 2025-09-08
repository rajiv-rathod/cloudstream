@echo off
echo Building CloudStream Desktop for Windows...

:: Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    exit /b 1
)

:: Clean previous builds
echo Cleaning previous builds...
call gradlew clean

:: Build the application
echo Building application...
call gradlew :desktop:build
if %errorlevel% neq 0 (
    echo Error: Build failed
    exit /b 1
)

:: Create Windows executable
echo Creating Windows executable...
call gradlew :desktop:packageDistributionForCurrentOS
if %errorlevel% neq 0 (
    echo Error: Failed to create executable
    exit /b 1
)

:: Create MSI installer
echo Creating MSI installer...
call gradlew :desktop:packageMsi
if %errorlevel% neq 0 (
    echo Warning: Failed to create MSI installer
)

:: Sign the executable if certificate is available
if exist "certificate.p12" (
    echo Signing executable...
    signtool sign /f "certificate.p12" /p "%CERT_PASSWORD%" /t http://timestamp.sectigo.com /d "CloudStream Desktop" desktop\build\compose\binaries\main\exe\CloudStream.exe
    if %errorlevel% neq 0 (
        echo Warning: Failed to sign executable
    ) else (
        echo Executable signed successfully
    )
) else (
    echo Warning: No certificate found, executable will not be signed
    echo This may cause Windows SmartScreen warnings
)

echo Build complete!
echo Executable location: desktop\build\compose\binaries\main\exe\
echo MSI installer location: desktop\build\compose\binaries\main\msi\

pause