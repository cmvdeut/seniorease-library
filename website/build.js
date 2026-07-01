const fs = require('fs');
const path = require('path');

const VERCEL_ANALYTICS_SNIPPET = `
    <!-- Vercel Web Analytics -->
    <script>
      window.va = window.va || function () { (window.vaq = window.vaq || []).push(arguments); };
    </script>
    <script defer src="/_vercel/insights/script.js"></script>`;

function injectVercelAnalytics(html) {
  if (html.includes('/_vercel/insights/script.js')) {
    return html;
  }
  if (html.includes('</head>')) {
    return html.replace('</head>', `${VERCEL_ANALYTICS_SNIPPET}\n</head>`);
  }
  return html;
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
    fs.writeFileSync(dest, injectVercelAnalytics(html), 'utf-8');
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

console.log('Build completed!');
