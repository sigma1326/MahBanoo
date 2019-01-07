package com.simorgh.cyclecalendar.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.hijricalendar.UmmalquraCalendar;
import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.calendarutil.persiancalendar.PersianDate;
import com.simorgh.cyclecalendar.R;
import com.simorgh.cyclecalendar.util.SizeConverter;
import com.simorgh.cyclecalendar.util.Utils;
import com.simorgh.cycleutils.CycleData;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class BaseMonthView extends View {
    public static final String TAG = "baseMonthView";
    // Desired dimensions.
    protected static int mDesiredMonthHeight;
    protected static final int DAYS_IN_WEEK = 7;
    protected static final int MAX_WEEKS_IN_MONTH = 6;

    protected static final int DEFAULT_SELECTED_DAY = -1;
    protected static final int DEFAULT_WEEK_START = Calendar.SUNDAY;

    private int weeksInMonth = MAX_WEEKS_IN_MONTH;


    protected static TextPaint mMonthPaint;
    protected static TextPaint dayTextPaint;

    protected static NumberFormat mDayFormatter;

    public static final int MonthViewTypeShowCalendar = 0;
    public static final int MonthViewTypeChangeDays = 1;
    public static final int MonthViewTypeSetStartDay = 2;

    public static final int TYPE_RED = 0;
    public static final int TYPE_GREEN = 1;
    public static final int TYPE_GREEN2 = 2;
    public static final int TYPE_GRAY = 3;
    public static final int TYPE_YELLOW = 4;

    //rectangle colors
    protected static Paint rectTypeGrayPaint;
    protected static int rectTypeGrayColor;
    protected static int tvMonthDayNumberTextColorWhite;
    protected static int tvMonthDayNumberTextColorBlack;

    protected static int monthViewBkgColor;
    protected static int mDesiredDayHeight;
    protected static int mDesiredCellWidth;
    protected static Calendar mCalendar;

    // Dimensions as laid out.
    protected int mMonthHeight;
    protected int mDayHeight;
    protected int mCellWidth;

    protected int mPaddedWidth;
    protected int mPaddedHeight;


    protected int selectedDay = -1;
    protected int mActivatedDay = -1;
    protected int mToday = DEFAULT_SELECTED_DAY;
    protected int mWeekStart = DEFAULT_WEEK_START;
    protected int mDaysInMonth;
    protected int mDayOfWeekStart;
    protected int mEnabledDayStart = 1;
    protected int mEnabledDayEnd = 31;


    protected String mMonthYearLabel;

    protected int mMonth;
    protected int mYear;

    protected int mMonthPersian;
    protected int mYearPersian;

    protected int mYearHijri;
    protected int mMonthHijri;
    private static boolean isInitialized = false;
    protected static PersianCalendar persianCalendar = new PersianCalendar();
    protected static UmmalquraCalendar hijriCalendar = new UmmalquraCalendar();
    protected static PersianDate persianDate = new PersianDate();
    protected static Locale mLocale;
    protected static Calendar date = Calendar.getInstance();

    protected static PersianCalendar p = new PersianCalendar();
    protected static UmmalquraCalendar hijri = new UmmalquraCalendar();


    protected OnDayClickListener mOnDayClickListener;
    protected OnDaySelectedListener onDaySelectedListener;
    protected IsDaySelectedListener isDaySelectedListener;
    protected IsDayInRangeSelectedListener isDayInRangeSelectedListener;
    protected OnCycleDataListNeededListener onCycleDataListNeededListener;


    protected static CycleData cycleData;

    protected static boolean showInfo = true;
    protected static List<CycleData> cycleDataList = new LinkedList<>();
    protected int calendarType = CalendarType.PERSIAN;


    public BaseMonthView(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public BaseMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public BaseMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        init();
    }
    protected boolean shouldBeRTL;

    protected void initAttrs(Context context, AttributeSet attrs) {
        if (!isInitialized) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseMonthView);
            Resources resources = getResources();

            //circle colors
            rectTypeGrayColor = typedArray.getColor(R.styleable.BaseMonthView_rect_color_type3, resources.getColor(R.color.my_type_gray));
            monthViewBkgColor = resources.getColor(R.color.white);

            //main day number textView
            tvMonthDayNumberTextColorWhite = resources.getColor(R.color.white);
            tvMonthDayNumberTextColorBlack = resources.getColor(R.color.black);


            mLocale = resources.getConfiguration().locale;

            mDesiredMonthHeight = resources.getDimensionPixelSize(R.dimen.month_view_month_height);
            mDesiredDayHeight = resources.getDimensionPixelSize(R.dimen.month_view_day_height);
            mDesiredCellWidth = resources.getDimensionPixelSize(R.dimen.month_view_day_width);
            typedArray.recycle();
        }
    }

    protected void init() {
        if (!isInitialized) {
            mCalendar = Calendar.getInstance(mLocale);
            persianCalendar = new PersianCalendar(mCalendar.getTimeInMillis());
            hijriCalendar = new UmmalquraCalendar();
            mDayFormatter = NumberFormat.getIntegerInstance(mLocale);
        }
        updateMonthYearLabel();

        initPaints();
        isInitialized = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int padStart = ViewCompat.getPaddingStart(this);
        int padEnd = ViewCompat.getPaddingEnd(this);


        int preferredHeight = (int) ((mDesiredDayHeight * weeksInMonth + mDesiredMonthHeight
                + getPaddingTop() + getPaddingBottom()) + dp2px(8));
        final int preferredWidth = mDesiredCellWidth * DAYS_IN_WEEK + padStart + padEnd;
        final int resolvedWidth = resolveSize(preferredWidth, widthMeasureSpec);
        int resolvedHeight = resolveSize(preferredHeight, heightMeasureSpec);

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
        final int measuredPaddedHeight = (getMeasuredHeight() - paddingTop - paddingBottom);
        final float scaleH = paddedHeight / (measuredPaddedHeight + dp2px(8));
        final int monthHeight = (int) (mDesiredMonthHeight * scaleH);
        final int cellWidth = mPaddedWidth / DAYS_IN_WEEK;
        mMonthHeight = monthHeight;
        mDayHeight = (int) (mDesiredDayHeight * scaleH);
        mCellWidth = cellWidth;
    }

    public static long toTimeInMillis(@NonNull Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        long ret;
        ret = year;
        ret = ret * 100 + (month + 1);
        ret = ret * 100 + day;
        return ret;
    }

    protected void drawMonth(Canvas canvas) {
        final float x = mPaddedWidth / 2f;

        // Vertically centered within the month header height.
        final float lineHeight = mMonthPaint.ascent() + mMonthPaint.descent();
        final float y = (mMonthHeight + lineHeight);

        canvas.drawText(mMonthYearLabel, x, y, mMonthPaint);
    }

    protected void initPaints() {
        if (isInitialized) {
            return;
        }
        final Resources res = getResources();

        final Typeface monthTypeface = Utils.getTypeFace(getContext(), CalendarType.PERSIAN);
        final Typeface dayTypeface = Utils.getTypeFace(getContext(), CalendarType.PERSIAN);

        final int monthTextSize = res.getDimensionPixelSize(R.dimen.month_view_month_text_size);
        final int dayTextSize = res.getDimensionPixelSize(R.dimen.month_view_day_text_size);

        mMonthPaint = new TextPaint();
        mMonthPaint.setAntiAlias(true);
        mMonthPaint.setTextSize(monthTextSize);
        mMonthPaint.setTypeface(monthTypeface);
        mMonthPaint.setTextAlign(Paint.Align.CENTER);
        mMonthPaint.setFakeBoldText(true);
        mMonthPaint.setStyle(Paint.Style.FILL);
        mMonthPaint.setColor(tvMonthDayNumberTextColorBlack);


        dayTextPaint = new TextPaint();
        dayTextPaint.setAntiAlias(true);
        dayTextPaint.setTextSize(dayTextSize);
        dayTextPaint.setFakeBoldText(true);
        dayTextPaint.setTypeface(dayTypeface);
        dayTextPaint.setTextAlign(Paint.Align.CENTER);
        dayTextPaint.setStyle(Paint.Style.FILL);
        dayTextPaint.setColor(tvMonthDayNumberTextColorWhite);

        rectTypeGrayPaint = new Paint();
        rectTypeGrayPaint.setAntiAlias(true);
        rectTypeGrayPaint.setStyle(Paint.Style.FILL);
        rectTypeGrayPaint.setColor(rectTypeGrayColor);
    }

    protected Paint getDayPaint(int day) {
        return rectTypeGrayPaint;
    }

    protected Paint getDayPaint(Calendar date) {
        return rectTypeGrayPaint;
    }

    protected void drawDays(Canvas canvas) {
        final TextPaint p = dayTextPaint;
        final int headerHeight = mMonthHeight;
        final int rowHeight = mDayHeight;
        final int colWidth = mCellWidth;

        int rowCenter = (int) (headerHeight + dp2px(8) + rowHeight / 2f);
        int left;
        int right;
        int top;
        int bottom;
        for (int day = 1, col = findDayOffset(); day <= mDaysInMonth; day++) {
            final int colCenter = colWidth * col + colWidth / 2;
            final int colCenterRtl;
            if (shouldBeRTL) {
                colCenterRtl = mPaddedWidth - colCenter;
            } else {
                colCenterRtl = colCenter;
            }

            top = (int) (rowCenter - rowHeight / 2 + dp2px(3));
            bottom = (int) (rowCenter + rowHeight / 2 - dp2px(3));
            left = (int) (colCenterRtl - colWidth / 2 + dp2px(3));
            right = (int) (colCenterRtl + colWidth / 2 - dp2px(3));
            canvas.drawRect(left, top, right, bottom, getDayPaint(day));
            if (getDayType(day) == TYPE_GRAY) {
                dayTextPaint.setColor(tvMonthDayNumberTextColorBlack);
            } else {
                dayTextPaint.setColor(tvMonthDayNumberTextColorWhite);
            }
            canvas.drawText(mDayFormatter.format(day), right - p.getFontMetrics().descent - dp2px(6),
                    bottom - p.getFontMetrics().bottom, p);

            col++;
            if (col == DAYS_IN_WEEK) {
                col = 0;
                rowCenter += rowHeight;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        canvas.translate(paddingLeft, paddingTop);
        canvas.drawColor(monthViewBkgColor);

        shouldBeRTL = shouldBeRTL();


        drawMonth(canvas);
        drawDays(canvas);

        canvas.translate(-paddingLeft, -paddingTop);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) (event.getX() + 0.5f);
        final int y = (int) (event.getY() + 0.5f);

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                final int touchedItem = getDayAtLocation(x, y);
                if (action == MotionEvent.ACTION_DOWN && touchedItem < 0) {
                    // Touch something that's not an item, reject event.
                    return false;
                }
                break;

            case MotionEvent.ACTION_UP:
                final int clickedDay = getDayAtLocation(x, y);
                onDayClicked(clickedDay);
                // Fall through.
            case MotionEvent.ACTION_CANCEL:
                // Reset touched day on stream end.
                postInvalidate();
                break;
        }
        return true;
    }

    protected int getDayAtLocation(int x, int y) {
        final int paddedX = x - getPaddingLeft();
        if (paddedX < 0 || paddedX >= mPaddedWidth) {
            return -1;
        }

        final int headerHeight = mMonthHeight;
        final int paddedY = y - getPaddingTop();
        if (paddedY < headerHeight || paddedY >= mPaddedHeight) {
            return -1;
        }

        // Adjust for RTL after applying padding.
        final int paddedXRtl;
        //TODO isLayoutRTL
        if (shouldBeRTL()) {
            paddedXRtl = mPaddedWidth - paddedX;
        } else {
            paddedXRtl = paddedX;
        }

        final int row = (paddedY - headerHeight) / mDayHeight;
        final int col = (paddedXRtl * DAYS_IN_WEEK) / mPaddedWidth;
        final int index = col + row * DAYS_IN_WEEK;
        final int day = index + 1 - findDayOffset();
        if (!isValidDayOfMonth(day)) {
            return -1;
        }
        return day;
    }

    protected void onDayClicked(int day) {
//        if (!isValidDayOfMonth(day) || !isDayEnabled(day)) {
//            return false;
//        }
        Log.d(TAG, "onDayClicked: " + day);

        if (mOnDayClickListener != null) {
            Calendar date = Calendar.getInstance();
            switch (calendarType) {
                case CalendarType.PERSIAN:
                    persianDate.setShYear(mYearPersian);
                    persianDate.setShMonth(mMonthPersian + 1);
                    persianDate.setShDay(day);
                    date = CalendarTool.PersianToGregorian(persianDate);
                    break;
                case CalendarType.ARABIC:
                    hijriCalendar.set(UmmalquraCalendar.YEAR, mYearHijri);
                    hijriCalendar.set(UmmalquraCalendar.MONTH, mMonthHijri);
                    hijriCalendar.set(UmmalquraCalendar.DAY_OF_MONTH, day);
                    date = CalendarTool.HijriToGregorian(hijriCalendar);
                    break;
                case CalendarType.GREGORIAN:
                    date.set(mYear, mMonth, day);
                    break;
            }
            mOnDayClickListener.onDayClick(this, date);
            onDaySelectedListener.onDaySelected(date);
            selectedDay = day;
        }
    }


    public OnCycleDataListNeededListener getOnCycleDataListNeededListener() {
        return onCycleDataListNeededListener;
    }

    public void setOnCycleDataListNeededListener(OnCycleDataListNeededListener onCycleDataListNeededListener) {
        this.onCycleDataListNeededListener = onCycleDataListNeededListener;
    }

    protected static int mathConstrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    protected boolean isDayEnabled(int day) {
        return day >= mEnabledDayStart && day <= mEnabledDayEnd;
    }

    protected boolean isValidDayOfMonth(int day) {
        return day >= 1 && day <= mDaysInMonth;
    }

    protected static boolean isValidDayOfWeek(int day) {
        return day >= Calendar.SUNDAY && day <= Calendar.SATURDAY;
    }

    protected static boolean isValidMonth(int month) {
        return month >= 0 && month <= 11;
    }

    protected int findDayOffset() {
        final int offset = mDayOfWeekStart - mWeekStart;
        if (mDayOfWeekStart < mWeekStart) {
            return offset + DAYS_IN_WEEK;
        }
        return offset;
    }

    protected Calendar getCalendarForDay(int day) {
        switch (calendarType) {
            case CalendarType.PERSIAN:
                persianDate.setShYear(mYearPersian);
                persianDate.setShMonth(mMonthPersian + 1);
                persianDate.setShDay(day);
                date = CalendarTool.PersianToGregorian(persianDate);
                break;
            case CalendarType.ARABIC:
                hijriCalendar.set(UmmalquraCalendar.YEAR, mYearHijri);
                hijriCalendar.set(UmmalquraCalendar.MONTH, mMonthHijri);
                hijriCalendar.set(UmmalquraCalendar.DAY_OF_MONTH, day);
                date = CalendarTool.HijriToGregorian(hijriCalendar);
                break;
            case CalendarType.GREGORIAN:
                date.set(mYear, mMonth, day);
                break;
        }
        return date;
    }

    protected int getDayType(int day) {
        if (day == -1) {
            return TYPE_RED;
        }
        if (cycleData == null) {
            return TYPE_RED;
        }
        long days = 0L;
        switch (calendarType) {
            case CalendarType.PERSIAN:
                p.setPersianDate(persianCalendar.getPersianYear(), persianCalendar.getPersianMonth() + 1, day);
                days = CalendarTool.getDaysFromDiff(p, cycleData.getStartDate());
                break;
            case CalendarType.GREGORIAN:
                break;
            case CalendarType.ARABIC:
                break;
        }
        if (days >= 0) {
            day = (int) ((days) % cycleData.getTotalDays()) + 1;
        } else {
            return TYPE_GRAY;
        }
        if (day <= cycleData.getRedCount()) {
            return TYPE_RED;
        } else if (day <= cycleData.getTotalDays()) {
            if (day >= cycleData.getGreenStartIndex() && day <= cycleData.getGreenEndIndex()) {
                if (day == cycleData.getGreen2Index()) {
                    return TYPE_GREEN2;
                }
                return TYPE_GREEN;
            } else if (day >= cycleData.getYellowStartIndex() && day <= cycleData.getYellowEndIndex()) {
                return TYPE_YELLOW;
            }
        }
        return TYPE_GRAY;
    }

    protected int getDayType(Calendar date) {
        if (cycleData == null || !showInfo) {
            return TYPE_GRAY;
        }
        long days;
        int day;
        CycleData tempCycle = null;
        Calendar clearDate = Calendar.getInstance();
        clearDate.clear();

        if (onCycleDataListNeededListener != null) {
            cycleDataList.clear();
            cycleDataList.addAll(onCycleDataListNeededListener.onCycleDataNeeded());
        }

        if (cycleDataList == null || cycleDataList.isEmpty()) {
            return TYPE_GRAY;
        }

        for (CycleData cycleData : cycleDataList) {
            if (toTimeInMillis(cycleData.getEndDate()) == toTimeInMillis(clearDate)) {
                if (toTimeInMillis(date) >= toTimeInMillis(cycleData.getStartDate())) {
                    tempCycle = cycleData;
                    break;
                }
            } else {
                if (toTimeInMillis(date) >= toTimeInMillis(cycleData.getStartDate()) && toTimeInMillis(date) <= toTimeInMillis(cycleData.getEndDate())) {
                    tempCycle = cycleData;
                    break;
                }
            }
        }

        if (tempCycle == null) {
            return TYPE_GRAY;
        }

        days = CalendarTool.getDaysFromDiff(date, tempCycle.getStartDate());
        if (days >= 0) {
            day = (int) ((days) % tempCycle.getTotalDays()) + 1;
        } else {
            return TYPE_GRAY;
        }
        if (day <= tempCycle.getRedCount()) {
            return TYPE_RED;
        } else if (day <= tempCycle.getTotalDays()) {
            if (day >= tempCycle.getGreenStartIndex() && day <= tempCycle.getGreenEndIndex()) {
                if (day == tempCycle.getGreen2Index()) {
                    return TYPE_GREEN2;
                }
                return TYPE_GREEN;
            } else if (day >= tempCycle.getYellowStartIndex() && day <= tempCycle.getYellowEndIndex()) {
                return TYPE_YELLOW;
            }
        }
        return TYPE_GRAY;
    }

    public boolean isShowInfo() {
        return showInfo;
    }

    public void setMonthParams(int selectedDay, int month, int year, int weekStart, int enabledDayStart, int enabledDayEnd, int calendarType, boolean showInfo) {
        mActivatedDay = selectedDay;
        this.calendarType = calendarType;
        switch (calendarType) {
            case CalendarType.PERSIAN:
                mMonthPersian = month;
                mYearPersian = year;
                persianCalendar.setPersianDate(mYearPersian, mMonthPersian + 1, 1);
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
            mWeekStart = Calendar.SATURDAY;
        }

        mDaysInMonth = CalendarTool.getDaysInMonth(month, year, calendarType);

        mEnabledDayStart = mathConstrain(enabledDayStart, 1, mDaysInMonth);
        mEnabledDayEnd = mathConstrain(enabledDayEnd, mEnabledDayStart, mDaysInMonth);

        updateMonthYearLabel();
        if (mDaysInMonth <= 30) {
            switch (mDayOfWeekStart) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    weeksInMonth = 5;
                    break;
                case 6:
                    weeksInMonth = 6;
                    break;
            }
        } else if (CalendarTool.getDaysInMonth(month, year, calendarType) == 31) {
            switch (mDayOfWeekStart) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    weeksInMonth = 5;
                    break;
                case 5:
                case 6:
                    weeksInMonth = 6;
                    break;
            }
        }
        BaseMonthView.showInfo = showInfo;
        postInvalidate();
    }

    public void setShowInfo(boolean showInfo) {
        BaseMonthView.showInfo = showInfo;
    }

    protected void updateMonthYearLabel() {
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
        mMonthYearLabel = CalendarTool.getMonthName(month, calendarType) + " " + year;
    }

    public void setFirstDayOfWeek(int weekStart) {
        if (isValidDayOfWeek(weekStart)) {
            mWeekStart = weekStart;
        } else {
            mWeekStart = mCalendar.getFirstDayOfWeek();
        }
        postInvalidate();
    }

    public void setOnDayClickListener(OnDayClickListener listener) {
        mOnDayClickListener = listener;
    }

    public IsDaySelectedListener getIsDaySelectedListener() {
        return isDaySelectedListener;
    }

    public void setIsDaySelectedListener(IsDaySelectedListener isDaySelectedListener) {
        this.isDaySelectedListener = isDaySelectedListener;
    }

    protected boolean shouldBeRTL() {
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

    protected boolean isLayoutRtl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return (getLayoutDirection() == LAYOUT_DIRECTION_RTL);
        }

        return false;
    }

    public CycleData getCycleData() {
        return cycleData;
    }

    public void setCycleData(CycleData cycleData) {
        BaseMonthView.cycleData = cycleData;
        postInvalidate();
    }


    protected float dp2px(float dp) {
        return SizeConverter.dpToPx(getContext(), dp);
    }

    protected float px2dp(float px) {
        return SizeConverter.pxToDp(getContext(), px);
    }

    protected float sp2px(float sp) {
        return SizeConverter.spToPx(getContext(), sp);
    }


    public OnDaySelectedListener getOnDaySelectedListener() {
        return onDaySelectedListener;
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        this.onDaySelectedListener = onDaySelectedListener;
    }

    public IsDayInRangeSelectedListener getIsDayInRangeSelectedListener() {
        return isDayInRangeSelectedListener;
    }

    public void setIsDayInRangeSelectedListener(IsDayInRangeSelectedListener isDayInRangeSelectedListener) {
        this.isDayInRangeSelectedListener = isDayInRangeSelectedListener;
    }

    public void setCycleDataList(List<CycleData> cycleDataList) {
        BaseMonthView.cycleDataList.addAll(cycleDataList);
        postInvalidate();
    }

    public List<CycleData> getCycleDataList() {
        return cycleDataList;
    }

    public interface OnDayClickListener {
        void onDayClick(BaseMonthView view, Calendar day);
    }

    public interface OnDaySelectedListener {
        void onDaySelected(Calendar selectedDay);
    }

    public interface IsDaySelectedListener {
        boolean isDaySelected(Calendar day);
    }

    public interface IsDayInRangeSelectedListener {
        boolean isDayInRangeSelected(Calendar day, CycleData cycleData);
    }

    public interface OnCycleDataListNeededListener {
        List<CycleData> onCycleDataNeeded();
    }
}
