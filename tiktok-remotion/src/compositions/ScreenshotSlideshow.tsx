import React from "react";
import { z } from "zod";
import {
  AbsoluteFill,
  Sequence,
  interpolate,
  useCurrentFrame,
  useVideoConfig,
  staticFile,
  Easing,
} from "remotion";

export const slideshowSchema = z.object({
  slides: z.array(
    z.object({
      imagePath: z.string(),
      title: z.string(),
      subtitle: z.string(),
    })
  ),
  secondsPerSlide: z.number().default(3),
  backgroundColor: z.string().default("#1a1a2e"),
  accentColor: z.string().default("#4A90D9"),
});

type SlideshowProps = z.infer<typeof slideshowSchema>;

const Slide: React.FC<{
  imagePath: string;
  title: string;
  subtitle: string;
  accentColor: string;
  framesPerSlide: number;
}> = ({ imagePath, title, subtitle, accentColor, framesPerSlide }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const scale = interpolate(frame, [0, framesPerSlide], [1.0, 1.07], {
    extrapolateRight: "clamp",
  });
  const fadeIn = interpolate(frame, [0, fps * 0.3], [0, 1], {
    extrapolateRight: "clamp",
  });
  const fadeOut = interpolate(
    frame,
    [framesPerSlide - fps * 0.25, framesPerSlide],
    [1, 0],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );
  const textY = interpolate(frame, [0, fps * 0.5], [40, 0], {
    easing: Easing.out(Easing.cubic),
    extrapolateRight: "clamp",
  });

  return (
    <AbsoluteFill style={{ opacity: fadeIn * fadeOut }}>
      <AbsoluteFill
        style={{ transform: `scale(${scale})`, transformOrigin: "center center" }}
      >
        <img
          src={staticFile(imagePath)}
          style={{ width: "100%", height: "100%", objectFit: "cover" }}
        />
      </AbsoluteFill>

      <AbsoluteFill
        style={{
          background:
            "linear-gradient(to top, rgba(0,0,0,0.88) 0%, rgba(0,0,0,0.4) 42%, transparent 70%)",
        }}
      />

      <AbsoluteFill
        style={{
          justifyContent: "flex-end",
          alignItems: "center",
          paddingBottom: 130,
          transform: `translateY(${textY}px)`,
        }}
      >
        <div style={{ textAlign: "center", padding: "0 70px" }}>
          {title ? (
            <h1
              style={{
                fontFamily: "sans-serif",
                fontSize: 66,
                fontWeight: 800,
                color: "white",
                margin: 0,
                lineHeight: 1.25,
                textShadow: "0 4px 20px rgba(0,0,0,0.7)",
              }}
            >
              {title}
            </h1>
          ) : null}
          {subtitle ? (
            <p
              style={{
                fontFamily: "sans-serif",
                fontSize: 44,
                fontWeight: 600,
                color: accentColor,
                margin: "22px 0 0",
                lineHeight: 1.3,
                textShadow: "0 2px 10px rgba(0,0,0,0.6)",
              }}
            >
              {subtitle}
            </p>
          ) : null}
        </div>
      </AbsoluteFill>
    </AbsoluteFill>
  );
};

export const ScreenshotSlideshow: React.FC<SlideshowProps> = ({
  slides,
  secondsPerSlide,
  backgroundColor,
  accentColor,
}) => {
  const { fps } = useVideoConfig();
  const framesPerSlide = secondsPerSlide * fps;

  return (
    <AbsoluteFill style={{ backgroundColor }}>
      {slides.map((slide, i) => (
        <Sequence
          key={i}
          from={i * framesPerSlide}
          durationInFrames={framesPerSlide}
        >
          <Slide
            imagePath={slide.imagePath}
            title={slide.title}
            subtitle={slide.subtitle}
            accentColor={accentColor}
            framesPerSlide={framesPerSlide}
          />
        </Sequence>
      ))}
    </AbsoluteFill>
  );
};
