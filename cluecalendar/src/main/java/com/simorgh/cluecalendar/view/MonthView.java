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
import com.simorgh.cluecalendar.hijricalendar.UmmalquraCalendar;
import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.persiancalendar.PersianCalendar;
import com.simorgh.cluecalendar.persiancalendar.PersianDate;
import com.simorgh.cluecalendar.util.CalendarTool;
import com.simorgh.cluecalendar.util.StateSet;
import com.simorgh.cluecalendar.util.Utils;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class MonthView extends View {
    private int calendarType;
    private static final int DAYS_IN_WEEK = 7;
    private static final int MAX_WEEKS_IN_MONTH = 6;

    private static final int DEFAULT_SELECTED_DAY = -1;
    private static final int DEFAULT_WEEK_START = Calendar.SUNDAY;

    private TextPaint mMonthPaint;
    private TextPaint mDayPaint;
    private Paint mDaySelectorPaint;

    private NumberFormat mDayFormatter;


    // Desired dimensions.
    private int mDesiredMonthHeight;
    private int mDesiredDayHeight;
    private int mDesiredCellWidth;
    private int mDesiredDaySelectorRadius;

    // Dimensions as laid out.
    private int mMonthHeight;
    private int mDayHeight;
    private int mCellWidth;
    private int mDaySelectorRadius;

    private int mPaddedWidth;
    private int mPaddedHeight;


    private int mActivatedDay = -1;
    private int mToday = DEFAULT_SELECTED_DAY;
    private int mWeekStart = DEFAULT_WEEK_START;
    private int mDaysInMonth;
    private int mDayOfWeekStart;
    private int mEnabledDayStart = 1;
    private int mEnabledDayEnd = 31;


    private String mMonthYearLabel;

    private int mMonth;
    private int mYear;

    private int mMonthPersian;
    private int mYearPersian;

    private int mYearHijri;
    private int mMonthHijri;


    private Calendar mCalendar;
    private PersianCalendar persianCalendar;
    private Locale mLocale;
    private PersianDate persianDate;
    private UmmalquraCalendar hijriDate;
    private UmmalquraCalendar hijriCalendar;

    private OnDayClickListener mOnDayClickListener;


    public MonthView(Context context) {
        super(context);
        init();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void fakeInit() {
        calendarType = 1;
        mDaysInMonth = 31;
        mEnabledDayEnd = 31;
        mEnabledDayStart = 1;
        mMonth = mCalendar.get(Calendar.MONTH);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonthPersian = persianCalendar.getPersianMonth();
        mYearPersian = persianCalendar.getPersianYear();
        mWeekStart = PersianCalendar.SATURDAY;
        mDayOfWeekStart = PersianCalendar.MONDAY;
    }

    private void init() {
        final Resources res = getResources();
        mDesiredMonthHeight = res.getDimensionPixelSize(R.dimen.month_view_month_height);
        mDesiredDayHeight = res.getDimensionPixelSize(R.dimen.month_view_day_height);
        mDesiredCellWidth = res.getDimensionPixelSize(R.dimen.month_view_day_width);
        mDesiredDaySelectorRadius = res.getDimensionPixelSize(R.dimen.month_view_day_selector_radius);


        mLocale = res.getConfiguration().locale;
        mCalendar = Calendar.getInstance(mLocale);
        persianCalendar = new PersianCalendar(mCalendar.getTimeInMillis());
        hijriCalendar = new UmmalquraCalendar();

        fakeInit();

        mDayFormatter = NumberFormat.getIntegerInstance(mLocale);


        updateMonthYearLabel();

        initPaints(res);
    }

    private void initPaints(Resources res) {
        final Typeface monthTypeface = Utils.getTypeFace(getContext(), calendarType);
        final Typeface dayTypeface = Utils.getTypeFace(getContext(), calendarType);

        final int monthTextSize = res.getDimensionPixelSize(R.dimen.month_view_month_text_size);
        final int dayTextSize = res.getDimensionPixelSize(R.dimen.month_view_day_text_size);

        mMonthPaint = new TextPaint();
        mMonthPaint.setAntiAlias(true);
        mMonthPaint.setTextSize(monthTextSize);
        mMonthPaint.setTypeface(monthTypeface);
        mMonthPaint.setTextAlign(Paint.Align.CENTER);
        mMonthPaint.setFakeBoldText(true);
        mMonthPaint.setStyle(Paint.Style.FILL);
        mMonthPaint.setColor(Color.WHITE);


        mDaySelectorPaint = new Paint();
        mDaySelectorPaint.setAntiAlias(true);
        mDaySelectorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDaySelectorPaint.setStrokeWidth(3);
        mDaySelectorPaint.setColor(Color.WHITE);

        mDayPaint = new TextPaint();
        mDayPaint.setAntiAlias(true);
        mDayPaint.setTextSize(dayTextSize);
        mDayPaint.setFakeBoldText(true);
        mDayPaint.setTypeface(dayTypeface);
        mDayPaint.setTextAlign(Paint.Align.CENTER);
        mDayPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int padStart = ViewCompat.getPaddingStart(this);
        int padEnd = ViewCompat.getPaddingEnd(this);

        final int preferredHeight = mDesiredDayHeight * MAX_WEEKS_IN_MONTH + mDesiredMonthHeight + getPaddingTop() + getPaddingBottom();
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
        final int monthHeight = (int) (mDesiredMonthHeight * scaleH);
        final int cellWidth = mPaddedWidth / DAYS_IN_WEEK;
        mMonthHeight = monthHeight;
        mDayHeight = (int) (mDesiredDayHeight * scaleH);
        mCellWidth = cellWidth;

        // Compute the largest day selector radius that's still within the clip
        // bounds and desired selector radius.
        final int maxSelectorWidth = cellWidth / 2 + Math.min(paddingLeft, paddingRight);
        final int maxSelectorHeight = mDayHeight / 2 + paddingBottom;
        mDaySelectorRadius = Math.min(mDesiredDaySelectorRadius, Math.min(maxSelectorWidth, maxSelectorHeight));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        canvas.translate(paddingLeft, paddingTop);
        drawMonth(canvas);
        drawDays(canvas);

        canvas.translate(-paddingLeft, -paddingTop);

    }

    private void drawMonth(Canvas canvas) {
        final float x = mPaddedWidth / 2f;

        // Vertically centered within the month header height.
        final float lineHeight = mMonthPaint.ascent() + mMonthPaint.descent();
        final float y = (mMonthHeight - lineHeight) / 2f;

        //TODO month label color
        mMonthPaint.setColor(Color.WHITE);
        canvas.drawText(mMonthYearLabel, x, y, mMonthPaint);
    }

    private void drawDays(Canvas canvas) {
        final TextPaint p = mDayPaint;
        final int headerHeight = mMonthHeight;
        final int rowHeight = mDayHeight;
        final int colWidth = mCellWidth;

        // Text is vertically centered within the row height.
        final float halfLineHeight = (p.ascent() + p.descent()) / 2f;
        int rowCenter = headerHeight + rowHeight / 2;

        for (int day = 1, col = findDayOffset(); day <= mDaysInMonth; day++) {
            final int colCenter = colWidth * col + colWidth / 2;
            final int colCenterRtl;
            //TODO rtl
            if (shouldBeRTL()) {
                colCenterRtl = mPaddedWidth - colCenter;
            } else {
                colCenterRtl = colCenter;
            }

            int stateMask = 0;

            final boolean isDayEnabled = isDayEnabled(day);
            if (isDayEnabled) {
                stateMask |= StateSet.VIEW_STATE_ENABLED;
            }

            final boolean isDayActivated = mActivatedDay == day;
            if (isDayActivated) {
                stateMask |= StateSet.VIEW_STATE_ACTIVATED;

                // Adjust the circle to be centered on the row.
                final Paint paint = mDaySelectorPaint;
                canvas.drawCircle(colCenterRtl, rowCenter, mDaySelectorRadius, paint);
            }

            final boolean isDayToday = mToday == day;
            final int dayTextColor;
            if (isDayToday && !isDayActivated) {
//                dayTextColor = mDaySelectorPaint.getColor();
                dayTextColor = Color.WHITE;
//                dayTextColor = Color.parseColor("#009688");
                mDaySelectorPaint.setStyle(Paint.Style.STROKE);
                mDaySelectorPaint.setColor(Color.parseColor("#F65824"));
                canvas.drawCircle(colCenterRtl, rowCenter, mDaySelectorRadius, mDaySelectorPaint);
                mDaySelectorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                mDaySelectorPaint.setColor(Color.WHITE);
            } else if (isDayToday) {
                dayTextColor = Color.parseColor("#F65824");

            } else {
                final int[] stateSet = StateSet.get(stateMask);
//                dayTextColor = mDayTextColor.getColorForState(stateSet, 0);
                if (isDayEnabled && isDayActivated) {
                    dayTextColor = Color.parseColor("#009688");
                } else {
                    dayTextColor = Color.WHITE;
                }
            }
            p.setColor(dayTextColor);
            //TODO day number text color
//            p.setColor(Color.WHITE);

            canvas.drawText(mDayFormatter.format(day), colCenterRtl, rowCenter - halfLineHeight, p);

            col++;

            if (col == DAYS_IN_WEEK) {
                col = 0;
                rowCenter += rowHeight;
            }
        }
    }

    public void setMonthParams(int selectedDay, int month, int year, int weekStart, int enabledDayStart, int enabledDayEnd, int calendarType) {
        mActivatedDay = selectedDay;
        this.calendarType = calendarType;

        switch (calendarType) {
            case CalendarType.PERSIAN:
                mMonthPersian = month;
                mYearPersian = year;
                persianCalendar.setPersianDate(mYearPersian, mMonthPersian + 1, 1);
                PersianDate persianDate = new PersianDate();
                persianDate.setShDate(mYearPersian, mMonthPersian + 1, 1);
                mDayOfWeekStart = persianDate.dayOfWeek();
                break;
            case CalendarType.ARABIC:
                mYearHijri = year;
                mMonthHijri = month;
                hijriCalendar.set(UmmalquraCalendar.MONTH, mMonthHijri);
                hijriCalendar.set(UmmalquraCalendar.YEAR, mYearHijri);
                hijriCalendar.set(UmmalquraCalendar.DAY_OF_MONTH, 1);
                mDayOfWeekStart = hijriCalendar.get(Calendar.DAY_OF_WEEK);
                break;
            case CalendarType.GREGORIAN:
                if (isValidMonth(month)) {
                    mMonth = month;
                }
                mYear = year;
                mCalendar.set(Calendar.MONTH, mMonth);
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, 1);
                mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK);
                break;
        }
        if (isValidDayOfWeek(weekStart)) {
            mWeekStart = weekStart;
        } else {
            mWeekStart = mCalendar.getFirstDayOfWeek();
        }

        // Figure out what day today is.
        final Calendar today = Calendar.getInstance();
        mToday = -1;
        mDaysInMonth = CalendarTool.getDaysInMonth(month, year, calendarType);
        for (int i = 0; i < mDaysInMonth; i++) {
            final int day = i + 1;
            if (sameDay(day, today)) {
                mToday = day;
            }
        }

        initPaints(getResources());

        mEnabledDayStart = mathConstrain(enabledDayStart, 1, mDaysInMonth);
        mEnabledDayEnd = mathConstrain(enabledDayEnd, mEnabledDayStart, mDaysInMonth);

        updateMonthYearLabel();

        invalidate();
    }

    private boolean sameDay(int day, Calendar today) {
        int todayDay = 0;
        int todayYear = 0;
        int todayMonth = 0;
        int compYear = 0;
        int compMonth = 0;
        switch (calendarType) {
            case CalendarType.PERSIAN:
                PersianCalendar p = CalendarTool.GregorianToPersian(today);
                todayDay = p.getPersianDay();
                todayMonth = p.getPersianMonth();
                todayYear = p.getPersianYear();
                compYear = mYearPersian;
                compMonth = mMonthPersian;
                break;
            case CalendarType.ARABIC:
                UmmalquraCalendar hijri = CalendarTool.GregorianToHijri(today);
                compYear = mYearHijri;
                compMonth = mMonthHijri;
                todayDay = hijri.get(UmmalquraCalendar.DAY_OF_MONTH);
                todayMonth = hijri.get(UmmalquraCalendar.MONTH);
                todayYear = hijri.get(UmmalquraCalendar.YEAR);
                break;
            case CalendarType.GREGORIAN:
                compYear = mYear;
                compMonth = mMonth;
                todayDay = today.get(Calendar.DAY_OF_MONTH);
                todayMonth = today.get(Calendar.MONTH);
                todayYear = today.get(Calendar.YEAR);
                break;
        }
        return compYear == todayYear && compMonth == todayMonth && day == todayDay;
    }


    public static int mathConstrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    private boolean isDayEnabled(int day) {
        return day >= mEnabledDayStart && day <= mEnabledDayEnd;
    }

    private boolean isValidDayOfMonth(int day) {
        return day >= 1 && day <= mDaysInMonth;
    }

    private static boolean isValidDayOfWeek(int day) {
        return day >= Calendar.SUNDAY && day <= Calendar.SATURDAY;
    }

    private static boolean isValidMonth(int month) {
        return month >= 0 && month <= 11;
    }

    private int findDayOffset() {
        final int offset = mDayOfWeekStart - mWeekStart;
        if (mDayOfWeekStart < mWeekStart) {
            return offset + DAYS_IN_WEEK;
        }
        return offset;
    }


    private void updateMonthYearLabel() {
        int month = 0;
        int year = 0;
        switch (calendarType) {
            case CalendarType.PERSIAN:
                month = mMonthPersian;
                year = mYearPersian;
                break;
            case CalendarType.ARABIC:
                month = mMonthHijri;
                year = mYearHijri;
                break;
            case CalendarType.GREGORIAN:
                month = mMonth;
                year = mYear;
                break;
        }
        mMonthYearLabel = CalendarTool.getMonthName(month, calendarType) + "    " + year;
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


    /**
     * Handles callbacks when the user clicks on a time object.
     */
    public interface OnDayClickListener {
        void onDayClick(MonthView view, Calendar day);
    }
}
