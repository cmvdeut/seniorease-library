/**
 * Voegt dagelijks gegenereerde scripts toe aan seniorease-tiktok-scripts-en.csv
 * (klaar voor npm run render:scripts:en).
 */

import fs from "fs";
import path from "path";
import type { GeneratedContent } from "./script-writer";

const SCRIPTS_CSV_EN = path.join(
  __dirname,
  "..",
  "content",
  "seniorease-tiktok-scripts-en.csv"
);

const CSV_HEADERS = [
  "id",
  "hook_text",
  "body_text",
  "cta_text",
  "duration_sec",
  "visual_suggestion",
  "caption",
  "hashtags",
];

export interface ScriptCsvRow {
  id: string;
  hook_text: string;
  body_text: string;
  cta_text: string;
  duration_sec: string;
  visual_suggestion: string;
  caption: string;
  hashtags: string;
}

function parseCsvLine(line: string): string[] {
  const out: string[] = [];
  let current = "";
  let inQuotes = false;

  for (let i = 0; i < line.length; i++) {
    const ch = line[i];
    if (ch === '"') {
      if (inQuotes && line[i + 1] === '"') {
        current += '"';
        i++;
      } else {
        inQuotes = !inQuotes;
      }
      continue;
    }
    if (ch === "," && !inQuotes) {
      out.push(current);
      current = "";
      continue;
    }
    current += ch;
  }
  out.push(current);
  return out;
}

function escapeCsvField(value: string): string {
  if (/[",\n\r]/.test(value)) {
    return `"${value.replace(/"/g, '""')}"`;
  }
  return value;
}

export function readScriptRows(csvPath = SCRIPTS_CSV_EN): ScriptCsvRow[] {
  if (!fs.existsSync(csvPath)) return [];

  const raw = fs.readFileSync(csvPath, "utf-8").replace(/\r/g, "");
  const lines = raw.split("\n").filter((l) => l.trim().length > 0);
  if (lines.length < 2) return [];

  const headers = parseCsvLine(lines[0]);
  const rows: ScriptCsvRow[] = [];

  for (let i = 1; i < lines.length; i++) {
    const values = parseCsvLine(lines[i]);
    const row = Object.fromEntries(
      headers.map((h, idx) => [h, values[idx] ?? ""])
    ) as ScriptCsvRow;
    if (row.id && row.hook_text) rows.push(row);
  }

  return rows;
}

function nextScriptId(rows: ScriptCsvRow[]): string {
  const max = rows.reduce((acc, row) => {
    const n = Number.parseInt(row.id, 10);
    return Number.isFinite(n) && n > acc ? n : acc;
  }, 0);
  return String(max + 1);
}

function defaultVisualSuggestion(content: GeneratedContent): string {
  if (content.tiktok.visual_suggestion?.trim()) {
    return content.tiktok.visual_suggestion.trim();
  }
  const topic = content.topic.toLowerCase();
  if (topic.match(/\b(vinyl|record|lp|pressing|fair)\b/)) {
    return "Vinyl flat-lay or record fair shot, barcode scan screen, library match check, CTA end card.";
  }
  if (topic.match(/\b(book|read|bookstore|tbr)\b/)) {
    return "Bookshelf close-up, barcode scan, duplicate check result, CTA end card.";
  }
  return "Hook text card, app scan screen, calm library view, CTA end card.";
}

export function appendDailyScript(
  content: GeneratedContent
): { id: string; row: ScriptCsvRow } {
  const rows = readScriptRows();
  const id = nextScriptId(rows);

  const row: ScriptCsvRow = {
    id,
    hook_text: content.tiktok.hook_text.trim(),
    body_text: content.tiktok.body_text.trim(),
    cta_text: content.tiktok.cta_text.trim(),
    duration_sec: content.tiktok.duration_sec?.trim() || "15",
    visual_suggestion: defaultVisualSuggestion(content),
    caption: content.tiktok.caption.trim(),
    hashtags: content.tiktok.hashtags.trim(),
  };

  const line = CSV_HEADERS.map((h) =>
    escapeCsvField(row[h as keyof ScriptCsvRow])
  ).join(",");

  const prefix =
    rows.length === 0
      ? `${CSV_HEADERS.join(",")}\n`
      : fs.readFileSync(SCRIPTS_CSV_EN, "utf-8").endsWith("\n")
        ? ""
        : "\n";

  fs.appendFileSync(SCRIPTS_CSV_EN, `${prefix}${line}\n`, "utf-8");
  console.log(`   ✅ CSV rij ${id} toegevoegd aan seniorease-tiktok-scripts-en.csv`);

  return { id, row };
}
