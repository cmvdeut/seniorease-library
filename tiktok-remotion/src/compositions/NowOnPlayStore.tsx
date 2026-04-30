import React from "react";
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

const Scene: React.FC<{
  imagePath: string;
  title: string;
  subtitle?: string;
  accentColor?: string;
  totalFrames: number;
}> = ({ imagePath, title, subtitle, accentColor = "#4A90D9", totalFrames }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const scale = interpolate(frame, [0, totalFrames], [1.0, 1.07], {
    extrapolateRight: "clamp",
  });
  const fadeIn = interpolate(frame, [0, fps * 0.3], [0, 1], {
    extrapolateRight: "clamp",
  });
  const fadeOut = interpolate(
    frame,
    [totalFrames - fps * 0.3, totalFrames],
    [1, 0],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp" }
  );
  const textY = interpolate(frame, [0, fps * 0.5], [50, 0], {
    easing: Easing.out(Easing.cubic),
    extrapolateRight: "clamp",
  });

  return (
    <AbsoluteFill style={{ opacity: fadeIn * fadeOut }}>
      {/* Achtergrond screenshot */}
      <AbsoluteFill style={{ transform: `scale(${scale})`, transformOrigin: "center center" }}>
        <img
          src={staticFile(imagePath)}
          style={{ width: "100%", height: "100%", objectFit: "cover" }}
        />
      </AbsoluteFill>

      {/* Donkere gradient onderaan */}
      <AbsoluteFill
        style={{
          background:
            "linear-gradient(to top, rgba(0,0,0,0.90) 0%, rgba(0,0,0,0.45) 45%, transparent 75%)",
        }}
      />

      {/* Tekst overlay onderaan */}
      <AbsoluteFill
        style={{
          justifyContent: "flex-end",
          alignItems: "center",
          paddingBottom: 130,
          transform: `translateY(${textY}px)`,
        }}
      >
        <div style={{ textAlign: "center", padding: "0 70px" }}>
          <h1
            style={{
              fontFamily: "sans-serif",
              fontSize: 66,
              fontWeight: 800,
              color: "white",
              margin: 0,
              lineHeight: 1.25,
              textShadow: "0 4px 20px rgba(0,0,0,0.7)",
            }}
          >
            {title}
          </h1>
          {subtitle && (
            <p
              style={{
                fontFamily: "sans-serif",
                fontSize: 44,
                fontWeight: 600,
                color: accentColor,
                margin: "22px 0 0",
                lineHeight: 1.3,
                textShadow: "0 2px 10px rgba(0,0,0,0.6)",
              }}
            >
              {subtitle}
            </p>
          )}
        </div>
      </AbsoluteFill>

      {/* Badge rechtsboven */}
      <AbsoluteFill
        style={{
          justifyContent: "flex-start",
          alignItems: "flex-end",
          flexDirection: "column",
          padding: "60px 60px 0 0",
        }}
      >
        <div
          style={{
            backgroundColor: accentColor,
            borderRadius: 20,
            padding: "12px 28px",
            opacity: 0.92,
          }}
        >
          <span
            style={{ fontFamily: "sans-serif", fontSize: 30, fontWeight: 700, color: "white" }}
          >
            SeniorEase
          </span>
        </div>
      </AbsoluteFill>
    </AbsoluteFill>
  );
};

// 15 seconden @ 30fps = 450 frames
export const NowOnPlayStore: React.FC = () => {
  return (
    <AbsoluteFill>
      {/* 0–3s: hook op start-screenshot */}
      <Sequence from={0} durationInFrames={90}>
        <Scene
          imagePath="start.jpeg"
          title="You asked. 👵📚"
          subtitle="We listened."
          totalFrames={90}
        />
      </Sequence>

      {/* 3–6s: collectie screenshot */}
      <Sequence from={90} durationInFrames={90}>
        <Scene
          imagePath="shot3library.png"
          title="Books, music, DVDs & games"
          subtitle="All in one place"
          totalFrames={90}
        />
      </Sequence>

      {/* 6–9s: scan screenshot */}
      <Sequence from={180} durationInFrames={90}>
        <Scene
          imagePath="shot 2scan.jpeg"
          title="Scan any barcode"
          subtitle="No account. Works offline."
          totalFrames={90}
        />
      </Sequence>

      {/* 9–12s: boekdetail screenshot */}
      <Sequence from={270} durationInFrames={90}>
        <Scene
          imagePath="boekdetail.png"
          title="Free to download"
          subtitle="€4.99 to unlock unlimited · No subscription"
          accentColor="#3DDC84"
          totalFrames={90}
        />
      </Sequence>

      {/* 12–15s: CTA eindscherm */}
      <Sequence from={360} durationInFrames={90}>
        <CtaEndScreen />
      </Sequence>
    </AbsoluteFill>
  );
};
