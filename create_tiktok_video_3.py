#!/usr/bin/env python3
"""
Creates a TikTok instructional video for adding books.
Shows the barcode scanning and marking as owned/read workflow.
"""

from moviepy import (
    ImageClip,
    TextClip,
    CompositeVideoClip,
    concatenate_videoclips,
    ColorClip
)

# Video configuration
DURATION_PER_SHOT = 4.0  # Very slow pacing - 4 seconds per shot
VIDEO_WIDTH = 1080
VIDEO_HEIGHT = 1920  # TikTok vertical format
FONT_SIZE = 80  # Large font
FONT = '/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf'
TEXT_COLOR = 'white'
STROKE_COLOR = 'black'
STROKE_WIDTH = 6  # Increased for better visibility

# Screenshot paths
SCREENSHOT_ADD = 'Screenshots/add book.png'
SCREENSHOT_EDIT = 'Screenshots/edit.png'

def create_text_clip(text, duration):
    """Create a text clip with specified styling."""
    txt_clip = TextClip(
        font=FONT,
        text=text,
        font_size=FONT_SIZE,
        color=TEXT_COLOR,
        stroke_color=STROKE_COLOR,
        stroke_width=STROKE_WIDTH,
        method='caption',
        size=(VIDEO_WIDTH - 100, None),
        text_align='center',
        horizontal_align='center'
    )
    txt_clip = txt_clip.with_duration(duration)
    txt_clip = txt_clip.with_position(('center', 100))
    return txt_clip

def create_shot(screenshot_path, text, duration):
    """Create a video shot with screenshot and text overlay."""
    # Load screenshot
    img_clip = ImageClip(screenshot_path)

    # Resize to fit TikTok dimensions while maintaining aspect ratio
    img_clip = img_clip.resized(height=VIDEO_HEIGHT)

    # If image is wider than video width, center crop it
    if img_clip.w > VIDEO_WIDTH:
        x_center = img_clip.w / 2
        img_clip = img_clip.crop(
            x_center=x_center,
            width=VIDEO_WIDTH,
            height=VIDEO_HEIGHT
        )

    img_clip = img_clip.with_duration(duration)

    # Create text overlay
    txt_clip = create_text_clip(text, duration)

    # Create semi-transparent black background for text
    # Make it slightly larger than the text area
    text_bg_height = 200  # Height of the background bar
    text_bg = ColorClip(
        size=(VIDEO_WIDTH, text_bg_height),
        color=(0, 0, 0)  # Black
    )
    text_bg = text_bg.with_duration(duration)
    text_bg = text_bg.with_position(('center', 50))  # Position at top
    text_bg = text_bg.with_opacity(0.7)  # 70% opacity for semi-transparency

    # Composite video, background bar, and text
    video = CompositeVideoClip([img_clip, text_bg, txt_clip], size=(VIDEO_WIDTH, VIDEO_HEIGHT))

    return video

def main():
    """Main function to create the TikTok video."""
    print("Creating TikTok instructional video...")

    # Create each shot
    shots = []

    # Shot 1: Add a book
    print("Creating shot 1: Add a book")
    shot1 = create_shot(SCREENSHOT_ADD, "Add a book", DURATION_PER_SHOT)
    shots.append(shot1)

    # Shot 2: Scan the barcode (same screenshot, different text)
    print("Creating shot 2: Scan the barcode")
    shot2 = create_shot(SCREENSHOT_ADD, "Scan the barcode", DURATION_PER_SHOT)
    shots.append(shot2)

    # Shot 3: Mark it as owned or read
    print("Creating shot 3: Mark it as owned or read")
    shot3 = create_shot(SCREENSHOT_EDIT, "Mark it as owned or read", DURATION_PER_SHOT)
    shots.append(shot3)

    # Shot 4: Done
    print("Creating shot 4: Done")
    shot4 = create_shot(SCREENSHOT_EDIT, "Done.", DURATION_PER_SHOT)
    shots.append(shot4)

    # Concatenate all shots
    print("Concatenating shots...")
    final_video = concatenate_videoclips(shots, method="compose")

    # Write the final video
    output_file = "tiktok_video_3_add_book.mp4"
    print(f"Writing video to {output_file}...")
    final_video.write_videofile(
        output_file,
        fps=30,
        codec='libx264',
        audio=False,  # No music
        preset='medium',
        threads=4
    )

    print(f"Video created successfully: {output_file}")
    print(f"Duration: {final_video.duration} seconds")
    print(f"Dimensions: {VIDEO_WIDTH}x{VIDEO_HEIGHT}")

if __name__ == "__main__":
    main()
