package com.simorgh.cluecalendar.view;

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
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import com.simorgh.cluecalendar.R;
import com.simorgh.cluecalendar.hijricalendar.UmmalquraCalendar;
import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.persiancalendar.PersianCalendar;
import com.simorgh.cluecalendar.persiancalendar.PersianDate;
import com.simorgh.cluecalendar.util.CalendarTool;

import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;

public class ShowInfoMonthView extends BaseMonthView {
    public static final String TAG = "monthView";
    private Paint dayBkgPaint;
    private Paint mDaySelectorPaint;


    //rectangle colors
    private Paint rectTypeRedPaint;
    private Paint rectTypeGreenPaint;
    private Paint rectTypeGreen2Paint;
    private Paint rectTypeYellowPaint;
    private Paint rectTypeMarkedPaint;
    private int rectTypeRedColor;
    private int rectTypeGreenColor;
    private int rectTypeGreen2Color;
    private int rectTypeYellowColor;
    private int rectTypeMarkedColor;
    private int tvMonthDayNumberTextColorWhite;
    private int tvMonthDayNumberTextColorBlack;

    //Marked Triangle
    private Path markedPath;


    private final float CELL_LENGTH_RATIO = 8.2f;
    private final float CELL_MARGIN_RATIO = 7.5f;
    private final float MONTH_NAME_HEIGHT_RATIO = 8.8f;
    private final float BOTTOM_MARGIN_RATIO = 24f;
    private final int MIN_WIDTH = 360;
    private int bottom_margin = -1;

    public static class ClueData {
        private static final int DEFAULT_RED_COUNT = 4;
        private static final int DEFAULT_GRAY_COUNT = 24;
        private static final int DEFAULT_GREEN_COUNT = 3;
        private static final int DEFAULT_YELLOW_COUNT = 2;
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

