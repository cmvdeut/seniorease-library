const fs = require('fs');
const path = require('path');

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
  } else {
    fs.copyFileSync(src, dest);
  }
}

// Copy all necessary files
const filesToCopy = [
  'index.html',
  'nl',
  'privacy-policy.html',
  'terms.html',
  'terms',
  'contact.html',
  'tiktok-callback.html',
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
