# PowerShell script om Vercel Dev te starten met Stripe key

Write-Host "🚀 Starting Vercel Dev Server..." -ForegroundColor Cyan
Write-Host ""

# Set Stripe key
$env:STRIPE_SECRET_KEY="sk_test_YOUR_KEY_HERE"

Write-Host "✅ Stripe key set" -ForegroundColor Green
Write-Host ""

# Change to website directory
Set-Location "D:\MAUREEN\DEV\SeniorEase-Library\website"

Write-Host "📁 Changed to website directory" -ForegroundColor Green
Write-Host ""

# Start vercel dev
Write-Host "⏳ Starting Vercel dev server..." -ForegroundColor Yellow
Write-Host "   (This may take 30-60 seconds)" -ForegroundColor Yellow
Write-Host ""

vercel dev
