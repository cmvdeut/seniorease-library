/**
 * Blotato Schedule Script voor SeniorEase + ShelfieEase
 *
 * Leest een blotato-schedule CSV en plant alle "pending" posts in via de Blotato API.
 * Na inplannen wordt de status in de CSV bijgewerkt naar "scheduled".
 *
 * CSV-kolommen:
 *   id, brand, date, time_utc, platform, video_filename, caption, status
 *   brand = seniorease | shelfieease (default: seniorease als kolom ontbreekt)
 *
 * Vereisten:
 *   1. BLOTATO_API_KEY in .env (via my.blotato.com → Settings → API)
 *   2. Account IDs per merk in .env (BLOTATO_SENIOREASE_* / BLOTATO_SHELFIEASE_*)
 *   3. Video's gerenderd in output/ — vul video_filename in de CSV in vóór je dit script draait
 *
 * Gebruik:
 *   npm run post:blotato -- --csv content/week-2026-06-02-blotato.csv
 */

import fs from "fs";
import path from "path";
import dotenv from "dotenv";

dotenv.config({ path: path.join(__dirname, "..", ".env") });

const BLOTATO_API = "https://backend.blotato.com/v2";

type Brand = "seniorease" | "shelfieease";

interface BlotatoRow {
  id: string;
  brand: string;
  date: string;
  time_utc: string;
  platform: string;
  video_filename: string;
  media_url: string;
  caption: string;
  status: string;
}

const BRAND_ACCOUNT_ENV: Record<Brand, Partial<Record<string, string>>> = {
  seniorease: {
    tiktok: "BLOTATO_SENIOREASE_TIKTOK_ID",
    instagram: "BLOTATO_SENIOREASE_INSTAGRAM_ID",
  },
  shelfieease: {
    tiktok: "BLOTATO_SHELFIEASE_TIKTOK_ID",
    instagram: "BLOTATO_SHELFIEASE_INSTAGRAM_ID",
  },
};

const BRAND_USERNAME_ENV: Record<Brand, Partial<Record<string, string>>> = {
  seniorease: {
    tiktok: "BLOTATO_TIKTOK_USERNAME",
    instagram: "BLOTATO_INSTAGRAM_USERNAME",
  },
  shelfieease: {
    tiktok: "BLOTATO_SHELFIEASE_TIKTOK_USERNAME",
    instagram: "BLOTATO_SHELFIEASE_INSTAGRAM_USERNAME",
  },
};

const DEFAULT_CSV_HEADERS = [
  "id",
  "brand",
  "date",
  "time_utc",
  "platform",
  "video_filename",
  "media_url",
  "caption",
  "status",
];

// CSV-parser die geciteerde velden ondersteunt
function parseCSVLine(line: string): string[] {
  const result: string[] = [];
  let current = "";
  let inQuotes = false;

  for (let i = 0; i < line.length; i++) {
    const char = line[i];
    if (char === '"') {
      inQuotes = !inQuotes;
    } else if (char === "," && !inQuotes) {
      result.push(current);
      current = "";
    } else {
      current += char;
    }
  }
  result.push(current);
  return result;
}

function normalizeBrand(raw?: string): Brand {
  const brand = (raw?.trim().toLowerCase() || "seniorease") as Brand;
  if (brand !== "seniorease" && brand !== "shelfieease") {
    throw new Error(`Onbekend brand "${raw}". Gebruik: seniorease of shelfieease`);
  }
  return brand;
}

function parseCsv(content: string): { headers: string[]; rows: BlotatoRow[] } {
  const lines = content.trim().split("\n").filter(Boolean);
  const headers = parseCSVLine(lines[0]).map((h) => h.trim());
  const hasBrand = headers.includes("brand");

  const rows = lines.slice(1).map((line) => {
    const values = parseCSVLine(line);
    const obj: Record<string, string> = {};
    headers.forEach((h, i) => {
      obj[h] = (values[i] ?? "").trim();
    });

    return {
      id: obj.id ?? "",
      brand: normalizeBrand(hasBrand ? obj.brand : "seniorease"),
      date: obj.date ?? "",
      time_utc: obj.time_utc ?? "",
      platform: obj.platform ?? "",
      video_filename: obj.video_filename ?? "",
      media_url: obj.media_url ?? "",
      caption: obj.caption ?? "",
      status: obj.status ?? "",
    };
  });

  return { headers, rows };
}

