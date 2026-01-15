# Genereer QR Code voor APK Download
# Run: .\GENEREER-QR-CODE.ps1

Write-Host "🔲 QR Code Generator" -ForegroundColor Cyan
Write-Host ""

$url = "https://www.seniorease.eu/downloads/app-demo-release.apk"
$outputFile = "qr-code-apk-download.png"

Write-Host "URL: $url" -ForegroundColor Yellow
Write-Host ""

# Try Node.js first
if (Get-Command node -ErrorAction SilentlyContinue) {
    Write-Host "✅ Node.js gevonden" -ForegroundColor Green
    Write-Host ""
    
    # Check if qrcode is installed
    $qrcodeInstalled = $false
    if (Test-Path "node_modules\qrcode") {
        $qrcodeInstalled = $true
    } elseif (Test-Path "package.json") {
        $packageJson = Get-Content "package.json" | ConvertFrom-Json
        if ($packageJson.dependencies.qrcode -or $packageJson.devDependencies.qrcode) {
            $qrcodeInstalled = $true
        }
    }
    
    if (-not $qrcodeInstalled) {
        Write-Host "📦 Installeer qrcode package..." -ForegroundColor Yellow
        npm install qrcode --save-dev 2>&1 | Out-Null
    }
    
    Write-Host "🔲 Genereer QR code..." -ForegroundColor Cyan
    
    # Create temporary script
    $tempScript = @"
const QRCode = require('qrcode');
const fs = require('fs');
const path = require('path');

const url = '$url';
const outputFile = path.join(__dirname, '$outputFile');

QRCode.toFile(outputFile, url, {
    errorCorrectionLevel: 'H',
    type: 'png',
    width: 400,
    margin: 4,
    color: {
        dark: '#000000',
        light: '#FFFFFF'
    }
}, function (err) {
    if (err) {
        console.error('❌ Fout:', err.message);
        process.exit(1);
    }
    console.log('✅ QR code gegenereerd: $outputFile');
    console.log('');
    console.log('📱 URL: $url');
});
"@
    
    $tempScript | Out-File -FilePath "temp-qr-gen.js" -Encoding UTF8
    
    node temp-qr-gen.js
    
    if (Test-Path "temp-qr-gen.js") {
        Remove-Item "temp-qr-gen.js"
    }
    
    if (Test-Path $outputFile) {
        Write-Host ""
        Write-Host "✅ QR code succesvol gegenereerd!" -ForegroundColor Green
        exit 0
    }
}

# Fallback: Use browser method
Write-Host ""
Write-Host "⚠️  Node.js methode niet beschikbaar" -ForegroundColor Yellow
Write-Host ""
Write-Host "Gebruik browser methode:" -ForegroundColor Cyan
Write-Host "  1. Open: http://localhost:8001/generate-qr.html" -ForegroundColor White
Write-Host "  2. QR code wordt automatisch gegenereerd" -ForegroundColor White
Write-Host "  3. Klik: '💾 Download QR Code'" -ForegroundColor White
Write-Host "  4. Sla op als: qr-code-apk-download.png" -ForegroundColor White
