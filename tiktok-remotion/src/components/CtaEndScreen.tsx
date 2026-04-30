import React from "react";
import {
  AbsoluteFill,
  interpolate,
  spring,
  useCurrentFrame,
  useVideoConfig,
  staticFile,
} from "remotion";

export const CtaEndScreen: React.FC = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const fadeIn = interpolate(frame, [0, fps * 0.3], [0, 1], {
    extrapolateRight: "clamp",
  });

  const iconScale = spring({
    frame,
    fps,
    config: { damping: 14, stiffness: 130 },
  });

  const buttonOpacity = interpolate(frame, [fps * 0.35, fps * 0.65], [0, 1], {
    extrapolateRight: "clamp",
  });

  const buttonY = interpolate(frame, [fps * 0.35, fps * 0.65], [40, 0], {
    extrapolateRight: "clamp",
  });

  return (
    <AbsoluteFill
      style={{
        backgroundColor: "#0f0f1a",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        gap: 44,
        opacity: fadeIn,
      }}
    >
      <img
        src={staticFile("icon-512.png")}
        style={{
          width: 200,
          height: 200,
          borderRadius: 40,
          boxShadow: "0 16px 64px rgba(0,0,0,0.5)",
          transform: `scale(${iconScale})`,
        }}
      />

      <p
        style={{
          fontFamily: "sans-serif",
          fontSize: 58,
          fontWeight: 800,
          color: "white",
          margin: 0,
          textAlign: "center",
          padding: "0 80px",
          lineHeight: 1.2,
        }}
      >
        SeniorEase Library
      </p>

      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: 24,
          opacity: buttonOpacity,
          transform: `translateY(${buttonY}px)`,
        }}
      >
        <div
          style={{
            backgroundColor: "#3DDC84",
            borderRadius: 64,
            padding: "30px 72px",
            boxShadow: "0 8px 40px rgba(61,220,132,0.4)",
          }}
        >
          <p
            style={{
              fontFamily: "sans-serif",
              fontSize: 52,
              fontWeight: 800,
              color: "#0a1a0f",
              margin: 0,
              textAlign: "center",
              lineHeight: 1,
            }}
          >
            Download free
          </p>
          <p
            style={{
              fontFamily: "sans-serif",
              fontSize: 34,
              fontWeight: 600,
              color: "#0a1a0f",
              margin: "8px 0 0",
              textAlign: "center",
              opacity: 0.7,
            }}
          >
            Google Play
          </p>
        </div>

        <p
          style={{
            fontFamily: "sans-serif",
            fontSize: 40,
            fontWeight: 700,
            color: "#4A90D9",
            margin: 0,
            textAlign: "center",
          }}
        >
          seniorease.eu
        </p>

        <p
          style={{
            fontFamily: "sans-serif",
            fontSize: 30,
            fontWeight: 400,
            color: "#888",
            margin: 0,
            textAlign: "center",
          }}
        >
          Try 10 items free · No account needed
        </p>
      </div>
    </AbsoluteFill>
  );
};
