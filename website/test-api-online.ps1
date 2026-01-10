# Test API Online (Production)
Write-Host "Testing API: https://www.seniorease.eu/api/verify-purchase" -ForegroundColor Cyan
Write-Host ""

$url = "https://www.seniorease.eu/api/verify-purchase"
$body = @{
    email = "test@example.com"
} | ConvertTo-Json

try {
    Write-Host "Sending request..." -ForegroundColor Yellow
    $response = Invoke-RestMethod -Uri $url -Method POST -ContentType "application/json" -Body $body -ErrorAction Stop
    
    Write-Host "API Response:" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
    
    if ($response.paid -eq $false) {
        Write-Host ""
        Write-Host "Response: { paid: false }" -ForegroundColor Cyan
        Write-Host "This is normal - test@example.com has no payment" -ForegroundColor Gray
    } else {
        Write-Host ""
        Write-Host "Response: { paid: true }" -ForegroundColor Green
    }
    
} catch {
    Write-Host "ERROR:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    
    if ($_.Exception.Message -like "*404*") {
        Write-Host ""
        Write-Host "Possible causes:" -ForegroundColor Yellow
        Write-Host "  - API route not deployed yet" -ForegroundColor Gray
        Write-Host "  - Check Vercel deployment status" -ForegroundColor Gray
    }
    
    if ($_.Exception.Message -like "*500*" -or $_.Exception.Message -like "*Internal*") {
        Write-Host ""
        Write-Host "Possible causes:" -ForegroundColor Yellow
        Write-Host "  - STRIPE_SECRET_KEY not added to Vercel" -ForegroundColor Gray
        Write-Host "  - Check Vercel Environment Variables" -ForegroundColor Gray
    }
}

Write-Host ""
Write-Host "Next steps:" -ForegroundColor Cyan
Write-Host "  1. Check Vercel Dashboard -> Settings -> Environment Variables" -ForegroundColor White
Write-Host "  2. Make sure STRIPE_SECRET_KEY is added" -ForegroundColor White
Write-Host "  3. Redeploy if needed" -ForegroundColor White
