# PowerShell script om Cloudflare Tunnel te starten

Write-Host "🌐 Starting Cloudflare Tunnel..." -ForegroundColor Cyan
Write-Host ""

# Wait a moment for vercel dev to be ready
Write-Host "⏳ Waiting 5 seconds for local server..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# Start Cloudflare Tunnel
Write-Host "🚀 Starting tunnel to http://localhost:3000" -ForegroundColor Green
Write-Host ""

C:\cloudflared\cloudflared.exe tunnel --url http://localhost:3000

Write-Host ""
Write-Host "✅ Cloudflare Tunnel started!" -ForegroundColor Green
Write-Host "   Copy the URL above (https://...) to test the API" -ForegroundColor Cyan
