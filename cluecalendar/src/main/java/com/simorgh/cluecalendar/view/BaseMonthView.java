package com.simorgh.cluecalendar.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.simorgh.cluecalendar.R;
import com.simorgh.cluecalendar.hijricalendar.UmmalquraCalendar;
import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.model.YearMonthDay;
import com.simorgh.cluecalendar.persiancalendar.PersianCalendar;
import com.simorgh.cluecalendar.persiancalendar.PersianDate;
import com.simorgh.cluecalendar.util.CalendarTool;
import com.simorgh.cluecalendar.util.SizeConverter;
import com.simorgh.cluecalendar.util.Utils;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class BaseMonthView extends View {
    public static final String TAG = "baseMonthView";
    protected int calendarType;
    protected int MonthViewType;
    protected static final int DAYS_IN_WEEK = 7;
    protected static final int MAX_WEEKS_IN_MONTH = 6;

    protected static final int DEFAULT_SELECTED_DAY = -1;
    protected static final int DEFAULT_WEEK_START = Calendar.SUNDAY;

    protected TextPaint mMonthPaint;
    protected TextPaint dayTextPaint;

    protected NumberFormat mDayFormatter;

    public static final int MonthViewTypeShowCalendar = 0;
    public static final int MonthViewTypeChangeDays = 1;
    public static final int MonthViewTypesetStartDay = 2;

    public static final int TYPE_RED = 0;
    public static final int TYPE_GREEN = 1;
    public static final int TYPE_GREEN2 = 2;
    public static final int TYPE_GRAY = 3;
    public static final int TYPE_YELLOW = 4;
    public static final int TYPE_NOTE = 5;

    //rectangle colors
    protected Paint rectTypeGrayPaint;
    protected int rectTypeGrayColor;
    protected int tvMonthDayNumberTextColorWhite;
    protected int tvMonthDayNumberTextColorBlack;

    protected int monthViewBkgColor;

    // Desired dimensions.
    protected int mDesiredMonthHeight;
    protected int mDesiredDayHeight;
    protected int mDesiredCellWidth;

    // Dimensions as laid out.
    protected int mMonthHeight;
    protected int mDayHeight;
    protected int mCellWidth;

    protected int mPaddedWidth;
    protected int mPaddedHeight;


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


    protected Calendar mCalendar = Calendar.getInstance();
    protected PersianCalendar persianCalendar = new PersianCalendar();
    protected UmmalquraCalendar hijriCalendar = new UmmalquraCalendar();
    protected PersianDate persianDate = new PersianDate();
    protected Locale mLocale;

    protected OnDayClickListener mOnDayClickListener;
    protected ClueData clueData;
    private int WEEKS_IN_MONTH = MAX_WEEKS_IN_MONTH;

    protected static class ClueData {
        private static final int DEFAULT_RED_COUNT = 4;
        private static final int DEFAULT_GRAY_COUNT = 24;
        private static final int DEFAULT_GREEN_COUNT = 3;
        private static final int DEFAULT_YELLOW_COUNT = 5;
        private static final int DEFAULT_GREEN2_INDEX = 7;

        private int redCount = DEFAULT_RED_COUNT;
        private int grayCount = DEFAULT_GRAY_COUNT;
        private int greenStartIndex = DEFAULT_GREEN2_INDEX - 1;
        private int greenEndIndex = DEFAULT_GREEN2_INDEX + 1;
        private int yellowCount = DEFAULT_YELLOW_COUNT;
        private int yellowStartIndex = -1;
        private int yellowEndIndex = -1;
        private int green2Index = DEFAULT_GREEN2_INDEX;
        private int totalDays = 0;
        private Calendar startDate;

        public ClueData(int redCount, int grayCount, int yellowCount, Calendar startDate) {
            this.startDate = startDate;
            this.redCount = redCount;
            this.grayCount = grayCount;
            this.yellowCount = yellowCount;
            this.totalDays = redCount + grayCount;
            switch (grayCount) {
                case 21:
                    green2Index = redCount + 7;
                    break;
                case 22:
                    green2Index = redCount + 8;
                    break;
                case 23:
                    green2Index = redCount + 9;
                    break;
                case 24:
                    green2Index = redCount + 10;
                    break;
                case 25:
                    green2Index = redCount + 11;
                    break;
                case 26:
                    green2Index = redCount + 12;
                    break;
                case 27:
                    green2Index = redCount + 13;
                    break;
                case 28:
                    green2Index = redCount + 14;
                    break;
                default:
                    Log.d(TAG, "ClueData: invalid cycle length ");
            }
            greenStartIndex = green2Index - 1;
            greenEndIndex = green2Index + 1;
            yellowStartIndex = totalDays - yellowCount + 1;
            yellowEndIndex = totalDays;
        }

        public ClueData(int redCount, int grayCount, Calendar startDate) {
            this.redCount = redCount;
            this.grayCount = grayCount;
            this.yellowCount = DEFAULT_YELLOW_COUNT;
            this.totalDays = redCount + grayCount;
            switch (grayCount) {
                case 21:
                    green2Index = redCount + 7;
                    break;
                case 22:
                    green2Index = redCount + 8;
                    break;
                case 23:
                    green2Index = redCount + 9;
                    break;
                case 24:
                    green2Index = redCount + 10;
                    break;
                case 25:
                    green2Index = redCount + 11;
                    break;
                case 26:
                    green2Index = redCount + 12;
                    break;
                case 27:
                    green2Index = redCount + 13;
                    break;
                case 28:
                    green2Index = redCount + 14;
                    break;
                default:
                    Log.d(TAG, "ClueData: invalid cycle length ");
            }
            greenStartIndex = green2Index - 1;
            greenEndIndex = green2Index + 1;
            yellowStartIndex = totalDays - yellowCount + 1;
            yellowEndIndex = totalDays;
        }

        public ClueData() {
        }

        public int getRedCount() {
            return redCount;
        }

        public void setRedCount(int redCount) {
            this.redCount = redCount;
        }

        public int getGrayCount() {
            return grayCount;
        }

        public void setGrayCount(int grayCount) {
            this.grayCount = grayCount;
        }

        public int getYellowCount() {
            return yellowCount;
        }

        public void setYellowCount(int yellowCount) {
            this.yellowCount = yellowCount;
        }

        public int getGreen2Index() {
            return green2Index;
        }

        public void setGreen2Index(int green2Index) {
            this.green2Index = green2Index;
        }

        public int getTotalDays() {
            return totalDays;
        }

        public void setTotalDays(int totalDays) {
            this.totalDays = totalDays;
        }

        public int getGreenStartIndex() {
            return greenStartIndex;
        }

        public void setGreenStartIndex(int greenStartIndex) {
            this.greenStartIndex = greenStartIndex;
        }

        public int getGreenEndIndex() {
            return greenEndIndex;
        }

        public void setGreenEndIndex(int greenEndIndex) {
            this.greenEndIndex = greenEndIndex;
        }

        public int getYellowStartIndex() {
            return yellowStartIndex;
        }

        public void setYellowStartIndex(int yellowStartIndex) {
            this.yellowStartIndex = yellowStartIndex;
        }

        public int getYellowEndIndex() {
            return yellowEndIndex;
        }

        public void setYellowEndIndex(int yellowEndIndex) {
            this.yellowEndIndex = yellowEndIndex;
        }

        public Calendar getStartDate() {
            return startDate;
        }

        public void setStartDate(Calendar startDate) {
            this.startDate = startDate;
        }
    }

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

    protected void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseMonthView);
        Resources resources = getResources();

        //circle colors
        rectTypeGrayColor = typedArray.getColor(R.styleable.BaseMonthView_rect_color_type3, resources.getColor(R.color.my_type_gray));
        monthViewBkgColor = resources.getColor(R.color.white);

        mDesiredMonthHeight = resources.getDimensionPixelSize(R.dimen.month_view_month_height);
        mDesiredDayHeight = resources.getDimensionPixelSize(R.dimen.month_view_day_height);
        mDesiredCellWidth = resources.getDimensionPixelSize(R.dimen.month_view_day_width);
        mLocale = resources.getConfiguration().locale;


        //main day number textView
        tvMonthDayNumberTextColorWhite = resources.getColor(R.color.white);
        tvMonthDayNumberTextColorBlack = resources.getColor(R.color.black);

        typedArray.recycle();
    }

    protected void init() {

        mCalendar = Calendar.getInstance(mLocale);
        persianCalendar = new PersianCalendar(mCalendar.getTimeInMillis());
        hijriCalendar = new UmmalquraCalendar();

        mDayFormatter = NumberFormat.getIntegerInstance(mLocale);

        updateMonthYearLabel();

        initPaints();
    }

    protected void initPaints() {
        final Resources res = getResources();

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int padStart = ViewCompat.getPaddingStart(this);
        int padEnd = ViewCompat.getPaddingEnd(this);


        int preferredHeight = (int) ((mDesiredDayHeight * WEEKS_IN_MONTH + mDesiredMonthHeight
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

    @Override
    protected void onDraw(Canvas canvas) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        canvas.translate(paddingLeft, paddingTop);

        drawMonth(canvas);
        drawDays(canvas);

        canvas.translate(-paddingLeft, -paddingTop);

        setBackgroundColor(monthViewBkgColor);

    }

    protected void drawMonth(Canvas canvas) {
        final float x = mPaddedWidth / 2f;

        // Vertically centered within the month header height.
        final float lineHeight = mMonthPaint.ascent() + mMonthPaint.descent();
        final float y = (mMonthHeight + lineHeight);

        canvas.drawText(mMonthYearLabel, x, y, mMonthPaint);
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
            if (shouldBeRTL()) {
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

    protected Paint getDayPaint(int day) {
        return rectTypeGrayPaint;
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
            mWeekStart = Calendar.SATURDAY;
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

        initPaints();

        mEnabledDayStart = mathConstrain(enabledDayStart, 1, mDaysInMonth);
        mEnabledDayEnd = mathConstrain(enabledDayEnd, mEnabledDayStart, mDaysInMonth);

        updateMonthYearLabel();
        if (CalendarTool.getDaysInMonth(month, year, calendarType) <= 30) {
            switch (mDayOfWeekStart) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    WEEKS_IN_MONTH = 5;
                    break;
                case 6:
                    WEEKS_IN_MONTH = 6;
                    break;
            }
        } else if (CalendarTool.getDaysInMonth(month, year, calendarType) == 31) {
            switch (mDayOfWeekStart) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    WEEKS_IN_MONTH = 5;
                    break;
                case 5:
                case 6:
                    WEEKS_IN_MONTH = 6;
                    break;
            }
        }
//        requestLayout();
        postInvalidate();
    }

    protected boolean sameDay(int day, Calendar today) {
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

    protected boolean onDayClicked(int day) {
//        if (!isValidDayOfMonth(day) || !isDayEnabled(day)) {
//            return false;
//        }

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
        }
//        Toast.makeText(getContext(), "clicked " + day, Toast.LENGTH_SHORT).show();
        return true;
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

    PersianCalendar p = new PersianCalendar();

    protected int getDayType(int day) {
        if (day == -1) {
            return TYPE_RED;
        }
        if (clueData == null) {
            return TYPE_RED;
        }
        long days = 0L;
        switch (calendarType) {
            case CalendarType.PERSIAN:
                PersianCalendar p = new PersianCalendar();
                p.setPersianDate(persianCalendar.getPersianYear(), persianCalendar.getPersianMonth() + 1, day);
                days = CalendarTool.getDaysFromDiff(p, clueData.getStartDate());
                break;
            case CalendarType.GREGORIAN:
                break;
            case CalendarType.ARABIC:
                break;
        }
        if (days >= 0) {
            day = (int) ((days) % clueData.getTotalDays()) + 1;
        } else {
            return TYPE_GRAY;
        }
        if (day <= clueData.redCount) {
            return TYPE_RED;
        } else if (day <= clueData.totalDays) {
            if (day >= clueData.greenStartIndex && day <= clueData.greenEndIndex) {
                if (day == clueData.green2Index) {
                    return TYPE_GREEN2;
                }
                return TYPE_GREEN;
            } else if (day >= clueData.yellowStartIndex && day <= clueData.yellowEndIndex) {
                return TYPE_YELLOW;
            }
        }
        return TYPE_GRAY;
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


    /**
     * Handles callbacks when the user clicks on a time object.
     */
    public interface OnDayClickListener {
        void onDayClick(BaseMonthView view, Calendar day);
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
}
