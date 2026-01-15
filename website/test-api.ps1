# PowerShell Script om API te testen

Write-Host "🧪 Testing API: /api/verify-purchase" -ForegroundColor Cyan
Write-Host ""

# Test 1: Geldig email (geen betaling)
Write-Host "Test 1: Valid email (no payment)" -ForegroundColor Yellow
$body1 = @{
    email = "test@example.com"
} | ConvertTo-Json

try {
    $response1 = Invoke-RestMethod -Uri "https://www.seniorease.eu/api/verify-purchase" `
        -Method POST `
        -ContentType "application/json" `
        -Body $body1
    
    Write-Host "Response: " -NoNewline
    Write-Host ($response1 | ConvertTo-Json) -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
}

# Test 2: Ongeldig email
Write-Host "Test 2: Invalid email" -ForegroundColor Yellow
$body2 = @{
    email = "invalid-email"
} | ConvertTo-Json

try {
    $response2 = Invoke-RestMethod -Uri "https://www.seniorease.eu/api/verify-purchase" `
        -Method POST `
        -ContentType "application/json" `
        -Body $body2
    
    Write-Host "Response: " -NoNewline
    Write-Host ($response2 | ConvertTo-Json) -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Yellow
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
}

Write-Host "✅ Tests completed!" -ForegroundColor Green
Write-Host ""
Write-Host "💡 Tip: Test met een email waar je een Stripe betaling mee hebt gedaan om { paid: true } te krijgen" -ForegroundColor Cyan
