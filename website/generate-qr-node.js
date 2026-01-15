// QR Code Generator voor SeniorEase Library APK Download
// Run: node generate-qr-node.js

const fs = require('fs');
const path = require('path');

// Check if qrcode is installed
let QRCode;
try {
    QRCode = require('qrcode');
} catch (e) {
    console.log('❌ QR code library niet gevonden!');
    console.log('');
    console.log('Installeer met:');
    console.log('  npm install qrcode');
    console.log('');
    process.exit(1);
}

// Download URL
const url = 'https://www.seniorease.eu/downloads/app-demo-release.apk';

// Output file
const outputFile = path.join(__dirname, 'qr-code-apk-download.png');

console.log('🔲 QR Code Generator');
console.log('');
console.log(`URL: ${url}`);
console.log('');

// Generate QR code
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
        console.error('❌ Fout bij genereren QR code:', err);
        process.exit(1);
    }
    
    console.log(`✅ QR code gegenereerd: ${outputFile}`);
    console.log('');
    console.log('📱 QR code bevat URL:');
    console.log(`   ${url}`);
});
