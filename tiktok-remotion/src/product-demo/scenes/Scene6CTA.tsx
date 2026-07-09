import React from "react";
import {
  AbsoluteFill,
  useCurrentFrame,
  useVideoConfig,
  interpolate,
  spring,
} from "remotion";
import { ACCENT, BG, SAFE_BOTTOM, SAFE_SIDE, CONTENT_W, H } from "../constants";
import { fontFamily } from "../font";

export const Scene6CTA: React.FC = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const fadeIn = interpolate(frame, [0, 18], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const ratingSpring = spring({
    frame: frame - 8,
    fps,
    config: { damping: 15 },
  });
  const ratingCount = interpolate(ratingSpring, [0, 1], [0, 4.8], {
    extrapolateRight: "clamp",
  });
  const ratingOpacity = interpolate(ratingSpring, [0, 0.2], [0, 1]);

  const urlSpring = spring({
    frame: frame - 22,
    fps,
    config: { damping: 12, stiffness: 120 },
  });
  const urlScale = interpolate(urlSpring, [0, 1], [0.8, 1]);
  const urlOpacity = interpolate(urlSpring, [0, 0.2], [0, 1]);
  const urlPulse = 1 + 0.03 * Math.sin((frame / fps) * Math.PI * 2 * 0.4);

  const subOpacity = interpolate(frame, [38, 52], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const priceOpacity = interpolate(frame, [50, 66], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const dividerW = interpolate(
    spring({ frame: frame - 15, fps, config: { damping: 18 } }),
    [0, 1],
    [0, 400]
  );

  const URL_CY = H - SAFE_BOTTOM - 240;
  const CENTER_X = 540;

  return (
    <AbsoluteFill
      style={{
        background: BG,
        opacity: fadeIn,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <div
        style={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%,-50%)",
          width: 900,
          height: 900,
          background:
            "radial-gradient(circle, rgba(139,94,60,0.10) 0%, transparent 65%)",
          pointerEvents: "none",
        }}
      />

      <div
        style={{
          position: "absolute",
          top: "32%",
          left: 0,
          right: 0,
          textAlign: "center",
          fontFamily,
          opacity: ratingOpacity,
        }}
      >
        <div style={{ fontSize: 72, fontWeight: 800, color: "#f59e0b" }}>
          ★ {ratingCount.toFixed(1)}
        </div>
        <div
          style={{
            fontSize: 36,
            fontWeight: 400,
            color: "rgba(255,255,255,0.5)",
            marginTop: 8,
          }}
        >
          12 ratings on Google Play
        </div>
      </div>

      <div
        style={{
          position: "absolute",
          top: "47%",
          left: CENTER_X - dividerW / 2,
          width: dividerW,
          height: 2,
          background: `linear-gradient(90deg, transparent, ${ACCENT}, transparent)`,
        }}
      />

      <div
        style={{
          position: "absolute",
          top: URL_CY - 60,
          left: SAFE_SIDE,
          width: CONTENT_W,
          textAlign: "center",
          transform: `scale(${urlScale * urlPulse})`,
          opacity: urlOpacity,
          transformOrigin: "center center",
          fontFamily,
        }}
      >
        <div
          style={{
            fontSize: 68,
            fontWeight: 800,
            color: ACCENT,
            letterSpacing: -1,
          }}
        >
          seniorease.eu
        </div>
      </div>

      <div
        style={{
          position: "absolute",
          top: URL_CY + 56,
          left: 0,
          right: 0,
          textAlign: "center",
          fontSize: 34,
          fontWeight: 400,
          color: "rgba(255,255,255,0.45)",
          fontFamily,
          opacity: subOpacity,
        }}
      >
        Free to try · €4.99 once · No subscription
      </div>

      <div
        style={{
          position: "absolute",
          top: URL_CY + 130,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "center",
          opacity: priceOpacity,
        }}
      >
        <div
          style={{
            background: "rgba(139,94,60,0.1)",
            border: "1.5px solid rgba(139,94,60,0.35)",
            borderRadius: 40,
            padding: "14px 40px",
            fontSize: 32,
            fontWeight: 700,
            color: ACCENT,
            fontFamily,
          }}
        >
          Scan. Know. Never buy twice.
        </div>
      </div>
    </AbsoluteFill>
  );
};
