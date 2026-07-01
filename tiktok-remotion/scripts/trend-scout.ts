/**
 * Trend Scout — Agent 1
 *
 * Haalt trending posts op van Reddit RSS-feeds voor SeniorEase-relevante subreddits.
 * Als Reddit niet bereikbaar is, roteert automatisch over een curated terugvallijst.
 *
 * Output: content/trends-DATUM.json + print naar stdout
 *
 * Gebruik:
 *   npm run agent:scout
 */

import fs from "fs";
import path from "path";
import dotenv from "dotenv";

dotenv.config({ path: path.join(__dirname, "..", ".env") });

export interface TrendTopic {
  title: string;
  subreddit: string;
  score: number;
  url: string;
  created_utc: number;
}

// Terugvallijst: evergreen SeniorEase-topics, roteert dagelijks
const FALLBACK_TOPICS: TrendTopic[] = [
  { title: "Bought the same vinyl record twice at a record fair", subreddit: "fallback", score: 100, url: "https://seniorease.eu", created_utc: 0 },
  { title: "How do you keep track of 500+ books in your collection?", subreddit: "fallback", score: 95, url: "https://seniorease.eu", created_utc: 0 },
  { title: "Found duplicates while sorting through my DVD collection", subreddit: "fallback", score: 90, url: "https://seniorease.eu", created_utc: 0 },
  { title: "Best way to organize a large CD collection without spreadsheets", subreddit: "fallback", score: 88, url: "https://seniorease.eu", created_utc: 0 },
  { title: "Scanning barcodes to catalog vintage board game collection", subreddit: "fallback", score: 85, url: "https://seniorease.eu", created_utc: 0 },
  { title: "Charity shop finds: how I stopped buying books I already own", subreddit: "fallback", score: 82, url: "https://seniorease.eu", created_utc: 0 },
  { title: "Record store tips: check your collection before you buy", subreddit: "fallback", score: 80, url: "https://seniorease.eu", created_utc: 0 },
];

function todaysFallback(): TrendTopic[] {
  const dayOfYear = Math.floor((Date.now() - new Date(new Date().getFullYear(), 0, 0).getTime()) / 86400000);
  const idx = dayOfYear % FALLBACK_TOPICS.length;
  const today = { ...FALLBACK_TOPICS[idx], created_utc: Math.floor(Date.now() / 1000) };
  return [today];
}

async function fetchSubredditRss(sub: string): Promise<TrendTopic[]> {
  const url = `https://www.reddit.com/r/${sub}/hot.json?limit=10&raw_json=1`;
  try {
    const res = await fetch(url, {
      headers: {
        "User-Agent": "Mozilla/5.0 (compatible; SeniorEaseBot/1.0; +https://seniorease.eu)",
        "Accept": "application/json",
      },
    });

    if (!res.ok) return [];

    const json = (await res.json()) as any;
    const cutoff = Math.floor(Date.now() / 1000) - 48 * 3600;
    const posts: TrendTopic[] = [];

    for (const child of json?.data?.children ?? []) {
      const p = child.data;
      if (p.stickied || p.created_utc < cutoff) continue;
      posts.push({
        title: p.title,
        subreddit: sub,
        score: p.score,
        url: `https://reddit.com${p.permalink}`,
        created_utc: p.created_utc,
      });
    }
    return posts;
  } catch {
    return [];
  }
}

export async function scout(): Promise<TrendTopic[]> {
  console.log("🔍 Trend Scout — Reddit scannen...\n");

  const SUBREDDITS = ["vinyl", "books", "dvdcollection", "retrogaming", "cdcollecting"];
  const allPosts: TrendTopic[] = [];

  for (const sub of SUBREDDITS) {
    const posts = await fetchSubredditRss(sub);
    if (posts.length > 0) {
      console.log(`  r/${sub}: ${posts.length} recente posts`);
      allPosts.push(...posts);
    }
  }

  let top: TrendTopic[];

  if (allPosts.length > 0) {
    top = allPosts.sort((a, b) => b.score - a.score).slice(0, 5);
    console.log(`\n✅ ${top.length} trending topics via Reddit:`);
  } else {
    console.log("  Reddit niet bereikbaar — curated terugvallijst wordt gebruikt.");
    top = todaysFallback();
    console.log(`\n✅ Vandaag's curated topic:`);
  }

  top.forEach((t, i) => {
    const bron = t.subreddit === "fallback" ? "curated" : `r/${t.subreddit}`;
    console.log(`  ${i + 1}. [${bron}] "${t.title}"`);
  });

  // Sla op als JSON
  const datum = new Date().toISOString().slice(0, 10);
  const outDir = path.join(__dirname, "..", "content");
  const outFile = path.join(outDir, `trends-${datum}.json`);
  fs.writeFileSync(outFile, JSON.stringify(top, null, 2));
  console.log(`\n💾 Opgeslagen: content/trends-${datum}.json`);

  return top;
}

// Direct uitvoeren als hoofdscript
if (require.main === module) {
  scout().catch((err) => {
    console.error("❌ Trend Scout mislukt:", err.message);
    process.exit(1);
  });
}
