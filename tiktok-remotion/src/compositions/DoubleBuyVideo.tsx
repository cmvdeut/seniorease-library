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
import { CtaEndScreen } from "../components/CtaEndScreen";

const FadeText: React.FC<{
  children: React.ReactNode;
  fontSize: number;
}> = ({ children, fontSize }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const opacity = interpolate(frame, [0, fps * 0.3], [0, 1], {
    extrapolateRight: "clamp",
  });
  const translateY = interpolate(frame, [0, fps * 0.35], [30, 0], {
    extrapolateRight: "clamp",
  });

  return (
    <AbsoluteFill style={{ justifyContent: "center", alignItems: "center" }}>
      <p
        style={{
          fontFamily: "sans-serif",
          fontSize,
          fontWeight: 800,
          color: "#1a1a1a",
          textAlign: "center",
          padding: "0 100px",
          margin: 0,
          lineHeight: 1.25,
          opacity,
          transform: `translateY(${translateY}px)`,
        }}
      >
        {children}
      </p>
    </AbsoluteFill>
  );
};

const ScreenshotScene: React.FC<{
  imagePath: string;
  caption: string;
}> = ({ imagePath, caption }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const scale = interpolate(frame, [0, fps * 3], [1.0, 1.07], {
    extrapolateRight: "clamp",
  });
  const fadeIn = interpolate(frame, [0, fps * 0.3], [0, 1], {
    extrapolateRight: "clamp",
  });
  const textOpacity = interpolate(frame, [fps * 0.45, fps * 0.85], [0, 1], {
    extrapolateRight: "clamp",
  });
  const textY = interpolate(frame, [fps * 0.45, fps * 0.85], [40, 0], {
    extrapolateRight: "clamp",
  });

  return (
    <AbsoluteFill style={{ opacity: fadeIn }}>
      {/* Fullscreen screenshot met Ken-Burns */}
      <AbsoluteFill style={{ transform: `scale(${scale})`, transformOrigin: "center center" }}>
        <img
          src={staticFile(imagePath)}
          style={{ width: "100%", height: "100%", objectFit: "cover" }}
        />
      </AbsoluteFill>

      {/* Gradient onderaan */}
      <AbsoluteFill
        style={{
          background:
            "linear-gradient(to top, rgba(0,0,0,0.88) 0%, rgba(0,0,0,0.4) 42%, transparent 70%)",
        }}
      />

      {/* Tekst overlay */}
      <AbsoluteFill
        style={{
          justifyContent: "flex-end",
          alignItems: "center",
          paddingBottom: 130,
          opacity: textOpacity,
          transform: `translateY(${textY}px)`,
        }}
      >
        <p
          style={{
            fontFamily: "sans-serif",
            fontSize: 64,
            fontWeight: 800,
            color: "white",
            textAlign: "center",
            margin: 0,
            padding: "0 80px",
            lineHeight: 1.25,
            textShadow: "0 4px 20px rgba(0,0,0,0.7)",
          }}
        >
          {caption}
        </p>
      </AbsoluteFill>
    </AbsoluteFill>
  );
};

// 16 seconden @ 30fps = 480 frames
export const DoubleBuyVideo: React.FC = () => {
  return (
    <AbsoluteFill style={{ backgroundColor: "#F7F5F2" }}>
      {/* 0–2s: hook */}
      <Sequence from={0} durationInFrames={60}>
        <FadeText fontSize={82}>
          Stop buying the same book twice.
        </FadeText>
      </Sequence>

      {/* 2–4.5s: relatable */}
      <Sequence from={60} durationInFrames={75}>
        <FadeText fontSize={68}>
          I did this more than once. 😅
        </FadeText>
      </Sequence>

      {/* 4.5–7.5s: bibliotheek screenshot */}
      <Sequence from={135} durationInFrames={90}>
        <ScreenshotScene
          imagePath="shot3library.png"
          caption="Your entire collection"
        />
      </Sequence>

      {/* 7.5–10.5s: scan screenshot */}
      <Sequence from={225} durationInFrames={90}>
        <ScreenshotScene
          imagePath="shot 2scan.jpeg"
          caption="Scan any book in seconds"
        />
      </Sequence>

      {/* 10.5–13.5s: boekdetail screenshot */}
      <Sequence from={315} durationInFrames={90}>
        <ScreenshotScene
          imagePath="boekdetail.png"
          caption="See what you already own"
        />
      </Sequence>

      {/* 13.5–17s: CTA eindscherm */}
      <Sequence from={405} durationInFrames={90}>
        <CtaEndScreen />
      </Sequence>
    </AbsoluteFill>
  );
};
