import React from "react";
import { AbsoluteFill, interpolate, useCurrentFrame, useVideoConfig } from "remotion";

/** TikTok UI safe zone (username, buttons, caption bar). */
const SAFE_BOTTOM = 340;
const HORIZONTAL_PAD = 48;

type SubtitlePhase = "hook" | "body" | "cta";

export type SubtitleOverlayProps = {
  hookText: string;
  bodyText: string;
  ctaText: string;
  hookDurationFrames: number;
  stepsDurationFrames: number;
  framesPerStep: number;
  stepInstructions?: string[];
  accentColor?: string;
  /** standard = 48px; large = 62px with stronger bar (test in preview). */
  variant?: "standard" | "large";
  /** When true, only show captions during step screens (avoids duplicate hook/CTA text). */
  stepsOnly?: boolean;
};

function getPhase(
  frame: number,
  hookDurationFrames: number,
  stepsDurationFrames: number
): SubtitlePhase {
  if (frame < hookDurationFrames) return "hook";
  if (frame < hookDurationFrames + stepsDurationFrames) return "body";
  return "cta";
}

/** TikTok-safe burned-in captions (bottom, high contrast). */
export const SubtitleOverlay: React.FC<SubtitleOverlayProps> = ({
  hookText,
  bodyText,
  ctaText,
  hookDurationFrames,
  stepsDurationFrames,
  framesPerStep,
  stepInstructions = [],
  accentColor = "#FFE566",
  variant = "large",
  stepsOnly = true,
}) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  const phase = getPhase(frame, hookDurationFrames, stepsDurationFrames);

  if (stepsOnly && phase !== "body") return null;

  let text: string;
  if (phase === "hook") {
    text = hookText;
  } else if (phase === "body") {
    const bodyFrame = frame - hookDurationFrames;
    const stepIndex = Math.min(
      Math.floor(bodyFrame / framesPerStep),
      Math.max(stepInstructions.length - 1, 0)
    );
    text = stepInstructions[stepIndex] ?? bodyText;
  } else {
    text = ctaText;
  }

  const phaseStart =
    phase === "hook"
      ? 0
      : phase === "body"
        ? hookDurationFrames
        : hookDurationFrames + stepsDurationFrames;

  const localFrame = frame - phaseStart;
  const fadeIn = interpolate(localFrame, [0, fps * 0.12], [0, 1], {
    extrapolateRight: "clamp",
  });

  if (!text.trim()) return null;

  const isLarge = variant === "large";
  const fontSize = isLarge ? 62 : 48;
  const padY = isLarge ? "22px 36px" : "20px 32px";

  return (
    <AbsoluteFill
      style={{
        justifyContent: "flex-end",
        alignItems: "center",
        paddingBottom: SAFE_BOTTOM,
        paddingLeft: HORIZONTAL_PAD,
        paddingRight: HORIZONTAL_PAD,
        pointerEvents: "none",
        opacity: fadeIn,
      }}
    >
      <div
        style={{
          backgroundColor: isLarge ? "rgba(0,0,0,0.78)" : "rgba(0,0,0,0.55)",
          borderRadius: 18,
          padding: padY,
          maxWidth: "100%",
          borderLeft: `6px solid ${accentColor}`,
          boxShadow: isLarge ? "0 8px 32px rgba(0,0,0,0.45)" : undefined,
        }}
      >
        <p
          style={{
            fontFamily: "sans-serif",
            fontSize,
            fontWeight: 800,
            color: "#FFFFFF",
            margin: 0,
            textAlign: "center",
            lineHeight: 1.22,
            letterSpacing: isLarge ? -0.5 : 0,
            textShadow:
              "0 0 4px #000, 0 2px 8px #000, 2px 2px 0 #000, -2px 2px 0 #000, 2px -2px 0 #000, -2px -2px 0 #000",
          }}
        >
          {text}
        </p>
      </div>
    </AbsoluteFill>
  );
};