function writeCsv(rows: BlotatoRow[], filePath: string, headers: string[]): void {
  const outHeaders = headers.includes("brand") ? headers : DEFAULT_CSV_HEADERS;
  const headerLine = outHeaders.join(",");
  const lines = rows.map((r) => {
    const values = outHeaders.map((h) => {
      const value = (r as unknown as Record<string, string>)[h] ?? "";
      return h === "caption" ? `"${value.replace(/"/g, '""')}"` : value;
    });
    return values.join(",");
  });
  fs.writeFileSync(filePath, [headerLine, ...lines].join("\n") + "\n", "utf-8");
}

function loadMediaCatalog(filename: string): Record<string, string> {
  const catalogPath = path.join(__dirname, "..", "content", filename);
  if (!fs.existsSync(catalogPath)) return {};
  const raw = JSON.parse(fs.readFileSync(catalogPath, "utf-8")) as Record<
    string,
    string
  >;
  return Object.fromEntries(
    Object.entries(raw).filter(([key]) => !key.startsWith("_"))
  );
}

function loadMediaCatalogs(): Record<string, string> {
  return {
    ...loadMediaCatalog("blotato-media-urls-seniorease.json"),
    ...loadMediaCatalog("blotato-media-urls-shelfieease.json"),
  };
}

function resolveMediaSource(
  row: BlotatoRow,
  brand: Brand,
  outputDir: string,
  catalog: Record<string, string>
): { url?: string; localPath?: string } {
  const directUrl = row.media_url?.trim();
  if (directUrl) return { url: directUrl };

  const filename = row.video_filename?.trim();
  if (filename?.startsWith("http")) return { url: filename };
  if (filename && catalog[filename]) return { url: catalog[filename] };

  const searchDirs = [outputDir];
  const shelfieDir = process.env.BLOTATO_SHELFIEASE_VIDEOS_DIR?.trim();
  if (brand === "shelfieease" && shelfieDir) {
    searchDirs.unshift(shelfieDir);
  }

  if (filename) {
    for (const dir of searchDirs) {
      const candidate = path.join(dir, filename);
      if (fs.existsSync(candidate)) return { localPath: candidate };
    }
  }

  return {};
}

function validateBrandCaption(brand: Brand, caption: string, postId: string): void {
  const lower = caption.toLowerCase();
  const hasShelfie =
    lower.includes("shelfieease") || lower.includes("shelfieease.app");
  const hasSenior =
    lower.includes("seniorease") ||
    lower.includes("google play") ||
    lower.includes("play store");

  if (brand === "seniorease" && hasShelfie) {
    throw new Error(
      `Post ${postId}: ShelfieEase-content op SeniorEase-account. Zet brand=shelfieease in de CSV.`
    );
  }
  if (brand === "shelfieease" && hasSenior) {
    throw new Error(
      `Post ${postId}: SeniorEase-content op ShelfieEase-account. Zet brand=seniorease in de CSV.`
    );
  }
}

// Zoek meest recente video voor een post-ID als video_filename leeg is
function autoDetectVideo(
  postId: string,
  platform: string,
  brand: Brand,
  outputDir: string
): string | null {
  if (!fs.existsSync(outputDir)) return null;
  const lang = platform === "instagram" ? "nl" : "en";
  const prefix =
    brand === "shelfieease"
      ? `shelfieease-script-${lang}-${postId}-`
      : `seniorease-script-${lang}-${postId}-`;
  const matches = fs
    .readdirSync(outputDir)
    .filter((f) => f.startsWith(prefix) && f.endsWith(".mp4"))
    .sort()
    .reverse();
  return matches.length > 0 ? path.join(outputDir, matches[0]) : null;
}

function getAccountIdFromEnv(brand: Brand, platform: string): string | null {
  if (platform === "facebook") {
    return process.env.BLOTATO_FACEBOOK_ACCOUNT_ID ?? null;
  }
  const envKey = BRAND_ACCOUNT_ENV[brand][platform];
  if (envKey && process.env[envKey]) {
    return process.env[envKey]!;
  }
  return null;
}

