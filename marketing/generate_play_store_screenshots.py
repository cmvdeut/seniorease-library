#!/usr/bin/env python3
"""
Genereert Play Console phone screenshots (1080x1920, 9:16), Engels en Nederlands.

Output:
  marketing/play-console-screenshots/en/  — Engelse headlines, merkregel "SeniorEase Library"
  marketing/play-console-screenshots/nl/  — Nederlandse headlines, merkregel "SeniorEase Bibliotheek"

Voegt gradient, marketing-titel en telefoonframe toe (SeniorEase-kleuren).
"""
from __future__ import annotations

import os
import sys
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont, ImageFilter

# Merkkleuren (match website / app)
C_TOP = (245, 238, 230)      # #F5EEE6
C_BOTTOM = (224, 213, 202)   # #E0D5CA
C_ACCENT = (139, 94, 60)     # #8B5E3C
C_TITLE = (31, 31, 31)       # #1F1F1F
C_SHADOW = (80, 55, 35, 90)

OUT_W, OUT_H = 1080, 1920
SCREEN_CORNER = 48
FRAME_PAD = 14
TITLE_TOP = 72
TITLE_SIZE = 46
TITLE_LINE_GAP = 8
SUB_SIZE = 26
BRAND_BOTTOM = 56

REPO = Path(__file__).resolve().parent.parent


def _gradient(size: tuple[int, int], c1: tuple[int, int, int], c2: tuple[int, int, int]) -> Image.Image:
    w, h = size
    strip = Image.new("RGB", (1, h))
    px = strip.load()
    for y in range(h):
        t = y / max(h - 1, 1)
        r = int(c1[0] * (1 - t) + c2[0] * t)
        g = int(c1[1] * (1 - t) + c2[1] * t)
        b = int(c1[2] * (1 - t) + c2[2] * t)
        px[0, y] = (r, g, b)
    return strip.resize((w, h), Image.Resampling.LANCZOS)


def _font_bold(size_px: int) -> ImageFont.FreeTypeFont | ImageFont.ImageFont:
    for path in (
        os.environ.get("PLAY_STORE_FONT_BOLD", ""),
        r"C:\Windows\Fonts\segoeuib.ttf",
        r"C:\Windows\Fonts\arialbd.ttf",
        "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf",
        "/System/Library/Fonts/Supplemental/Arial Bold.ttf",
    ):
        if path and Path(path).is_file():
            try:
                return ImageFont.truetype(path, size_px)
            except OSError:
                continue
    return ImageFont.load_default()


def _font_regular(size_px: int) -> ImageFont.FreeTypeFont | ImageFont.ImageFont:
    for path in (
        r"C:\Windows\Fonts\segoeui.ttf",
        r"C:\Windows\Fonts\arial.ttf",
        "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
        "/System/Library/Fonts/Supplemental/Arial.ttf",
    ):
        if Path(path).is_file():
            try:
                return ImageFont.truetype(path, size_px)
            except OSError:
                continue
    return ImageFont.load_default()


def _rounded_mask(size: tuple[int, int], radius: int) -> Image.Image:
    m = Image.new("L", size, 0)
    d = ImageDraw.Draw(m)
    d.rounded_rectangle((0, 0, size[0] - 1, size[1] - 1), radius=radius, fill=255)
    return m


def _round_screenshot(img: Image.Image, radius: int) -> Image.Image:
    img = img.convert("RGBA")
    w, h = img.size
    mask = _rounded_mask((w, h), radius)
    out = Image.new("RGBA", (w, h))
    out.paste(img, (0, 0))
    out.putalpha(mask)
    return out


def _fit_inside(img: Image.Image, max_w: int, max_h: int) -> Image.Image:
    iw, ih = img.size
    scale = min(max_w / iw, max_h / ih)
    nw, nh = int(iw * scale), int(ih * scale)
    return img.resize((nw, nh), Image.Resampling.LANCZOS)


def _draw_title(canvas: Image.Image, lines: list[str], y_start: int, font: ImageFont.FreeTypeFont) -> int:
    d = ImageDraw.Draw(canvas)
    y = y_start
    for line in lines:
        bbox = d.textbbox((0, 0), line, font=font)
        tw = bbox[2] - bbox[0]
        x = (OUT_W - tw) // 2
        d.text((x, y), line, font=font, fill=C_TITLE)
        y += (bbox[3] - bbox[1]) + TITLE_LINE_GAP
    return y


