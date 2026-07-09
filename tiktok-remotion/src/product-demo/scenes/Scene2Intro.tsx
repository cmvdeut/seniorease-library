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
import { ACCENT, ACCENT_DIM, BG, CENTER_X, CENTER_Y } from "../constants";
import { fontFamily } from "../font";

const PARTICLES = Array.from({ length: 20 }, (_, i) => ({
  angle: (i / 20) * Math.PI * 2,
  distance: 160 + (i % 4) * 50,
  size: 6 + (i % 5) * 3,
  delay: i * 1.5,
}));

export const Scene2Intro: React.FC = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const logoSpring = spring({ frame, fps, config: { damping: 12, stiffness: 120 } });
  const logoScale = interpolate(logoSpring, [0, 1], [3, 1]);
  const logoOpacity = interpolate(logoSpring, [0, 0.1], [0, 1]);

  const taglineSpring = spring({
    frame: frame - 18,
    fps,
    config: { damping: 15, stiffness: 150 },
  });
  const taglineY = interpolate(taglineSpring, [0, 1], [60, 0]);
  const taglineOpacity = interpolate(frame, [18, 34], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const subOpacity = interpolate(frame, [30, 48], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const fadeOut = interpolate(frame, [72, 90], [1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const LOGO_CY = CENTER_Y - 80;

  return (
    <AbsoluteFill style={{ background: BG }}>
      <div
        style={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          width: 800,
          height: 800,
          background:
            "radial-gradient(circle, rgba(139,94,60,0.12) 0%, transparent 65%)",
          pointerEvents: "none",
        }}
      />

      {PARTICLES.map((p, i) => {
        const pSpring = spring({
          frame: Math.max(0, frame - p.delay - 8),
          fps,
          config: { damping: 18, stiffness: 100 },
          durationInFrames: 35,
        });
        const dist = interpolate(pSpring, [0, 1], [0, p.distance]);
        const opacity = interpolate(pSpring, [0, 0.25, 1], [0, 0.85, 0]);
        const px = CENTER_X + Math.cos(p.angle) * dist;
        const py = LOGO_CY + Math.sin(p.angle) * dist;

        return (
          <div
            key={i}
            style={{
              position: "absolute",
              left: px - p.size / 2,
              top: py - p.size / 2,
              width: p.size,
              height: p.size,
              borderRadius: "50%",
              background: ACCENT,
              opacity: opacity * fadeOut,
            }}
          />
        );
      })}

      <div
        style={{
          position: "absolute",
          left: CENTER_X - 72,
          top: LOGO_CY - 72,
          width: 144,
          height: 144,
          transform: `scale(${logoScale})`,
          opacity: logoOpacity * fadeOut,
          transformOrigin: "center center",
          borderRadius: 32,
          overflow: "hidden",
          boxShadow: "0 12px 40px rgba(0,0,0,0.45)",
        }}
      >
        <Img
          src={staticFile("icon-512.png")}
          style={{ width: 144, height: 144, objectFit: "cover" }}
        />
      </div>

      <div
        style={{
          position: "absolute",
          left: 0,
          right: 0,
          top: LOGO_CY + 100,
          transform: `translateY(${taglineY}px)`,
          opacity: taglineOpacity * fadeOut,
          textAlign: "center",
          fontFamily,
        }}
      >
        <div
          style={{
            fontSize: 64,
            fontWeight: 800,
            color: "#ffffff",
            letterSpacing: -1,
            lineHeight: 1.15,
          }}
        >
          SeniorEase Library
        </div>
      </div>

      <div
        style={{
          position: "absolute",
          left: 60,
          right: 60,
          top: LOGO_CY + 200,
          opacity: subOpacity * fadeOut,
          textAlign: "center",
          fontFamily,
        }}
      >
        <div
          style={{
            fontSize: 40,
            fontWeight: 400,
            color: ACCENT_DIM,
            lineHeight: 1.4,
          }}
        >
          Scan. Know. Never buy twice.
        </div>
      </div>
    </AbsoluteFill>
  );
};
