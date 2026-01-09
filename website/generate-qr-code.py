#!/usr/bin/env python3
"""
QR Code Generator voor SeniorEase Library APK Download
"""
import qrcode
from qrcode.image.pil import PilImage

# Download URL
url = "https://seniorease.eu/downloads/app-demo-release.apk"

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
output_file = "qr-code-apk-download.png"
img.save(output_file)
print("QR code gegenereerd: " + output_file)
print("URL: " + url)