// Haal accountId op — eerst expliciete ID uit .env, anders username lookup
async function getAccountId(
  apiKey: string,
  platform: string,
  brand: Brand
): Promise<string> {
  const envId = getAccountIdFromEnv(brand, platform);
  if (envId) {
    console.log(`   🔗 ${brand}/${platform}: account ID ${envId} (uit .env)`);
    return envId;
  }

  if (platform === "facebook" && process.env.BLOTATO_FACEBOOK_ACCOUNT_ID) {
    const id = process.env.BLOTATO_FACEBOOK_ACCOUNT_ID;
    console.log(`   🔗 facebook account: Senior Ease (${id})`);
    return id;
  }

  const res = await fetch(`${BLOTATO_API}/users/me/accounts?platform=${platform}`, {
    headers: { "blotato-api-key": apiKey },
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`Accounts ophalen mislukt (${platform}): ${res.status} — ${text}`);
  }
  const data = (await res.json()) as { items?: Array<{ id: string; username?: string; fullname?: string }> };
  const items = data.items ?? [];
  if (items.length === 0) {
    throw new Error(
      `Geen ${platform}-account gevonden in Blotato. Koppel het account eerst via my.blotato.com.`
    );
  }

  const usernameEnvKey = BRAND_USERNAME_ENV[brand][platform];
  const preferredUsername = usernameEnvKey
    ? process.env[usernameEnvKey]?.toLowerCase()
    : undefined;

  const account = preferredUsername
    ? items.find((a) => a.username?.toLowerCase() === preferredUsername) ??
      (() => {
        const available = items.map((a) => `@${a.username} (${a.id})`).join(", ");
        throw new Error(
          `${brand}/${platform}: account "@${preferredUsername}" niet gevonden. Beschikbaar: ${available}`
        );
      })()
    : items[0];

  console.log(
    `   🔗 ${brand}/${platform}: @${account.username || account.fullname} (${account.id})`
  );
  return account.id;
}

// Upload video naar Blotato via presigned URL — geeft publieke URL terug
async function uploadMedia(apiKey: string, videoPath: string): Promise<string> {
  const fileName = path.basename(videoPath);
  const fileSize = fs.statSync(videoPath).size;

  console.log(
    `   📤 Uploaden: ${fileName} (${(fileSize / 1024 / 1024).toFixed(1)} MB)...`
  );

  const initRes = await fetch(`${BLOTATO_API}/media/uploads`, {
    method: "POST",
    headers: {
      "blotato-api-key": apiKey,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ filename: fileName, contentType: "video/mp4" }),
  });

  if (!initRes.ok) {
    const text = await initRes.text();
    throw new Error(`Media upload init mislukt: ${initRes.status} — ${text}`);
  }

  const uploadInit = (await initRes.json()) as { presignedUrl: string; publicUrl: string };
  const { presignedUrl, publicUrl } = uploadInit;

  const videoBuffer = fs.readFileSync(videoPath);
  const putRes = await fetch(presignedUrl, {
    method: "PUT",
    headers: {
      "Content-Type": "video/mp4",
      "Content-Length": String(fileSize),
    },
    body: videoBuffer,
  });

  if (!putRes.ok) {
    const text = await putRes.text();
    throw new Error(`Video PUT mislukt: ${putRes.status} — ${text}`);
  }

  console.log(`   ✅ Geüpload → ${publicUrl}`);
  return publicUrl;
}

