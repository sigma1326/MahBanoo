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

    private ClueData clueData;

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

        typedArray.recycle();
    }

    private void init() {
        clueData = new ClueData(5, 25, 3, Calendar.getInstance());

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
        cycleBarWidth = realWidth - cycleBarX;
        cycleBarHeight = realHeight / 2.4f;
        cycleBarY = realHeight - cycleBarHeight;

//        circleRadius = realHeight / 3f;
//        circleX = realWidth / 2f;
//        circleY = realHeight / 3f + 5;
//
//        circleIconWidth = realWidth / 10.6f;
//        circleIconHeight = realWidth / 10.6f;
//
//        itemIconHeight = realWidth / 18f;
//        itemIconWidth = realWidth / 18f;
//        backgroundLeftY = realHeight / 3f;
//        icons_y_center = backgroundLeftY + 22;
//        icons_x_center = realWidth / 8.4f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        cycleBarRectF.set(dp2px(cycleBarX + 2), dp2px(cycleBarY + 2), dp2px(realWidth - 2), dp2px(realHeight - 2));

        cycleBarBorderRectF.set(dp2px(cycleBarX + 2), dp2px(cycleBarY + 2), dp2px(realWidth - 2), dp2px(realHeight - 2));

        cycleBarRedDaysRectF.set(dp2px(realWidth - 2 - (clueData.getRedCount() + 2) * (cycleBarWidth / clueData.getTotalDays()))
                , dp2px(cycleBarY + 2)
                , dp2px(realWidth - 2)
                , dp2px(realHeight - 2));

        cycleBarGreenDaysRectF.set(dp2px(realWidth - 2 - 3 * (cycleBarWidth / clueData.getTotalDays()) - (cycleBarWidth / clueData.getTotalDays() * clueData.getGreenEndIndex()))
                , dp2px(cycleBarY + 2)
                , dp2px(realWidth - 2 - (cycleBarWidth / clueData.getTotalDays() * clueData.getGreenStartIndex()))
                , dp2px(realHeight - 2));

        cycleBarGreen2RectF.set(cycleBarGreenDaysRectF.left + cycleBarGreenDaysRectF.width() / 3 + dp2px(1)
                , cycleBarGreenDaysRectF.top + dp2px(3)
                , cycleBarGreenDaysRectF.right - cycleBarGreenDaysRectF.width() / 3 - dp2px(1)
                , cycleBarGreenDaysRectF.bottom - dp2px(3));

        canvas.drawRoundRect(cycleBarRectF, dp2px(20), dp2px(20), grayTypePaint);
        canvas.drawRoundRect(cycleBarBorderRectF, dp2px(20), dp2px(20), borderPaint);
        canvas.drawRoundRect(cycleBarRedDaysRectF, dp2px(20), dp2px(20), redTypePaint);
        canvas.drawRoundRect(cycleBarGreenDaysRectF, dp2px(20), dp2px(20), greenTypePaint);
        canvas.drawBitmap(icon_greenType2, null, cycleBarGreen2RectF, greenTypePaint);

        canvas.drawLine(dp2px(150), dp2px(cycleBarY + 1), dp2px(150), dp2px(cycleBarY - 15), todayMarkerPaint);
        canvas.drawCircle(dp2px(150),dp2px(cycleBarY-15-4),dp2px(4),todayMarkerPaint);
        canvas.drawText("امروز",dp2px(150)+0.2f*todayTextPaint.getFontMetrics().descent
                ,dp2px(cycleBarY-15-4)-todayTextPaint.getFontMetrics().descent
                ,todayTextPaint);

        totalDaysText = "26 روز";
        redDaysText = "3 روز";
        canvas.drawText(totalDaysText, 2 * totalDaysTextPaint.getFontMetrics().descent, dp2px(realHeight) - totalDaysTextPaint.getFontMetrics().bottom, totalDaysTextPaint);
        canvas.drawText(redDaysText, cycleBarRedDaysRectF.centerX() + 0.5f * redDaysTextPaint.getFontMetrics().descent, dp2px(realHeight) - 1.5f*redDaysTextPaint.getFontMetrics().bottom, redDaysTextPaint);
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
