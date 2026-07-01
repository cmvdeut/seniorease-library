/**
 * Upload lokale MP4 naar Blotato — gedeeld door post-blotato en daily-pipeline.
 */

import fs from "fs";
import path from "path";

const BLOTATO_API = "https://backend.blotato.com/v2";

export async function uploadVideoToBlotato(
  apiKey: string,
  videoPath: string
): Promise<string> {
  const fileName = path.basename(videoPath);
  const fileSize = fs.statSync(videoPath).size;

  console.log(
    `   📤 Uploaden naar Blotato: ${fileName} (${(fileSize / 1024 / 1024).toFixed(1)} MB)...`
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

  const uploadInit = (await initRes.json()) as {
    presignedUrl: string;
    publicUrl: string;
  };
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

  console.log(`   ✅ Blotato media URL: ${publicUrl}`);
  return publicUrl;
}
