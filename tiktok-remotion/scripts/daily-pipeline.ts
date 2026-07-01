/**
 * Daily Pipeline — Agent 3
 *
 * Orchestreert de volledige content-pipeline:
 *   1. Trend Scout   → haalt trending topics op van Reddit
 *   2. Script Writer → genereert content voor 4 platforms via Claude
 *   3. Remotion CSV  → voegt rij toe aan seniorease-tiktok-scripts-en.csv
 *   4. Render        → optioneel: nieuwe MP4 via Remotion (DAILY_RENDER)
 *   5. Blotato       → upload + inplannen op eerstvolgende vrije slot
 *   6. E-mail digest → stuurt alles naar Maureen via Gmail
 *
 * Vereisten in .env:
 *   ANTHROPIC_API_KEY, BLOTATO_API_KEY (+ account IDs)
 *   Optioneel: GMAIL_*, DAILY_RENDER=false om render over te slaan
 *
 * Gebruik:
 *   npm run agent:daily
 */

import path from "path";
import fs from "fs";
import dotenv from "dotenv";

dotenv.config({ path: path.join(__dirname, "..", ".env") });

import nodemailer from "nodemailer";
import { scout } from "./trend-scout";
import {
  writeContent,
  printContent,
  type GeneratedContent,
} from "./script-writer";
import {
  scheduleDailyToBlotato,
  type BlotatoScheduleResult,
} from "./blotato-daily-scheduler";
import { appendDailyScript } from "./remotion-csv";
import { renderScriptById } from "./daily-render";
import { uploadVideoToBlotato } from "./blotato-media";

interface DailyReportMeta {
  scriptId: string;
  videoFile?: string;
  mediaUrl?: string;
}

function formatDigest(
  content: GeneratedContent,
  blotato: BlotatoScheduleResult[],
  meta: DailyReportMeta
): string {
  const sep = "─".repeat(60);
  const lines = [
    `SeniorEase — dagelijkse content (${new Date().toISOString().slice(0, 10)})`,
    `Topic: ${content.topic}`,
    "",
    sep,
    "REMOTION CSV",
    sep,
    `Script ID: ${meta.scriptId} → seniorease-tiktok-scripts-en.csv`,
    meta.videoFile ? `Video: output/${meta.videoFile}` : "Video: niet gerenderd (DAILY_RENDER=false of mislukt)",
    meta.mediaUrl ? `Blotato media: ${meta.mediaUrl}` : "",
    "",
    sep,
    "TIKTOK / INSTAGRAM",
    sep,
    `Hook:    ${content.tiktok.hook_text}`,
    `Script:  ${content.tiktok.body_text}`,
    `CTA:     ${content.tiktok.cta_text}`,
    `Caption: ${content.tiktok.caption}`,
    `Tags:    ${content.tiktok.hashtags}`,
    "",
    sep,
    "LINKEDIN (Engels)",
    sep,
    content.linkedin,
    "",
    sep,
    "FACEBOOK (Nederlands)",
    sep,
    content.facebook,
    "",
    "Bestand in repo: tiktok-remotion/content/daily-DATUM.json",
  ];

  if (blotato.length > 0) {
    lines.push("", sep, "BLOTATO (ingepland)", sep);
    for (const row of blotato) {
      if (row.skipped) {
        lines.push(`${row.platform}: overgeslagen — ${row.skipped}`);
      } else {
        lines.push(
          `${row.platform}: ${row.scheduledAt} (ID ${row.postSubmissionId})`
        );
      }
    }
  }

  return lines.filter(Boolean).join("\n");
}

async function sendEmailDigest(
  content: GeneratedContent,
  blotato: BlotatoScheduleResult[],
  meta: DailyReportMeta
): Promise<void> {
  const { GMAIL_FROM, GMAIL_APP_PASSWORD, GMAIL_TO } = process.env;
  if (!GMAIL_FROM || !GMAIL_APP_PASSWORD || !GMAIL_TO) {
    console.log("📧 Geen Gmail-config — e-mail overgeslagen.");
    return;
  }

  const transporter = nodemailer.createTransport({
    service: "gmail",
    auth: { user: GMAIL_FROM, pass: GMAIL_APP_PASSWORD },
  });

  const datum = new Date().toISOString().slice(0, 10);
  await transporter.sendMail({
    from: GMAIL_FROM,
    to: GMAIL_TO,
    subject: `SeniorEase daily content — ${datum}`,
    text: formatDigest(content, blotato, meta),
  });

  console.log(`📧 Digest verstuurd naar ${GMAIL_TO}`);
}

function saveDailyReport(
  content: GeneratedContent,
  blotato: BlotatoScheduleResult[],
  meta: DailyReportMeta
): void {
  const datum = new Date().toISOString().slice(0, 10);
  const outFile = path.join(__dirname, "..", "content", `daily-${datum}.json`);
  fs.writeFileSync(
    outFile,
    JSON.stringify({ ...content, remotion: meta, blotato }, null, 2)
  );
  console.log(`💾 Opgeslagen: content/daily-${datum}.json`);
}

async function runPipeline() {
  console.log("🚀 Daily Pipeline gestart\n");
  console.log("=".repeat(60));

  console.log("\n[1/5] Trend Scout");
  const trends = await scout();
  const topTopic = trends[0]?.title ?? "";

  if (!topTopic) {
    console.error("❌ Geen trending topics gevonden. Pipeline gestopt.");
    process.exit(1);
  }

  console.log("\n[2/5] Script Writer");
  const content = await writeContent(topTopic);
  printContent(content);

  console.log("\n[3/5] Remotion CSV");
  const { id: scriptId } = appendDailyScript(content);

  console.log("\n[4/5] Render + upload");
  let freshVideoUrl: string | undefined;
  const videoPath = renderScriptById(scriptId);
  if (videoPath && process.env.BLOTATO_API_KEY) {
    try {
      freshVideoUrl = await uploadVideoToBlotato(
        process.env.BLOTATO_API_KEY,
        videoPath
      );
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : String(err);
      console.error(`⚠️  Blotato upload mislukt: ${message}`);
    }
  } else if (videoPath) {
    console.log("ℹ️  Video gerenderd; upload overgeslagen (geen BLOTATO_API_KEY).");
  }

  console.log("\n[5/5] Blotato inplannen");
  const blotato = await scheduleDailyToBlotato(content, { freshVideoUrl });

  const meta: DailyReportMeta = {
    scriptId,
    videoFile: videoPath ? path.basename(videoPath) : undefined,
    mediaUrl: freshVideoUrl,
  };

  saveDailyReport(content, blotato, meta);

  try {
    await sendEmailDigest(content, blotato, meta);
  } catch (err: unknown) {
    const message = err instanceof Error ? err.message : String(err);
    console.warn(`⚠️  E-mail mislukt (pipeline verder OK): ${message}`);
  }

  console.log("\n" + "=".repeat(60));
  console.log("✅ Pipeline klaar!\n");
}

runPipeline().catch((err) => {
  console.error("❌ Pipeline mislukt:", err.message);
  process.exit(1);
});
