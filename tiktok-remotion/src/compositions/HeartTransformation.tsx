import React from "react";
import { z } from "zod";
import {
  AbsoluteFill,
  interpolate,
  spring,
  useCurrentFrame,
  useVideoConfig,
  Easing,
} from "remotion";

export const heartTransformSchema = z.object({
  sadText: z.string(),
  happyText: z.string(),
  ctaText: z.string().default("Try free → seniorease.eu"),
});

type HeartTransformProps = z.infer<typeof heartTransformSchema>;

// Root.tsx registreert deze compositie met durationInFrames={10 * FPS} = 300 frames.
// Timeline: 0–3s droef | 3–5s overgang | 5–10s blij + CTA
export const HeartTransformation: React.FC<HeartTransformProps> = ({
  sadText,
  happyText,
  ctaText,
}) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const sadEnd = 3 * fps;       // frame 90
  const transitionEnd = 5 * fps; // frame 150

  // Achtergrondkleur: donkerblauw (droef) → warme crème (blij)
  const bgProgress = interpolate(frame, [sadEnd, transitionEnd], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: Easing.inOut(Easing.cubic),
  });

  const bgR = Math.round(15 + (245 - 15) * bgProgress);
  const bgG = Math.round(15 + (238 - 15) * bgProgress);
  const bgB = Math.round(35 + (230 - 35) * bgProgress);

  const sadOpacity = interpolate(frame, [sadEnd - fps * 0.3, sadEnd], [1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const happyOpacity = interpolate(
    frame,
    [transitionEnd, transitionEnd + fps * 0.4],
    [0, 1],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );

  const ctaOpacity = interpolate(
    frame,
    [transitionEnd + fps, transitionEnd + fps * 1.5],
    [0, 1],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );

  const ctaY = interpolate(
    frame,
    [transitionEnd + fps, transitionEnd + fps * 1.5],
    [40, 0],
    {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: Easing.out(Easing.cubic),
    }
  );

  const heartScale = spring({
    frame: Math.max(0, frame - transitionEnd),
    fps,
    config: { damping: 10, stiffness: 100 },
  });

  const isDark = bgProgress < 0.5;
  const textColor = isDark ? "white" : "#3D2B1F";

  return (
    <AbsoluteFill
      style={{
        backgroundColor: `rgb(${bgR},${bgG},${bgB})`,
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      {/* Droeve fase */}
      {frame < sadEnd ? (
        <div style={{ opacity: sadOpacity, textAlign: "center", padding: "0 80px" }}>
          <div style={{ fontSize: 120, marginBottom: 40 }}>😟</div>
          <p
            style={{
              fontFamily: "sans-serif",
              fontSize: 60,
              fontWeight: 700,
              color: "white",
              margin: 0,
              lineHeight: 1.3,
            }}
          >
            {sadText}
          </p>
        </div>
      ) : (
        <div style={{ textAlign: "center", padding: "0 80px" }}>
          {/* Hart */}
          <div
            style={{
              fontSize: 160,
              display: "block",
              marginBottom: 50,
              transform: `scale(${frame >= transitionEnd ? heartScale : 1})`,
            }}
          >
            {bgProgress < 0.5 ? "💔" : "❤️"}
          </div>

          {/* Blije tekst */}
          <p
            style={{
              fontFamily: "sans-serif",
              fontSize: 72,
              fontWeight: 800,
              color: textColor,
              margin: 0,
              lineHeight: 1.2,
              opacity: happyOpacity,
            }}
          >
            {happyText}
          </p>

          {/* CTA */}
          <div
            style={{
              marginTop: 60,
              opacity: ctaOpacity,
              transform: `translateY(${ctaY}px)`,
            }}
          >
            <div
              style={{
                backgroundColor: "#8B5E3C",
                borderRadius: 60,
                padding: "28px 64px",
                display: "inline-block",
                marginBottom: 30,
              }}
            >
              <p
                style={{
                  fontFamily: "sans-serif",
                  fontSize: 44,
                  fontWeight: 700,
                  color: "white",
                  margin: 0,
                }}
              >
                SeniorEase Library
              </p>
            </div>
            <p
              style={{
                fontFamily: "sans-serif",
                fontSize: 38,
                fontWeight: 600,
                color: "#8B5E3C",
                margin: 0,
              }}
            >
              {ctaText}
            </p>
          </div>
        </div>
      )}
    </AbsoluteFill>
  );
};