// Plan één post in via Blotato
async function schedulePost(
  apiKey: string,
  accountId: string,
  row: BlotatoRow,
  mediaUrl: string
): Promise<void> {
  const scheduledTime = `${row.date}T${row.time_utc}:00+00:00`;

  const target =
    row.platform === "tiktok"
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
      : row.platform === "facebook"
        ? {
            targetType: "facebook",
            mediaType: "reel",
            pageId: process.env.BLOTATO_FACEBOOK_PAGE_ID,
          }
        : {
            targetType: "instagram",
            mediaType: "reel",
            shareToFeed: true,
          };

  const body = {
    scheduledTime,
    post: {
      accountId,
      content: {
        text: row.caption,
        mediaUrls: [mediaUrl],
        platform: row.platform,
      },
      target,
    },
  };

  const res = await fetch(`${BLOTATO_API}/posts`, {
    method: "POST",
    headers: {
      "blotato-api-key": apiKey,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(`Post inplannen mislukt: ${res.status} — ${text}`);
  }

  const data = (await res.json()) as { postSubmissionId?: string; id?: string };
  console.log(
    `   ✅ Ingepland op ${scheduledTime} (Blotato ID: ${data.postSubmissionId ?? data.id ?? "?"})`
  );
}

function getArg(flag: string): string | undefined {
  const args = process.argv.slice(2);
  const index = args.indexOf(flag);
  if (index !== -1 && args[index + 1]) return args[index + 1];
  return undefined;
}

function accountCacheKey(brand: Brand, platform: string): string {
  return `${brand}:${platform}`;
}

async function main() {
  const apiKey = process.env.BLOTATO_API_KEY;
  if (!apiKey) {
    console.error(
      "❌ BLOTATO_API_KEY ontbreekt in .env\n" +
        "   Haal je API key op via: my.blotato.com → Settings → API"
    );
    process.exit(1);
  }

  const csvArg = getArg("--csv");
  if (!csvArg) {
    console.error(
      "❌ Gebruik: npm run post:blotato -- --csv content/week-2026-06-02-blotato.csv"
    );
    process.exit(1);
  }

  const csvPath = path.resolve(__dirname, "..", csvArg);
  if (!fs.existsSync(csvPath)) {
    console.error(`❌ CSV niet gevonden: ${csvPath}`);
    process.exit(1);
  }

  const outputDir = path.join(__dirname, "..", "output");
  const mediaCatalog = loadMediaCatalogs();
  const { headers, rows } = parseCsv(fs.readFileSync(csvPath, "utf-8"));
  const pending = rows.filter((r) => r.status === "pending");

  if (pending.length === 0) {
    console.log("ℹ️  Geen pending posts gevonden in de CSV.");
    return;
  }

  console.log(
    `\n📅 Blotato inplannen — ${pending.length} posts uit ${path.basename(csvPath)}\n`
  );

  console.log("🔍 Account IDs ophalen...");
  const accountIds = new Map<string, string>();
  const needed = Array.from(
    new Set(pending.map((r) => accountCacheKey(normalizeBrand(r.brand), r.platform)))
  );
  for (const key of needed) {
    const [brand, platform] = key.split(":") as [Brand, string];
    accountIds.set(key, await getAccountId(apiKey, platform, brand));
  }

  const uploadCache = new Map<string, string>();
  let scheduled = 0;
  let failed = 0;

  for (const row of pending) {
    const brand = normalizeBrand(row.brand);
    console.log(
      `\n→ Post ${row.id} | ${brand} | ${row.platform.toUpperCase()} | ${row.date} ${row.time_utc} UTC`
    );
    console.log(`  Caption: ${row.caption.slice(0, 70)}...`);

    try {
      validateBrandCaption(brand, row.caption, row.id);

      let mediaUrl: string;
      const cacheKey =
        row.media_url ||
        row.video_filename ||
        `auto-${brand}-${row.platform}-${row.id}`;

      if (uploadCache.has(cacheKey)) {
        mediaUrl = uploadCache.get(cacheKey)!;
        console.log(`   ♻️  Hergebruik media: ${mediaUrl}`);
      } else {
        const source = resolveMediaSource(row, brand, outputDir, mediaCatalog);

        if (source.url) {
          mediaUrl = source.url;
          console.log(`   🔗 Blotato/media URL (geen upload nodig)`);
        } else if (source.localPath) {
          mediaUrl = await uploadMedia(apiKey, source.localPath);
        } else {
          let resolvedVideoPath = autoDetectVideo(row.id, row.platform, brand, outputDir);
          if (resolvedVideoPath) {
            console.log(`   🔍 Auto-detectie: ${path.basename(resolvedVideoPath)}`);
            mediaUrl = await uploadMedia(apiKey, resolvedVideoPath);
          } else {
            console.warn(
              `   ⚠️  Geen media voor post ${row.id} (${brand}/${row.platform}). ` +
                `Vul media_url in, zet een MP4 in output/ of ${process.env.BLOTATO_SHELFIEASE_VIDEOS_DIR || "ShelfieEase videos map"}.`
            );
            failed++;
            continue;
          }
        }
        uploadCache.set(cacheKey, mediaUrl);
      }

      const accountId = accountIds.get(accountCacheKey(brand, row.platform))!;
      await schedulePost(apiKey, accountId, row, mediaUrl);

      rows[rows.indexOf(row)].status = "scheduled";
      scheduled++;
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : String(err);
      console.error(`   ❌ Fout: ${message}`);
      failed++;
    }
  }

  writeCsv(rows, csvPath, headers);

  console.log(`\n${"─".repeat(50)}`);
  console.log(`✅ Ingepland: ${scheduled}   ❌ Mislukt: ${failed}`);
  console.log(`📄 CSV bijgewerkt: ${path.basename(csvPath)}`);
  console.log(`🔗 Kalender: https://my.blotato.com/calendar`);
}

main().catch((err) => {
  console.error("❌ Onverwachte fout:", err);
  process.exit(1);
});
