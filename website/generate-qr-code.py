#!/usr/bin/env python3
"""
QR Code Generator voor SeniorEase Library APK Download
Genereert een QR code PNG bestand met de download URL
"""
import sys
import os

# Check if qrcode library is available
try:
    import qrcode
    from PIL import Image
except ImportError:
    print("❌ QR code library niet gevonden!")
    print("")
    print("Installeer met:")
    print("  pip install qrcode[pil]")
    print("")
    sys.exit(1)

# Download URL
url = "https://www.seniorease.eu/downloads/SeniorEase-Library-1.0.5.apk"

# Output file
output_file = os.path.join(os.path.dirname(__file__), "qr-code-apk-download.png")

print("🔲 QR Code Generator")
print("")
print(f"URL: {url}")
print("")

# Maak QR code
qr = qrcode.QRCode(
    version=1,
    error_correction=qrcode.constants.ERROR_CORRECT_H,
    box_size=10,
    border=4,
)
qr.add_data(url)
qr.make(fit=True)

# Maak afbeelding
img = qr.make_image(fill_color="black", back_color="white")

# Sla op
img.save(output_file)
print(f"✅ QR code gegenereerd: {output_file}")
print("")
print("📱 QR code bevat URL:")
print(f"   {url}")
