# Test API with specific email
param(
    [string]$Email = "cmvdeut@gmail.com"
)

Write-Host "Testing API with email: $Email" -ForegroundColor Cyan
Write-Host ""

$url = "https://www.seniorease.eu/api/verify-purchase"
$body = @{
    email = $Email
} | ConvertTo-Json

try {
    Write-Host "Sending request..." -ForegroundColor Yellow
    $response = Invoke-RestMethod -Uri $url -Method POST -ContentType "application/json" -Body $body -ErrorAction Stop
    
    Write-Host "API Response:" -ForegroundColor Green
    $response | ConvertTo-Json | Write-Host
    
    Write-Host ""
    if ($response.paid -eq $true) {
        Write-Host "SUCCESS: Payment found!" -ForegroundColor Green
    } else {
        Write-Host "INFO: No payment found for this email" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Check Stripe Dashboard:" -ForegroundColor Cyan
        Write-Host "  1. Is payment successful?" -ForegroundColor White
        Write-Host "  2. Does email match exactly?" -ForegroundColor White
        Write-Host "  3. Is Price ID correct?" -ForegroundColor White
    }
    
} catch {
    Write-Host "ERROR:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}
