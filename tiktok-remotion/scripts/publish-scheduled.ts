/**
 * Dispatcher voor geplande posts — wordt aangeroepen door GitHub Actions
 *
 * Leest scheduled-posts.json (in de root van het project),
 * vindt de post(s) voor vandaag en stuurt ze naar TikTok en/of Instagram.
 *
 * Omgevingsvariabelen (via GitHub Secrets):
 *   TIKTOK_ACCESS_TOKEN, TIKTOK_CLIENT_KEY, TIKTOK_CLIENT_SECRET
 *   INSTAGRAM_ACCESS_TOKEN, INSTAGRAM_USER_ID
 *
 * Optioneel (via workflow_dispatch):
 *   POST_DATE    — datum overschrijven (YYYY-MM-DD)
 *   POST_PLATFORM — 'tiktok' | 'instagram' | 'both' (default: 'both')
 */

import fs from "fs";
import path from "path";
import dotenv from "dotenv";

dotenv.config({ path: path.join(__dirname, "..", ".env") });

const QUEUE_FILE = path.join(__dirname, "..", "..", "scheduled-posts.json");
const VIDEO_BASE_URL = "https://seniorease.eu/videos/";
const VIDEO_LOCAL_DIR = path.join(__dirname, "..", "..", "website", "videos");
const GRAPH_API = "https://graph.instagram.com/v22.0";
const TIKTOK_API_BASE = "https://open.tiktokapis.com";

interface ScheduledPost {
  id: string;
  scheduledDate: string;
  platforms: string[];
  videoFile: string;
  caption: string;
  status: "pending" | "posted" | "skipped";
}

// --- TikTok posting ---
async function postToTikTok(videoPath: string, caption: string): Promise<void> {
  const accessToken = process.env.TIKTOK_ACCESS_TOKEN;
  if (!accessToken) throw new Error("TIKTOK_ACCESS_TOKEN ontbreekt");
  if (!fs.existsSync(videoPath)) throw new Error(`Video niet gevonden: ${videoPath}`);

  const videoSizeBytes = fs.statSync(videoPath).size;

  // Init upload
  const initBody = JSON.stringify({
    post_info: {
      title: caption.slice(0, 150),
      privacy_level: "PUBLIC_TO_EVERYONE",
      disable_comment: false,
      disable_duet: false,
      disable_stitch: false,
    },
    source_info: {
      source: "FILE_UPLOAD",
      video_size: videoSizeBytes,
      chunk_size: videoSizeBytes,
      total_chunk_count: 1,
    },
  });

  const initRes = await fetch(`${TIKTOK_API_BASE}/v2/post/publish/video/init/`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "Content-Type": "application/json; charset=UTF-8",
    },
    body: initBody,
  }).then((r) => r.json() as any);

  if (initRes.error?.code !== "ok") {
    throw new Error(`TikTok init mislukt: ${JSON.stringify(initRes.error)}`);
  }

  const { publish_id, upload_url } = initRes.data;

  // Upload video
  const videoBuffer = fs.readFileSync(videoPath);
  const uploadRes = await fetch(upload_url, {
    method: "PUT",
    headers: {
      "Content-Type": "video/mp4",
      "Content-Range": `bytes 0-${videoSizeBytes - 1}/${videoSizeBytes}`,
      "Content-Length": String(videoSizeBytes),
    },
    body: videoBuffer,
  });

  if (!uploadRes.ok) {
    throw new Error(`TikTok upload mislukt: ${uploadRes.status}`);
  }

  // Wacht op publicatie
  let status = "PROCESSING_UPLOAD";
  let attempts = 0;
  while ((status === "PROCESSING_UPLOAD" || status === "PROCESSING_DOWNLOAD") && attempts < 20) {
    await new Promise((r) => setTimeout(r, 3000));
    const statusRes = await fetch(`${TIKTOK_API_BASE}/v2/post/publish/status/fetch/`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json; charset=UTF-8",
      },
      body: JSON.stringify({ publish_id }),
    }).then((r) => r.json() as any);
    status = statusRes?.data?.status || "UNKNOWN";
    attempts++;
  }

  if (status !== "PUBLISH_COMPLETE") {
    throw new Error(`TikTok publicatie onverwachte status: ${status}`);
  }
}

