package com.particle.wallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.Locale;

public class MainActivity extends Activity {

    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        web.setBackgroundColor(0xFF05060F);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(false); // assets are served via file:///android_asset regardless
        web.addJavascriptInterface(new Bridge(), "Android");
        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        web.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        web.onResume();
    }

    @Override
    protected void onDestroy() {
        web.destroy();
        super.onDestroy();
    }

    /** Methods callable from the page as window.Android.* */
    private class Bridge {
        @JavascriptInterface
        public String getPrefs() {
            SharedPreferences sp = Prefs.get(MainActivity.this);
            return String.format(Locale.US,
                    "{\"count\":%d,\"speed\":%d,\"links\":%d,\"bg\":\"%s\",\"fg\":\"%s\"}",
                    sp.getInt(Prefs.COUNT, 140), sp.getInt(Prefs.SPEED, 8),
                    sp.getInt(Prefs.LINKS, 120),
                    hex(sp.getInt(Prefs.BG, Prefs.DEF_BG)),
                    hex(sp.getInt(Prefs.FG, Prefs.DEF_FG)));
        }

        @JavascriptInterface
        public void savePrefs(int count, int speed, int links, String bg, String fg) {
            SharedPreferences.Editor ed = Prefs.get(MainActivity.this).edit()
                    .putInt(Prefs.COUNT, count)
                    .putInt(Prefs.SPEED, speed)
                    .putInt(Prefs.LINKS, links);
            try { ed.putInt(Prefs.BG, Color.parseColor(bg) | 0xFF000000); } catch (Exception ignored) { }
            try { ed.putInt(Prefs.FG, Color.parseColor(fg) | 0xFF000000); } catch (Exception ignored) { }
            ed.apply();
        }

        private String hex(int argb) {
            return String.format(Locale.US, "#%06x", argb & 0xFFFFFF);
        }

        @JavascriptInterface
        public void setWallpaper() {
            runOnUiThread(() -> {
                Intent i = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(MainActivity.this, ParticleWallpaperService.class));
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    // Some devices lack the direct preview screen; fall back to the picker.
                    try {
                        startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER));
                    } catch (ActivityNotFoundException ignored) { }
                }
            });
        }
    }
}
