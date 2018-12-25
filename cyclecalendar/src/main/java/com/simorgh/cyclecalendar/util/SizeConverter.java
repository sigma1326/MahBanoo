package com.simorgh.cyclecalendar.util;

import android.content.Context;

public class SizeConverter {
    public static float spToPx(Context ctx, float sp) {
        return sp * ctx.getResources().getDisplayMetrics().scaledDensity;
    }

    public static float pxToDp(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float dpToPx(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
