/**
 * Plant dagelijks gegenereerde content in via Blotato — eerste vrije slot per platform.
 * Hergebruikt bewezen video-URL's uit blotato-media-urls-seniorease.json.
 */

import fs from "fs";
import path from "path";
import type { GeneratedContent } from "./script-writer";

const BLOTATO_API = "https://backend.blotato.com/v2";

type Platform = "tiktok" | "instagram" | "linkedin" | "facebook";

export interface BlotatoScheduleResult {
  platform: Platform;
  scheduledAt: string;
  postSubmissionId?: string;
  mediaUrl?: string;
  skipped?: string;
}

interface SlotRule {
  hour: string;
  minute: string;
  weekdaysOnly: boolean;
}

const SLOT_RULES: Record<Platform, SlotRule> = {
  linkedin: { hour: "07", minute: "00", weekdaysOnly: true },
  tiktok: { hour: "17", minute: "30", weekdaysOnly: false },
  instagram: { hour: "18", minute: "00", weekdaysOnly: false },
  facebook: { hour: "18", minute: "00", weekdaysOnly: false },
};

const VIDEO_PAIRS: Array<{ tt: string; ig: string }> = [
  {
    tt: "seniorease-slideshow-mixed-vinyl-en-2026-06-19.mp4",
    ig: "seniorease-slideshow-mixed-vinyl-en-2026-06-19-ig.mp4",
  },
  {
    tt: "seniorease-script-en-20-stop-buying-duplicates-2026-06-19.mp4",
    ig: "seniorease-script-en-20-stop-buying-duplicates-2026-06-19-ig.mp4",
  },
  {
    tt: "seniorease-script-en-7-5-second-thrift-tip-2026-06-19.mp4",
    ig: "seniorease-script-en-7-5-second-thrift-tip-2026-06-19-ig.mp4",
  },
  {
    tt: "seniorease-script-en-10-collecting-for-years-2026-06-19.mp4",
    ig: "seniorease-script-en-10-collecting-for-years-2026-06-19-ig.mp4",
  },
  {
    tt: "seniorease-dad-record-fair-en-2026-06-22.mp4",
    ig: "seniorease-dad-record-fair-en-2026-06-22-ig.mp4",
  },
];

const FB_VINYL_ROTATION = [
  "seniorease-slideshow-mixed-vinyl-en-2026-06-19.mp4",
  "seniorease-dad-record-fair-en-2026-06-22.mp4",
  "reuse-vinyl-fair.mp4",
  "seniorease-script-en-20-stop-buying-duplicates-2026-06-19.mp4",
];

function loadMediaCatalog(): Record<string, string> {
  const catalogPath = path.join(
    __dirname,
    "..",
    "content",
    "blotato-media-urls-seniorease.json"
  );
  if (!fs.existsSync(catalogPath)) return {};
  const raw = JSON.parse(fs.readFileSync(catalogPath, "utf-8")) as Record<
    string,
    string
  >;
  return Object.fromEntries(
    Object.entries(raw).filter(([key]) => !key.startsWith("_"))
  );
}

function accountId(platform: Platform): string | null {
  switch (platform) {
    case "tiktok":
      return process.env.BLOTATO_SENIOREASE_TIKTOK_ID ?? null;
    case "instagram":
      return process.env.BLOTATO_SENIOREASE_INSTAGRAM_ID ?? null;
    case "linkedin":
      return process.env.BLOTATO_LINKEDIN_ID ?? null;
    case "facebook":
      return process.env.BLOTATO_FACEBOOK_ACCOUNT_ID ?? null;
    default:
      return null;
  }
}

function addUtcDays(base: Date, days: number): Date {
  const d = new Date(base);
  d.setUTCDate(d.getUTCDate() + days);
  return d;
}

function bumpToWeekday(d: Date): Date {
  const day = d.getUTCDay();
  if (day === 6) return addUtcDays(d, 2);
  if (day === 0) return addUtcDays(d, 1);
  return d;
}

function toScheduledIso(date: Date, rule: SlotRule): string {
  const y = date.getUTCFullYear();
  const m = String(date.getUTCMonth() + 1).padStart(2, "0");
  const day = String(date.getUTCDate()).padStart(2, "0");
  return `${y}-${m}-${day}T${rule.hour}:${rule.minute}:00+00:00`;
}

