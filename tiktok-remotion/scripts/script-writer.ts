/**
 * Script Writer — Agent 2
 *
 * Neemt het top trending topic van Trend Scout en genereert via Claude API:
 *   1. TikTok/Instagram script (hook, body, CTA, caption, hashtags, visual_suggestion)
 *   2. LinkedIn post (Engels, professioneel)
 *   3. Facebook post (Nederlands, warm)
 *
 * De daily pipeline voegt het script toe aan seniorease-tiktok-scripts-en.csv.
 *
 * Vereisten:
 *   ANTHROPIC_API_KEY in .env
 *
 * Gebruik:
 *   npm run agent:write                          (leest meest recente trends-*.json)
 *   npm run agent:write -- --topic "vinyl tips"  (direct topic opgeven)
 */

import fs from "fs";
import path from "path";
import dotenv from "dotenv";
import Anthropic from "@anthropic-ai/sdk";

dotenv.config({ path: path.join(__dirname, "..", ".env") });

const client = new Anthropic({ apiKey: process.env.ANTHROPIC_API_KEY });

export interface GeneratedContent {
  topic: string;
  tiktok: {
    hook_text: string;
    body_text: string;
    cta_text: string;
    caption: string;
    hashtags: string;
    duration_sec?: string;
    visual_suggestion?: string;
  };
  linkedin: string;
  facebook: string;
}

function loadBrandProfile(): string {
  const profilePath = path.join(__dirname, "..", "..", "brand-profile.json");
  if (!fs.existsSync(profilePath)) return "";
  const p = JSON.parse(fs.readFileSync(profilePath, "utf-8"));
  return `
Brand: ${p.brand_name}
Doelgroep: ${p.target_audience.age_range} jaar, verzamelaars van boeken/vinyl/dvd/games
Pijnpunten: ${p.target_audience.pain_points.join("; ")}
Merkstijl: ${p.voice.descriptors.join(", ")}
Merkwaarden: ${p.brand_values.join("; ")}
App: €4.99 eenmalig, geen account, geen cloud, scant barcodes
  `.trim();
}

function loadLatestTrend(): string {
  const contentDir = path.join(__dirname, "..", "content");
  const files = fs
    .readdirSync(contentDir)
    .filter((f) => f.startsWith("trends-") && f.endsWith(".json"))
    .sort()
    .reverse();

  if (files.length === 0) return "";

  const trends = JSON.parse(
    fs.readFileSync(path.join(contentDir, files[0]), "utf-8")
  );
  if (!trends.length) return "";

  const top = trends[0];
  return `${top.title} (r/${top.subreddit})`;
}

function parseJsonFromLLM(raw: string): Record<string, unknown> {
  const cleaned = raw.replace(/```json\s*/gi, "").replace(/```/g, "").trim();
  try {
    return JSON.parse(cleaned) as Record<string, unknown>;
  } catch {
    const start = cleaned.indexOf("{");
    if (start === -1) {
      throw new Error("Claude gaf geen geldige JSON terug:\n" + raw);
    }
    let depth = 0;
    for (let i = start; i < cleaned.length; i++) {
      const ch = cleaned[i];
      if (ch === "{") depth++;
      if (ch === "}") depth--;
      if (depth === 0) {
        return JSON.parse(cleaned.slice(start, i + 1)) as Record<string, unknown>;
      }
    }
    throw new Error("Claude gaf onvolledige JSON terug:\n" + raw);
  }
}

