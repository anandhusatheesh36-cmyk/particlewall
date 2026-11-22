package com.particle.wallpaper;

import android.content.Context;
import android.content.SharedPreferences;

/** Settings shared between the in-app preview (WebView) and the live wallpaper service. */
final class Prefs {
    static final String NAME = "particle_prefs";
    static final String COUNT = "count";
    static final String SPEED = "speed";
    static final String LINKS = "links";
    static final String BG = "bg";   // background colour, ARGB int
    static final String FG = "fg";   // particle + bond colour, ARGB int

    static final int DEF_BG = 0xFF05060F;
    static final int DEF_FG = 0xFF55C1F6;

    private Prefs() {}

    static SharedPreferences get(Context c) {
        return c.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }
}
