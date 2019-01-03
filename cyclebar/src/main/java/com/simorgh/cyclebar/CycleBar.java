package com.simorgh.cyclebar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.simorgh.cycleutils.CycleData;

import java.util.Calendar;

import androidx.annotation.Nullable;


public class CycleBar extends View {
    //default view height and width
    private static final int DEFAULT_HEIGHT = 66;
    private static final int DEFAULT_WIDTH = 402;

    private Paint redTypePaint;
    private int redTypeColor;

    private Paint greenTypePaint;
    private int greenTypeColor;

    private Paint grayTypePaint;
    private int grayTypeColor;

    private Paint borderPaint;
    private int borderColor;

    private Paint todayMarkerPaint;
    private TextPaint todayTextPaint;
    private int todayMarkerColor;

    private TextPaint totalDaysTextPaint;
    private int totalDaysTextColor;
    private float totalDaysTextSize;
    private float todayTextSize;
    private String totalDaysText = "";

    private TextPaint redDaysTextPaint;
    private int redDaysTextColor;
    private float redDaysTextSize;
    private String redDaysText = "";

    private Bitmap icon_greenType2;

    private Typeface typeface;

    private float realHeight = -1;
    private float realWidth = -1;

    private float cycleBarX = -1;
    private float cycleBarY = -1;
    private float cycleBarWidth = -1;
    private float cycleBarHeight = -1;
    private RectF cycleBarRectF;
    private RectF cycleBarBorderRectF;
    private RectF cycleBarRedDaysRectF;
    private RectF cycleBarGreenDaysRectF;
    private RectF cycleBarGreen2RectF;

    private CycleData cycleData;

    private static Calendar in = Calendar.getInstance();
    private static Calendar start = Calendar.getInstance();

    private static final float radius = 15f;

    private String dayText;
    private String todayText;


    public CycleBar(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public CycleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public CycleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CycleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        init();
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CycleBar);
        Resources resources = getResources();


        redTypeColor = typedArray.getColor(R.styleable.CycleBar_redTypeColor, resources.getColor(R.color.redTypeColor));
        greenTypeColor = typedArray.getColor(R.styleable.CycleBar_greenTypeColor, resources.getColor(R.color.greenTypeColor));
        grayTypeColor = typedArray.getColor(R.styleable.CycleBar_grayTypeColor, resources.getColor(R.color.grayTypeColor));
        borderColor = typedArray.getColor(R.styleable.CycleBar_borderColor, resources.getColor(R.color.borderColor));
        todayMarkerColor = typedArray.getColor(R.styleable.CycleBar_todayColor, resources.getColor(R.color.todayColor));
        redDaysTextColor = typedArray.getColor(R.styleable.CycleBar_redDaysTextColor, resources.getColor(R.color.redDaysTextColor));
        totalDaysTextColor = typedArray.getColor(R.styleable.CycleBar_totalDaysTextColor, resources.getColor(R.color.totalDaysTextColor));

        redDaysTextSize = typedArray.getDimension(R.styleable.CycleBar_redDaysTextSize, resources.getDimension(R.dimen.redDaysTextSize));
        totalDaysTextSize = typedArray.getDimension(R.styleable.CycleBar_totalDaysTextSize, resources.getDimension(R.dimen.totalDaysTextSize));
        todayTextSize = typedArray.getDimension(R.styleable.CycleBar_todayTextSize, resources.getDimension(R.dimen.todayTextSize));

        todayText = resources.getString(R.string.today);
        dayText = resources.getString(R.string.day);

