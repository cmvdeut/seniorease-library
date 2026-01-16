# Genereer QR Code voor APK Download
# Run: .\GENEREER-QR-CODE.ps1

Write-Host "QR Code Generator" -ForegroundColor Cyan
Write-Host ""

$url = "https://www.seniorease.eu/downloads/SeniorEase-Library-1.0.5.apk"
$outputFile = "qr-code-apk-download.png"

Write-Host "URL: $url" -ForegroundColor Yellow
Write-Host ""

Write-Host "Genereer QR code..." -ForegroundColor Cyan
$escapedUrl = [System.Uri]::EscapeDataString($url)
$apiUrl = "https://api.qrserver.com/v1/create-qr-code/?size=400x400&data=$escapedUrl"
Invoke-WebRequest -Uri $apiUrl -OutFile $outputFile

if (Test-Path $outputFile) {
    Write-Host ""
    Write-Host "QR code succesvol gegenereerd: $outputFile" -ForegroundColor Green
    Write-Host "URL: $url" -ForegroundColor Cyan
} else {
    Write-Host "QR code genereren mislukt" -ForegroundColor Red
}
