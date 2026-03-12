import { bundle } from "@remotion/bundler";
import { renderMedia, selectComposition } from "@remotion/renderer";
import path from "path";
import fs from "fs";

const OUTPUT_DIR = path.join(__dirname, "..", "output");

async function render(compositionId: string, outputFileName: string) {
  console.log(`\n🎬 Rendering: ${compositionId} → ${outputFileName}`);

  // Zorg dat output directory bestaat
  if (!fs.existsSync(OUTPUT_DIR)) {
    fs.mkdirSync(OUTPUT_DIR, { recursive: true });
  }

  // Bundle de Remotion root
  console.log("  📦 Bundling...");
  const bundleLocation = await bundle({
    entryPoint: path.join(__dirname, "..", "src", "index.ts"),
    webpackOverride: (config) => config,
  });

  // Haal de compositie op
  const composition = await selectComposition({
    serveUrl: bundleLocation,
    id: compositionId,
    inputProps: {},
  });

  const outputPath = path.join(OUTPUT_DIR, outputFileName);

  // Render naar MP4
  console.log("  🎥 Rendering video...");
  await renderMedia({
    composition,
    serveUrl: bundleLocation,
    codec: "h264",
    outputLocation: outputPath,
    inputProps: {},
  });

  console.log(`  ✅ Klaar: ${outputPath}`);
  return outputPath;
}

async function main() {
  const arg = process.argv[2] || "all";

  try {
    const ts = new Date().toISOString().slice(0, 10);

    if (arg === "slideshow" || arg === "all") {
      await render("ScreenshotSlideshow", `seniorease-slideshow-${ts}.mp4`);
    }

    if (arg === "feature" || arg === "all") {
      await render("FeatureDemo", `seniorease-feature-demo-${ts}.mp4`);
    }

    if (arg === "heart" || arg === "all") {
      await render("HeartTransformation", `seniorease-heart-animatie-${ts}.mp4`);
    }

    if (arg === "doublebuy" || arg === "all") {
      await render("DoubleBuyVideo", `seniorease-doublebuy-${ts}.mp4`);
    }

    console.log("\n🎉 Alle videos zijn gerenderd in: output/");
    console.log("   Je kunt ze nu uploaden naar TikTok of via het post script posten.\n");
  } catch (err) {
    console.error("❌ Render mislukt:", err);
    process.exit(1);
  }
}

main();
