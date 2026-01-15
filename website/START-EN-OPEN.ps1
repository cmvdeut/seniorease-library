# Start Lokale Server en Open Browser
# Run: .\START-EN-OPEN.ps1

Write-Host "🚀 Starting local server and opening browser..." -ForegroundColor Cyan
Write-Host ""

$port = 8001  # Start with 8001 to avoid conflicts
$websitePath = $PSScriptRoot

# Check if port is available, try 8002 if needed
$portInUse = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host "⚠️  Port $port is already in use!" -ForegroundColor Yellow
    Write-Host "Trying port 8002..." -ForegroundColor Yellow
    $port = 8002
}

$url = "http://localhost:$port"

# Start server in background
$serverProcess = $null

# Try Python first
if (Get-Command python -ErrorAction SilentlyContinue) {
    Write-Host "✅ Starting Python HTTP server on port $port..." -ForegroundColor Green
    Set-Location $websitePath
    $serverProcess = Start-Process python -ArgumentList "-m", "http.server", "$port" -PassThru -WindowStyle Hidden
    Start-Sleep -Seconds 2
}
# Try Python3
elseif (Get-Command python3 -ErrorAction SilentlyContinue) {
    Write-Host "✅ Starting Python3 HTTP server on port $port..." -ForegroundColor Green
    Set-Location $websitePath
    $serverProcess = Start-Process python3 -ArgumentList "-m", "http.server", "$port" -PassThru -WindowStyle Hidden
    Start-Sleep -Seconds 2
}
# Try Vercel Dev
elseif (Get-Command vercel -ErrorAction SilentlyContinue) {
    Write-Host "✅ Starting Vercel Dev..." -ForegroundColor Green
    $port = 3000
    $url = "http://localhost:$port"
    Set-Location $websitePath
    $serverProcess = Start-Process vercel -ArgumentList "dev" -PassThru -WindowStyle Hidden
    Start-Sleep -Seconds 5
}
else {
    Write-Host "❌ No server found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Install Python: https://www.python.org/downloads/" -ForegroundColor Yellow
    exit 1
}

# Open browser
Write-Host ""
Write-Host "🌐 Opening browser..." -ForegroundColor Cyan
Write-Host "URL: $url" -ForegroundColor Yellow
Write-Host ""

Start-Process $url

Write-Host "✅ Server is running!" -ForegroundColor Green
Write-Host ""
Write-Host "Server URL: $url" -ForegroundColor Cyan
Write-Host ""
Write-Host "To stop the server:" -ForegroundColor Yellow
Write-Host "  Press Ctrl+C in this window" -ForegroundColor White
Write-Host "  Or close this PowerShell window" -ForegroundColor White
Write-Host ""

# Keep script running
try {
    Wait-Process -Id $serverProcess.Id -ErrorAction SilentlyContinue
} catch {
    Write-Host "Server stopped." -ForegroundColor Yellow
}