        typedArray.recycle();
    }

    private void init() {
        today = Calendar.getInstance();

        redTypePaint = new Paint();
        redTypePaint.setAntiAlias(true);
        redTypePaint.setStyle(Paint.Style.FILL);
        redTypePaint.setColor(redTypeColor);

        greenTypePaint = new Paint();
        greenTypePaint.setAntiAlias(true);
        greenTypePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        greenTypePaint.setColor(greenTypeColor);

        grayTypePaint = new Paint();
        grayTypePaint.setAntiAlias(true);
        grayTypePaint.setStyle(Paint.Style.FILL);
        grayTypePaint.setColor(grayTypeColor);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(dp2px(1));
        borderPaint.setColor(borderColor);

        todayMarkerPaint = new Paint();
        todayMarkerPaint.setAntiAlias(true);
        todayMarkerPaint.setStyle(Paint.Style.FILL);
        todayMarkerPaint.setColor(todayMarkerColor);
        todayMarkerPaint.setStrokeWidth(dp2px(1));

        AssetManager assetMgr = getContext().getAssets();
        typeface = Typeface.createFromAsset(assetMgr, "fonts/iransans_medium.ttf");

        redDaysTextPaint = new TextPaint();
        redDaysTextPaint.setAntiAlias(true);
        redDaysTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        redDaysTextPaint.setTextSize(redDaysTextSize);
        redDaysTextPaint.setTextAlign(Paint.Align.CENTER);
        redDaysTextPaint.setColor(redDaysTextColor);
        redDaysTextPaint.setTypeface(typeface);


        totalDaysTextPaint = new TextPaint();
        totalDaysTextPaint.setAntiAlias(true);
        totalDaysTextPaint.setStyle(Paint.Style.FILL);
        totalDaysTextPaint.setTextSize(totalDaysTextSize);
        totalDaysTextPaint.setTextAlign(Paint.Align.CENTER);
        totalDaysTextPaint.setColor(totalDaysTextColor);
        totalDaysTextPaint.setTypeface(typeface);


        todayTextPaint = new TextPaint();
        todayTextPaint.setAntiAlias(true);
        todayTextPaint.setStyle(Paint.Style.FILL);
        todayTextPaint.setTextSize(todayTextSize);
        todayTextPaint.setTextAlign(Paint.Align.CENTER);
        todayTextPaint.setColor(todayMarkerColor);
        todayTextPaint.setTypeface(typeface);


        icon_greenType2 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_green2);

        cycleBarRectF = new RectF();
        cycleBarBorderRectF = new RectF();
        cycleBarRedDaysRectF = new RectF();
        cycleBarGreenDaysRectF = new RectF();
        cycleBarGreen2RectF = new RectF();
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

        cycleBarX = realWidth / 7.5f;
        cycleBarWidth = realWidth - cycleBarX - 5;
        cycleBarHeight = realHeight / 2.4f;
        cycleBarY = realHeight - cycleBarHeight;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);
            cycleData = new CycleData(5, 25, 3, c,c);
        }
        if (cycleData == null) {
            return;
        }
        drawToday(canvas);
        drawCycleBar(canvas);
    }

    private void drawCycleBar(Canvas canvas) {
        cycleBarRectF.set(dp2px(cycleBarX + 2), dp2px(cycleBarY + 2), dp2px(realWidth - 2), dp2px(realHeight - 2));

        cycleBarBorderRectF.set(dp2px(cycleBarX + 2), dp2px(cycleBarY + 2), dp2px(realWidth - 2), dp2px(realHeight - 2));

        cycleBarRedDaysRectF.set(dp2px(realWidth - 2 - (cycleData.getRedCount() + 2) * (cycleBarWidth / cycleData.getTotalDays()))
                , dp2px(cycleBarY + 2)
                , dp2px(realWidth - 2)
                , dp2px(realHeight - 2));

        cycleBarGreenDaysRectF.set(dp2px(realWidth - 2 - 3 * (cycleBarWidth / cycleData.getTotalDays()) - (cycleBarWidth / cycleData.getTotalDays() * cycleData.getGreenEndIndex()))
                , dp2px(cycleBarY + 2)
                , dp2px(realWidth - 2 - (cycleBarWidth / cycleData.getTotalDays() * cycleData.getGreenStartIndex()))
                , dp2px(realHeight - 2));

        cycleBarGreen2RectF.set(cycleBarGreenDaysRectF.centerX() - dp2px(8)
                , cycleBarGreenDaysRectF.centerY() - dp2px(8)
                , cycleBarGreenDaysRectF.centerX() + dp2px(8)
                , cycleBarGreenDaysRectF.centerY() + dp2px(8));

        canvas.drawRoundRect(cycleBarRectF, dp2px(radius), dp2px(radius), grayTypePaint);
        canvas.drawRoundRect(cycleBarBorderRectF, dp2px(radius), dp2px(radius), borderPaint);
        canvas.drawRoundRect(cycleBarRedDaysRectF, dp2px(radius), dp2px(radius), redTypePaint);
        canvas.drawRoundRect(cycleBarGreenDaysRectF, dp2px(radius), dp2px(radius), greenTypePaint);
        canvas.drawBitmap(icon_greenType2, null, cycleBarGreen2RectF, greenTypePaint);

        redDaysText = cycleData.getRedCount() + " " + dayText;
        canvas.drawText(redDaysText, cycleBarRedDaysRectF.centerX() + 0.5f * redDaysTextPaint.getFontMetrics().descent, dp2px(realHeight) - 1.5f * redDaysTextPaint.getFontMetrics().bottom, redDaysTextPaint);

        totalDaysText = cycleData.getTotalDays() + " " + dayText;
        canvas.drawText(totalDaysText, dp2px(5) + 2 * totalDaysTextPaint.getFontMetrics().descent, dp2px(realHeight) - totalDaysTextPaint.getFontMetrics().bottom, totalDaysTextPaint);
    }

    private void drawToday(Canvas canvas) {
        canvas.drawLine(getTodayX(), dp2px(cycleBarY + 3), getTodayX(), dp2px(cycleBarY - 15), todayMarkerPaint);
        canvas.drawCircle(getTodayX(), dp2px(cycleBarY - 15 - 4), dp2px(4), todayMarkerPaint);
        canvas.drawText(todayText, getTodayX() - 0.2f * todayTextPaint.getFontMetrics().descent
                , dp2px(cycleBarY - 15 - 4) - todayTextPaint.getFontMetrics().descent
                , todayTextPaint);
    }

    private Calendar today;

    private float getTodayX() {
        if (cycleData == null) {
            return dp2px(cycleBarX);
        }
        int day = (int) getDaysFromDiff(today, cycleData.getStartDate());
        if (day < 0) {
            day = 1;
        } else {
            day = (day) % cycleData.getTotalDays() + 1;
//            day--;
        }
        float unit = cycleBarWidth / cycleData.getTotalDays();
        return dp2px(cycleBarX + cycleBarWidth - (unit * day) + unit / 2);
    }


    public static long getDaysFromDiff(Calendar input, Calendar startDate) {
        float diffDays = -1;
        in.clear();
        in.set(input.get(Calendar.YEAR), input.get(Calendar.MONTH), input.get(Calendar.DAY_OF_MONTH));
        start.clear();
        start.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
        if (!input.after(startDate)) {
            return (long) diffDays;
        }
        diffDays = (float) (in.getTimeInMillis() - start.getTimeInMillis()) / (60 * 60 * 24 * 1000);
        if (diffDays < 0) {
            return (long) diffDays;
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

    public Calendar getToday() {
        return today;
    }

    public void setToday(Calendar today) {
        this.today.setTimeInMillis(today.getTimeInMillis());
        postInvalidate();
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
