import { bundle } from "@remotion/bundler";
import { renderStill, selectComposition } from "@remotion/renderer";
import path from "path";
import fs from "fs";

const OUTPUT_DIR = path.join(__dirname, "..", "output", "screenshots");

const SLIDES = [
  "Screenshot1Scan",
  "Screenshot2Library",
  "Screenshot3Track",
  "Screenshot4Export",
  "Screenshot5NoAccount",
  "Screenshot1ScanNL",
  "Screenshot2LibraryNL",
  "Screenshot3TrackNL",
  "Screenshot4ExportNL",
  "Screenshot5NoAccountNL",
];

async function renderScreenshots() {
  if (!fs.existsSync(OUTPUT_DIR)) {
    fs.mkdirSync(OUTPUT_DIR, { recursive: true });
  }

  console.log("📦 Bundling Remotion...");
  const bundleLocation = await bundle({
    entryPoint: path.join(__dirname, "..", "src", "index.ts"),
    webpackOverride: (config) => config,
  });

  for (const id of SLIDES) {
    const outputPath = path.join(OUTPUT_DIR, `${id}.png`);
    console.log(`\n📸 Rendering: ${id}`);

    const composition = await selectComposition({
      serveUrl: bundleLocation,
      id,
      inputProps: {},
    });

    await renderStill({
      composition,
      serveUrl: bundleLocation,
      output: outputPath,
      inputProps: {},
      imageFormat: "png",
    });

    console.log(`   ✅ Opgeslagen: ${outputPath}`);
  }

  console.log(`\n🎉 Alle 10 screenshots klaar in: output/screenshots/`);
  console.log("   EN: Screenshot1Scan t/m Screenshot5NoAccount");
  console.log("   NL: Screenshot1ScanNL t/m Screenshot5NoAccountNL\n");
}

renderScreenshots().catch((err) => {
  console.error("❌ Mislukt:", err);
  process.exit(1);
});
