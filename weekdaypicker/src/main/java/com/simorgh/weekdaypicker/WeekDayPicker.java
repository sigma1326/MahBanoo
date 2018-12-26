package com.simorgh.weekdaypicker;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
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

import com.simorgh.cycleutils.CycleData;

import java.util.Calendar;

import androidx.annotation.Nullable;

public class WeekDayPicker extends View {
    //default view height and width
    private static final int DEFAULT_HEIGHT = 56;
    private static final int DEFAULT_WIDTH = 360;

    private float realHeight = -1;
    private float realWidth = -1;

    private Typeface typeface;

    private float circleRadius = -1;
    private float dotCircleRadius = -1;

    private Paint circlePaint;
    private int selectedDayCircleColor;

    private TextPaint selectedDayTextPaint;
    private int selectedDayTextColor;
    private TextPaint unSelectedDayTextPaint;
    private int unSelectedDayTextColor;
    private float dayTextSize = -1;

    private CycleData cycleData;

    private Calendar selectedDate = Calendar.getInstance();
    private Calendar todayDate = Calendar.getInstance();
    private Calendar temp = Calendar.getInstance();


    private String[] days = new String[7];

    private static Calendar in = Calendar.getInstance();
    private static Calendar start = Calendar.getInstance();
    private Calendar weekStartDate = Calendar.getInstance();

    private onDaySelectedListener onDaySelectedListener;

    public WeekDayPicker(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public WeekDayPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public WeekDayPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WeekDayPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeekDayPicker);
        Resources resources = getResources();


        selectedDayCircleColor = typedArray.getColor(R.styleable.WeekDayPicker_selectedCircleColor, resources.getColor(R.color.selectedCircleColor));
        selectedDayTextColor = typedArray.getColor(R.styleable.WeekDayPicker_selectedTextColor, resources.getColor(R.color.selectedTextColor));
        unSelectedDayTextColor = typedArray.getColor(R.styleable.WeekDayPicker_unSelectedTextColor, resources.getColor(R.color.unSelectedTextColor));

        dayTextSize = typedArray.getDimension(R.styleable.WeekDayPicker_dayTextSize, resources.getDimension(R.dimen.dayTextSize));

        days[0] = resources.getString(R.string.saturday);
        days[1] = resources.getString(R.string.sunday);
        days[2] = resources.getString(R.string.monday);
        days[3] = resources.getString(R.string.tuesday);
        days[4] = resources.getString(R.string.wednesday);
        days[5] = resources.getString(R.string.thursday);
        days[6] = resources.getString(R.string.friday);

        typedArray.recycle();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(selectedDayCircleColor);

        AssetManager assetMgr = getContext().getAssets();
        typeface = Typeface.createFromAsset(assetMgr, "fonts/iransans_medium.ttf");

        selectedDayTextPaint = new TextPaint();
        selectedDayTextPaint.setAntiAlias(true);
        selectedDayTextPaint.setStyle(Paint.Style.FILL);
        selectedDayTextPaint.setTextSize(dayTextSize);
        selectedDayTextPaint.setTextAlign(Paint.Align.CENTER);
        selectedDayTextPaint.setColor(selectedDayTextColor);
        selectedDayTextPaint.setTypeface(typeface);