export async function writeContent(topic?: string): Promise<GeneratedContent> {
  const brand = loadBrandProfile();
  const trendTopic = topic ?? loadLatestTrend();

  if (!trendTopic) {
    throw new Error("Geen topic opgegeven en geen trends-*.json gevonden. Draai eerst: npm run agent:scout");
  }

  console.log(`✍️  Script Writer — topic: "${trendTopic}"\n`);

  const systemPrompt = `Je bent een expert social media content writer voor SeniorEase Library.

${brand}

Schrijf altijd vanuit begrip voor de doelgroep: 45-75 jaar, houdt van analoge collecties, wil geen gedoe met tech.
De app lost één concreet probleem op: nooit meer dubbel kopen.`;

  const userPrompt = `Trending topic vandaag: "${trendTopic}"

Genereer content voor 3 platforms. Geef je antwoord ALLEEN als geldig JSON, geen extra tekst:

{
  "tiktok": {
    "hook_text": "Pakkende openingszin (max 8 woorden, Engels, stelt een herkenbaar probleem)",
    "body_text": "Korte uitleg hoe SeniorEase dit oplost (2-3 zinnen, Engels, simpel)",
    "cta_text": "Call to action (max 6 woorden, Engels)",
    "caption": "TikTok/Instagram caption (Engels, max 150 tekens, inclusief emoji)",
    "hashtags": "#seniorease #vinyl #bookcollector (max 5 hashtags totaal)",
    "duration_sec": "12-18 (geheel getal, seconden voor Remotion video)",
    "visual_suggestion": "Korte Engelse shotlist voor Remotion (hook card, scan screen, library, CTA)"
  },
  "linkedin": "LinkedIn post in het Engels (150-200 woorden). Professionele toon. Focus op het probleem van verzamelaars die dubbel kopen. Sluit af met een zachte CTA naar seniorease.eu. Geen hashtag-spam — max 3 tags.",
  "facebook": "Facebook post in het NEDERLANDS (100-130 woorden). Warme, persoonlijke toon. Spreek de lezer direct aan. Herkenbaar probleem voor iemand van 60+. Eindig met een vraag of uitnodiging. Max 2 hashtags."
}`;

  const message = await client.messages.create({
    model: "claude-haiku-4-5-20251001",
    max_tokens: 1024,
    messages: [{ role: "user", content: userPrompt }],
    system: systemPrompt,
  });

  const block = message.content[0];
  if (!block || block.type !== "text") {
    throw new Error("Claude gaf geen tekstantwoord terug.");
  }
  const parsed = parseJsonFromLLM(block.text);
  const tiktok = parsed.tiktok as GeneratedContent["tiktok"] | undefined;
  if (!tiktok?.hook_text || !tiktok?.body_text) {
    throw new Error("Claude JSON mist verplichte tiktok-velden (hook_text, body_text).");
  }

  return {
    topic: trendTopic,
    tiktok,
    linkedin: String(parsed.linkedin ?? ""),
    facebook: String(parsed.facebook ?? ""),
  };
}

function printContent(content: GeneratedContent) {
  const sep = "─".repeat(60);

  console.log(`\n${sep}`);
  console.log(`📱 TIKTOK / INSTAGRAM`);
  console.log(sep);
  console.log(`Hook:     ${content.tiktok.hook_text}`);
  console.log(`Script:   ${content.tiktok.body_text}`);
  console.log(`CTA:      ${content.tiktok.cta_text}`);
  console.log(`Caption:  ${content.tiktok.caption}`);
  console.log(`Tags:     ${content.tiktok.hashtags}`);

  console.log(`\n${sep}`);
  console.log(`💼 LINKEDIN (Engels)`);
  console.log(sep);
  console.log(content.linkedin);

  console.log(`\n${sep}`);
  console.log(`👥 FACEBOOK (Nederlands)`);
  console.log(sep);
  console.log(content.facebook);
  console.log(sep + "\n");
}

function saveContent(content: GeneratedContent) {
  const datum = new Date().toISOString().slice(0, 10);
  const outFile = path.join(__dirname, "..", "content", `daily-${datum}.json`);
  fs.writeFileSync(outFile, JSON.stringify(content, null, 2));
  console.log(`💾 Opgeslagen: content/daily-${datum}.json`);
}

// Direct uitvoeren als hoofdscript
if (require.main === module) {
  const topicArg = process.argv.includes("--topic")
    ? process.argv[process.argv.indexOf("--topic") + 1]
    : undefined;

  writeContent(topicArg)
    .then((content) => {
      printContent(content);
      saveContent(content);
    })
    .catch((err) => {
      console.error("❌ Script Writer mislukt:", err.message);
      process.exit(1);
    });
}

export { printContent, saveContent };
