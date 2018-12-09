package com.simorgh.cluecalendar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.simorgh.cluecalendar.R;
import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.util.CalendarTool;
import com.simorgh.cluecalendar.util.SizeConverter;
import com.simorgh.cluecalendar.util.Utils;

import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class WeekDaysLabelView extends View {
    private int calendarType;
    private static final int DAYS_IN_WEEK = 7;
    private static final int DEFAULT_WEEK_START = Calendar.SUNDAY;
    private int mWeekStart = DEFAULT_WEEK_START;
    private final String[] mDayOfWeekLabels = new String[7];

    private Locale mLocale;

    // Desired dimensions.
    private int mDesiredDayOfWeekHeight;
    private int mDesiredCellWidth;


    // Dimensions as laid out.
    private int mDayOfWeekHeight;
    private int mCellWidth;

    private int mPaddedWidth;
    private int mPaddedHeight;

    private final TextPaint mDayOfWeekPaint = new TextPaint();

    public WeekDaysLabelView(Context context) {
        super(context);
        init();
    }

    public WeekDaysLabelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeekDaysLabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WeekDaysLabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        final Resources res = getResources();

        calendarType = 1;

        mLocale = res.getConfiguration().locale;

        mDesiredDayOfWeekHeight = (int) dp2px(40);

        updateDayOfWeekLabels();


        initPaints(res);
    }

    private void initPaints(Resources res) {
        final Typeface dayOfWeekTypeface = Utils.getTypeFace(getContext(), calendarType);

        final int dayOfWeekTextSize = res.getDimensionPixelSize(R.dimen.month_view_day_of_week_text_size);


        mDayOfWeekPaint.setAntiAlias(true);
        mDayOfWeekPaint.setTextSize(dayOfWeekTextSize);
        mDayOfWeekPaint.setTypeface(dayOfWeekTypeface);
        mDayOfWeekPaint.setTextAlign(Paint.Align.CENTER);
        mDayOfWeekPaint.setColor(Color.parseColor("#00819b"));
        mDayOfWeekPaint.setFakeBoldText(true);
        mDayOfWeekPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int padStart = ViewCompat.getPaddingStart(this);
        int padEnd = ViewCompat.getPaddingEnd(this);

        final int preferredHeight = mDesiredDayOfWeekHeight + getPaddingTop() + getPaddingBottom();
        final int preferredWidth = mDesiredCellWidth * DAYS_IN_WEEK + padStart + padEnd;
        final int resolvedWidth = resolveSize(preferredWidth, widthMeasureSpec);
        final int resolvedHeight = resolveSize(preferredHeight, heightMeasureSpec);
        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!changed) {
            return;
        }

        // Let's initialize a completely reasonable number of variables.
        final int w = right - left;
        final int h = bottom - top;
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        final int paddedRight = w - paddingRight;
        final int paddedBottom = h - paddingBottom;
        final int paddedWidth = paddedRight - paddingLeft;
        final int paddedHeight = paddedBottom - paddingTop;
        if (paddedWidth == mPaddedWidth || paddedHeight == mPaddedHeight) {
            return;
        }

        mPaddedWidth = paddedWidth;
        mPaddedHeight = paddedHeight;

        // We may have been laid out smaller than our preferred size. If so,
        // scale all dimensions to fit.
        final int measuredPaddedHeight = getMeasuredHeight() - paddingTop - paddingBottom;
        final float scaleH = paddedHeight / (float) measuredPaddedHeight;
        final int cellWidth = mPaddedWidth / DAYS_IN_WEEK;
        mDayOfWeekHeight = (int) (mDesiredDayOfWeekHeight * scaleH);
        mCellWidth = cellWidth;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        drawDaysOfWeek(canvas);

        setBackgroundColor(Color.WHITE);
        setElevation(10);
    }

    private void drawDaysOfWeek(Canvas canvas) {
        final TextPaint p = mDayOfWeekPaint;
        final int headerHeight = mDayOfWeekHeight;
        final int rowHeight = mDayOfWeekHeight;
        final int colWidth = mCellWidth;

        // Text is vertically centered within the day of week height.
        final float halfLineHeight = (p.ascent() + p.descent()) / 2f;
        final int rowCenter = headerHeight + rowHeight / 2;

        for (int col = 0; col < DAYS_IN_WEEK; col++) {
            final int colCenter = colWidth * col + colWidth / 2;
            final int colCenterRtl;
            //TODO rtl
            if (shouldBeRTL()) {
                colCenterRtl = mPaddedWidth - colCenter;
            } else {
                colCenterRtl = colCenter;
            }

            final String label = mDayOfWeekLabels[col];
            canvas.drawText(label, colCenterRtl, getHeight() / 2.2f + p.getFontMetrics().descent, p);
        }
    }


    private void updateDayOfWeekLabels() {
        // Use tiny (e.g. single-character) weekday names. The indices
        // for this list correspond to Calendar days, e.g. SATURDAY is index 1.

        final String[] tinyWeekdayNames = new String[DAYS_IN_WEEK];

        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY + i);
            String dayName = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, mLocale).toUpperCase(mLocale);

            switch (calendarType) {
                case CalendarType.GREGORIAN:
                    tinyWeekdayNames[i] = dayName.substring(0, 3);
                    break;
                case CalendarType.PERSIAN:
                    tinyWeekdayNames[i] = CalendarTool.getPersianDayName(dayName.substring(0, 3));
                    break;
                case CalendarType.ARABIC:
                    tinyWeekdayNames[i] = CalendarTool.getArabicDayName(dayName.substring(0, 3));
                    break;
                default:
            }
        }


        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            mDayOfWeekLabels[i] = tinyWeekdayNames[(mWeekStart + i - 1) % DAYS_IN_WEEK];
        }
//        Log.d("d13", "updateDayOfWeekLabels: " + Arrays.toString(mDayOfWeekLabels));
    }

    private boolean shouldBeRTL() {
        boolean isRTLLanguage;
        switch (calendarType) {
            case CalendarType.PERSIAN:
            case CalendarType.ARABIC:
                isRTLLanguage = true;
                break;
            case CalendarType.GREGORIAN:
            default:
                isRTLLanguage = false;
        }
        return isLayoutRtl() ^ isRTLLanguage;
    }

    private boolean isLayoutRtl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return (getLayoutDirection() == LAYOUT_DIRECTION_RTL);
        }

        return false;
    }

    private float dp2px(float dp) {
        return SizeConverter.dpToPx(getContext(), dp);
    }

    private float px2dp(float px) {
        return SizeConverter.pxToDp(getContext(), px);
    }

    private float sp2px(float sp) {
        return SizeConverter.spToPx(getContext(), sp);
    }
}
