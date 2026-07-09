import { google } from "googleapis";
import path from "path";

const CREDENTIALS_PATH = path.join(
  process.env.USERPROFILE || process.env.HOME || "",
  ".claude",
  "google-credentials.json"
);

const SPREADSHEET_ID = "1hHc03gXMRpFd2zQr48fQVP4ohF2_eh2ErBsROuryYEQ";

const SCHEDULE = [
  ["Datum", "Video", "Bestand", "Caption", "Status"],
  [
    "10 april",
    "VinylSlideshow",
    "seniorease-vinyl-2026-04-03.mp4",
    "Into vinyl records? 🎵 Never accidentally buy the same LP twice. Scan the barcode — done. Try free 👉 seniorease.eu #vinylrecords #vinylcollection #recordcollector #seniorease #android",
    "✅ Gepost",
  ],
  [
    "11 april",
    "GamesSlideshow",
    "seniorease-games-2026-04-03.mp4",
    "Got a board game collection? 🎮 Never buy a game you already own. Scan the barcode — done. Try free 👉 seniorease.eu #boardgames #boardgamecollection #tabletopgames #seniorease #android",
    "✅ Gepost",
  ],
  [
    "12 april",
    "ScreenshotSlideshow",
    "seniorease-slideshow-2026-04-03.mp4",
    "Books, music, DVDs and games — all in one app 📚🎵📀🎮 Your entire collection, always with you. Try free 👉 seniorease.eu #bookcollection #dvdcollection #seniorease #android #nosubscription",
    "",
  ],
  [
    "13 april",
    "DoubleBuyVideo",
    "seniorease-doublebuy-2026-04-03.mp4",
    "Stop buying the same book twice 😅 It happens to everyone. There's a fix. Try free 👉 seniorease.eu #books #booklover #bookworm #seniorease #android",
    "",
  ],
  [
    "14 april",
    "FeatureDemo",
    "seniorease-feature-demo-2026-04-03.mp4",
    "Add anything to your collection in 2 seconds 📱 Books, CDs, DVDs, games — just scan the barcode. Try free 👉 seniorease.eu #lifehack #organization #seniorease #android",
    "",
  ],
  [
    "15 april",
    "HeartTransformation",
    "seniorease-heart-animatie-2026-04-03.mp4",
    "Accidentally buying the same book again... 😔 Not anymore. Try free 👉 seniorease.eu #books #seniorliving #seniorease #android #organization",
    "",
  ],
  [
    "16 april",
    "PlayStoreSlideshow",
    "seniorease-playstore-2026-04-10.mp4",
    "Books, music, DVDs and games — all in one place 📚🎵📀🎮 See what SeniorEase Library can do for you. No account. No subscription. Just your collection — always with you. Try it free 👉 seniorease.eu #seniorease #bookcollection #vinylrecords #dvdcollection #boardgames #android #nosubscription",
    "",
  ],
];

async function main() {
  const auth = new google.auth.GoogleAuth({
    keyFile: CREDENTIALS_PATH,
    scopes: [
      "https://www.googleapis.com/auth/spreadsheets",
      "https://www.googleapis.com/auth/drive",
    ],
  });

  const sheets = google.sheets({ version: "v4", auth });

  const spreadsheetId = SPREADSHEET_ID;
  const spreadsheetUrl = `https://docs.google.com/spreadsheets/d/${spreadsheetId}`;

  // Vul de data in
  console.log("✏️  Data invullen...");
  await sheets.spreadsheets.values.update({
    spreadsheetId,
    range: "A1",
    valueInputOption: "RAW",
    requestBody: {
      values: SCHEDULE,
    },
  });

  // Maak de header vet en kolommen breder
  await sheets.spreadsheets.batchUpdate({
    spreadsheetId,
    requestBody: {
      requests: [
        {
          repeatCell: {
            range: { sheetId: 0, startRowIndex: 0, endRowIndex: 1 },
            cell: {
              userEnteredFormat: {
                textFormat: { bold: true },
                backgroundColor: { red: 0.2, green: 0.4, blue: 0.8 },
              },
            },
            fields: "userEnteredFormat(textFormat,backgroundColor)",
          },
        },
        {
          updateDimensionProperties: {
            range: { sheetId: 0, dimension: "COLUMNS", startIndex: 3, endIndex: 4 },
            properties: { pixelSize: 500 },
            fields: "pixelSize",
          },
        },
      ],
    },
  });

  console.log("\n✅ Klaar!");
  console.log(`📊 Open je sheet hier: ${spreadsheetUrl}`);
}

main().catch((err) => {
  console.error("❌ Mislukt:", err.message);
  process.exit(1);
});
