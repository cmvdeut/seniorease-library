# Start Lokale Server voor Website Testen
# Run: .\START-LOKALE-SERVER.ps1

Write-Host "🚀 Starting local server..." -ForegroundColor Cyan
Write-Host ""

$port = 8000
$websitePath = $PSScriptRoot

# Check if port is available
$portInUse = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host "⚠️  Port $port is already in use!" -ForegroundColor Yellow
    Write-Host "Trying port 8001..." -ForegroundColor Yellow
    $port = 8001
}

# Try Python first
if (Get-Command python -ErrorAction SilentlyContinue) {
    Write-Host "✅ Using Python HTTP server" -ForegroundColor Green
    Write-Host ""
    Write-Host "Server starting on: http://localhost:$port" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Press Ctrl+C to stop" -ForegroundColor Yellow
    Write-Host ""
    
    Set-Location $websitePath
    python -m http.server $port
}
# Try Python3
elseif (Get-Command python3 -ErrorAction SilentlyContinue) {
    Write-Host "✅ Using Python3 HTTP server" -ForegroundColor Green
    Write-Host ""
    Write-Host "Server starting on: http://localhost:$port" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Press Ctrl+C to stop" -ForegroundColor Yellow
    Write-Host ""
    
    Set-Location $websitePath
    python3 -m http.server $port
}
# Try Node.js http-server
elseif (Get-Command npx -ErrorAction SilentlyContinue) {
    Write-Host "✅ Using Node.js http-server" -ForegroundColor Green
    Write-Host ""
    Write-Host "Server starting on: http://localhost:$port" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Press Ctrl+C to stop" -ForegroundColor Yellow
    Write-Host ""
    
    Set-Location $websitePath
    npx -y http-server -p $port
}
# Try Vercel Dev
elseif (Get-Command vercel -ErrorAction SilentlyContinue) {
    Write-Host "✅ Using Vercel Dev" -ForegroundColor Green
    Write-Host ""
    Write-Host "Server starting on: http://localhost:3000" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Press Ctrl+C to stop" -ForegroundColor Yellow
    Write-Host ""
    
    Set-Location $websitePath
    vercel dev
}
else {
    Write-Host "❌ No server found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Install one of these:" -ForegroundColor Yellow
    Write-Host "  1. Python: https://www.python.org/downloads/" -ForegroundColor White
    Write-Host "  2. Node.js: https://nodejs.org/" -ForegroundColor White
    Write-Host "  3. Vercel CLI: npm install -g vercel" -ForegroundColor White
    Write-Host ""
    Write-Host "Or use Vercel Dev if you have it installed" -ForegroundColor Cyan
}
