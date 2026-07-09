const fs = require("fs");

function parseCsv(text) {
  const rows = [];
  let i = 0;
  let field = "";
  let row = [];
  let inQ = false;
  while (i < text.length) {
    const c = text[i];
    if (inQ) {
      if (c === '"' && text[i + 1] === '"') {
        field += '"';
        i += 2;
        continue;
      }
      if (c === '"') {
        inQ = false;
        i++;
        continue;
      }
      field += c;
      i++;
      continue;
    }
    if (c === '"') {
      inQ = true;
      i++;
      continue;
    }
    if (c === ",") {
      row.push(field);
      field = "";
      i++;
      continue;
    }
    if (c === "\n" || c === "\r") {
      if (c === "\r" && text[i + 1] === "\n") i++;
      row.push(field);
      if (row.some((x) => x !== "")) rows.push(row);
      row = [];
      field = "";
      i++;
      continue;
    }
    field += c;
    i++;
  }
  if (field || row.length) {
    row.push(field);
    rows.push(row);
  }
  const headers = rows[0];
  return rows.slice(1).map((r) =>
    Object.fromEntries(headers.map((h, idx) => [h, r[idx] || ""]))
  );
}

const months = {
  januari: 0,
  februari: 1,
  maart: 2,
  april: 3,
  mei: 4,
  juni: 5,
  juli: 6,
  augustus: 7,
  september: 8,
  oktober: 9,
  november: 10,
  december: 11,
};

function parsePostDate(s) {
  const m = (s || "").trim().match(/^(\d{1,2})\s+(\w+)$/i);
  if (!m) return null;
  const day = +m[1];
  const mon = months[m[2].toLowerCase()];
  if (mon == null) return null;
  return new Date(2026, mon, day);
}

const cutoff = new Date(2026, 5, 8);
const exportDate = new Date(2026, 5, 22);

function analyze(file, account) {
  const rows = parseCsv(fs.readFileSync(file, "utf8"));
  const all = rows
    .map((r) => ({
      account,
      postDate: parsePostDate(r["Post time"]),
      views: +r["Total views"] || 0,
      likes: +r["Total likes"] || 0,
      comments: +r["Total comments"] || 0,
      shares: +r["Total shares"] || 0,
      title: (r["Video title"] || "").replace(/\s+/g, " ").trim(),
      link: r["Video link"] || "",
    }))
    .filter((r) => r.postDate);

  const recent = all.filter(
    (r) => r.postDate >= cutoff && r.postDate <= exportDate
  );
  recent.forEach((r) => {
    r.engagement = r.likes + r.comments + r.shares;
    r.rate = r.views ? ((r.engagement / r.views) * 100).toFixed(2) : "0";
    r.hook = r.title.slice(0, 75) || "(geen titel)";
  });
  recent.sort((a, b) => b.views - a.views || b.engagement - a.engagement);
  return { all: all.length, recent };
}

const se = analyze(
  "c:/Users/cmvde/Downloads/Content_seniorease/Content.csv",
  "@seniorease"
);
const sh = analyze(
  "c:/Users/cmvde/Downloads/Content_shelfieease/Content.csv",
  "@shelfieease"
);

function printReport(name, data) {
  console.log("\n===== " + name + " =====");
  console.log(
    "Totaal in export:",
    data.all,
    "| Posts 8-22 juni:",
    data.recent.length
  );
  data.recent.forEach((r, i) => {
    console.log(
      i +
        1 +
        ". " +
        r.postDate.toISOString().slice(0, 10) +
        " | views " +
        r.views +
        " | eng " +
        r.engagement +
        " (" +
        r.rate +
        "%)"
    );
    console.log("   " + r.hook);
  });
  if (data.recent.length) {
    const avg = Math.round(
      data.recent.reduce((s, r) => s + r.views, 0) / data.recent.length
    );
    console.log("Gemiddelde views:", avg);
  }
}

printReport("SENIOREASE", se);
printReport("SHELFIEEASE", sh);

const combined = [...se.recent, ...sh.recent];
console.log("\n===== TOP ENGAGEMENT (8-22 juni) =====");
combined
  .sort((a, b) => b.engagement - a.engagement || b.views - a.views)
  .slice(0, 5)
  .forEach((r, i) => {
    console.log(
      i +
        1 +
        ". " +
        r.account +
        " | eng " +
        r.engagement +
        " | views " +
        r.views +
        " | " +
        r.hook.slice(0, 60)
    );
  });
