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
import { BG, SAFE_TOP, SAFE_BOTTOM, H } from "../constants";
import { fontFamily } from "../font";

const IMAGES = [
  {
    src: "screenshots/Screenshot1Scan.png",
    headline: "📸 Scan any barcode in seconds",
  },
  {
    src: "screenshots/Screenshot2Library.png",
    headline: "📚 Your entire collection",
  },
  {
    src: "screenshots/Screenshot5NoAccount.png",
    headline: "🔒 No account needed",
  },
];

function imageOpacity(
  frame: number,
  inStart: number,
  inEnd: number,
  outStart: number,
  outEnd: number
) {
  const fadeIn = interpolate(frame, [inStart, inEnd], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const fadeOut = interpolate(frame, [outStart, outEnd], [1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  return Math.min(fadeIn, fadeOut);
}

interface ImageSlideProps {
  src: string;
  headline: string;
  opacity: number;
  scaleSpringFrame: number;
  fps: number;
}

const ImageSlide: React.FC<ImageSlideProps> = ({
  src,
  headline,
  opacity,
  scaleSpringFrame,
  fps,
}) => {
  const scaleSpring = spring({
    frame: scaleSpringFrame,
    fps,
    config: { damping: 18, stiffness: 120 },
    durationInFrames: 30,
  });
  const scale = interpolate(scaleSpring, [0, 1], [0.92, 1]);

  const IMG_W = 920;
  const HEADLINE_H = 100;
  const IMAGE_AREA_H = H - SAFE_TOP - SAFE_BOTTOM - HEADLINE_H - 20;
  const IMG_MAX_H = Math.min(IMAGE_AREA_H, 1400);

  return (
    <AbsoluteFill
      style={{
        opacity,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        gap: 32,
        background: BG,
      }}
    >
      <div
        style={{
          fontSize: 56,
          fontWeight: 800,
          color: "#ffffff",
          textAlign: "center",
          fontFamily,
          lineHeight: 1.2,
          paddingTop: SAFE_TOP,
          paddingLeft: 60,
          paddingRight: 60,
          opacity: interpolate(scaleSpring, [0, 0.5, 1], [0, 1, 1]),
        }}
      >
        {headline}
      </div>

      <div
        style={{
          width: IMG_W,
          maxHeight: IMG_MAX_H,
          transform: `scale(${scale})`,
          borderRadius: 20,
          overflow: "hidden",
          boxShadow: "0 40px 80px rgba(0,0,0,0.65), 0 8px 24px rgba(0,0,0,0.4)",
          flexShrink: 0,
        }}
      >
        <Img
          src={staticFile(src)}
          style={{
            width: "100%",
            height: "100%",
            objectFit: "contain",
            display: "block",
            background: "#111",
          }}
        />
      </div>
    </AbsoluteFill>
  );
};

export const Scene4Images: React.FC = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const op0 = imageOpacity(frame, 0, 12, 45, 58);
  const op1 = imageOpacity(frame, 45, 58, 90, 105);
  const op2 = imageOpacity(frame, 90, 105, 138, 150);

  return (
    <AbsoluteFill style={{ background: BG }}>
      <div
        style={{
          position: "absolute",
          bottom: 0,
          left: 0,
          right: 0,
          height: 200,
          background: `linear-gradient(to top, ${BG} 0%, transparent 100%)`,
          pointerEvents: "none",
          zIndex: 10,
        }}
      />

      <ImageSlide
        src={IMAGES[0].src}
        headline={IMAGES[0].headline}
        opacity={op0}
        scaleSpringFrame={frame}
        fps={fps}
      />
      <ImageSlide
        src={IMAGES[1].src}
        headline={IMAGES[1].headline}
        opacity={op1}
        scaleSpringFrame={Math.max(0, frame - 45)}
        fps={fps}
      />
      <ImageSlide
        src={IMAGES[2].src}
        headline={IMAGES[2].headline}
        opacity={op2}
        scaleSpringFrame={Math.max(0, frame - 90)}
        fps={fps}
      />
    </AbsoluteFill>
  );
};