        unSelectedDayTextPaint = new TextPaint();
        unSelectedDayTextPaint.setAntiAlias(true);
        unSelectedDayTextPaint.setStyle(Paint.Style.FILL);
        unSelectedDayTextPaint.setTextSize(dayTextSize);
        unSelectedDayTextPaint.setTextAlign(Paint.Align.CENTER);
        unSelectedDayTextPaint.setColor(unSelectedDayTextColor);
        unSelectedDayTextPaint.setTypeface(typeface);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minWidth = (int) dp2px(DEFAULT_WIDTH);
        int minHeight = (int) dp2px(DEFAULT_HEIGHT);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(minWidth, widthSize);
        } else {
            // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
            // If width is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make width equal to height
            height = heightSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(minHeight, heightSize);
        } else {
            // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
            // If height is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make height equal to width
            width = widthSize;
        }

        int paddingHeight = getPaddingBottom() + getPaddingTop();
        int paddingWidth = getPaddingRight() + getPaddingLeft();
        if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            width = minWidth;
            height = minHeight;
        }

        setMeasuredDimension((width + paddingWidth), (height + paddingHeight));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        realWidth = px2dp(getWidth());
        realHeight = px2dp(getHeight());
        circleRadius = (realHeight - 2) / 2f;
        dotCircleRadius = (realHeight - 2) / 23f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        temp.setTimeInMillis(weekStartDate.getTimeInMillis());
        if (isInEditMode()) {
            calculateWeekStart(selectedDate);
            cycleData = new CycleData(5, 26, 3, Calendar.getInstance());
        }
        if (cycleData == null) {
            return;
        }
        float y;
        y = dp2px(realHeight / 2f);
        float radius = dp2px(circleRadius);
        float dotRadius = dp2px(dotCircleRadius);
        int i = 0;
        for (float x = dp2px(realWidth) - dp2px(realWidth) / 14f; x > 0; x -= dp2px(realWidth) / 7f, i++) {
            int day = (int) getDaysFromDiff(temp, cycleData.getCurrentCycleStart(Calendar.getInstance()));
            day = day % cycleData.getTotalDays() + 1;
            if (getDaysFromDiff(temp, selectedDate) == 0) {
                canvas.drawCircle(x, y, radius, circlePaint);
                canvas.drawText(days[i], x, y + dp2px(10), selectedDayTextPaint);
                if (day != 0) {
                    canvas.drawText(day + "", x, y - dp2px(10), selectedDayTextPaint);
                }
                if (getDaysFromDiff(temp, todayDate) == 0) {
                    canvas.drawCircle(x, y + dp2px(20), dotRadius, selectedDayTextPaint);
                }
            } else {
                canvas.drawText(days[i], x, y + dp2px(10), unSelectedDayTextPaint);
                if (day != 0) {
                    canvas.drawText(day + "", x, y - dp2px(10), unSelectedDayTextPaint);
                }
                if (getDaysFromDiff(temp, todayDate) == 0) {
                    canvas.drawCircle(x, y + dp2px(20), dotRadius, circlePaint);
                }
            }
            temp.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float y = dp2px(realHeight / 2f);
                float radius = dp2px(circleRadius);
                int i = 0;
                for (float x = dp2px(realWidth) - dp2px(realWidth) / 14f; x > 0; x -= dp2px(realWidth) / 7f, i++) {
                    if (Math.sqrt(Math.pow(Math.abs(event.getX() - x), 2) + Math.pow(Math.abs(event.getY() - y), 2)) < radius) {
                        selectedDate.setTimeInMillis(weekStartDate.getTimeInMillis());
                        selectedDate.add(Calendar.DAY_OF_MONTH, i);
                        if (onDaySelectedListener != null) {
                            onDaySelectedListener.onDaySelected(selectedDate);
                        }
                        break;
                    }
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                invalidate();
                return true;
            default:
                return false;
        }
    }

    public static long getDaysFromDiff(Calendar input, Calendar startDate) {
        float diffDays;
        in.clear();
        in.set(input.get(Calendar.YEAR), input.get(Calendar.MONTH), input.get(Calendar.DAY_OF_MONTH));
        start.clear();
        start.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
        if (in.compareTo(start) == 0) {
            return 0L;
        }
        if (in.before(start)) {
            return -1L;
        }
        diffDays = (float) (in.getTimeInMillis() - start.getTimeInMillis()) / (60 * 60 * 24 * 1000);
        if (diffDays < 0) {
            return (long) -3;
        }
        return (long) diffDays;
    }

    public CycleData getCycleData() {
        return cycleData;
    }

    public void setCycleData(CycleData cycleData) {
        this.cycleData = cycleData;
        postInvalidate();
    }

    public WeekDayPicker.onDaySelectedListener getOnDaySelectedListener() {
        return onDaySelectedListener;
    }

    public void setOnDaySelectedListener(WeekDayPicker.onDaySelectedListener onDaySelectedListener) {
        this.onDaySelectedListener = onDaySelectedListener;
    }

    public Calendar getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Calendar selectedDate) {
        this.selectedDate.setTimeInMillis(selectedDate.getTimeInMillis());
        calculateWeekStart(selectedDate);
        postInvalidate();
    }

    private void calculateWeekStart(Calendar selectedDate) {
        weekStartDate.setTimeInMillis(selectedDate.getTimeInMillis());
        if (selectedDate.get(Calendar.DAY_OF_WEEK) != 7) {
            weekStartDate.add(Calendar.DAY_OF_MONTH, -1 * selectedDate.get(Calendar.DAY_OF_WEEK));
        }
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

    public interface onDaySelectedListener {
        public void onDaySelected(Calendar day);
    }

}
