const fs = require('fs');
const path = require('path');

const VERCEL_ANALYTICS_SNIPPET = `
    <!-- Vercel Web Analytics -->
    <script>
      window.va = window.va || function () { (window.vaq = window.vaq || []).push(arguments); };
    </script>
    <script defer src="/_vercel/insights/script.js"></script>`;

function loadEnvFile() {
  const envPath = path.join(__dirname, '.env');
  if (!fs.existsSync(envPath)) return;
  for (const line of fs.readFileSync(envPath, 'utf-8').split('\n')) {
    const trimmed = line.trim();
    if (!trimmed || trimmed.startsWith('#')) continue;
    const eq = trimmed.indexOf('=');
    if (eq === -1) continue;
    const key = trimmed.slice(0, eq).trim();
    const value = trimmed.slice(eq + 1).trim();
    if (!process.env[key]) process.env[key] = value;
  }
}

loadEnvFile();

const GOOGLE_SITE_VERIFICATION = process.env.GOOGLE_SITE_VERIFICATION || '';

function injectHeadExtras(html) {
  let result = html;
  if (!result.includes('/_vercel/insights/script.js') && result.includes('</head>')) {
    result = result.replace('</head>', `${VERCEL_ANALYTICS_SNIPPET}\n</head>`);
  }
  if (GOOGLE_SITE_VERIFICATION && !result.includes('google-site-verification') && result.includes('</head>')) {
    const tag = `    <meta name="google-site-verification" content="${GOOGLE_SITE_VERIFICATION}">\n`;
    result = result.replace('</head>', `${tag}</head>`);
  }
  return result;
}

// Create public directory if it doesn't exist
if (!fs.existsSync('public')) {
  fs.mkdirSync('public', { recursive: true });
}

// Function to copy file or directory
function copyRecursiveSync(src, dest) {
  const exists = fs.existsSync(src);
  const stats = exists && fs.statSync(src);
  const isDirectory = exists && stats.isDirectory();

  if (isDirectory) {
    if (!fs.existsSync(dest)) {
      fs.mkdirSync(dest, { recursive: true });
    }
    fs.readdirSync(src).forEach(childItemName => {
      copyRecursiveSync(
        path.join(src, childItemName),
        path.join(dest, childItemName)
      );
    });
  } else if (src.endsWith('.html')) {
    const html = fs.readFileSync(src, 'utf-8');
    fs.writeFileSync(dest, injectHeadExtras(html), 'utf-8');
  } else {
    fs.copyFileSync(src, dest);
  }
}

// Copy all necessary files
const filesToCopy = [
  'index.html',
  'nl',
  'blog',
  'privacy-policy.html',
  'terms.html',
  'terms',
  'contact.html',
  'tiktok-callback.html',
  'llms.txt',
  'pricing.html',
  'about.html',
  'about-photo.jpg',
  'family-setup.html',
  'robots.txt',
  'sitemap.xml',
  'downloads',
  'qr-code-apk-download.png',
  'qr-code-playstore.png',
  'google-play-badge-en.png',
  'google-play-badge-nl.png',
  'seniorease-icon.png',
  'favicon.png',
  'assets'
];

filesToCopy.forEach(file => {
  if (fs.existsSync(file)) {
    try {
      copyRecursiveSync(file, path.join('public', file));
      console.log(`Copied: ${file}`);
    } catch (error) {
      console.error(`Error copying ${file}:`, error.message);
    }
  }
});

// Google Search Console HTML file verification (e.g. google123abc.html)
fs.readdirSync(__dirname)
  .filter((name) => /^google[a-z0-9]+\.html$/i.test(name))
  .forEach((name) => {
    fs.copyFileSync(path.join(__dirname, name), path.join('public', name));
    console.log(`Copied: ${name}`);
  });

if (GOOGLE_SITE_VERIFICATION) {
  console.log('Google Search Console meta tag injected.');
} else {
  console.log('Tip: set GOOGLE_SITE_VERIFICATION in .env for Search Console HTML-tag verification.');
}

console.log('Build completed!');
