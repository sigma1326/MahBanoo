package com.simorgh.weightview;

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
import android.view.View;

import androidx.annotation.Nullable;

public class WeightView extends View {
    //default view height and width
    private static final int DEFAULT_HEIGHT = 150;
    private static final int DEFAULT_WIDTH = 360;

    private float realHeight = -1;
    private float realWidth = -1;

    private String title = "";
    private String weightString = "0.0";
    private String kg = "";

    private TextPaint weightTextPaint;
    private int weightTextColor;
    private float weightTextSize;

    private TextPaint titleTextPaint;
    private int titleTextColor;
    private float titleTextSize;

    private TextPaint kgTextPaint;
    private int kgTextColor;
    private float kgTextSize;

    private Paint linePaint;
    private int lineColor;
    private float lineLength;

    private Typeface typeface;

    public WeightView(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public WeightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public WeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeightView);
        Resources resources = getResources();


        weightTextColor = typedArray.getColor(R.styleable.WeightView_weightTextColor, resources.getColor(R.color.weightTextColor));
        kgTextColor = typedArray.getColor(R.styleable.WeightView_kgTextColor, resources.getColor(R.color.weightTextColor));
        titleTextColor = typedArray.getColor(R.styleable.WeightView_titleTextColor, resources.getColor(R.color.titleTextColor));
        lineColor = typedArray.getColor(R.styleable.WeightView_lineColor, resources.getColor(R.color.lineColor));

        weightTextSize = typedArray.getDimension(R.styleable.WeightView_weightTextSize, resources.getDimension(R.dimen.weightTextSize));
        titleTextSize = typedArray.getDimension(R.styleable.WeightView_titleTextSize, resources.getDimension(R.dimen.titleTextSize));
        kgTextSize = typedArray.getDimension(R.styleable.WeightView_kgTextSize, resources.getDimension(R.dimen.kgTextSize));


        title = resources.getString(R.string.weight);
        kg = resources.getString(R.string.kg);

        typedArray.recycle();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(dp2px(1));
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(lineColor);

        AssetManager assetMgr = getContext().getAssets();
        typeface = Typeface.createFromAsset(assetMgr, "fonts/iransans_medium.ttf");

        weightTextPaint = new TextPaint();
        weightTextPaint.setAntiAlias(true);
        weightTextPaint.setStyle(Paint.Style.FILL);
        weightTextPaint.setTextSize(weightTextSize);
        weightTextPaint.setTextAlign(Paint.Align.CENTER);
        weightTextPaint.setColor(weightTextColor);
        weightTextPaint.setTypeface(typeface);

        kgTextPaint = new TextPaint();
        kgTextPaint.setAntiAlias(true);
        kgTextPaint.setStyle(Paint.Style.FILL);
        kgTextPaint.setTextSize(kgTextSize);
        kgTextPaint.setTextAlign(Paint.Align.CENTER);
        kgTextPaint.setColor(kgTextColor);
        kgTextPaint.setTypeface(typeface);

        titleTextPaint = new TextPaint();
        titleTextPaint.setAntiAlias(true);
        titleTextPaint.setStyle(Paint.Style.FILL);
        titleTextPaint.setTextSize(titleTextSize);
        titleTextPaint.setTextAlign(Paint.Align.RIGHT);
        titleTextPaint.setColor(titleTextColor);
        titleTextPaint.setTypeface(typeface);
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
        lineLength = realWidth / 1.45f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, titleTextPaint.getFontMetrics().descent / 2f + dp2px(12)
                , dp2px(lineLength), titleTextPaint.getFontMetrics().descent / 2f + dp2px(12), linePaint);
        canvas.drawText(title, dp2px(realWidth - 32), titleTextPaint.getFontMetrics().descent / 2f + dp2px(12), titleTextPaint);

        float y = dp2px(realHeight / 2.08f);
        float x = dp2px(realWidth / 2);

        canvas.drawText(weightString, x, y, weightTextPaint);
        canvas.drawText(kg, x - dp2px(50), y, kgTextPaint);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        postInvalidate();
    }

    public String getWeightString() {
        return weightString;
    }

    public void setWeightString(String weightString) {
        this.weightString = weightString;
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
