import React from "react";
import {
  AbsoluteFill,
  useCurrentFrame,
  useVideoConfig,
  interpolate,
  spring,
  Img,
  staticFile,
} from "remotion";
import { ACCENT, BG, SAFE_SIDE, SAFE_TOP, SAFE_BOTTOM, CONTENT_W, H, SUCCESS } from "../constants";
import { fontFamily } from "../font";

const FEATURES = [
  { icon: "✓", color: SUCCESS, text: "No account needed" },
  { icon: "⚡", color: ACCENT, text: "Works fully offline" },
  { icon: "★", color: "#d4a574", text: "Books, vinyl, DVDs & games" },
];

const IMG_TOP = SAFE_TOP;
const SAFE_H = H - SAFE_TOP - SAFE_BOTTOM;
const IMG_H = Math.round(SAFE_H * 0.38);
const IMG_W = Math.round(IMG_H * 0.55);

export const Scene5Features: React.FC = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const fadeIn = interpolate(frame, [0, 12], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const fadeOut = interpolate(frame, [75, 90], [1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const globalOpacity = Math.min(fadeIn, fadeOut);

  const imgSpring = spring({ frame, fps, config: { damping: 18 }, durationInFrames: 25 });
  const imgScale = interpolate(imgSpring, [0, 1], [0.88, 1]);

  const featureSprings = FEATURES.map((_, i) =>
    spring({
      frame: frame - (20 + i * 10),
      fps,
      config: { damping: 14, stiffness: 140 },
    })
  );

  const FEATURES_TOP = IMG_TOP + IMG_H + 48;

  return (
    <AbsoluteFill style={{ background: BG, opacity: globalOpacity }}>
      <div
        style={{
          position: "absolute",
          top: IMG_TOP,
          left: "50%",
          transform: `translateX(-50%) scale(${imgScale})`,
          transformOrigin: "top center",
          width: IMG_W,
          height: IMG_H,
          borderRadius: 16,
          overflow: "hidden",
          boxShadow: "0 20px 48px rgba(0,0,0,0.55)",
        }}
      >
        <Img
          src={staticFile("screenshots/Screenshot2Library.png")}
          style={{ width: "100%", height: "100%", objectFit: "cover", display: "block" }}
        />
      </div>

      <div
        style={{
          position: "absolute",
          top: FEATURES_TOP,
          left: SAFE_SIDE,
          width: CONTENT_W,
          display: "flex",
          flexDirection: "column",
          gap: 32,
        }}
      >
        {FEATURES.map((f, i) => {
          const sp = featureSprings[i];
          const slideX = interpolate(sp, [0, 1], [160, 0]);
          const fOpacity = interpolate(sp, [0, 0.3], [0, 1]);

          return (
            <div
              key={i}
              style={{
                display: "flex",
                alignItems: "center",
                gap: 24,
                transform: `translateX(${slideX}px)`,
                opacity: fOpacity,
              }}
            >
              <div
                style={{
                  width: 68,
                  height: 68,
                  borderRadius: 16,
                  background: `${f.color}18`,
                  border: `2px solid ${f.color}55`,
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  fontSize: 34,
                  color: f.color,
                  fontWeight: 800,
                  fontFamily,
                  flexShrink: 0,
                }}
              >
                {f.icon}
              </div>

              <div
                style={{
                  fontSize: 38,
                  fontWeight: 700,
                  color: "#ffffff",
                  lineHeight: 1.3,
                  fontFamily,
                }}
              >
                {f.text}
              </div>
            </div>
          );
        })}
      </div>
    </AbsoluteFill>
  );
};
