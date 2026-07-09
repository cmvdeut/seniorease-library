/**
 * TikTok OAuth token ophalen voor @seniorease
 * Gebruik: node get-tiktok-token.mjs
 */
import { createServer } from "http";
import { createHash, randomBytes } from "crypto";

const CLIENT_KEY = "sbaweqqmh0vzuk1jhz";
const CLIENT_SECRET = "eL1DkE2A7O1ZxOXVqaVitLJGxUYg2hZi";
const REDIRECT_URI = "http://localhost:3000/callback";
const PORT = 3000;

// PKCE genereren
const codeVerifier = randomBytes(32).toString("base64url");
const codeChallenge = createHash("sha256")
  .update(codeVerifier)
  .digest("base64url");

const authUrl =
  `https://www.tiktok.com/v2/auth/authorize/?` +
  `client_key=${CLIENT_KEY}` +
  `&scope=video.publish,video.upload` +
  `&response_type=code` +
  `&redirect_uri=${encodeURIComponent(REDIRECT_URI)}` +
  `&state=seniorease` +
  `&code_challenge=${codeChallenge}` +
  `&code_challenge_method=S256`;

console.log("\n=== TikTok Token Setup voor @seniorease ===\n");
console.log("1. Zorg dat je in je browser ingelogd bent als @seniorease");
console.log("2. Open deze URL:\n");
console.log(authUrl);
console.log("\n3. Geef toestemming — je wordt teruggestuurd naar localhost");
console.log("   (wachten op callback...)\n");

const server = createServer(async (req, res) => {
  const url = new URL(req.url, `http://localhost:${PORT}`);
  const code = url.searchParams.get("code");
  const error = url.searchParams.get("error");

  if (error) {
    res.end(`Fout: ${error} — ${url.searchParams.get("error_string")}`);
    console.error("❌ OAuth fout:", error);
    server.close();
    return;
  }

  if (!code) {
    res.end("Wachten op code...");
    return;
  }

  console.log("✅ Code ontvangen! Token ophalen...\n");
  res.end("<h2>✅ Code ontvangen! Tokens worden opgehaald... Bekijk de terminal.</h2>");

  // Wissel code in voor tokens
  const body = new URLSearchParams({
    client_key: CLIENT_KEY,
    client_secret: CLIENT_SECRET,
    code,
    grant_type: "authorization_code",
    redirect_uri: REDIRECT_URI,
    code_verifier: codeVerifier,
  });

  const tokenRes = await fetch("https://open.tiktokapis.com/v2/oauth/token/", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body,
  });

  const data = await tokenRes.json();

  if (data.access_token) {
    console.log("🎉 Tokens opgehaald!\n");
    console.log("=== Kopieer dit naar GitHub Secrets ===\n");
    console.log(`TIKTOK_ACCESS_TOKEN=${data.access_token}`);
    console.log(`TIKTOK_REFRESH_TOKEN=${data.refresh_token}`);
    console.log(`TIKTOK_CLIENT_KEY=${CLIENT_KEY}`);
    console.log(`TIKTOK_CLIENT_SECRET=${CLIENT_SECRET}`);
    console.log(`TIKTOK_USE_DIRECT_POST=false`);
    console.log(`TIKTOK_PRIVACY_LEVEL=SELF_ONLY`);
    console.log("\n=====================================\n");
  } else {
    console.error("❌ Token ophalen mislukt:", JSON.stringify(data, null, 2));
  }

  server.close();
});

server.listen(PORT, () => {});
