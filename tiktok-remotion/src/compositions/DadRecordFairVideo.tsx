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

const ACCENT = "#C9A06A";
const FPS_SEGMENT = 90; // 3 seconds per beat @ 30fps

const WarmTextCard: React.FC<{
  lines: string[];
  emoji?: string;
}> = ({ lines, emoji }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const opacity = interpolate(frame, [0, fps * 0.35], [0, 1], {
    extrapolateRight: "clamp",
  });
  const y = interpolate(frame, [0, fps * 0.45], [28, 0], {
    extrapolateRight: "clamp",
  });
  const scale = spring({
    frame,
    fps,
    config: { damping: 14, stiffness: 85 },
    from: 0.96,
    to: 1,
  });

  return (
    <AbsoluteFill
      style={{
        background: "linear-gradient(160deg, #2a2118 0%, #1a1510 55%, #0f0f1a 100%)",
        justifyContent: "center",
        alignItems: "center",
        opacity,
      }}
    >
      <div
        style={{
          transform: `translateY(${y}px) scale(${scale})`,
          textAlign: "center",
          padding: "0 88px",
        }}
      >
        {emoji ? (
          <div style={{ fontSize: 120, marginBottom: 36 }}>{emoji}</div>
        ) : null}
        {lines.map((line, i) => (
          <p
            key={i}
            style={{
              margin: i === 0 ? 0 : "18px 0 0",
              fontFamily: "sans-serif",
              fontSize: i === 0 ? 72 : 58,
              fontWeight: i === 0 ? 900 : 700,
              lineHeight: 1.15,
              color: i === 0 ? "#fff" : "#e8dfd3",
            }}
          >
            {line}
          </p>
        ))}
        <div
          style={{
            width: 72,
            height: 6,
            backgroundColor: ACCENT,
            borderRadius: 6,
            margin: "40px auto 0",
          }}
        />
      </div>
    </AbsoluteFill>
  );
};

const SceneImage: React.FC<{
  imagePath: string;
  caption: string;
  highlight?: string;
}> = ({ imagePath, caption, highlight }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const scale = interpolate(frame, [0, fps * 3], [1, 1.06], {
    extrapolateRight: "clamp",
  });
  const fadeIn = interpolate(frame, [0, fps * 0.25], [0, 1], {
    extrapolateRight: "clamp",
  });
  const textOpacity = interpolate(frame, [fps * 0.35, fps * 0.75], [0, 1], {
    extrapolateRight: "clamp",
  });
  const textY = interpolate(frame, [fps * 0.35, fps * 0.75], [36, 0], {
    extrapolateRight: "clamp",
  });

  return (
    <AbsoluteFill style={{ opacity: fadeIn }}>
      <AbsoluteFill style={{ transform: `scale(${scale})` }}>
        <img
          src={staticFile(imagePath)}
          style={{ width: "100%", height: "100%", objectFit: "cover" }}
        />
      </AbsoluteFill>

      <AbsoluteFill
        style={{
          background:
            "linear-gradient(to top, rgba(0,0,0,0.9) 0%, rgba(0,0,0,0.45) 42%, transparent 72%)",
        }}
      />

      <AbsoluteFill
        style={{
          justifyContent: "flex-end",
          alignItems: "center",
          paddingBottom: 120,
          opacity: textOpacity,
          transform: `translateY(${textY}px)`,
        }}
      >
        {highlight ? (
          <div
            style={{
              backgroundColor: ACCENT,
              borderRadius: 14,
              padding: "10px 28px",
              marginBottom: 20,
            }}
          >
            <span
              style={{
                fontFamily: "sans-serif",
                fontSize: 34,
                fontWeight: 800,
                color: "#1a1510",
                letterSpacing: 1,
              }}
            >
              {highlight}
            </span>
          </div>
        ) : null}
        <p
          style={{
            margin: 0,
            padding: "0 72px",
            fontFamily: "sans-serif",
            fontSize: 62,
            fontWeight: 800,
            color: "#fff",
            textAlign: "center",
            lineHeight: 1.2,
            textShadow: "0 4px 24px rgba(0,0,0,0.75)",
          }}
        >
          {caption}
        </p>
      </AbsoluteFill>
    </AbsoluteFill>
  );
};

// 18 seconds @ 30fps = 540 frames
export const DAD_RECORD_FAIR_FRAMES = 6 * FPS_SEGMENT;

export const DadRecordFairVideo: React.FC = () => {
  return (
    <AbsoluteFill style={{ backgroundColor: "#0f0f1a" }}>
      <Sequence from={0} durationInFrames={FPS_SEGMENT}>
        <WarmTextCard emoji="💿" lines={["My dad collects vinyl.", "30 years deep."]} />
      </Sequence>

      <Sequence from={FPS_SEGMENT} durationInFrames={FPS_SEGMENT}>
        <WarmTextCard
          lines={["Every record fair,", "the same pressing twice."]}
        />
      </Sequence>

      <Sequence from={FPS_SEGMENT * 2} durationInFrames={FPS_SEGMENT}>
        <SceneImage
          imagePath="screenshots/vinyl-lifestyle-2.webp"
          highlight="RECORD FAIR"
          caption="Scan the sleeve before you buy"
        />
      </Sequence>

      <Sequence from={FPS_SEGMENT * 3} durationInFrames={FPS_SEGMENT}>
        <SceneImage
          imagePath="screenshots/Screenshot1Scan.png"
          caption="Check his collection in seconds"
        />
      </Sequence>

      <Sequence from={FPS_SEGMENT * 4} durationInFrames={FPS_SEGMENT}>
        <SceneImage
          imagePath="screenshots/Screenshot2Library.png"
          caption="Already at home — put it back"
        />
      </Sequence>

      <Sequence from={FPS_SEGMENT * 5} durationInFrames={FPS_SEGMENT}>
        <CtaEndScreen />
      </Sequence>
    </AbsoluteFill>
  );
};
