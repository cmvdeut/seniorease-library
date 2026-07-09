import React from "react";
import {
  AbsoluteFill,
  useCurrentFrame,
  useVideoConfig,
  interpolate,
  spring,
} from "remotion";
import {
  ACCENT,
  BG,
  SAFE_SIDE,
  SAFE_TOP,
  CONTENT_W,
  CENTER_X,
  SUCCESS,
} from "../constants";
import { fontFamily } from "../font";

const LABEL_Y = SAFE_TOP + 40;
const BTN_TOP = 720;
const BTN_H = 72;
const VIEWFINDER_TOP = 860;
const VIEWFINDER_H = 320;
const RESULT_TOP = 1240;

const BTN_CX = CENTER_X;
const BTN_CY = BTN_TOP + BTN_H / 2;
const VIEWFINDER_CY = VIEWFINDER_TOP + VIEWFINDER_H / 2;

const CURSOR_R = 12;

export const Scene3Demo: React.FC = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const fadeIn = interpolate(frame, [0, 12], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const fadeOut = interpolate(frame, [232, 240], [1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const globalOpacity = Math.min(fadeIn, fadeOut);

  const toBtn = spring({
    frame: frame - 12,
    fps,
    config: { damping: 15, stiffness: 100 },
    durationInFrames: 20,
  });
  const toViewfinder = spring({
    frame: frame - 48,
    fps,
    config: { damping: 15, stiffness: 100 },
    durationInFrames: 20,
  });

  const CURSOR_INIT_X = 200;
  const CURSOR_INIT_Y = 1750;

  let cursorX: number;
  let cursorY: number;
  if (frame < 12) {
    cursorX = CURSOR_INIT_X;
    cursorY = CURSOR_INIT_Y;
  } else if (frame < 48) {
    cursorX = interpolate(toBtn, [0, 1], [CURSOR_INIT_X, BTN_CX]);
    cursorY = interpolate(toBtn, [0, 1], [CURSOR_INIT_Y, BTN_CY]);
  } else {
    cursorX = interpolate(toViewfinder, [0, 1], [BTN_CX, CENTER_X]);
    cursorY = interpolate(toViewfinder, [0, 1], [BTN_CY, VIEWFINDER_CY]);
  }

  const btnScale = interpolate(frame, [32, 37, 42], [1, 0.95, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const rip1 = interpolate(frame, [32, 50], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const rip1Scale = rip1 * 3.5;
  const rip1Opacity = interpolate(rip1, [0, 0.25, 1], [0.7, 0.4, 0]);

  const showViewfinder = frame >= 44;
  const viewfinderOpacity = interpolate(frame, [44, 58], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const scanProgress = interpolate(frame, [58, 130], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const scanLineY = VIEWFINDER_TOP + 40 + scanProgress * (VIEWFINDER_H - 80);

  const showLoading = frame >= 130 && frame < 152;
  const spinAngle = interpolate(frame, [130, 152], [0, 360], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  const showResult = frame >= 152;
  const resultSpring = spring({
    frame: frame - 152,
    fps,
    config: { damping: 12, stiffness: 140 },
  });
  const resultScale = interpolate(resultSpring, [0, 1], [0.85, 1]);
  const resultY = interpolate(resultSpring, [0, 1], [50, 0]);
  const resultOpacity = interpolate(resultSpring, [0, 1], [0, 1]);

  const barcodeLines = Array.from({ length: 18 }, (_, i) => {
    const w = 4 + (i % 5) * 3;
    return { x: 120 + i * 42, w };
  });

  return (
    <AbsoluteFill style={{ background: BG, opacity: globalOpacity }}>
      <div
        style={{
          position: "absolute",
          top: LABEL_Y,
          left: 0,
          right: 0,
          textAlign: "center",
          fontSize: 30,
          fontWeight: 700,
          letterSpacing: 5,
          color: "rgba(139,94,60,0.55)",
          fontFamily,
          opacity: interpolate(frame, [6, 18], [0, 1], { extrapolateRight: "clamp" }),
        }}
      >
        HOW IT WORKS
      </div>

      <div
        style={{
          position: "absolute",
          top: BTN_TOP,
          left: SAFE_SIDE,
          width: CONTENT_W,
          height: BTN_H,
          background: ACCENT,
          borderRadius: 16,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          transform: `scale(${btnScale})`,
          overflow: "hidden",
          opacity: interpolate(frame, [8, 20], [0, 1], { extrapolateRight: "clamp" }),
        }}
      >
        {showLoading ? (
          <div
            style={{
              width: 38,
              height: 38,
              border: "3px solid rgba(26,26,46,0.25)",
              borderTop: "3px solid #1a1a2e",
              borderRadius: "50%",
              transform: `rotate(${spinAngle}deg)`,
            }}
          />
        ) : (
          <span style={{ fontSize: 38, fontWeight: 700, color: "#fff", fontFamily }}>
            Scan barcode
          </span>
        )}

        {rip1Opacity > 0.01 && (
          <div
            style={{
              position: "absolute",
              left: "50%",
              top: "50%",
              width: 80,
              height: 80,
              borderRadius: "50%",
              background: `rgba(255,255,255,${rip1Opacity})`,
              transform: `translate(-50%,-50%) scale(${rip1Scale})`,
              pointerEvents: "none",
            }}
          />
        )}
      </div>

      {showViewfinder && (
        <div
          style={{
            position: "absolute",
            top: VIEWFINDER_TOP,
            left: SAFE_SIDE,
            width: CONTENT_W,
            height: VIEWFINDER_H,
            background: "rgba(255,255,255,0.05)",
            border: `2px solid rgba(139,94,60,0.35)`,
            borderRadius: 20,
            opacity: viewfinderOpacity,
            overflow: "hidden",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            gap: 28,
          }}
        >
          <div
            style={{
              display: "flex",
              alignItems: "flex-end",
              gap: 6,
              height: 100,
              padding: "0 40px",
            }}
          >
            {barcodeLines.map((line, i) => (
              <div
                key={i}
                style={{
                  width: line.w,
                  height: 40 + (i % 4) * 14,
                  background: scanProgress > i / 18 ? "#fff" : "rgba(255,255,255,0.2)",
                  borderRadius: 2,
                  transition: "none",
                }}
              />
            ))}
          </div>

          <div
            style={{
              position: "absolute",
              left: 40,
              right: 40,
              top: scanLineY,
              height: 3,
              background: ACCENT,
              boxShadow: `0 0 12px ${ACCENT}`,
              opacity: frame < 130 ? 0.9 : 0,
            }}
          />

          <div
            style={{
              fontSize: 36,
              fontWeight: 600,
              color: "rgba(255,255,255,0.65)",
              fontFamily,
            }}
          >
            {frame < 130 ? "Scanning ISBN…" : ""}
          </div>
        </div>
      )}

      {showResult && (
        <div
          style={{
            position: "absolute",
            top: RESULT_TOP + resultY,
            left: SAFE_SIDE,
            width: CONTENT_W,
            background: "rgba(90,158,111,0.12)",
            border: `2px solid ${SUCCESS}`,
            borderRadius: 20,
            padding: "40px 36px",
            boxSizing: "border-box",
            transform: `scale(${resultScale})`,
            transformOrigin: "top center",
            opacity: resultOpacity,
            fontFamily,
          }}
        >
          <div style={{ fontSize: 44, fontWeight: 800, color: SUCCESS, marginBottom: 16 }}>
            ✓ Already in your collection
          </div>
          <div style={{ fontSize: 40, fontWeight: 700, color: "#fff", lineHeight: 1.25 }}>
            Abbey Road
          </div>
          <div style={{ fontSize: 32, color: "rgba(255,255,255,0.5)", marginTop: 8 }}>
            The Beatles · Vinyl
          </div>
        </div>
      )}

      <div
        style={{
          position: "absolute",
          left: cursorX - CURSOR_R,
          top: cursorY - CURSOR_R,
          width: CURSOR_R * 2,
          height: CURSOR_R * 2,
          borderRadius: "50%",
          background: "rgba(255,255,255,0.92)",
          boxShadow:
            "0 0 0 4px rgba(255,255,255,0.18), 0 0 16px rgba(255,255,255,0.25)",
          pointerEvents: "none",
          zIndex: 50,
          opacity: interpolate(frame, [8, 18], [0, 1], { extrapolateRight: "clamp" }),
        }}
      />
    </AbsoluteFill>
  );
};
