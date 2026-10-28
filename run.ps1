# SecureU PowerShell Launcher
param([string]$mode)

$javaBin = "C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe"
$jarPath = Join-Path $PSScriptRoot "CollegeComplaint.jar"

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "Starting SecureU Pure Java Swing Desktop App..." -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Cyan

if (Test-Path $javaBin) {
    if ($mode) {
        & $javaBin -jar $jarPath $mode
    } else {
        & $javaBin -jar $jarPath
    }
} else {
    if ($mode) {
        java -jar $jarPath $mode
    } else {
        java -jar $jarPath
    }
}
