import { bundle } from "@remotion/bundler";
import { renderMedia, selectComposition } from "@remotion/renderer";
import path from "path";
import fs from "fs";

const OUTPUT_DIR = path.join(__dirname, "..", "output");
const CONTENT_DIR = path.join(__dirname, "..", "content");
const SCRIPTS_CSV = path.join(CONTENT_DIR, "seniorease-tiktok-scripts.csv");
const SCRIPTS_CSV_EN = path.join(CONTENT_DIR, "seniorease-tiktok-scripts-en.csv");
const SCRIPTS_CSV_NL = path.join(CONTENT_DIR, "seniorease-facebook-scripts-nl.csv");

type ScriptRow = {
  id: string;
  hook_text: string;
  body_text: string;
  cta_text: string;
  duration_sec: string;
  visual_suggestion: string;
  caption: string;
  hashtags: string;
};

async function render(compositionId: string, outputFileName: string, inputProps: Record<string, unknown> = {}) {
  console.log(`\n🎬 Rendering: ${compositionId} → ${outputFileName}`);

  // Zorg dat output directory bestaat
  if (!fs.existsSync(OUTPUT_DIR)) {
    fs.mkdirSync(OUTPUT_DIR, { recursive: true });
  }

  // Bundle de Remotion root
  console.log("  📦 Bundling...");
  const bundleLocation = await bundle({
    entryPoint: path.join(__dirname, "..", "src", "index.ts"),
    webpackOverride: (config) => config,
  });

  // Haal de compositie op
  const composition = await selectComposition({
    serveUrl: bundleLocation,
    id: compositionId,
    inputProps,
  });

  const outputPath = path.join(OUTPUT_DIR, outputFileName);

  // Render naar MP4
  console.log("  🎥 Rendering video...");
  await renderMedia({
    composition,
    serveUrl: bundleLocation,
    codec: "h264",
    outputLocation: outputPath,
    inputProps,
  });

  console.log(`  ✅ Klaar: ${outputPath}`);
  return outputPath;
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

function readScriptRows(csvPath: string): ScriptRow[] {
  const raw = fs.readFileSync(csvPath, "utf-8").replace(/\r/g, "");
  const lines = raw.split("\n").filter((l) => l.trim().length > 0);
  if (lines.length < 2) return [];

  const headers = parseCsvLine(lines[0]);
  const rows: ScriptRow[] = [];

  for (let i = 1; i < lines.length; i++) {
    const values = parseCsvLine(lines[i]);
    const row = Object.fromEntries(headers.map((h, idx) => [h, values[idx] ?? ""])) as ScriptRow;
    if (row.id && row.hook_text) rows.push(row);
  }
  return rows;
}

function slugify(input: string): string {
  return input
    .toLowerCase()
    .replace(/[^a-z0-9\s-]/g, "")
    .trim()
    .replace(/\s+/g, "-")
    .replace(/-+/g, "-")
    .slice(0, 42);
}

type Category = "vinyl" | "books" | "games" | "cds";

function detectCategory(row: ScriptRow): Category {
  const text = (row.hook_text + " " + row.body_text + " " + (row.visual_suggestion || "")).toLowerCase();
  if (text.match(/\b(vinyl|record|album|lp|pressing|plaat|platen|turntable)\b/)) return "vinyl";
  if (text.match(/\b(book|novel|read|boek|boeken|lezen|shelf|boekenplank|library)\b/)) return "books";
  if (text.match(/\b(game|console|cartridge|gaming|playstation|nintendo|sega|spel|spellen)\b/)) return "games";
  if (text.match(/\b(cd|disc|streaming|muziek|music)\b/)) return "cds";
  return "vinyl";
}

function featurePropsFromRow(row: ScriptRow, language: "en" | "nl" = "en") {
  const lang = language === "nl";
  const suffix = lang ? "NL" : "";
  const fallbackCta = lang
    ? "Download SeniorEase op Google Play"
    : "Download SeniorEase on Google Play";

  const category = detectCategory(row);

  // Lifestyle openingsafbeelding per categorie
  const lifestyleImage: Record<Category, string> = {
    vinyl: "screenshots/vinyl-lifestyle-1.webp",
    cds:   "screenshots/vinyl-lifestyle-2.webp",
    games: "screenshots/games-lifestyle-1.webp",
    books: "screenshots/books-lifestyle.png",
  };

  // Eerste stap: lifestyle beeld met categorie-relevante tekst
  const lifestyleInstruction: Record<Category, [string, string]> = {
    vinyl: ["That's a collection worth tracking", "Dat is een collectie die het waard is om bij te houden"],
    cds:   ["Your music collection, fully organized", "Je muziekcollectie, volledig georganiseerd"],
    games: ["Every console. Every cartridge.", "Elke console. Elke cartridge."],
    books: ["Your whole shelf, always with you", "Je hele boekenplank, altijd bij je"],
  };

  // Scanstap: categorie-specifieke tekst
  const scanInstruction: Record<Category, [string, string]> = {
    vinyl: ["Scan any record in seconds", "Scan een plaat in seconden"],
    cds:   ["Scan any CD in seconds", "Scan een cd in seconden"],
    games: ["Scan any game in seconds", "Scan een spel in seconden"],
    books: ["Scan any book in seconds", "Scan een boek in seconden"],
  };

  // Bibliotheekstap: categorie-specifieke tekst
  const libraryInstruction: Record<Category, [string, string]> = {
    vinyl: ["See every pressing you own", "Zie elke persing die je hebt"],
    cds:   ["See every album you own", "Elk album dat je bezit, overzichtelijk"],
    games: ["Your full game collection in one list", "Je volledige gamecollectie op één plek"],
    books: ["Know exactly what you own", "Weet precies wat je in huis hebt"],
  };

  const i = lang ? 1 : 0;

  const steps = [
    {
      imagePath: lifestyleImage[category],
      stepNumber: 1,
      instruction: lifestyleInstruction[category][i],
    },
    {
      imagePath: `screenshots/Screenshot1Scan${suffix}.png`,
      stepNumber: 2,
      instruction: scanInstruction[category][i],
    },
    {
      imagePath: `screenshots/Screenshot2Library${suffix}.png`,
      stepNumber: 3,
      instruction: libraryInstruction[category][i],
    },
    {
      imagePath: `screenshots/Screenshot5NoAccount${suffix}.png`,
      stepNumber: 4,
      instruction: lang ? "Geen account. Geen ads. Altijd offline." : "No account. No ads. Always offline.",
    },
  ];

  return {
    hookText: row.hook_text,
    bodyText: row.body_text,
    showSubtitles: true,
    captionVariant: "large" as const,
    steps,
    secondsPerStep: 3,
    backgroundColor: "#0f0f23",
    accentColor: "#8B5E3C",
    ctaText: row.cta_text || fallbackCta,
  };
}

function facebookPropsFromRow(row: ScriptRow) {
  return {
    hookText: row.hook_text,
    problemText: row.body_text || "Dat gebeurt vaker dan je denkt.",
    actionText: "Scan eerst met SeniorEase",
    benefitText: "Zo voorkom je dubbele aankopen.",
    ctaText: row.cta_text || "Download in de Play Store",
    screenshotPath: "shot 2scan.jpeg",
    accentColor: "#8B5E3C",
  };
}

function getFlag(flag: string): string | undefined {
  const args = process.argv.slice(2);
  const index = args.indexOf(flag);
  if (index !== -1 && args[index + 1]) return args[index + 1];
  return undefined;
}

const SLIDESHOW_STYLE = {
  secondsPerSlide: 3,
  backgroundColor: "#1a1a2e",
  accentColor: "#8B5E3C",
};

/** Pure app screenshots (5 slides). */
const APP_SCREENSHOT_SLIDES_EN = {
  slides: [
    {
      imagePath: "screenshots/Screenshot1Scan.png",
      title: "Scan anything in seconds",
      subtitle: "Books, vinyl, DVDs and games",
    },
    {
      imagePath: "screenshots/Screenshot2Library.png",
      title: "Your calm library",
      subtitle: "Everything in one place",
    },
    {
      imagePath: "screenshots/Screenshot3Track.png",
      title: "Track what you own",
      subtitle: "Read, listened, or TBR",
    },
    {
      imagePath: "screenshots/Screenshot4Export.png",
      title: "Export anytime",
      subtitle: "PDF or CSV",
    },
    {
      imagePath: "screenshots/Screenshot5NoAccount.png",
      title: "No account needed",
      subtitle: "Your data stays on your phone",
    },
  ],
  ...SLIDESHOW_STYLE,
};

const APP_SCREENSHOT_SLIDES_NL = {
  slides: [
    {
      imagePath: "screenshots/Screenshot1ScanNL.png",
      title: "Scan in seconden",
      subtitle: "Boeken, vinyl, dvd's en games",
    },
    {
      imagePath: "screenshots/Screenshot2LibraryNL.png",
      title: "Je rustige bibliotheek",
      subtitle: "Alles op één plek",
    },
    {
      imagePath: "screenshots/Screenshot3TrackNL.png",
      title: "Houd bij wat je hebt",
      subtitle: "Gelezen, beluisterd of TBR",
    },
    {
      imagePath: "screenshots/Screenshot4ExportNL.png",
      title: "Exporteer wanneer je wilt",
      subtitle: "PDF of CSV",
    },
    {
      imagePath: "screenshots/Screenshot5NoAccountNL.png",
      title: "Geen account nodig",
      subtitle: "Je data blijft op je telefoon",
    },
  ],
  ...SLIDESHOW_STYLE,
};

/** Lifestyle foto + app screenshots — sterker op TikTok/IG. */
const MIXED_SLIDESHOWS: Record<string, typeof APP_SCREENSHOT_SLIDES_EN> = {
  "mixed-vinyl-en": {
    slides: [
      {
        imagePath: "screenshots/vinyl-lifestyle-1.webp",
        title: "Into vinyl collecting?",
        subtitle: "SeniorEase tracks your records too",
      },
      {
        imagePath: "screenshots/Screenshot1Scan.png",
        title: "Scan any record in seconds",
        subtitle: "At home or at the record fair",
      },
      {
        imagePath: "screenshots/Screenshot2Library.png",
        title: "See every album you own",
        subtitle: "Sorted and searchable",
      },
      {
        imagePath: "screenshots/vinyl-lifestyle-2.webp",
        title: "Never buy the same LP twice",
        subtitle: "Check before you buy",
      },
      {
        imagePath: "screenshots/Screenshot5NoAccount.png",
        title: "No account needed",
        subtitle: "Your collection stays private",
      },
    ],
    ...SLIDESHOW_STYLE,
  },
  "mixed-vinyl-nl": {
    slides: [
      {
        imagePath: "screenshots/vinyl-lifestyle-1.webp",
        title: "Verzamel je vinyl?",
        subtitle: "SeniorEase houdt je platen bij",
      },
      {
        imagePath: "screenshots/Screenshot1ScanNL.png",
        title: "Scan een plaat in seconden",
        subtitle: "Thuis of op de platenbeurs",
      },
      {
        imagePath: "screenshots/Screenshot2LibraryNL.png",
        title: "Elk album dat je hebt",
        subtitle: "Overzichtelijk en doorzoekbaar",
      },
      {
        imagePath: "screenshots/vinyl-lifestyle-2.webp",
        title: "Nooit dezelfde LP twee keer",
        subtitle: "Check eerst, koop daarna",
      },
      {
        imagePath: "screenshots/Screenshot5NoAccountNL.png",
        title: "Geen account nodig",
        subtitle: "Je collectie blijft privé",
      },
    ],
    ...SLIDESHOW_STYLE,
  },
  "mixed-books-en": {
    slides: [
      {
        imagePath: "screenshots/books-lifestyle.png",
        title: "Book lover?",
        subtitle: "Keep your shelf in your pocket",
      },
      {
        imagePath: "screenshots/Screenshot1Scan.png",
        title: "Scan any book in seconds",
        subtitle: "ISBN barcode on the back",
      },
      {
        imagePath: "screenshots/Screenshot2Library.png",
        title: "Your whole library",
        subtitle: "Always with you",
      },
      {
        imagePath: "screenshots/Screenshot3Track.png",
        title: "Track read and TBR",
        subtitle: "Know what you own",
      },
      {
        imagePath: "screenshots/Screenshot5NoAccount.png",
        title: "No account needed",
        subtitle: "Offline and private",
      },
    ],
    ...SLIDESHOW_STYLE,
  },
  "mixed-books-nl": {
    slides: [
      {
        imagePath: "screenshots/books-lifestyle.png",
        title: "Boekenliefhebber?",
        subtitle: "Je boekenkast in je zak",
      },
      {
        imagePath: "screenshots/Screenshot1ScanNL.png",
        title: "Scan elk boek in seconden",
        subtitle: "Barcode op de achterkant",
      },
      {
        imagePath: "screenshots/Screenshot2LibraryNL.png",
        title: "Je hele bibliotheek",
        subtitle: "Altijd bij de hand",
      },
      {
        imagePath: "screenshots/Screenshot3TrackNL.png",
        title: "Gelezen en TBR bijhouden",
        subtitle: "Weet wat je hebt",
      },
      {
        imagePath: "screenshots/Screenshot5NoAccountNL.png",
        title: "Geen account nodig",
        subtitle: "Offline en privé",
      },
    ],
    ...SLIDESHOW_STYLE,
  },
  "mixed-games-en": {
    slides: [
      {
        imagePath: "screenshots/games-lifestyle-1.webp",
        title: "Game collector?",
        subtitle: "Every console, one list",
      },
      {
        imagePath: "screenshots/Screenshot1Scan.png",
        title: "Scan any game case",
        subtitle: "Added in seconds",
      },
      {
        imagePath: "screenshots/Screenshot2Library.png",
        title: "Your full collection",
        subtitle: "Sorted by title or platform",
      },
      {
        imagePath: "screenshots/games-lifestyle-2.webp",
        title: "Already own it?",
        subtitle: "Check before you buy",
      },
      {
        imagePath: "screenshots/Screenshot5NoAccount.png",
        title: "No account needed",
        subtitle: "Data stays on your phone",
      },
    ],
    ...SLIDESHOW_STYLE,
  },
};

async function main() {
  const arg = process.argv[2] || "all";
  const limit = Number.parseInt(process.argv[3] || "", 10);

  try {
    const ts = new Date().toISOString().slice(0, 10);

    if (arg === "screenshots-slideshow" || arg === "screenshots-slideshow-en") {
      await render(
        "AppScreenshotsSlideshow",
        `seniorease-screenshots-slideshow-en-${ts}.mp4`,
        APP_SCREENSHOT_SLIDES_EN
      );
    }

    if (arg === "screenshots-slideshow-nl") {
      await render(
        "AppScreenshotsSlideshow",
        `seniorease-screenshots-slideshow-nl-${ts}.mp4`,
        APP_SCREENSHOT_SLIDES_NL
      );
    }

    if (arg.startsWith("screenshots-mixed-")) {
      const preset = MIXED_SLIDESHOWS[arg.replace("screenshots-", "")];
      if (!preset) {
        throw new Error(
          `Onbekende mix: ${arg}. Beschikbaar: ${Object.keys(MIXED_SLIDESHOWS)
            .map((k) => `screenshots-${k}`)
            .join(", ")}`
        );
      }
      const lang = arg.includes("-nl") ? "nl" : "en";
      const theme = arg.replace("screenshots-mixed-", "").replace(/-en$|-nl$/, "");
      await render(
        "AppScreenshotsSlideshow",
        `seniorease-slideshow-mixed-${theme}-${lang}-${ts}.mp4`,
        preset
      );
    }

    if (arg === "slideshow" || arg === "all") {
      await render("ScreenshotSlideshow", `seniorease-slideshow-${ts}.mp4`);
    }

    if (arg === "feature" || arg === "all") {
      await render("FeatureDemo", `seniorease-feature-demo-${ts}.mp4`);
    }

    if (arg === "heart" || arg === "all") {
      await render("HeartTransformation", `seniorease-heart-animatie-${ts}.mp4`);
    }

    if (arg === "doublebuy" || arg === "all") {
      await render("DoubleBuyVideo", `seniorease-doublebuy-${ts}.mp4`);
    }

    if (arg === "vinyl" || arg === "all") {
      await render("VinylSlideshow", `seniorease-vinyl-${ts}.mp4`);
    }

    if (arg === "games" || arg === "all") {
      await render("GamesSlideshow", `seniorease-games-${ts}.mp4`);
    }

    if (arg === "playstore" || arg === "all") {
      await render("PlayStoreSlideshow", `seniorease-playstore-${ts}.mp4`);
    }

    if (arg === "nowonplaystore" || arg === "all") {
      await render("NowOnPlayStore", `seniorease-nowonplaystore-${ts}.mp4`);
    }

    if (arg === "product-demo") {
      await render("ProductDemo", `seniorease-product-demo-launch-${ts}.mp4`);
    }

    if (arg === "facebook-calm") {
      await render("FacebookCalmDemo", `seniorease-facebook-calm-${ts}.mp4`);
    }

    if (arg === "dad-record-fair") {
      await render("DadRecordFairVideo", `seniorease-dad-record-fair-en-${ts}.mp4`);
    }

    if (arg === "scripts" || arg === "scripts-en" || arg === "scripts-nl") {
      const mode = arg === "scripts-nl" ? "nl" : "en";
      const customCsv = getFlag("--csv");
      const csvPath = customCsv
        ? path.resolve(__dirname, "..", customCsv)
        : arg === "scripts-nl" ? SCRIPTS_CSV_NL : arg === "scripts-en" ? SCRIPTS_CSV_EN : SCRIPTS_CSV;

      if (!fs.existsSync(csvPath)) {
        throw new Error(`CSV not found: ${csvPath}`);
      }
      const idFilter = getFlag("--id");
      const rows = readScriptRows(csvPath);
      if (rows.length === 0) {
        throw new Error("No script rows found in CSV.");
      }

      let selected: ScriptRow[];
      if (idFilter) {
        selected = rows.filter((r) => r.id === idFilter);
        if (selected.length === 0) {
          throw new Error(`Script id "${idFilter}" not found in ${path.basename(csvPath)}`);
        }
      } else if (Number.isFinite(limit) && limit > 0) {
        selected = rows.slice(0, limit);
      } else {
        selected = rows;
      }
      console.log(`\n🧾 Rendering ${selected.length} ${mode.toUpperCase()} script videos from: ${path.relative(process.cwd(), csvPath)}`);

      for (const row of selected) {
        const slug = slugify(row.hook_text);
        const fileName = `seniorease-script-${mode}-${row.id}-${slug}-${ts}.mp4`;
        await render("FeatureDemo", fileName, featurePropsFromRow(row, mode as "en" | "nl"));
      }
    }

    if (arg === "facebook-test5") {
      if (!fs.existsSync(SCRIPTS_CSV_NL)) {
        throw new Error(`CSV not found: ${SCRIPTS_CSV_NL}`);
      }
      const rows = readScriptRows(SCRIPTS_CSV_NL).slice(0, 5);
      console.log(`\n🧾 Rendering ${rows.length} Facebook calm NL test videos...`);
      for (const row of rows) {
        const slug = slugify(row.hook_text);
        const fileName = `seniorease-facebook-nl-calm-${row.id}-${slug}-${ts}.mp4`;
        await render("FacebookCalmDemo", fileName, facebookPropsFromRow(row));
      }
    }

    if (arg === "facebook-ab") {
      const variants = [
        {
          name: "2a",
          props: {
            hookText: "Heb ik dit boek al gelezen?",
            problemText: "In de winkel twijfel je snel.",
            actionText: "Scan het boek in SeniorEase",
            benefitText: "Je ziet direct of je het al hebt.",
            ctaText: "Download in de Play Store",
            screenshotPath: "shot 2scan.jpeg",
            accentColor: "#8B5E3C",
          },
        },
        {
          name: "2b",
          props: {
            hookText: "In de winkel dacht ik: heb ik dit al?",
            problemText: "Dat moment kent bijna elke lezer.",
            actionText: "Even scannen met SeniorEase",
            benefitText: "Geen dubbele aankopen meer.",
            ctaText: "Probeer gratis tot 10 items",
            screenshotPath: "shot 2scan.jpeg",
            accentColor: "#8B5E3C",
          },
        },
        {
          name: "4a",
          props: {
            hookText: "Boeken, vinyl, dvd's en games?",
            problemText: "Losse lijstjes maken het onoverzichtelijk.",
            actionText: "Houd alles bij in één app",
            benefitText: "Rust en overzicht op je telefoon.",
            ctaText: "Download in de Play Store",
            screenshotPath: "shot3library.png",
            accentColor: "#8B5E3C",
          },
        },
        {
          name: "4b",
          props: {
            hookText: "Eén app voor je hele collectie.",
            problemText: "Geen aparte apps of losse notities meer.",
            actionText: "Boeken, vinyl, dvd's en games samen",
            benefitText: "Snel zoeken, filteren en terugvinden.",
            ctaText: "Start gratis met SeniorEase",
            screenshotPath: "shot 4library.jpeg",
            accentColor: "#8B5E3C",
          },
        },
      ];

      for (const variant of variants) {
        const fileName = `seniorease-facebook-nl-calm-${variant.name}-${ts}.mp4`;
        await render("FacebookCalmDemo", fileName, variant.props);
      }
    }

    console.log("\n🎉 Alle videos zijn gerenderd in: output/");
    console.log("   Je kunt ze nu uploaden naar TikTok of via het post script posten.\n");
  } catch (err) {
    console.error("❌ Render mislukt:", err);
    process.exit(1);
  }
}

main();
