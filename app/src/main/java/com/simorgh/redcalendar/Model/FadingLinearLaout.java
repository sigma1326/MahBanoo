package com.simorgh.redcalendar.Model;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class FadingLinearLaout extends LinearLayout {
    public FadingLinearLaout(Context context) {
        super(context);
    }

    public FadingLinearLaout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FadingLinearLaout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FadingLinearLaout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected float getLeftFadingEdgeStrength() {
        return 50;
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        return 50;
    }

    @Override
    protected float getRightFadingEdgeStrength() {
        return 50;
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        return 50;
    }

    @Override
    public int getHorizontalFadingEdgeLength() {
        return 50;
    }

    @Override
    public boolean isHorizontalFadingEdgeEnabled() {
        return true;
    }

}