// --- Instagram posting ---
async function postToInstagram(videoUrl: string, caption: string): Promise<void> {
  const accessToken = process.env.INSTAGRAM_ACCESS_TOKEN;
  const userId = process.env.INSTAGRAM_USER_ID;
  if (!accessToken || !userId) throw new Error("INSTAGRAM_ACCESS_TOKEN of INSTAGRAM_USER_ID ontbreekt");

  // Container aanmaken
  const containerParams = new URLSearchParams({
    media_type: "REELS",
    video_url: videoUrl,
    caption,
    share_to_feed: "true",
    access_token: accessToken,
  });

  const containerRes = await fetch(`${GRAPH_API}/${userId}/media`, {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: containerParams.toString(),
  }).then((r) => r.json() as any);

  if (containerRes.error) throw new Error(`Instagram container: ${JSON.stringify(containerRes.error)}`);
  const containerId = containerRes.id;

  // Wacht op verwerking
  let containerStatus = "";
  for (let i = 0; i < 24; i++) {
    await new Promise((r) => setTimeout(r, 5000));
    const statusRes = await fetch(
      `${GRAPH_API}/${containerId}?fields=status_code,status&access_token=${accessToken}`
    ).then((r) => r.json() as any);
    containerStatus = statusRes.status_code;
    if (containerStatus === "FINISHED") break;
    if (containerStatus === "ERROR" || containerStatus === "EXPIRED") {
      throw new Error(`Instagram container mislukt: ${containerStatus} — ${statusRes.status}`);
    }
  }

  if (containerStatus !== "FINISHED") throw new Error("Instagram container timeout");

  // Publiceer
  const publishParams = new URLSearchParams({ creation_id: containerId, access_token: accessToken });
  const publishRes = await fetch(`${GRAPH_API}/${userId}/media_publish`, {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: publishParams.toString(),
  }).then((r) => r.json() as any);

  if (publishRes.error) throw new Error(`Instagram publiceren: ${JSON.stringify(publishRes.error)}`);
}

// --- Hoofd ---
async function main() {
  const targetDate = process.env.POST_DATE || new Date().toISOString().slice(0, 10);
  const platformFilter = process.env.POST_PLATFORM || "both";

  console.log(`📅 Datum: ${targetDate}`);
  console.log(`📱 Platform filter: ${platformFilter}`);

  if (!fs.existsSync(QUEUE_FILE)) {
    console.error(`❌ scheduled-posts.json niet gevonden: ${QUEUE_FILE}`);
    process.exit(1);
  }

  const queue = JSON.parse(fs.readFileSync(QUEUE_FILE, "utf-8"));
  const todaysPosts: ScheduledPost[] = queue.posts.filter(
    (p: ScheduledPost) => p.scheduledDate === targetDate && p.status === "pending"
  );

  if (todaysPosts.length === 0) {
    console.log(`ℹ️  Geen posts gepland voor ${targetDate}.`);
    return;
  }

  console.log(`\n📋 ${todaysPosts.length} post(s) gevonden voor vandaag:\n`);

  for (const post of todaysPosts) {
    console.log(`\n▶️  Post: ${post.id} — ${post.videoFile}`);

    const videoPath = path.join(VIDEO_LOCAL_DIR, post.videoFile);
    const videoUrl = `${VIDEO_BASE_URL}${post.videoFile}`;

    const platforms = post.platforms.filter((p) =>
      platformFilter === "both" ? true : p === platformFilter
    );

    for (const platform of platforms) {
      try {
        if (platform === "tiktok") {
          console.log("   📤 TikTok...");
          await postToTikTok(videoPath, post.caption);
          console.log("   ✅ TikTok gepost!");
        } else if (platform === "instagram") {
          console.log("   📸 Instagram...");
          await postToInstagram(videoUrl, post.caption);
          console.log("   ✅ Instagram gepost!");
        }
      } catch (err) {
        console.error(`   ❌ ${platform} mislukt:`, err instanceof Error ? err.message : err);
        process.exitCode = 1;
      }
    }
  }

  console.log("\n🎉 Klaar!");
}

main();
