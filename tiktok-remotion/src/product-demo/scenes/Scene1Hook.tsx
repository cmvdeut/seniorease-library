import React from "react";
import {
  AbsoluteFill,
  useCurrentFrame,
  useVideoConfig,
  interpolate,
  spring,
} from "remotion";
import { ACCENT, ACCENT_GLOW, BG, SAFE_SIDE } from "../constants";
import { fontFamily } from "../font";

export const Scene1Hook: React.FC = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const springVal = spring({ frame, fps, config: { damping: 8, stiffness: 180 } });
  const scale = interpolate(springVal, [0, 1], [2, 1]);

  const fadeIn = interpolate(frame, [0, 8], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const fadeOut = interpolate(frame, [72, 90], [1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const glowOpacity = interpolate(springVal, [0, 1], [0, 1]);

  return (
    <AbsoluteFill
      style={{
        background: BG,
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
          transform: "translate(-50%, -50%)",
          width: 1000,
          height: 1000,
          background: `radial-gradient(circle, ${ACCENT_GLOW} 0%, rgba(139,94,60,0.06) 40%, transparent 70%)`,
          opacity: glowOpacity,
          pointerEvents: "none",
        }}
      />

      <div
        style={{
          opacity: Math.min(fadeIn, fadeOut),
          transform: `scale(${scale})`,
          padding: `0 ${SAFE_SIDE + 24}px`,
          textAlign: "center",
          fontFamily,
        }}
      >
        <div
          style={{
            fontSize: 72,
            fontWeight: 800,
            color: "#ffffff",
            lineHeight: 1.2,
          }}
        >
          Already own
          <br />
          this one?
        </div>
        <div
          style={{
            marginTop: 28,
            fontSize: 40,
            fontWeight: 400,
            color: "rgba(255,255,255,0.55)",
            lineHeight: 1.35,
          }}
        >
          At the record fair or book market —
          <br />
          check before you buy.
        </div>
      </div>

      <div
        style={{
          position: "absolute",
          bottom: "30%",
          left: "50%",
          transform: "translateX(-50%)",
          width: interpolate(springVal, [0, 1], [0, 300]),
          height: 2,
          background: `linear-gradient(90deg, transparent, ${ACCENT}, transparent)`,
          opacity: Math.min(fadeIn, fadeOut) * 0.6,
        }}
      />
    </AbsoluteFill>
  );
};