interface BlotatoScheduleItem {
  scheduledAt: string;
  draft?: {
    content?: { platform?: string };
    accountId?: string;
  };
}

async function listFutureSchedules(apiKey: string): Promise<BlotatoScheduleItem[]> {
  const items: BlotatoScheduleItem[] = [];
  let cursor: string | undefined;

  do {
    const url = new URL(`${BLOTATO_API}/schedules`);
    url.searchParams.set("limit", "50");
    if (cursor) url.searchParams.set("cursor", cursor);

    const res = await fetch(url.toString(), {
      headers: { "blotato-api-key": apiKey },
    });
    if (!res.ok) {
      const text = await res.text();
      throw new Error(`Schedules ophalen mislukt: ${res.status} — ${text}`);
    }

    const data = (await res.json()) as {
      items?: BlotatoScheduleItem[];
      cursor?: string;
    };
    items.push(...(data.items ?? []));
    cursor = data.cursor;
  } while (cursor);

  const now = Date.now();
  return items.filter((s) => new Date(s.scheduledAt).getTime() > now);
}

function latestForPlatform(
  schedules: BlotatoScheduleItem[],
  platform: Platform,
  account: string
): Date | null {
  let latest: Date | null = null;
  for (const s of schedules) {
    const p = s.draft?.content?.platform;
    const acc = s.draft?.accountId;
    if (p !== platform || acc !== account) continue;
    const at = new Date(s.scheduledAt);
    if (!latest || at > latest) latest = at;
  }
  return latest;
}

function nextSlotDate(
  schedules: BlotatoScheduleItem[],
  platform: Platform,
  account: string,
  rule: SlotRule
): Date {
  const latest = latestForPlatform(schedules, platform, account);
  let candidate = latest
    ? addUtcDays(latest, 1)
    : addUtcDays(new Date(), 1);

  if (rule.weekdaysOnly) {
    candidate = bumpToWeekday(candidate);
  }

  return candidate;
}

function buildTikTokCaption(content: GeneratedContent): string {
  const { hook_text, body_text, cta_text, hashtags } = content.tiktok;
  return `${hook_text} ${body_text} ${cta_text} ↗️ ${hashtags}`.trim();
}

function buildInstagramCaption(content: GeneratedContent): string {
  const base = content.tiktok.caption.trim();
  const tags = content.tiktok.hashtags.trim();
  return tags && !base.includes("#") ? `${base}\n\n${tags}` : base;
}

function pickVideos(catalog: Record<string, string>): {
  tt: string;
  ig: string;
  fb: string;
} {
  const dayOfYear = Math.floor(
    (Date.now() - new Date(new Date().getFullYear(), 0, 0).getTime()) / 86400000
  );
  const pair = VIDEO_PAIRS[dayOfYear % VIDEO_PAIRS.length];
  const fbFile = FB_VINYL_ROTATION[dayOfYear % FB_VINYL_ROTATION.length];

  const tt = catalog[pair.tt];
  const ig = catalog[pair.ig];
  const fb = catalog[fbFile] ?? catalog[pair.tt];

  if (!tt || !ig || !fb) {
    throw new Error(
      "Media-catalog incompleet — controleer blotato-media-urls-seniorease.json"
    );
  }

  return { tt, ig, fb };
}

async function scheduleOne(
  apiKey: string,
  platform: Platform,
  account: string,
  scheduledTime: string,
  text: string,
  mediaUrls: string[]
): Promise<string> {
  const target =
    platform === "tiktok"
      ? {
          targetType: "tiktok",
          privacyLevel: "PUBLIC_TO_EVERYONE",
          isAiGenerated: false,
          disabledComments: false,
          disabledDuet: false,
          disabledStitch: false,
          isBrandedContent: false,
          isYourBrand: false,
        }
      : platform === "facebook"
        ? {
            targetType: "facebook",
            mediaType: "reel",
            pageId: process.env.BLOTATO_FACEBOOK_PAGE_ID,
          }
        : platform === "instagram"
          ? {
              targetType: "instagram",
              mediaType: "reel",
              shareToFeed: true,
            }
          : { targetType: "linkedin" };

  const res = await fetch(`${BLOTATO_API}/posts`, {
    method: "POST",
    headers: {
      "blotato-api-key": apiKey,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      scheduledTime,
      post: {
        accountId: account,
        content: { text, mediaUrls, platform },
        target,
      },
    }),
  });

  if (!res.ok) {
    const body = await res.text();
    throw new Error(`${platform}: ${res.status} — ${body}`);
  }

  const data = (await res.json()) as { postSubmissionId?: string; id?: string };
  return data.postSubmissionId ?? data.id ?? "?";
}

