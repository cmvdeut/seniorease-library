# Maak een TikTok-video (9:16) van de 4 app-screenshots
# Vereiste: ffmpeg (winget install ffmpeg of https://ffmpeg.org/download.html)
# Gebruik: .\MAKE-TIKTOK-VAN-SCREENSHOTS.ps1

$ErrorActionPreference = "Stop"
$scriptDir = $PSScriptRoot
$assetsDir = Join-Path (Split-Path $scriptDir -Parent) "assets"
$outFile = Join-Path $scriptDir "SeniorEase-Library-4screenshots.mp4"

$images = @(
    (Join-Path $assetsDir "app-library.png"),
    (Join-Path $assetsDir "app-scan.png"),
    (Join-Path $assetsDir "app-filter.png"),
    (Join-Path $assetsDir "app-menu.png")
)

if (-not (Get-Command ffmpeg -ErrorAction SilentlyContinue)) {
    Write-Host "ffmpeg niet gevonden. Installeer met: winget install ffmpeg" -ForegroundColor Red
    exit 1
}

foreach ($img in $images) {
    if (-not (Test-Path $img)) {
        Write-Host "Bestand niet gevonden: $img" -ForegroundColor Red
        exit 1
    }
}

# Elke screenshot 4 sec, 1080x1920 (9:16)
$dur = 4
$w = 1080
$h = 1920

Write-Host "Video maken (4 x $dur sec, $w x $h)..." -ForegroundColor Cyan

$args = @(
    "-loop", "1", "-t", $dur, "-i", $images[0],
    "-loop", "1", "-t", $dur, "-i", $images[1],
    "-loop", "1", "-t", $dur, "-i", $images[2],
    "-loop", "1", "-t", $dur, "-i", $images[3],
    "-filter_complex",
    "[0:v]scale=$w`:$h`:force_original_aspect_ratio=decrease,pad=$w`:$h`:(ow-iw)/2:(oh-ih)/2,setsar=1[v0];[1:v]scale=$w`:$h`:force_original_aspect_ratio=decrease,pad=$w`:$h`:(ow-iw)/2:(oh-ih)/2,setsar=1[v1];[2:v]scale=$w`:$h`:force_original_aspect_ratio=decrease,pad=$w`:$h`:(ow-iw)/2:(oh-ih)/2,setsar=1[v2];[3:v]scale=$w`:$h`:force_original_aspect_ratio=decrease,pad=$w`:$h`:(ow-iw)/2:(oh-ih)/2,setsar=1[v3];[v0][v1][v2][v3]concat=n=4:v=1:a=0[outv]",
    "-map", "[outv]", "-r", "30", "-pix_fmt", "yuv420p", "-y", $outFile
)

& ffmpeg @args
if ($LASTEXITCODE -ne 0) { exit 1 }

Write-Host "Klaar: $outFile" -ForegroundColor Green
