@echo off
cd /d "%~dp0"
title SecureU — Local Web Portal

echo ========================================================
echo   SECURE U — LOCAL WEB PORTAL LAUNCHER
echo   Course: Java Project / DBMS Lab (Local Web View)
echo ========================================================
echo.

:: 1. Check if port 3000 is occupied by a previous process and clean it up
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000 ^| findstr LISTENING') do (
    echo [INFO] Freeing port 3000 from previous instance (PID %%a)...
    taskkill /F /PID %%a >nul 2>&1
    timeout /t 1 /nobreak >nul
)

:: 2. Launch browser after 1 second in background
start "" cmd /c "timeout /t 1 /nobreak >nul && start http://localhost:3000"

:: 3. Start local server via Node.js
echo [INFO] Starting local web server on http://localhost:3000...
echo [INFO] Opening default browser...
echo.
node local-server.js

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [NOTICE] If Node.js is not in PATH, you can also launch:
    echo          Launch-CollegeComplaint.bat (runs Java built-in server on port 3000)
)

echo.
pause