export interface DailyScheduleMedia {
  /** Nieuwe Remotion-render — zelfde video voor TT, IG en FB reel */
  freshVideoUrl?: string;
}

export async function scheduleDailyToBlotato(
  content: GeneratedContent,
  media?: DailyScheduleMedia
): Promise<BlotatoScheduleResult[]> {
  const apiKey = process.env.BLOTATO_API_KEY;
  if (!apiKey) {
    console.log("ℹ️  BLOTATO_API_KEY ontbreekt — inplannen overgeslagen.");
    return [];
  }

  if (process.env.BLOTATO_DAILY_SCHEDULE === "false") {
    console.log("ℹ️  BLOTATO_DAILY_SCHEDULE=false — inplannen overgeslagen.");
    return [];
  }

  console.log("\n[3/3] Blotato — inplannen op eerstvolgende vrije slot\n");

  const catalog = loadMediaCatalog();
  const videos = media?.freshVideoUrl
    ? {
        tt: media.freshVideoUrl,
        ig: media.freshVideoUrl,
        fb: media.freshVideoUrl,
      }
    : pickVideos(catalog);

  if (media?.freshVideoUrl) {
    console.log("   🆕 Nieuwe Remotion-video voor TT / IG / FB");
  }
  const schedules = await listFutureSchedules(apiKey);
  const results: BlotatoScheduleResult[] = [];

  const jobs: Array<{
    platform: Platform;
    text: string;
    mediaUrls: string[];
    mediaUrl?: string;
  }> = [
    { platform: "linkedin", text: content.linkedin, mediaUrls: [] },
    {
      platform: "tiktok",
      text: buildTikTokCaption(content),
      mediaUrls: [videos.tt],
      mediaUrl: videos.tt,
    },
    {
      platform: "instagram",
      text: buildInstagramCaption(content),
      mediaUrls: [videos.ig],
      mediaUrl: videos.ig,
    },
    {
      platform: "facebook",
      text: content.facebook,
      mediaUrls: [videos.fb],
      mediaUrl: videos.fb,
    },
  ];

  for (const job of jobs) {
    const acc = accountId(job.platform);
    if (!acc) {
      const msg = `Geen account-ID voor ${job.platform} in .env`;
      console.warn(`   ⚠️  ${msg}`);
      results.push({ platform: job.platform, scheduledAt: "", skipped: msg });
      continue;
    }

    if (job.platform === "facebook" && !process.env.BLOTATO_FACEBOOK_PAGE_ID) {
      const msg = "BLOTATO_FACEBOOK_PAGE_ID ontbreekt";
      console.warn(`   ⚠️  Facebook: ${msg}`);
      results.push({ platform: "facebook", scheduledAt: "", skipped: msg });
      continue;
    }

    const rule = SLOT_RULES[job.platform];
    const slotDate = nextSlotDate(schedules, job.platform, acc, rule);
    const scheduledAt = toScheduledIso(slotDate, rule);

    try {
      console.log(`   → ${job.platform} @ ${scheduledAt}`);
      const postSubmissionId = await scheduleOne(
        apiKey,
        job.platform,
        acc,
        scheduledAt,
        job.text,
        job.mediaUrls
      );
      console.log(`   ✅ ${job.platform} ingepland (${postSubmissionId})`);
      results.push({
        platform: job.platform,
        scheduledAt,
        postSubmissionId,
        mediaUrl: job.mediaUrl,
      });
      schedules.push({
        scheduledAt,
        draft: { content: { platform: job.platform }, accountId: acc },
      });
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : String(err);
      console.error(`   ❌ ${job.platform}: ${message}`);
      results.push({
        platform: job.platform,
        scheduledAt: "",
        skipped: message,
      });
    }
  }

  console.log(`\n🔗 Kalender: https://my.blotato.com/calendar`);
  return results;
}
