package com.simorgh.weekdaypicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class WeekDayPicker extends View {
    public WeekDayPicker(Context context) {
        super(context);
    }

    public WeekDayPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekDayPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WeekDayPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



}
