/**
 * Instagram Auto-Publish Script voor SeniorEase
 *
 * Werkt via de Meta Graph API (Instagram Content Publishing API).
 * Video's moeten publiek toegankelijk zijn via een URL.
 *
 * Vereisten:
 *   1. Instagram Professional account (Creator of Business)
 *   2. Facebook App met Instagram Basic Display + Content Publishing API
 *   3. Langlevende access token (60 dagen, via Meta for Developers)
 *   4. .env invullen: INSTAGRAM_ACCESS_TOKEN + INSTAGRAM_USER_ID
 *
 * Video-URL: video's staan op https://seniorease.eu/videos/<bestandsnaam>
 *
 * Gebruik:
 *   npm run post:instagram -- --url "https://seniorease.eu/videos/video.mp4" --caption "Tekst #hashtags"
 */

import dotenv from "dotenv";
import path from "path";

dotenv.config({ path: path.join(__dirname, "..", ".env") });

const GRAPH_API = "https://graph.instagram.com/v22.0";

interface InstagramPostOptions {
  videoUrl: string;
  caption: string;
}

// Stap 1: Maak een media container aan
async function createMediaContainer(
  userId: string,
  accessToken: string,
  videoUrl: string,
  caption: string
): Promise<string> {
  const params = new URLSearchParams({
    media_type: "REELS",
    video_url: videoUrl,
    caption,
    share_to_feed: "true",
    access_token: accessToken,
  });

  const response = await fetch(`${GRAPH_API}/${userId}/media`, {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: params.toString(),
  });

  const data = await response.json() as any;

  if (!response.ok || data.error) {
    throw new Error(
      `Container aanmaken mislukt: ${JSON.stringify(data.error || data)}`
    );
  }

  return data.id;
}

// Stap 2: Wacht tot de video verwerkt is
async function waitForContainer(
  containerId: string,
  accessToken: string,
  maxAttempts = 20
): Promise<void> {
  for (let i = 0; i < maxAttempts; i++) {
    await new Promise((r) => setTimeout(r, 5000));

    const response = await fetch(
      `${GRAPH_API}/${containerId}?fields=status_code,status&access_token=${accessToken}`
    );
    const data = await response.json() as any;
    const status = data.status_code;

    process.stdout.write(`   Status: ${status}\r`);

    if (status === "FINISHED") return;
    if (status === "ERROR" || status === "EXPIRED") {
      throw new Error(`Container verwerking mislukt: ${status} — ${data.status}`);
    }
  }

  throw new Error("Timeout: container klaar na max pogingen");
}

// Stap 3: Publiceer de container
async function publishContainer(
  userId: string,
  containerId: string,
  accessToken: string
): Promise<string> {
  const params = new URLSearchParams({
    creation_id: containerId,
    access_token: accessToken,
  });

  const response = await fetch(`${GRAPH_API}/${userId}/media_publish`, {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: params.toString(),
  });

  const data = await response.json() as any;

  if (!response.ok || data.error) {
    throw new Error(
      `Publiceren mislukt: ${JSON.stringify(data.error || data)}`
    );
  }

  return data.id;
}

// Hoofd-functie
async function postToInstagram(options: InstagramPostOptions) {
  const accessToken = process.env.INSTAGRAM_ACCESS_TOKEN;
  const userId = process.env.INSTAGRAM_USER_ID;

  if (!accessToken || !userId) {
    console.error(
      "❌ INSTAGRAM_ACCESS_TOKEN of INSTAGRAM_USER_ID ontbreekt in .env\n" +
        "   Zie: https://developers.facebook.com/docs/instagram-api/getting-started"
    );
    process.exit(1);
  }

  console.log(`📸 Video posten naar Instagram @seniorease...`);
  console.log(`   URL:     ${options.videoUrl}`);
  console.log(`   Caption: ${options.caption.slice(0, 60)}...`);

  // Stap 1: Container aanmaken
  console.log("\n1️⃣  Media container aanmaken...");
  const containerId = await createMediaContainer(
    userId,
    accessToken,
    options.videoUrl,
    options.caption
  );
  console.log(`   ✅ Container ID: ${containerId}`);

  // Stap 2: Wacht op verwerking
  console.log("2️⃣  Wachten op videoverwerking...");
  await waitForContainer(containerId, accessToken);
  console.log("\n   ✅ Video verwerkt");

  // Stap 3: Publiceer
  console.log("3️⃣  Publiceren...");
  const mediaId = await publishContainer(userId, containerId, accessToken);
  console.log(`   ✅ Gepubliceerd! Media ID: ${mediaId}`);

  console.log("\n🎉 Video succesvol gepost op Instagram @seniorease!");
  console.log("   Bekijk je video op: https://www.instagram.com/seniorease");
}

// CLI argument parsing
function getArg(flag: string): string | undefined {
  const args = process.argv.slice(2);
  const index = args.indexOf(flag);
  if (index !== -1 && args[index + 1]) return args[index + 1];
  return undefined;
}

const videoUrl = getArg("--url");
const caption = getArg("--caption") ?? "📚 Your books, your peace of mind. SeniorEase Library — scan, track, done. #books #reading #seniors #library";

if (!videoUrl) {
  console.error(
    "❌ Gebruik: npm run post:instagram -- --url \"https://seniorease.eu/videos/video.mp4\" --caption \"Tekst\""
  );
  process.exit(1);
}

postToInstagram({ videoUrl, caption });