def build_slide(
    screenshot_path: Path,
    title_lines: list[str],
    out_path: Path,
    *,
    brand: str = "SeniorEase Library",
) -> None:
    shot = Image.open(screenshot_path).convert("RGBA")
    canvas = _gradient((OUT_W, OUT_H), C_TOP, C_BOTTOM)

    font_title = _font_bold(TITLE_SIZE)
    font_sub = _font_regular(SUB_SIZE)

    title_block_h = len(title_lines) * (TITLE_SIZE + TITLE_LINE_GAP + 12)
    avail_h = OUT_H - TITLE_TOP - title_block_h - BRAND_BOTTOM - 80
    max_screen_w = OUT_W - 2 * 72
    max_screen_h = min(1520, avail_h)

    fitted = _fit_inside(shot, max_screen_w - 2 * FRAME_PAD, max_screen_h - 2 * FRAME_PAD)
    fw, fh = fitted.size

    # Telefoonframe (lichte rand + schaduw)
    frame_w, frame_h = fw + 2 * FRAME_PAD, fh + 2 * FRAME_PAD
    sx = (OUT_W - frame_w) // 2
    sy = TITLE_TOP + title_block_h + 24

    shadow = Image.new("RGBA", (OUT_W, OUT_H), (0, 0, 0, 0))
    s_layer = Image.new("RGBA", (frame_w + 32, frame_h + 48), (0, 0, 0, 0))
    sd = ImageDraw.Draw(s_layer)
    sd.rounded_rectangle(
        (16, 20, frame_w + 16 + 8, frame_h + 20 + 10),
        radius=SCREEN_CORNER + 20,
        fill=C_SHADOW,
    )
    s_blur = s_layer.filter(ImageFilter.GaussianBlur(12))
    shadow.paste(s_blur, (sx - 16, sy - 18), s_blur)

    canvas_rgba = canvas.convert("RGBA")
    canvas_rgba = Image.alpha_composite(canvas_rgba, shadow)
    canvas = canvas_rgba.convert("RGB")

    d = ImageDraw.Draw(canvas)
    d.rounded_rectangle(
        (sx, sy, sx + frame_w - 1, sy + frame_h - 1),
        radius=SCREEN_CORNER + 6,
        outline=C_ACCENT,
        width=5,
    )

    inner = _round_screenshot(fitted, SCREEN_CORNER)
    canvas_rgba = canvas.convert("RGBA")
    paste_x = sx + FRAME_PAD
    paste_y = sy + FRAME_PAD
    canvas_rgba.paste(inner, (paste_x, paste_y), inner)
    canvas = canvas_rgba.convert("RGB")

    _draw_title(canvas, title_lines, TITLE_TOP, font_title)

    bd = ImageDraw.Draw(canvas)
    bb = bd.textbbox((0, 0), brand, font=font_sub)
    bt_w = bb[2] - bb[0]
    bd.text(
        ((OUT_W - bt_w) // 2, OUT_H - BRAND_BOTTOM),
        brand,
        font=font_sub,
        fill=C_ACCENT,
    )

    out_path.parent.mkdir(parents=True, exist_ok=True)
    canvas.save(out_path, "PNG", optimize=True)
    print(f"OK: {out_path}")


def main() -> int:
    # (relatief pad, out-bestandsnaam, EN regels, NL regels)
    slides: list[tuple[str, str, list[str], list[str]]] = [
        (
            "website/assets/app-library.png",
            "01-library.png",
            ["Your library,", "simply organized"],
            ["Jouw boeken,", "overzichtelijk bij elkaar"],
        ),
        (
            "website/assets/app-scan.png",
            "02-scan.png",
            ["Add books fast", "with ISBN scan"],
            ["Snel boeken toevoegen", "met ISBN-scan"],
        ),
        (
            "website/assets/app-filter.png",
            "03-sort-filter.png",
            ["Sort & filter", "in one tap"],
            ["Sorteer en filter", "met één tik"],
        ),
        (
            "website/assets/app-menu.png",
            "04-menu.png",
            ["Calm settings", "plain language"],
            ["Rustige instellingen", "in duidelijke taal"],
        ),
    ]

    extra = REPO / "website/assets/app-preview.png"
    if extra.is_file():
        slides.append(
            (
                str(extra.relative_to(REPO)),
                "05-preview.png",
                ["See your collection", "at a glance"],
                ["Bekijk je collectie", "in één oogopslag"],
            )
        )

    out_root = REPO / "marketing" / "play-console-screenshots"

    for rel, fname, lines_en, lines_nl in slides:
        src = REPO / rel
        if not src.is_file():
            print(f"Skip (missing): {src}", file=sys.stderr)
            continue
        build_slide(src, lines_en, out_root / "en" / fname, brand="SeniorEase Library")
        build_slide(src, lines_nl, out_root / "nl" / fname, brand="SeniorEase Bibliotheek")

    print(f"\nDone. English: {out_root / 'en'}")
    print(f"         Dutch:  {out_root / 'nl'}")
    print("Play Console: Store listing -> Phone screenshots (1080x1920).")
    print("  Use en/ for default/English listing, nl/ for Dutch custom store listing.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
