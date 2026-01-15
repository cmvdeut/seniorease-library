# Get Line Items from Checkout Session
param(
    [string]$SessionId = "cs_test_a1JxTJovIux5sf47jxAWwH3qOVq130jORXFTf7pfJyI55DbNwXuIGBhcwW"
)

Write-Host "🔍 Fetching Line Items for Checkout Session..." -ForegroundColor Cyan
Write-Host "Session ID: $SessionId" -ForegroundColor White
Write-Host ""

# Check if Stripe CLI is available
if (Get-Command stripe -ErrorAction SilentlyContinue) {
    Write-Host "✅ Stripe CLI gevonden" -ForegroundColor Green
    Write-Host ""
    Write-Host "Fetching line items..." -ForegroundColor Yellow
    
    $result = stripe checkout sessions list_line_items $SessionId --api-key $env:STRIPE_SECRET_KEY 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✅ Line Items:" -ForegroundColor Green
        Write-Host $result
    } else {
        Write-Host "❌ Error fetching line items" -ForegroundColor Red
        Write-Host $result
        Write-Host ""
        Write-Host "💡 Alternatief: Gebruik Stripe Dashboard" -ForegroundColor Yellow
        Write-Host "   1. Ga naar: https://dashboard.stripe.com/test/checkout_sessions" -ForegroundColor White
        Write-Host "   2. Klik op de session" -ForegroundColor White
        Write-Host "   3. Scroll naar 'Line Items' of gebruik de API tab" -ForegroundColor White
    }
} else {
    Write-Host "❌ Stripe CLI niet gevonden" -ForegroundColor Red
    Write-Host ""
    Write-Host "💡 Opties:" -ForegroundColor Yellow
    Write-Host "   1. Installeer Stripe CLI: https://stripe.com/docs/stripe-cli" -ForegroundColor White
    Write-Host "   2. Of check in Stripe Dashboard:" -ForegroundColor White
    Write-Host "      - Ga naar: https://dashboard.stripe.com/test/checkout_sessions" -ForegroundColor White
    Write-Host "      - Klik op session: $SessionId" -ForegroundColor White
    Write-Host "      - Scroll naar 'Line Items' sectie" -ForegroundColor White
    Write-Host "      - Of gebruik de 'API' tab om de raw response te zien" -ForegroundColor White
    Write-Host ""
    Write-Host "   3. Of gebruik de Vercel API logs:" -ForegroundColor White
    Write-Host "      - De API haalt Line Items op en logt de Price IDs" -ForegroundColor White
    Write-Host "      - Check Vercel logs na een API test" -ForegroundColor White
}
