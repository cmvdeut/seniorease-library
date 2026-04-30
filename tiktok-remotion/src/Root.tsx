import React from "react";
import { Composition, Still } from "remotion";
import { ScreenshotSlideshow, slideshowSchema } from "./compositions/ScreenshotSlideshow";
import { FeatureDemo, featureDemoSchema } from "./compositions/FeatureDemo";
import { HeartTransformation, heartTransformSchema } from "./compositions/HeartTransformation";
import { DoubleBuyVideo } from "./compositions/DoubleBuyVideo";
import { PlayStoreScreenshot, playStoreSchema } from "./compositions/PlayStoreScreenshot";
import { NowOnPlayStore } from "./compositions/NowOnPlayStore";

// TikTok vertical format: 1080x1920, 30fps
const TIKTOK_WIDTH = 1080;
const TIKTOK_HEIGHT = 1920;
const FPS = 30;

export const RemotionRoot: React.FC = () => {
  return (
    <>
      {/* === TEMPLATE 1: Screenshot Slideshow ===
          Toont app-screenshots als geanimeerde slideshow.
          Elke slide = 4 seconden. Standaard 5 slides = 150 frames = 5 seconden.
          Aanpasbaar via props. */}
      <Composition
        id="ScreenshotSlideshow"
        component={ScreenshotSlideshow}
        durationInFrames={5 * FPS} // 5 seconden standaard (aanpasbaar)
        fps={FPS}
        width={TIKTOK_WIDTH}
        height={TIKTOK_HEIGHT}
        schema={slideshowSchema}
        defaultProps={{
          slides: [
            {
              imagePath: "start.jpeg",
              title: "Never buy the same thing twice",
              subtitle: "SeniorEase Library",
            },
            {
              imagePath: "shot3library.png",
              title: "Books, music, DVDs & games",
              subtitle: "All in one place",
            },
            {
              imagePath: "shot 2scan.jpeg",
              title: "Scan any barcode",
              subtitle: "Added in 2 seconds",
            },
            {
              imagePath: "shot 4library.jpeg",
              title: "Your entire collection",
              subtitle: "Always organized",
            },
            {
              imagePath: "boekdetail.png",
              title: "Try it for free",
              subtitle: "seniorease.eu",
            },
          ],
          secondsPerSlide: 3,
          backgroundColor: "#1a1a2e",
          accentColor: "#4A90D9",
        }}
      />

      {/* === TEMPLATE 2: Feature Demo ===
          Stap-voor-stap demo van een app-feature.
          Elke stap heeft een screenshot + tekst + animatie. */}
      <Composition
        id="FeatureDemo"
        component={FeatureDemo}
        durationInFrames={16 * FPS} // 16 seconden standaard
        fps={FPS}
        width={TIKTOK_WIDTH}
        height={TIKTOK_HEIGHT}
        schema={featureDemoSchema}
        defaultProps={{
          hookText: "How to add anything to your collection",
          steps: [
            {
              imagePath: "shot 1add book.png",
              stepNumber: 1,
              instruction: "Tap + to add a book, CD, DVD or game",
            },
            {
              imagePath: "shot 2scan.jpeg",
              stepNumber: 2,
              instruction: "Scan the barcode with your camera",
            },
            {
              imagePath: "boekdetail.png",
              stepNumber: 3,
              instruction: "Mark as read, listened or owned",
            },
            {
              imagePath: "shot3library.png",
              stepNumber: 4,
              instruction: "Done! It's in your collection",
            },
          ],
          secondsPerStep: 3,
          backgroundColor: "#0f0f23",
          accentColor: "#4A90D9",
          ctaText: "Try for free \u2192 seniorease.eu",
        }}
      />
      {/* === TEMPLATE 3: Hart-animatie (treurig → blij) ===
          Verhaal: senioren worden droevig van moderne technologie,
          maar SeniorEase maakt het simpel en blij.
          Timeline: 0–3s treurig | 3–5s overgang | 5–10s blij + CTA */}
      <Composition
        id="HeartTransformation"
        component={HeartTransformation}
        durationInFrames={10 * FPS}
        fps={FPS}
        width={TIKTOK_WIDTH}
        height={TIKTOK_HEIGHT}
        schema={heartTransformSchema}
        defaultProps={{
          sadText: "Accidentally buying the same book, CD or DVD again...",
          happyText: "Not anymore.",
          ctaText: "Try for free → seniorease.eu",
        }}
      />
      {/* === TEMPLATE 8: Now on Play Store ===
          Punchline-formaat: hook → features → prijs → logo → CTA
          Total: 450 frames = 15 seconden at 30fps */}
      <Composition
        id="NowOnPlayStore"
        component={NowOnPlayStore}
        durationInFrames={450}
        fps={FPS}
        width={TIKTOK_WIDTH}
        height={TIKTOK_HEIGHT}
      />

      {/* === TEMPLATE 4: Double Buy Video ===
          Simple text-driven story: stop buying the same book twice.
          Total: 495 frames = 16.5 seconds at 30fps */}
      <Composition
        id="DoubleBuyVideo"
        component={DoubleBuyVideo}
        durationInFrames={495}
        fps={FPS}
        width={TIKTOK_WIDTH}
        height={TIKTOK_HEIGHT}
      />
      {/* === TEMPLATE 5: Vinyl Slideshow === */}
      <Composition
        id="VinylSlideshow"
        component={ScreenshotSlideshow}
        durationInFrames={6 * FPS}
        fps={FPS}
        width={TIKTOK_WIDTH}
        height={TIKTOK_HEIGHT}
        schema={slideshowSchema}
        defaultProps={{
          slides: [
            {
              imagePath: "vinyl-1.png",
              title: "Into vinyl records?",
              subtitle: "SeniorEase tracks those too",
            },
            {
              imagePath: "vinyl-2.png",
              title: "Never accidentally buy the same LP twice",
              subtitle: "Scan the barcode — done",
            },
            {
              imagePath: "shot 4library.jpeg",
              title: "Books, music, DVDs & games",
              subtitle: "Try free at seniorease.eu",
            },
          ],
          secondsPerSlide: 3,
          backgroundColor: "#1a1a2e",
          accentColor: "#C9A06A",
        }}
      />

      {/* === TEMPLATE 7: Play Store Screenshots Slideshow === */}
      <Composition
        id="PlayStoreSlideshow"
        component={ScreenshotSlideshow}
        durationInFrames={5 * 4 * FPS}
        fps={FPS}
        width={TIKTOK_WIDTH}
        height={TIKTOK_HEIGHT}
        schema={slideshowSchema}
        defaultProps={{
          slides: [
            { imagePath: "ps-scan.png", title: "", subtitle: "" },
            { imagePath: "ps-library.png", title: "", subtitle: "" },
            { imagePath: "ps-track.png", title: "", subtitle: "" },
            { imagePath: "ps-export.png", title: "", subtitle: "" },
            { imagePath: "ps-noaccount.png", title: "", subtitle: "" },
          ],
          secondsPerSlide: 4,
          backgroundColor: "#000000",
          accentColor: "#8B5E3C",
        }}
      />

      {/* === TEMPLATE 6: Games Slideshow === */}
      <Composition
        id="GamesSlideshow"
        component={ScreenshotSlideshow}
        durationInFrames={6 * FPS}
        fps={FPS}
        width={TIKTOK_WIDTH}
        height={TIKTOK_HEIGHT}
        schema={slideshowSchema}
        defaultProps={{
          slides: [
            {
              imagePath: "games-1.png",
              title: "Got a game collection?",
              subtitle: "SeniorEase tracks those too",
            },
            {
              imagePath: "games-2.png",
              title: "Never buy a game you already own",
              subtitle: "Scan the barcode — done",
            },
            {
              imagePath: "shot 4library.jpeg",
              title: "Books, music, DVDs & games",
              subtitle: "Try free at seniorease.eu",
            },
          ],
          secondsPerSlide: 3,
          backgroundColor: "#1a1a2e",
          accentColor: "#4A90D9",
        }}
      />

      {/* === PLAY STORE SCREENSHOTS ===
          5 statische PNG's voor de Play Store winkelvermelding.
          Formaat: 1080x1920 (portrait, 9:16)
          Export via: npm run render:screenshots */}

      <Still
        id="Screenshot1Scan"
        component={PlayStoreScreenshot}
        width={1080}
        height={1920}
        schema={playStoreSchema}
        defaultProps={{
          title: "Scan anything",
          subtitle: "Add books, music, DVDs\nand games in seconds",
          screenshotPath: "shot 2scan.jpeg",
          accentColor: "#8B5E3C",
        }}
      />

      <Still
        id="Screenshot2Library"
        component={PlayStoreScreenshot}
        width={1080}
        height={1920}
        schema={playStoreSchema}
        defaultProps={{
          title: "Your entire collection",
          subtitle: "Books, music, DVDs & games\nalways with you",
          screenshotPath: "shot 4library.jpeg",
          accentColor: "#8B5E3C",
        }}
      />

      <Still
        id="Screenshot3Track"
        component={PlayStoreScreenshot}
        width={1080}
        height={1920}
        schema={playStoreSchema}
        defaultProps={{
          title: "Track everything",
          subtitle: "Read, listened, watched —\nalways up to date",
          screenshotPath: "boekdetail.png",
          accentColor: "#8B5E3C",
        }}
      />

      <Still
        id="Screenshot4Export"
        component={PlayStoreScreenshot}
        width={1080}
        height={1920}
        schema={playStoreSchema}
        defaultProps={{
          title: "Export your collection",
          subtitle: "Share as PDF or CSV\nin one tap",
          screenshotPath: "share.png",
          accentColor: "#8B5E3C",
        }}
      />

      <Still
        id="Screenshot5NoAccount"
        component={PlayStoreScreenshot}
        width={1080}
        height={1920}
        schema={playStoreSchema}
        defaultProps={{
          title: "No account needed",
          subtitle: "Works offline.\nNo login. Ever.",
          screenshotPath: "settings.png",
          accentColor: "#8B5E3C",
        }}
      />
      {/* === PLAY STORE SCREENSHOTS — NEDERLANDS === */}

      <Still
        id="Screenshot1ScanNL"
        component={PlayStoreScreenshot}
        width={1080}
        height={1920}
        schema={playStoreSchema}
        defaultProps={{
          title: "Scan alles",
          subtitle: "Voeg boeken, muziek, dvd's\nen games toe in seconden",
          screenshotPath: "shot 2scan.jpeg",
          accentColor: "#8B5E3C",
        }}
      />

      <Still
        id="Screenshot2LibraryNL"
        component={PlayStoreScreenshot}
        width={1080}
        height={1920}
        schema={playStoreSchema}
        defaultProps={{
          title: "Uw volledige collectie",
          subtitle: "Boeken, muziek, dvd's & games\naltijd bij u",
          screenshotPath: "shot 4library.jpeg",
          accentColor: "#8B5E3C",
        }}
      />

      <Still
        id="Screenshot3TrackNL"
        component={PlayStoreScreenshot}
        width={1080}
        height={1920}
        schema={playStoreSchema}
        defaultProps={{
          title: "Alles bijhouden",
          subtitle: "Gelezen, geluisterd, gekeken —\naltijd up-to-date",
          screenshotPath: "boekdetail.png",
          accentColor: "#8B5E3C",
        }}
      />

      <Still
        id="Screenshot4ExportNL"
        component={PlayStoreScreenshot}
        width={1080}
        height={1920}
        schema={playStoreSchema}
        defaultProps={{
          title: "Exporteer uw collectie",
          subtitle: "Deel als PDF of CSV\nmet één tik",
          screenshotPath: "share.png",
          accentColor: "#8B5E3C",
        }}
      />

      <Still
        id="Screenshot5NoAccountNL"
        component={PlayStoreScreenshot}
        width={1080}
        height={1920}
        schema={playStoreSchema}
        defaultProps={{
          title: "Geen account nodig",
          subtitle: "Werkt offline.\nGeen login. Nooit.",
          screenshotPath: "settings.png",
          accentColor: "#8B5E3C",
        }}
      />
    </>
  );
};
