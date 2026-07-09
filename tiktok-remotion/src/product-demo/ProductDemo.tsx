import React from "react";
import { AbsoluteFill, Series } from "remotion";
import {
  S1_FRAMES,
  S2_FRAMES,
  S3_FRAMES,
  S4_FRAMES,
  S5_FRAMES,
  S6_FRAMES,
  BG,
} from "./constants";
import { Scene1Hook } from "./scenes/Scene1Hook";
import { Scene2Intro } from "./scenes/Scene2Intro";
import { Scene3Demo } from "./scenes/Scene3Demo";
import { Scene4Images } from "./scenes/Scene4Images";
import { Scene5Features } from "./scenes/Scene5Features";
import { Scene6CTA } from "./scenes/Scene6CTA";

export const ProductDemo: React.FC = () => {
  return (
    <AbsoluteFill style={{ background: BG }}>
      <Series>
        <Series.Sequence durationInFrames={S1_FRAMES} premountFor={30}>
          <Scene1Hook />
        </Series.Sequence>

        <Series.Sequence durationInFrames={S2_FRAMES} premountFor={30}>
          <Scene2Intro />
        </Series.Sequence>

        <Series.Sequence durationInFrames={S3_FRAMES} premountFor={30}>
          <Scene3Demo />
        </Series.Sequence>

        <Series.Sequence durationInFrames={S4_FRAMES} premountFor={30}>
          <Scene4Images />
        </Series.Sequence>

        <Series.Sequence durationInFrames={S5_FRAMES} premountFor={30}>
          <Scene5Features />
        </Series.Sequence>

        <Series.Sequence durationInFrames={S6_FRAMES} premountFor={30}>
          <Scene6CTA />
        </Series.Sequence>
      </Series>
    </AbsoluteFill>
  );
};
