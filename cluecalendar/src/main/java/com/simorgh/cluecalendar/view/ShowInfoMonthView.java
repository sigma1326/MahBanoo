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
import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.persiancalendar.PersianCalendar;
import com.simorgh.cluecalendar.util.CalendarTool;

import java.util.Calendar;

import androidx.annotation.Nullable;

public class ShowInfoMonthView extends BaseMonthView {
    public static final String TAG = "monthView";
    private Paint dayBkgPaint;
    private Paint mDaySelectorPaint;


    //rectangle colors
    private Paint rectTypeRedPaint;
    private Paint rectTypeGreenPaint;
    private Paint rectTypeYellowPaint;
    private Paint rectTypeMarkedPaint;
    private int rectTypeRedColor;
    private int rectTypeGreenColor;
    private int rectTypeGreen2Color;
    private int rectTypeYellowColor;
    private int rectTypeMarkedColor;

    //Marked Triangle
    private Path markedPath;

    private static Bitmap icon;
    private IsDayMarkedListener isDayMarkedListener;

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
        clueData = new BaseMonthView.ClueData(5, 27, 3, Calendar.getInstance());
    }

    @Override
    protected void initAttrs(Context context, AttributeSet attrs) {
        super.initAttrs(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseMonthView);
        Resources resources = getResources();

        //circle colors
        rectTypeRedColor = resources.getColor(R.color.type_red);
        rectTypeGreenColor = resources.getColor(R.color.type_green);
        rectTypeGreen2Color = resources.getColor(R.color.type_green);
        rectTypeYellowColor = resources.getColor(R.color.type_yellow);
        rectTypeMarkedColor = resources.getColor(R.color.type_marked);


        typedArray.recycle();
    }

    @Override
    protected void initPaints() {
        super.initPaints();

        markedPath = new Path();

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

        rectTypeYellowPaint = new Paint();
        rectTypeYellowPaint.setAntiAlias(true);
        rectTypeYellowPaint.setStyle(Paint.Style.FILL);
        rectTypeYellowPaint.setColor(rectTypeYellowColor);

        rectTypeMarkedPaint = new Paint();
        rectTypeMarkedPaint.setAntiAlias(true);
        rectTypeMarkedPaint.setStyle(Paint.Style.FILL);
        rectTypeMarkedPaint.setColor(rectTypeMarkedColor);

        icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
    }

    @Override
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
            int dayType = getDayType(day);
            if (dayType == TYPE_GRAY) {
                dayTextPaint.setColor(tvMonthDayNumberTextColorBlack);
            } else {
                dayTextPaint.setColor(tvMonthDayNumberTextColorWhite);
            }
            canvas.drawText(mDayFormatter.format(day), right - p.getFontMetrics().descent - dp2px(6),
                    bottom - p.getFontMetrics().bottom, p);

            if (isDayMarked(day)) {
                markedPath.moveTo(left, top);
                markedPath.lineTo(left + dp2px(14), top);
                markedPath.lineTo(left, top + dp2px(14));
                markedPath.lineTo(left, top);
                canvas.drawPath(markedPath, rectTypeMarkedPaint);
            }

            if (dayType == TYPE_GREEN2) {
                canvas.drawBitmap(icon, left + dp2px(7), top + dp2px(7), rectTypeGreenPaint);
            }

            col++;
            if (col == DAYS_IN_WEEK) {
                col = 0;
                rowCenter += rowHeight;
            }
        }
    }

    private boolean isDayMarked(int day) {
        if (!isValidDayOfMonth(day)) {
            return false;
        }
        if (isDayMarkedListener != null) {
            return isDayMarkedListener.isDayMarked(day);
        }
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
            return rectTypeGrayPaint;
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
            return rectTypeGrayPaint;
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

    public IsDayMarkedListener getIsDayMarkedListener() {
        return isDayMarkedListener;
    }

    public void setIsDayMarkedListener(IsDayMarkedListener isDayMarkedListener) {
        this.isDayMarkedListener = isDayMarkedListener;
    }

    public interface IsDayMarkedListener{
        boolean isDayMarked(int day);
    }
}
