# =============================================================
# SeniorEase TikTok + Instagram - Docker image
# Bevat Node.js 20 + Chrome (voor Remotion video rendering)
# =============================================================

FROM node:20-bookworm-slim

# Installeer Chrome-afhankelijkheden voor Remotion headless rendering
RUN apt-get update && apt-get install -y \
    ca-certificates \
    fonts-liberation \
    libasound2 \
    libatk-bridge2.0-0 \
    libatk1.0-0 \
    libc6 \
    libcairo2 \
    libcups2 \
    libdbus-1-3 \
    libexpat1 \
    libfontconfig1 \
    libgbm1 \
    libgcc1 \
    libglib2.0-0 \
    libgtk-3-0 \
    libnspr4 \
    libnss3 \
    libpango-1.0-0 \
    libpangocairo-1.0-0 \
    libstdc++6 \
    libx11-6 \
    libx11-xcb1 \
    libxcb1 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxi6 \
    libxrandr2 \
    libxrender1 \
    libxss1 \
    libxtst6 \
    lsb-release \
    wget \
    xdg-utils \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Kopieer package files en installeer dependencies
COPY package*.json ./
RUN npm ci

# Download Chrome voor Remotion (eenmalig bij build)
RUN npx remotion browser ensure

# Kopieer alle bronbestanden
COPY . .

# Maak output map aan (wordt ook via volume gemount)
RUN mkdir -p output

# Standaard: toon beschikbare commando's
CMD ["node", "-e", "console.log('\\n SeniorEase Docker klaar!\\n\\nGebruik:\\n  docker compose run app render    - video renderen\\n  docker compose run app post      - posten naar TikTok\\n  docker compose run app both      - render + post\\n')"]
