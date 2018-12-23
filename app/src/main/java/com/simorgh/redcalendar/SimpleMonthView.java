package com.simorgh.redcalendar;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.simorgh.cluecalendar.hijricalendar.UmmalquraCalendar;
import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.persiancalendar.PersianCalendar;
import com.simorgh.cluecalendar.persiancalendar.PersianDate;
import com.simorgh.cluecalendar.util.CalendarTool;
import com.simorgh.cluecalendar.util.SizeConverter;
import com.simorgh.cluecalendar.util.Utils;
import com.simorgh.cluecalendar.view.BaseMonthView;
import com.simorgh.cycleutils.ClueData;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class SimpleMonthView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private int mViewWidth;
    private int mViewHeight;
    private Paint paint;
    private Context context;
    private Thread thread = null;
    volatile boolean running = false;
    private int i;

    public static final String TAG = "baseMonthView";
    protected int calendarType;
    protected int monthViewType;
    protected static final int DAYS_IN_WEEK = 7;
    protected static final int MAX_WEEKS_IN_MONTH = 6;

    protected static final int DEFAULT_SELECTED_DAY = -1;
    protected static final int DEFAULT_WEEK_START = Calendar.SUNDAY;

    private int weeksInMonth = MAX_WEEKS_IN_MONTH;


    protected TextPaint mMonthPaint;
    protected TextPaint dayTextPaint;

    protected NumberFormat mDayFormatter;

    public static final int MonthViewTypeShowCalendar = 0;
    public static final int MonthViewTypeChangeDays = 1;
    public static final int MonthViewTypeSetStartDay = 2;

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
    private Paint rectTypeRedPaint;
    private int rectTypeRedColor;
    private Paint rectTypeGreenPaint;
    private int rectTypeGreenColor;


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


    protected int selectedDay = -1;
    protected int highlightedDay = -1;
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
    protected Calendar date = Calendar.getInstance();

    protected PersianCalendar p = new PersianCalendar();
    protected UmmalquraCalendar hijri = new UmmalquraCalendar();


    protected BaseMonthView.OnDayClickListener mOnDayClickListener;
    protected BaseMonthView.OnDaySelectedListener onDaySelectedListener;
    protected BaseMonthView.IsDaySelectedListener isDaySelectedListener;
    protected BaseMonthView.IsDayInRangeSelectedListener isDayInRangeSelectedListener;

    protected ClueData clueData;

    public SimpleMonthView(Context context) {
        super(context);
        init();
    }

    public SimpleMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleMonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        context = getContext();
        running = true;
