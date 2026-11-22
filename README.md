# Particle Wallpaper (Android)

Your HTML animation packaged as an Android app, with a **Set as wallpaper** button that installs it as a live wallpaper.

## How it works
- `assets/index.html` is your original page, shown in a WebView as the settings and preview screen. Slider, colour and theme changes are saved automatically. Pick a background colour and a particle/bond colour from the swatches, or tap the rainbow circle for a custom colour. The Theme button cycles through ready-made colour pairs.
- `ParticleWallpaperService.java` is a native Canvas version of the same animation. Android wallpapers can't run a WebView reliably, so the animation was ported. It reads the same saved settings and reacts to touch.
- The **Set as wallpaper** button opens Android's live wallpaper preview, where you tap "Set wallpaper".

## Build the APK

**Option A: GitHub (no installs, works from a phone browser)**
1. Create a new GitHub repo and upload this whole folder (including `.github`).
2. Open the repo's **Actions** tab, then **Build APK**, then **Run workflow**.
3. When it finishes (about 3-5 minutes), download `ParticleWallpaper-debug-apk` from the run page and unzip it to get `app-debug.apk`.

**Option B: Android Studio**
1. File > Open, then pick this folder and let Gradle sync.
2. Build > Build APK(s). The file is in `app/build/outputs/apk/debug/app-debug.apk`.

## Install
Copy the APK to your phone, open it, and allow "install unknown apps" for your file manager or browser if asked. The debug build is signed automatically, so it installs as-is.
