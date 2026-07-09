---
name: post-scheduler
description: Schedule a finished social post to one or more platforms via the Blotato API. Handles single-platform and multi-platform scheduling, fetches connected accounts, applies a final pre-publish check, and returns scheduled time + post IDs. Triggers on "schedule this," "post this to [platform]," or as the final step in content-coach and post-writer flows. Falls back to saving the post as a copy-paste file if Blotato isn't configured.
argument-hint: "[post text or path] [platform(s)] [optional time]"
allowed-tools: Read, Write, Edit, Glob, AskUserQuestion, Shell
---

# Post Scheduler

You take an approved post and ship it via Blotato. You do NOT write or revise the post — that's `post-writer`'s job. By the time a post reaches you, it's been graded and approved.

If Blotato isn't set up (no API key, no connected account), you fall back gracefully: save the post to a file the user can paste manually. Don't fail the flow.

## When to Activate

- "Schedule this post"
- "Post this to Instagram tomorrow at 9am"
- "Send this to LinkedIn"
- Auto-called as the final step of `content-coach` after user approval.

## Workflow

### Step 1: Get inputs

You need:
1. **Post text** — inline, or path to a file
2. **Platform(s)** — one or more from: instagram, facebook, twitter, linkedin, tiktok, threads, bluesky, youtube
3. **Time** — default `useNextFreeSlot: true`, or a specific ISO timestamp if user specified
4. **Media** — for TikTok/Instagram Reels: local MP4 path or public URL (see project layout below)

If platform is missing, ask. Don't guess.

### Step 1b: Detect project layout

Before scheduling, detect which repo you're in:

| Signal | Project | Env file | Schedule CSV | Post command | Video folder |
|--------|---------|----------|--------------|--------------|--------------|
| `tiktok-remotion/docker-compose.yml` or `tiktok-remotion/scripts/post-blotato.ts` | **SeniorEase Library** | `tiktok-remotion/.env` | `tiktok-remotion/content/*.csv` | `cd tiktok-remotion && docker compose run post-blotato -- --csv content/<file>.csv` | `tiktok-remotion/output/` |
| `scripts/post-blotato.ts` at project root + `package.json` has `post:blotato` | **ShelfieEase BookTok MVP** (`C:\Projects\shelfieease-booktok-mvp`) | `.env.local` or `.env` at root | `content/*.csv` | `npm run post:blotato -- --csv content/<file>.csv` | `public/videos/` |

If neither layout matches, use Blotato MCP (if available) or fall back to Step 5.

Read `brand-brief.md` from the paths in the `brand-brief` skill for voice/CTA context. Do not hardcode brand-specific defaults — pull CTA, language, and audience from the brief.

### Step 2: Final pre-publish check

Before hitting Blotato, scan the post one more time for:

- [ ] Zero em dashes
- [ ] No banned filler ("really," "very," "just," "basically," "literally," "actually")
- [ ] No filler openers ("in today's world," "let me tell you")
- [ ] Active voice
- [ ] Contractions used
- [ ] Hashtag count fits platform (0 for Twitter/Threads/Bluesky/LinkedIn/Facebook, 3-5 for Instagram, max 5 for TikTok)
- [ ] For Instagram/TikTok: media file or URL is attached
- [ ] For LinkedIn: no external links in post body

If anything fails, **stop and report** and wait for explicit user response.

### Step 3: Schedule via Blotato

**Prefer the project's existing API script** (detected in Step 1b) over MCP.

**SeniorEase Library:**
1. Read `tiktok-remotion/.env` for `BLOTATO_API_KEY` (never print the key).
2. Accounts: `BLOTATO_TIKTOK_USERNAME=seniorease`, `BLOTATO_INSTAGRAM_USERNAME=seniorease.library`
3. Append or update a row in `tiktok-remotion/content/blotato-schedule.csv` (or the active week CSV).
4. Run: `cd tiktok-remotion && docker compose run post-blotato -- --csv content/<schedule-file>.csv`
5. CSV columns: see `tiktok-remotion/BLOTATO-REPUBLISH-PLAN.md`

**ShelfieEase BookTok MVP:**
1. Read `.env.local` or `.env` at project root for `BLOTATO_API_KEY` (never print the key).
2. Accounts: `BLOTATO_TIKTOK_USERNAME=shelfieease`, `BLOTATO_INSTAGRAM_USERNAME=shelfieease`
3. Append or update a row in `content/<schedule-file>.csv` (e.g. `content/jun2026-blotato.csv`).
4. Run from project root: `npm run post:blotato -- --csv content/<schedule-file>.csv`
5. Videos must exist in `public/videos/` — `video_filename` in CSV must match.

**If Blotato MCP is available** (Claude Desktop with MCP): use `blotato_list_accounts` and `blotato_create_post` per [Blotato docs](https://help.blotato.com/api/mcp/setup.md).

Platform-specific fields:
- **TikTok**: `privacyLevel` PUBLIC_TO_EVERYONE, video required
- **Instagram**: `mediaUrls` required
- **Facebook**: `pageId` required

### Step 4: Report results

Show confirmation: platform, time, status, post ID. Link: https://my.blotato.com/scheduler

### Step 5: Fallback (no Blotato)

Save to `content/post-ready-to-paste.txt` (ShelfieEase) or `tiktok-remotion/content/post-ready-to-paste.txt` (SeniorEase), or project root if neither folder exists:

```
=== POST FOR [PLATFORM] ===
Scheduled for: [time or manual]

[POST TEXT HERE]

Video: [path if applicable]
=== END POST ===
```

Tell the user they can paste manually or connect Blotato at https://my.blotato.com

## Scheduling defaults (from brand brief)

If `brand-brief.md` exists, use its **Primary CTA** and **Voice** sections. Otherwise ask the user.

General guidance (override per brand):
- **Best posting window:** 19:00–21:00 CET for NL/EU audiences
- **TikTok:** completion + shares beat direct download CTAs for most brands
- **BookTok (ShelfieEase):** aesthetic, trope/TBR hooks; EN primary on TikTok
- **Calm collector (SeniorEase):** privacy/no-account angle; EN on-screen, NL optional for Facebook

## What NOT to Do

- Don't auto-fix voice issues — send back to post-grader.
- Don't post without explicit user approval.
- Don't log API keys.
- Default to scheduling, not publish-now, unless user says "post now".

## Setting Up Blotato (one-time)

1. Account: https://my.blotato.com
2. Connect TikTok + Instagram (+ Facebook if needed)
3. API key in the project's env file:
   - SeniorEase: `tiktok-remotion/.env` → `BLOTATO_API_KEY`
   - ShelfieEase: `.env.local` → `BLOTATO_API_KEY`
4. Docs: https://help.blotato.com/claude-skills/claude-skills.md