//        thread = new Thread();
//        thread.start();
        getHolder().addCallback(this);
        Resources resources = getResources();


        //circle colors
        rectTypeGrayColor = resources.getColor(com.simorgh.cluecalendar.R.color.my_type_gray);
        rectTypeRedColor = resources.getColor(com.simorgh.cluecalendar.R.color.type_red);
        rectTypeGreenColor = resources.getColor(com.simorgh.cluecalendar.R.color.type_green);
        monthViewBkgColor = resources.getColor(com.simorgh.cluecalendar.R.color.white);

        mDesiredMonthHeight = resources.getDimensionPixelSize(com.simorgh.cluecalendar.R.dimen.month_view_month_height);
        mDesiredDayHeight = resources.getDimensionPixelSize(com.simorgh.cluecalendar.R.dimen.month_view_day_height);
        mDesiredCellWidth = resources.getDimensionPixelSize(com.simorgh.cluecalendar.R.dimen.month_view_day_width);
        mLocale = resources.getConfiguration().locale;


        //main day number textView
        tvMonthDayNumberTextColorWhite = resources.getColor(com.simorgh.cluecalendar.R.color.white);
        tvMonthDayNumberTextColorBlack = resources.getColor(com.simorgh.cluecalendar.R.color.black);

        mDaysInMonth = 31;
        mDayOfWeekStart = 1;

        mCalendar = Calendar.getInstance(mLocale);
        persianCalendar = new PersianCalendar(mCalendar.getTimeInMillis());
        hijriCalendar = new UmmalquraCalendar();

        mDayFormatter = NumberFormat.getIntegerInstance(mLocale);

        initPaints();

    }

    protected void initPaints() {
        final Resources res = getResources();

        final Typeface monthTypeface = Utils.getTypeFace(getContext(), 1);
        final Typeface dayTypeface = Utils.getTypeFace(getContext(), 1);

        final int monthTextSize = res.getDimensionPixelSize(com.simorgh.cluecalendar.R.dimen.month_view_month_text_size);
        final int dayTextSize = res.getDimensionPixelSize(com.simorgh.cluecalendar.R.dimen.month_view_day_text_size);

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


        rectTypeRedPaint = new Paint();
        rectTypeRedPaint.setAntiAlias(true);
        rectTypeRedPaint.setStyle(Paint.Style.FILL);
        rectTypeRedPaint.setColor(rectTypeRedColor);

        rectTypeGreenPaint = new Paint();
        rectTypeGreenPaint.setAntiAlias(true);
        rectTypeGreenPaint.setStyle(Paint.Style.FILL);
        rectTypeGreenPaint.setColor(rectTypeGreenColor);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;

        // Set font size proportional to view size.
        paint.setTextSize(mViewWidth / 5f);
        paint.setColor(Color.WHITE);
    }

    public void run() {

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


    @SuppressLint("NewApi")
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
            if (day == selectedDay) {
                canvas.drawRoundRect(left, top, right, bottom, 10, 10, rectTypeRedPaint);
            } else if (day == highlightedDay) {
                canvas.drawRoundRect(left, top, right, bottom, 10, 10, rectTypeGreenPaint);
            } else {
                canvas.drawRoundRect(left, top, right, bottom, 10, 10, rectTypeGrayPaint);
            }
            if (day == selectedDay || day == highlightedDay) {
                dayTextPaint.setColor(tvMonthDayNumberTextColorWhite);
            } else {
                dayTextPaint.setColor(tvMonthDayNumberTextColorBlack);
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

    protected int getDayType(int day) {
        if (day == -1) {
            return TYPE_GRAY;
        }
        if (clueData == null) {
            return TYPE_GRAY;
        }
        long days = 0L;
        switch (calendarType) {
            case CalendarType.PERSIAN:
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
        if (day <= clueData.getRedCount()) {
            return TYPE_RED;
        } else if (day <= clueData.getTotalDays()) {
            if (day >= clueData.getGreenStartIndex() && day <= clueData.getGreenEndIndex()) {
                if (day == clueData.getGreen2Index()) {
                    return TYPE_GREEN2;
                }
                return TYPE_GREEN;
            } else if (day >= clueData.getYellowStartIndex() && day <= clueData.getYellowEndIndex()) {
                return TYPE_YELLOW;
            }
        }
        return TYPE_GRAY;
    }

    protected int findDayOffset() {
        final int offset = mDayOfWeekStart - mWeekStart;
        if (mDayOfWeekStart < mWeekStart) {
            return offset + DAYS_IN_WEEK;
        }
        return offset;
    }

    protected Paint getDayPaint(int day) {
        return rectTypeGrayPaint;
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

                highlightedDay = getDayAtLocation(x, y);

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
        running = true;
        surfaceChanged(getHolder(), 0, 0, 0);
        return true;
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

        if (CalendarTool.getDaysInMonth(month, year, calendarType) <= 30) {
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
                p = CalendarTool.GregorianToPersian(today);
                todayDay = p.getPersianDay();
                todayMonth = p.getPersianMonth();
                todayYear = p.getPersianYear();
                compYear = mYearPersian;
                compMonth = mMonthPersian;
                break;
            case CalendarType.ARABIC:
                hijri = CalendarTool.GregorianToHijri(today);
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

    protected static int mathConstrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
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

    protected boolean isValidDayOfMonth(int day) {
        return day >= 1 && day <= mDaysInMonth;
    }

    protected static boolean isValidDayOfWeek(int day) {
        return day >= Calendar.SUNDAY && day <= Calendar.SATURDAY;
    }

    protected static boolean isValidMonth(int month) {
        return month >= 0 && month <= 11;
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
        }
        selectedDay = day;
//        Toast.makeText(getContext(), "clicked " + day, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        running = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        running = true;
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    Canvas canvas;

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        while (running) {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            paint.setColor(Color.RED);
            canvas.drawColor(Color.WHITE);
//                canvas.drawText("Hello", mViewWidth / 2f - paint.getFontMetrics().leading, mViewHeight / 2f, paint);
            drawDays(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
//        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
