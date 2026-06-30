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
import { CtaEndScreen } from "../components/CtaEndScreen";

export const featureDemoSchema = z.object({
  hookText: z.string(),
  steps: z.array(
    z.object({
      imagePath: z.string(),
      stepNumber: z.number(),
      instruction: z.string(),
    })
  ),
  secondsPerStep: z.number().default(3),
  backgroundColor: z.string().default("#0f0f23"),
  accentColor: z.string().default("#4A90D9"),
  ctaText: z.string().default("Try free → seniorease.eu"),
});

type FeatureDemoProps = z.infer<typeof featureDemoSchema>;

const HookScreen: React.FC<{
  hookText: string;
  backgroundColor: string;
  accentColor: string;
}> = ({ hookText, backgroundColor, accentColor }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const opacity = interpolate(frame, [0, fps * 0.35], [0, 1], {
    extrapolateRight: "clamp",
  });
  const translateY = interpolate(frame, [0, fps * 0.4], [40, 0], {
    extrapolateRight: "clamp",
  });

  return (
    <AbsoluteFill
      style={{ backgroundColor, justifyContent: "center", alignItems: "center", opacity }}
    >
      <h1
        style={{
          fontFamily: "sans-serif",
          fontSize: 74,
          fontWeight: 800,
          color: "white",
          textAlign: "center",
          padding: "0 80px",
          margin: 0,
          lineHeight: 1.25,
          transform: `translateY(${translateY}px)`,
        }}
      >
        {hookText}
      </h1>
      <div style={{ position: "absolute", bottom: 120, left: 0, right: 0, textAlign: "center" }}>
        <span
          style={{
            fontFamily: "sans-serif",
            fontSize: 36,
            fontWeight: 600,
            color: accentColor,
          }}
        >
          SeniorEase Library
        </span>
      </div>
    </AbsoluteFill>
  );
};

const StepScene: React.FC<{
  imagePath: string;
  stepNumber: number;
  instruction: string;
  accentColor: string;
  framesPerStep: number;
}> = ({ imagePath, stepNumber, instruction, accentColor, framesPerStep }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const scale = interpolate(frame, [0, framesPerStep], [1.0, 1.06], {
    extrapolateRight: "clamp",
  });
  const fadeIn = interpolate(frame, [0, fps * 0.3], [0, 1], {
    extrapolateRight: "clamp",
  });
  const fadeOut = interpolate(
    frame,
    [framesPerStep - fps * 0.25, framesPerStep],
    [1, 0],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );
  const textY = interpolate(frame, [0, fps * 0.5], [30, 0], {
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
            "linear-gradient(to top, rgba(0,0,0,0.92) 0%, rgba(0,0,0,0.45) 40%, transparent 65%)",
        }}
      />

      <AbsoluteFill
        style={{
          justifyContent: "flex-start",
          alignItems: "flex-start",
          padding: "70px 60px 0",
          transform: `translateY(${textY}px)`,
        }}
      >
        <div
          style={{
            backgroundColor: accentColor,
            borderRadius: 50,
            width: 80,
            height: 80,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <span
            style={{ fontFamily: "sans-serif", fontSize: 42, fontWeight: 800, color: "white" }}
          >
            {stepNumber}
          </span>
        </div>
      </AbsoluteFill>

      <AbsoluteFill
        style={{
          justifyContent: "flex-end",
          alignItems: "center",
          paddingBottom: 100,
          transform: `translateY(${textY}px)`,
        }}
      >
        <p
          style={{
            fontFamily: "sans-serif",
            fontSize: 60,
            fontWeight: 800,
            color: "white",
            textAlign: "center",
            margin: 0,
            padding: "0 70px",
            lineHeight: 1.25,
            textShadow: "0 4px 20px rgba(0,0,0,0.7)",
          }}
        >
          {instruction}
        </p>
      </AbsoluteFill>
    </AbsoluteFill>
  );
};

// Root.tsx registreert deze compositie met durationInFrames={16 * FPS} = 480 frames.
// Layout: 1s hook + 4 steps × 3s + 3s CTA = 480 frames @ 30fps.
export const FeatureDemo: React.FC<FeatureDemoProps> = ({
  hookText,
  steps,
  secondsPerStep,
  backgroundColor,
  accentColor,
}) => {
  const { fps } = useVideoConfig();
  const hookFrames = fps; // 1 seconde
  const framesPerStep = secondsPerStep * fps;
  const ctaStart = hookFrames + steps.length * framesPerStep;
  const ctaFrames = 3 * fps;

  return (
    <AbsoluteFill style={{ backgroundColor }}>
      <Sequence from={0} durationInFrames={hookFrames}>
        <HookScreen
          hookText={hookText}
          backgroundColor={backgroundColor}
          accentColor={accentColor}
        />
      </Sequence>

      {steps.map((step, i) => (
        <Sequence
          key={i}
          from={hookFrames + i * framesPerStep}
          durationInFrames={framesPerStep}
        >
          <StepScene
            imagePath={step.imagePath}
            stepNumber={step.stepNumber}
            instruction={step.instruction}
            accentColor={accentColor}
            framesPerStep={framesPerStep}
          />
        </Sequence>
      ))}

      <Sequence from={ctaStart} durationInFrames={ctaFrames}>
        <CtaEndScreen />
      </Sequence>
    </AbsoluteFill>
  );
};
