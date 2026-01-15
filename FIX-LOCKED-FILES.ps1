# Fix Locked Gradle Files
# Run: .\FIX-LOCKED-FILES.ps1

Write-Host "🔧 Fix Locked Gradle Files" -ForegroundColor Cyan
Write-Host ""

# Stop Gradle daemon
Write-Host "1. Stop Gradle daemon..." -ForegroundColor Yellow
& ".\gradlew.bat" --stop 2>&1 | Out-Null
Start-Sleep -Seconds 2
Write-Host "✅ Gradle daemon gestopt" -ForegroundColor Green
Write-Host ""

# Find and kill Java processes (Gradle daemon)
Write-Host "2. Zoek Java processen (Gradle daemon)..." -ForegroundColor Yellow
$javaProcs = Get-Process -Name "java" -ErrorAction SilentlyContinue
if ($javaProcs) {
    Write-Host "⚠️  Java processen gevonden - stop deze..." -ForegroundColor Yellow
    foreach ($proc in $javaProcs) {
        try {
            Write-Host "   Stop PID: $($proc.Id)" -ForegroundColor White
            Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue
        } catch {
            Write-Host "   ⚠️  Kon niet stoppen: $($proc.Id)" -ForegroundColor Yellow
        }
    }
    Start-Sleep -Seconds 2
    Write-Host "✅ Java processen gestopt" -ForegroundColor Green
} else {
    Write-Host "✅ Geen Java processen gevonden" -ForegroundColor Green
}
Write-Host ""

# Check if Android Studio is running
Write-Host "3. Check Android Studio..." -ForegroundColor Yellow
$studioProcs = Get-Process -Name "studio64" -ErrorAction SilentlyContinue
if ($studioProcs) {
    Write-Host "⚠️  Android Studio draait nog!" -ForegroundColor Yellow
    Write-Host "   Sluit Android Studio volledig en probeer opnieuw" -ForegroundColor White
    Write-Host ""
    Write-Host "Of force kill (niet aanbevolen):" -ForegroundColor Yellow
    Write-Host "   Stop-Process -Name studio64 -Force" -ForegroundColor White
} else {
    Write-Host "✅ Android Studio is niet actief" -ForegroundColor Green
}
Write-Host ""

# Try to build without clean
Write-Host "4. Build zonder clean..." -ForegroundColor Yellow
Write-Host ""
& ".\gradlew.bat" assembleDemoRelease
