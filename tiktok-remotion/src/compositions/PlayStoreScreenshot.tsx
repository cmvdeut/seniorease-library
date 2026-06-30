import React from "react";
import { z } from "zod";
import { AbsoluteFill, staticFile } from "remotion";

export const playStoreSchema = z.object({
  title: z.string(),
  subtitle: z.string(),
  screenshotPath: z.string(),
  accentColor: z.string().default("#8B5E3C"),
});

type PlayStoreScreenshotProps = z.infer<typeof playStoreSchema>;

export const PlayStoreScreenshot: React.FC<PlayStoreScreenshotProps> = ({
  title,
  subtitle,
  screenshotPath,
  accentColor,
}) => {
  return (
    <AbsoluteFill style={{ backgroundColor: "#F5EEE6" }}>
      {/* App screenshot bovenin */}
      <AbsoluteFill>
        <img
          src={staticFile(screenshotPath)}
          style={{
            width: "100%",
            height: "72%",
            objectFit: "cover",
            objectPosition: "top",
          }}
        />
      </AbsoluteFill>

      {/* Gradient van screenshot naar crème */}
      <AbsoluteFill
        style={{
          background: "linear-gradient(to bottom, transparent 45%, #F5EEE6 68%)",
        }}
      />

      {/* Tekst onderaan */}
      <AbsoluteFill
        style={{
          justifyContent: "flex-end",
          alignItems: "center",
          paddingBottom: 120,
          textAlign: "center",
        }}
      >
        <div style={{ padding: "0 80px" }}>
          {title ? (
            <h1
              style={{
                fontFamily: "sans-serif",
                fontSize: 70,
                fontWeight: 800,
                color: "#3D2B1F",
                margin: "0 0 24px",
                lineHeight: 1.2,
              }}
            >
              {title}
            </h1>
          ) : null}
          {subtitle ? (
            <p
              style={{
                fontFamily: "sans-serif",
                fontSize: 46,
                fontWeight: 600,
                color: accentColor,
                margin: "0 0 40px",
                lineHeight: 1.4,
                whiteSpace: "pre-line",
              }}
            >
              {subtitle}
            </p>
          ) : null}
          <div
            style={{
              backgroundColor: accentColor,
              borderRadius: 20,
              padding: "16px 40px",
              display: "inline-block",
            }}
          >
            <span
              style={{
                fontFamily: "sans-serif",
                fontSize: 36,
                fontWeight: 700,
                color: "white",
              }}
            >
              SeniorEase Library
            </span>
          </div>
        </div>
      </AbsoluteFill>
    </AbsoluteFill>
  );
};
