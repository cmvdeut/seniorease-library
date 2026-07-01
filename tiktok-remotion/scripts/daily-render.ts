/**
 * Render één script-video uit de EN CSV (voor daily pipeline).
 */

import { execSync } from "child_process";
import fs from "fs";
import path from "path";

const OUTPUT_DIR = path.join(__dirname, "..", "output");
const PROJECT_DIR = path.join(__dirname, "..");

export function shouldRenderDaily(): boolean {
  return process.env.DAILY_RENDER !== "false";
}

export function renderScriptById(scriptId: string): string | null {
  if (!shouldRenderDaily()) {
    console.log("ℹ️  DAILY_RENDER=false — video render overgeslagen.");
    return null;
  }

  console.log(`\n🎬 Remotion render — script ${scriptId}...`);

  try {
    execSync(`npx ts-node scripts/render.ts scripts-en --id ${scriptId}`, {
      cwd: PROJECT_DIR,
      stdio: "inherit",
    });
  } catch {
    console.error(`❌ Render mislukt voor script ${scriptId}`);
    return null;
  }

  return findRenderedVideo(scriptId);
}

export function findRenderedVideo(scriptId: string): string | null {
  if (!fs.existsSync(OUTPUT_DIR)) return null;

  const ts = new Date().toISOString().slice(0, 10);
  const prefix = `seniorease-script-en-${scriptId}-`;
  const matches = fs
    .readdirSync(OUTPUT_DIR)
    .filter((f) => f.startsWith(prefix) && f.endsWith(`${ts}.mp4`))
    .sort();

  if (matches.length === 0) {
    console.warn(`⚠️  Geen output gevonden voor script ${scriptId} (${ts})`);
    return null;
  }

  const videoPath = path.join(OUTPUT_DIR, matches[matches.length - 1]);
  console.log(`   ✅ Video: ${path.basename(videoPath)}`);
  return videoPath;
}