        public ClueData(int redCount, int grayCount, int yellowCount) {
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

        public ClueData(int redCount, int grayCount) {
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
    }

    public ShowInfoMonthView(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public ShowInfoMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public ShowInfoMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShowInfoMonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        init();
    }

    @Override
    protected void init() {
        super.init();
        clueData = new BaseMonthView.ClueData(3, 25,2);
    }

    @Override
    protected void initAttrs(Context context, AttributeSet attrs) {
        super.initAttrs(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseMonthView);
        Resources resources = getResources();

        //circle colors
        rectTypeRedColor = typedArray.getColor(R.styleable.BaseMonthView_rect_color_type1, resources.getColor(R.color.type_red));
        rectTypeGreenColor = typedArray.getColor(R.styleable.BaseMonthView_rect_color_type2, resources.getColor(R.color.type_green));
        rectTypeGreen2Color = typedArray.getColor(R.styleable.BaseMonthView_rect_color_type2, resources.getColor(R.color.type_green));
        rectTypeYellowColor = typedArray.getColor(R.styleable.BaseMonthView_rect_color_type4, resources.getColor(R.color.type_yellow));
        rectTypeMarkedColor = resources.getColor(R.color.type_marked);



        //main day number textView
        tvMonthDayNumberTextColorWhite = resources.getColor(R.color.white);
        tvMonthDayNumberTextColorBlack = resources.getColor(R.color.black);

        typedArray.recycle();
    }

    @Override
    protected void initPaints() {
        super.initPaints();

        final Resources res = getResources();


        mDaySelectorPaint = new Paint();
        mDaySelectorPaint.setAntiAlias(true);
        mDaySelectorPaint.setStyle(Paint.Style.FILL);
        mDaySelectorPaint.setStrokeWidth(3);
        mDaySelectorPaint.setColor(Color.WHITE);


        dayBkgPaint = new Paint();
        dayBkgPaint.setAntiAlias(true);
        dayBkgPaint.setStyle(Paint.Style.FILL);

        rectTypeRedPaint = new Paint();
        rectTypeRedPaint.setAntiAlias(true);
        rectTypeRedPaint.setStyle(Paint.Style.FILL);
        rectTypeRedPaint.setColor(rectTypeRedColor);

        rectTypeGrayPaint = new Paint();
        rectTypeGrayPaint.setAntiAlias(true);
        rectTypeGrayPaint.setStyle(Paint.Style.FILL);
        rectTypeGrayPaint.setColor(rectTypeGrayColor);

        rectTypeGreenPaint = new Paint();
        rectTypeGreenPaint.setAntiAlias(true);
        rectTypeGreenPaint.setStyle(Paint.Style.FILL);
        rectTypeGreenPaint.setColor(rectTypeGreenColor);

        rectTypeGreen2Paint = new Paint();
        rectTypeGreen2Paint.setAntiAlias(true);
        rectTypeGreen2Paint.setStyle(Paint.Style.FILL);
        rectTypeGreen2Paint.setColor(rectTypeGreen2Color);

        rectTypeYellowPaint = new Paint();
        rectTypeYellowPaint.setAntiAlias(true);
        rectTypeYellowPaint.setStyle(Paint.Style.FILL);
        rectTypeYellowPaint.setColor(rectTypeYellowColor);

        rectTypeMarkedPaint = new Paint();
        rectTypeMarkedPaint.setAntiAlias(true);
        rectTypeMarkedPaint.setStyle(Paint.Style.FILL);
        rectTypeMarkedPaint.setColor(rectTypeMarkedColor);

    }

//    @Override
//    protected void drawDays(Canvas canvas) {
//        super.drawDays(canvas);
//        final TextPaint p = dayTextPaint;
//        final int headerHeight = mMonthHeight;
//        final int rowHeight = mDayHeight;
//        final int colWidth = mCellWidth;
//
//        // Vertically centered within the month header height.
//        final float lineHeight = mMonthPaint.ascent() + mMonthPaint.descent();
//        final float y = (mMonthHeight + lineHeight) * 3f + dp2px(20);
//
//        int rowCenter = (int) (y + rowHeight / 2);
//        int left;
//        int right;
//        int top;
//        int bottom;
//
//        Log.d(TAG, "drawDays: " + findDayOffset());
//
//        for (int day = 1, col = findDayOffset(); day <= mDaysInMonth; day++) {
//            final int colCenter = colWidth * col + colWidth / 2;
//            final int colCenterRtl;
//            if (shouldBeRTL()) {
//                colCenterRtl = mPaddedWidth - colCenter;
//            } else {
//                colCenterRtl = colCenter;
//            }
//
//
//            final boolean isDayEnabled = isDayEnabled(day);
//            final boolean isDayActivated = mActivatedDay == day;
//            final boolean isDayToday = mToday == day;
//
//            top = (int) (rowCenter - rowHeight / 2 + dp2px(3));
//            bottom = (int) (rowCenter + rowHeight / 2 - dp2px(3));
//            left = (int) (colCenterRtl - colWidth / 2 + dp2px(3));
//            right = (int) (colCenterRtl + colWidth / 2 - dp2px(3));
//            if (isDayEnabled) {
//
//            }
//            if (isDayActivated) {
//
//            }
//            if (isDayToday && !isDayActivated) {
//            } else if (isDayToday) {
//            } else {
//                if (isDayEnabled && isDayActivated) {
//                } else {
//                }
//            }
//            Log.d(TAG, "drawDays: " + getDayType(day));
//            canvas.drawRect(left, top, right, bottom, getDayPaint(day));
//            if (getDayType(day) == TYPE_GRAY) {
//                dayTextPaint.setColor(tvMonthDayNumberTextColorBlack);
//            } else {
//                dayTextPaint.setColor(tvMonthDayNumberTextColorWhite);
//            }
//            canvas.drawText(mDayFormatter.format(day), right - p.getFontMetrics().descent - dp2px(6),
//                    bottom - p.getFontMetrics().bottom, p);
//
//            if (isDayMarked(day)) {
//                markedPath.moveTo(left, top);
//                markedPath.lineTo(left + dp2px(14), top);
//                markedPath.lineTo(left, top + dp2px(14));
//                markedPath.lineTo(left, top);
//                canvas.drawPath(markedPath, rectTypeMarkedPaint);
//            }
//
//            if (getDayType(day) == TYPE_GREEN2) {
//                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
//                canvas.drawBitmap(icon, left + dp2px(7), top + dp2px(7), rectTypeGreenPaint);
//            }
//            col++;
//            if (col == DAYS_IN_WEEK) {
//                col = 0;
//                rowCenter += rowHeight;
//            }
//        }
//    }

    private boolean isDayMarked(int day) {
        if (!isValidDayOfMonth(day)) {
            return false;
        }
        //todo is day marked
//        if (day % 5 == 0) {
//            return true;
//        }
        return false;
    }


    public static int mathConstrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    @Override
    protected Paint getDayPaint(int day) {
        if (day == -1) {
            return rectTypeGrayPaint;
        }
        if (clueData == null) {
            return rectTypeRedPaint;
        }
        if (day <= clueData.getRedCount()) {
            return rectTypeRedPaint;
        } else if (day <= clueData.getTotalDays()) {
            if (day >= clueData.getGreenStartIndex() && day <= clueData.getGreenEndIndex()) {
                if (day == clueData.getGreen2Index()) {
                    return rectTypeGreenPaint;
                }
                return rectTypeGreenPaint;
            } else if (day >= clueData.getYellowStartIndex() && day <= clueData.getYellowEndIndex()) {
                return rectTypeYellowPaint;
            }
        }
        return rectTypeGrayPaint;
    }

}
