@echo off
cd /d "%~dp0"
title SecureU Desktop Java Application
echo ========================================================
echo Starting SecureU Java Application...
echo ========================================================
echo.

:: 1. Check if MySQL (port 3306) is running
netstat -ano | findstr "3306" >nul
if %ERRORLEVEL% NEQ 0 (
    echo [NOTICE] MySQL is not detected on port 3306.
    echo Please make sure XAMPP MySQL is started.
    echo.
) else (
    echo [OK] MySQL is running on port 3306.
)

:: 2. Check if port 3000 is occupied and free it
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000 ^| findstr LISTENING') do (
    echo [INFO] Port 3000 is in use by PID %%a. Cleaning up previous instance...
    taskkill /F /PID %%a >nul 2>&1
    timeout /t 1 /nobreak >nul
)

:: 3. Detect Java binary
set "JAVA_BIN=C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe"
if not exist "%JAVA_BIN%" (
    if defined JAVA_HOME (
        set "JAVA_BIN=%JAVA_HOME%\bin\java.exe"
    ) else (
        set "JAVA_BIN=java"
    )
)

:: 4. Launch the application
echo [JAVA] Launching via "%JAVA_BIN%"...
"%JAVA_BIN%" -jar CollegeComplaint.jar

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Application exited with code %ERRORLEVEL%.
)

echo.
echo Press any key to close this window...
pause >nul
