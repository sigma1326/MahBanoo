package com.simorgh.cluecalendar.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;

import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.cluecalendar.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

public class Utils {
    public static boolean colorHasState(ColorStateList color, int state) {
        Parcel parcel = Parcel.obtain();
        color.writeToParcel(parcel, 0);

        // hack from ColorStateList.hasState(...)
        final int specCount = parcel.readInt();
        for (int specIndex = 0; specIndex < specCount; specIndex++) {
            final int[] states = parcel.createIntArray();
            for (int state1 : states != null ? states : new int[0]) {
                if (state1 == state || state1 == ~state) {
                    return true;
                }
            }
        }
        return false;

        // this is not good because it will return true only if the said 'state' is the only selector on the color
        // int[] states = new int[]{state};
        // return color.getColorForState(states, Integer.MIN_VALUE) != Integer.MIN_VALUE && color.getColorForState(states, Integer.MAX_VALUE) != Integer.MAX_VALUE;
    }

    public static boolean isLightTheme(@NonNull Context context) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.isLightTheme});
        boolean isLightTheme = typedArray.getBoolean(0, false);
        typedArray.recycle();
        return isLightTheme;
    }

    public static Drawable tintDrawable(Context context, Drawable drawable, int tintAttr) {
        // start of FIX - tinting the drawable manually because the android:tint attribute crashes the app
        Drawable wrapped = DrawableCompat.wrap(drawable);

        TypedArray arr = context.obtainStyledAttributes(new int[]{tintAttr});
        ColorStateList tintList = Utils.getColorStateList(context, arr, 0);
        arr.recycle();

        if (tintList != null) {
            DrawableCompat.setTintList(wrapped, tintList);
        }

        return wrapped;
        // end of FIX
    }

    @Nullable
    public static ColorStateList getColorStateList(Context context, TypedArray original, int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return original.getColorStateList(index);
        }

        int resId = original.getResourceId(index, 0);
        return AppCompatResources.getColorStateList(context, resId);
    }

    @Nullable
    public static Drawable getDrawable(Context context, TypedArray original, int index, int tintResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return original.getDrawable(index);
        }

        int resId = original.getResourceId(index, 0);
        Drawable drawable = AppCompatResources.getDrawable(context, resId);

        if (drawable != null) {
            Drawable wrapped = DrawableCompat.wrap(drawable);

            DrawableCompat.applyTheme(wrapped, context.getTheme());

            TypedArray a = context.obtainStyledAttributes(new int[]{tintResId});

            ColorStateList tintList = a.getColorStateList(0);

            if (tintList != null) {
                DrawableCompat.setTintList(wrapped, tintList);
            }

            drawable = wrapped;

            a.recycle();
        }

        return drawable;
    }

    public static Typeface getTypeFace(Context context, int calendarType) {
        AssetManager assetManager = context.getAssets();

        switch (calendarType) {
            case CalendarType.PERSIAN:
                return Typeface.createFromAsset(assetManager, "fonts/vazir_medium.ttf");
            case CalendarType.ARABIC:
                return Typeface.createFromAsset(assetManager, "fonts/vazir_medium.ttf");

            case CalendarType.GREGORIAN:
                return Typeface.createFromAsset(assetManager, "fonts/rmedium.ttf");
            default:
                return Typeface.createFromAsset(assetManager, "fonts/rmedium.ttf");
        }
    }
}
