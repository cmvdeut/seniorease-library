/**
 * TikTok Auto-Post Script voor SeniorEase
 *
 * Vereisten:
 *   1. TikTok Developer account op https://developers.tiktok.com
 *   2. App aanmaken met "Content Posting API" product
 *   3. App goedkeuring aanvragen (duurt 1-5 werkdagen)
 *   4. OAuth access token ophalen voor @seniorease account
 *   5. .env invullen (zie .env.example)
 *
 * Gebruik:
 *   npm run post:tiktok -- --file output/seniorease-slideshow-2026-02-27.mp4 --title "..."
 */

import fs from "fs";
import path from "path";
import https from "https";
import dotenv from "dotenv";

dotenv.config({ path: path.join(__dirname, "..", ".env") });

const TIKTOK_API_BASE = "https://open.tiktokapis.com";

interface TikTokPostOptions {
  videoPath: string;
  title: string;
  privacyLevel?: "PUBLIC_TO_EVERYONE" | "MUTUAL_FOLLOW_FRIENDS" | "FOLLOWER_OF_CREATOR" | "SELF_ONLY";
  disableComment?: boolean;
  disableDuet?: boolean;
  disableStitch?: boolean;
}

// --- Stap 1: Initieer video upload ---
async function initVideoUpload(accessToken: string, videoSizeBytes: number) {
  const body = JSON.stringify({
    post_info: {
      title: "", // wordt later ingesteld
      privacy_level: "PUBLIC_TO_EVERYONE",
      disable_comment: false,
      disable_duet: false,
      disable_stitch: false,
    },
    source_info: {
      source: "FILE_UPLOAD",
      video_size: videoSizeBytes,
      chunk_size: videoSizeBytes, // 1 chunk voor kleine files (<64MB)
      total_chunk_count: 1,
    },
  });

  return fetch(`${TIKTOK_API_BASE}/v2/post/publish/video/init/`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json; charset=UTF-8",
    },
    body,
  }).then((r) => r.json());
}

// --- Stap 2: Upload video bestand ---
async function uploadVideoChunk(
  uploadUrl: string,
  videoPath: string,
  videoSizeBytes: number
) {
  const videoBuffer = fs.readFileSync(videoPath);

  const response = await fetch(uploadUrl, {
    method: "PUT",
    headers: {
      "Content-Type": "video/mp4",
      "Content-Range": `bytes 0-${videoSizeBytes - 1}/${videoSizeBytes}`,
      "Content-Length": String(videoSizeBytes),
    },
    body: videoBuffer,
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(`Upload mislukt: ${response.status} ${text}`);
  }

  return response;
}

// --- Stap 3: Controleer publicatiestatus ---
async function checkPublishStatus(
  accessToken: string,
  publishId: string
): Promise<string> {
  const response = await fetch(
    `${TIKTOK_API_BASE}/v2/post/publish/status/fetch/`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json; charset=UTF-8",
      },
      body: JSON.stringify({ publish_id: publishId }),
    }
  );

  const data = await response.json();
  return data?.data?.status || "UNKNOWN";
}

// --- Hoofd-functie ---
async function postToTikTok(options: TikTokPostOptions) {
  const accessToken = process.env.TIKTOK_ACCESS_TOKEN;

  if (!accessToken) {
    console.error(
      "❌ TIKTOK_ACCESS_TOKEN ontbreekt in .env\n" +
        "   Zie TIKTOK-SETUP.md voor instructies om de access token op te halen."
    );
    process.exit(1);
  }

  if (!fs.existsSync(options.videoPath)) {
    console.error(`❌ Video niet gevonden: ${options.videoPath}`);
    process.exit(1);
  }

  const videoSizeBytes = fs.statSync(options.videoPath).size;
  console.log(
    `📤 Video posten naar TikTok @seniorease...`
  );
  console.log(`   Bestand: ${path.basename(options.videoPath)} (${(videoSizeBytes / 1024 / 1024).toFixed(1)} MB)`);
  console.log(`   Titel:   ${options.title}`);

  // Stap 1: Init upload
  console.log("\n1️⃣  Upload initiëren...");
  const initData = await initVideoUpload(accessToken, videoSizeBytes);

  if (initData.error?.code !== "ok") {
    console.error("❌ Init mislukt:", JSON.stringify(initData.error, null, 2));
    console.error(
      "\n💡 Mogelijke oorzaken:\n" +
        "   - Access token verlopen (gebruik TIKTOK-SETUP.md om nieuwe te halen)\n" +
        "   - Content Posting API niet goedgekeurd\n" +
        "   - Dagelijks postlimiet bereikt (max 2 posts/dag)"
    );
    process.exit(1);
  }

  const { publish_id, upload_url } = initData.data;
  console.log(`   ✅ Publish ID: ${publish_id}`);

  // Stap 2: Upload video
  console.log("2️⃣  Video uploaden...");
  await uploadVideoChunk(upload_url, options.videoPath, videoSizeBytes);
  console.log("   ✅ Upload gelukt");

  // Stap 3: Wacht op publicatie
  console.log("3️⃣  Wachten op publicatie...");
  let status = "PROCESSING_UPLOAD";
  let attempts = 0;

  while (
    (status === "PROCESSING_UPLOAD" || status === "PROCESSING_DOWNLOAD") &&
    attempts < 20
  ) {
    await new Promise((r) => setTimeout(r, 3000)); // wacht 3 seconden
    status = await checkPublishStatus(accessToken, publish_id);
    process.stdout.write(`   Status: ${status}\r`);
    attempts++;
  }

  console.log("");

  if (status === "PUBLISH_COMPLETE") {
    console.log("\n🎉 Video succesvol gepost op @seniorease!");
    console.log(
      "   Bekijk je video op: https://www.tiktok.com/@seniorease"
    );
  } else {
    console.warn(`\n⚠️  Onverwachte status: ${status}`);
    console.warn("   Controleer de TikTok app of het dashboard.");
  }
}

// --- CLI argument parsing ---
function getArg(flag: string, defaultValue?: string): string | undefined {
  const args = process.argv.slice(2);
  const index = args.indexOf(flag);
  if (index !== -1 && args[index + 1]) return args[index + 1];
  return defaultValue;
}

const videoPath = getArg("--file");
const title = getArg("--title") ?? "Nooit meer een boek dubbel kopen 📚 #boeken #senioren #bibliotheek";

if (!videoPath) {
  // Zoek naar meest recente render in output/
  const outputDir = path.join(__dirname, "..", "output");
  if (fs.existsSync(outputDir)) {
    const files = fs.readdirSync(outputDir)
      .filter((f) => f.endsWith(".mp4"))
      .sort()
      .reverse();

    if (files.length > 0) {
      console.log(`📂 Geen --file opgegeven. Gebruik meest recente: ${files[0]}`);
      postToTikTok({ videoPath: path.join(outputDir, files[0]), title });
    } else {
      console.error("❌ Geen video gevonden. Render eerst: npm run render:all");
      process.exit(1);
    }
  } else {
    console.error("❌ Gebruik: npm run post:tiktok -- --file output/video.mp4 --title \"Tekst\"");
    process.exit(1);
  }
} else {
  postToTikTok({ videoPath: path.resolve(videoPath), title });
}
