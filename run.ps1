# CollegeComplaint PowerShell Launcher
$javaBin = "C:\Program Files\Java\jdk-27.0.1.0.3\bin\java.exe"
$jarPath = Join-Path $PSScriptRoot "CollegeComplaint.jar"

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "Starting CollegeComplaint Java Swing Desktop App..." -ForegroundColor Green
Write-Host "========================================================" -ForegroundColor Cyan

if (Test-Path $javaBin) {
    & $javaBin -jar $jarPath
} else {
    java -jar $jarPath
}
