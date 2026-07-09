import React from "react";
import {
  AbsoluteFill,
  Sequence,
  interpolate,
  spring,
  useCurrentFrame,
  useVideoConfig,
  staticFile,
} from "remotion";
import { z } from "zod";

export const facebookCalmSchema = z.object({
  hookText: z.string(),
  problemText: z.string(),
  actionText: z.string(),
  benefitText: z.string(),
  ctaText: z.string(),
  screenshotPath: z.string().default("shot 2scan.jpeg"),
  accentColor: z.string().default("#8B5E3C"),
});

type FacebookCalmProps = z.infer<typeof facebookCalmSchema>;

const TextCard: React.FC<{
  text: string;
  accentColor: string;
  fontSize?: number;
}> = ({ text, accentColor, fontSize = 62 }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const opacity = interpolate(frame, [0, fps * 0.4], [0, 1], {
    extrapolateRight: "clamp",
  });
  const y = interpolate(frame, [0, fps * 0.5], [32, 0], {
    extrapolateRight: "clamp",
  });
  const scale = spring({
    frame,
    fps,
    config: { damping: 14, stiffness: 85 },
    from: 0.95,
    to: 1,
  });

  return (
    <AbsoluteFill
      style={{
        background: "linear-gradient(135deg, #F5EEE6 0%, #E0D5CA 100%)",
        justifyContent: "center",
        alignItems: "center",
        opacity,
      }}
    >
      <div
        style={{
          transform: `translateY(${y}px) scale(${scale})`,
          textAlign: "center",
          padding: "0 90px",
        }}
      >
        <div
          style={{
            width: 90,
            height: 8,
            backgroundColor: accentColor,
            borderRadius: 8,
            margin: "0 auto 34px",
          }}
        />
        <p
          style={{
            margin: 0,
            fontFamily: "sans-serif",
            fontSize,
            fontWeight: 800,
            lineHeight: 1.2,
            color: "#1f1f1f",
          }}
        >
          {text}
        </p>
      </div>
    </AbsoluteFill>
  );
};

const ScreenshotCard: React.FC<{
  screenshotPath: string;
  text: string;
  accentColor: string;
}> = ({ screenshotPath, text, accentColor }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const imageScale = interpolate(frame, [0, fps * 4], [1, 1.05], {
    extrapolateRight: "clamp",
  });
  const captionOpacity = interpolate(frame, [fps * 0.35, fps * 0.9], [0, 1], {
    extrapolateRight: "clamp",
  });

  return (
    <AbsoluteFill>
      <AbsoluteFill style={{ transform: `scale(${imageScale})` }}>
        <img
          src={staticFile(screenshotPath)}
          style={{ width: "100%", height: "100%", objectFit: "cover", objectPosition: "top center" }}
        />
      </AbsoluteFill>

      <AbsoluteFill
        style={{
          background:
            "linear-gradient(to top, rgba(0,0,0,0.86) 0%, rgba(0,0,0,0.42) 40%, transparent 68%)",
        }}
      />

      <AbsoluteFill style={{ justifyContent: "flex-end", alignItems: "center", paddingBottom: 120 }}>
        <div
          style={{
            backgroundColor: "rgba(0,0,0,0.35)",
            border: `2px solid ${accentColor}`,
            borderRadius: 20,
            padding: "24px 30px",
            margin: "0 44px",
            opacity: captionOpacity,
          }}
        >
          <p
            style={{
              margin: 0,
              fontFamily: "sans-serif",
              fontSize: 56,
              fontWeight: 700,
              color: "#fff",
              textAlign: "center",
              lineHeight: 1.25,
              textShadow: "0 3px 10px rgba(0,0,0,0.65)",
            }}
          >
            {text}
          </p>
        </div>
      </AbsoluteFill>
    </AbsoluteFill>
  );
};

export const FacebookCalmDemo: React.FC<FacebookCalmProps> = ({
  hookText,
  problemText,
  actionText,
  benefitText,
  ctaText,
  screenshotPath,
  accentColor,
}) => {
  // 22 seconden totaal bij 30fps
  // 0-4s hook, 4-8s probleem, 8-14s actie op screenshot, 14-18s benefit, 18-22s CTA
  return (
    <AbsoluteFill>
      <Sequence from={0} durationInFrames={120}>
        <TextCard text={hookText} accentColor={accentColor} />
      </Sequence>

      <Sequence from={120} durationInFrames={120}>
        <TextCard text={problemText} accentColor={accentColor} />
      </Sequence>

      <Sequence from={240} durationInFrames={180}>
        <ScreenshotCard screenshotPath={screenshotPath} text={actionText} accentColor={accentColor} />
      </Sequence>

      <Sequence from={420} durationInFrames={120}>
        <TextCard text={benefitText} accentColor={accentColor} />
      </Sequence>

      <Sequence from={540} durationInFrames={120}>
        <TextCard text={ctaText} accentColor={accentColor} fontSize={58} />
      </Sequence>
    </AbsoluteFill>
  );
};

