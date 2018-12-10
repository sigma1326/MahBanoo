package com.simorgh.clueview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ClueView extends View implements OnViewDataChangedListener {
    public static final String TAG = "clueView";
    public static final int TYPE_RED = 0;
    public static final int TYPE_GREEN = 1;
    public static final int TYPE_GREEN2 = 2;
    public static final int TYPE_GRAY = 3;
    public static final int TYPE_YELLOW = 4;
    //circle paints
    private Paint mainCirclePaint;
    private Paint mediumCirclePaint;
    private Paint smallCircleTypeRedPaint;
    private Paint smallCircleTypeGrayPaint;
    private Paint smallCircleTypeGreenPaint;
    private Paint smallCircleTypeGreen2Paint;
    private Paint smallCircleTypeYellowPaint;
    //circle colors
    private int mainCircleColor;
    private int mediumCircleColor;
    private int smallCircleTypeRedColor;
    private int smallCircleTypeGrayColor;
    private int smallCircleTypeGreenColor;
    private int smallCircleTypeGreen2Color;
    private int smallCircleTypeYellowColor;
    //circle radius
    private int mainCircleRadius = -1;
    private int mediumCircleRadius = -1;
    private float mediumCircleX = -1;
    private float mediumCircleY = -1;
    private int smallCircleRadius = -1;
    private int smallCircleOuterRadius = -1;
    private int outerRadius = -1;

    //button change attrs
    private Paint btnChangeBkgPaint;
    private RectF btnChangeBkgRectF;
    private int btnChangeBkgColor;
    private int btnChangeBkgShadowColor;
    private int btnChangeBkgRectLeft = -1;
    private int btnChangeBkgRectRight = -1;
    private int btnChangeBkgRectTop = -1;
    private int btnChangeBkgRectBottom = -1;
    private float btnChangeBkgRectRoundnessRadius = -1;
    private Paint btnChangeTextPaint;
    private String btnChangeText = "";
    private int btnChangeTextColor;
    private float btnChangeTextSize = -1;
    private float btnChangeTextX = -1;
    private float btnChangeTextY = -1;
    private static final float BTN_CHANGE_TEXT_SIZE_RATIO = 10f;
    private static final int BTN_CHANGE_LEFT_OFFSET = 5;
    private static final int BTN_CHANGE_RIGHT_OFFSET = 5;
    private static final float BTN_CHANGE_TOP_OFFSET = 2.3f;
    private static final float BTN_CHANGE_BOTTOM_OFFSET = 1.6f;
    private static final float BTN_CHANGE_ROUNDNESS_RATIO = 8.3f;
    private static final float BTN_CHANGE_SHADOW_RADIUS = 10f;
    private static final float BTN_CHANGE_SHADOW_X_OFFSET = 0f;
    private static final float BTN_CHANGE_SHADOW_Y_OFFSET = 10f;
    private static final float BTN_CHANGE_TEXT_Y_OFFSET = 5f;

    //optional TextView
    private Paint tvOptionalPaint;
    private String tvOptionalText = "";
    private int tvOptionalTextColor;
    private float tvOptionalTextSize = -1;
    private float tvOptionalX = -1;
    private float tvOptionalY = -1;
    private static final float OPTIONAL_TV_Y_RATIO = 4.8f;
    private static final float OPTIONAL_TV_TEXT_SIZE_RATIO = 9f;


    //Main Day Number TextView
    private Paint tvMainDayNumberPaint;
    private String tvMainDayNumberText = "";
    private int tvMainDayNumberTextColor;
    private float tvMainDayNumberTextSize = -1;
    private float tvMainDayNumberX = -1;
    private float tvMainDayNumberY = -1;
    private static final float MAIN_DAY_NUMBER_TV_Y_RATIO = 10f;
    private static final float MAIN_DAY_NUMBER_TV_TEXT_SIZE_RATIO = 5f;


    //Week Day Name TextView
    private Paint tvWeekDayNamePaint;
    private String tvWeekDayNameText = "";
    private int tvWeekDayNameTextColor;
    private float tvWeekDayNameTextSize = -1;
    private float tvWeekDayNameX = -1;
    private float tvWeekDayNameY = -1;
    private static final float WEEK_DAY_NAME_TV_Y_RATIO = 1.8f;
    private static final float WEEK_DAY_NAME_TV_TEXT_SIZE_RATIO = 7f;


    //Month Day Number TextView
    private Paint tvMonthDayNumberPaint;
    private String tvMonthDayNumberText = "";
    private int tvMonthDayNumberTextColor;
    private float tvMonthDayNumberTextSize = -1;
    private float tvMonthDayNumberX = -1;
    private float tvMonthDayNumberY = -1;
    private static final float DAY_OF_MONTH_TV_TEXT_SIZE_RATIO = 2f;
    private static final float DAY_OF_MONTH_TV_Y_RATIO = 4.5f;


    //Month Name TextView
    private Paint tvMonthNamePaint;
    private String tvMonthNameText = "";
    private int tvMonthNameTextColor;
    private float tvMonthNameTextSize = -1;
    private float tvMonthNameX = -1;
    private float tvMonthNameY = -1;
    private static final float MONTH_NAME_TV_TEXT_SIZE_RATIO = 2f;
    private static final float MONTH_NAME_TV_Y_RATIO = 9.2f;


    ///////////////////////////////////////

    //center of view coordinates
    private float midX = -1;
    private float midY = -1;

    //selected angle in degree
    private float angle = -1;
    private float ANGLE_UNIT = -1;
    private boolean isAnimRunning = false;
    private int lastDay = -1;


    //pressed states
    private boolean pressedBtnChange = false;
    private boolean pressedMediumCircle = false;

    //typeface for all texts
    private Typeface typeface;

    //default view height and width
    private static final int DEFAULT_HEIGHT = 300;
    private static final int DEFAULT_WIDTH = 300;

    private boolean isFirstDraw = true;

    private int offsetAngle = -1;
    private static final int DEFAULT_START_OFFSET_ANGLE = 10;

    private Path clipPath = new Path();
    private float mediumCircleHoverX;
    private float mediumCircleHoverY;
    private float mediumCircleHoverAlphaFactor;
    private float mediumCircleHoverRadius;
    private float mediumCircleHoverMaxRadius;
    private int mediumCircleHoverRippleColor;
    private boolean mediumCircleHoverIsAnimating = false;
    private boolean mediumCircleHover = true;
    private RadialGradient mediumCircleHoverRadialGradient;
    private Paint mediumCircleHoverPaint;
    private ObjectAnimator mediumCircleHoverRadiusAnimator;


    private float mainCircleHoverX;
    private float mainCircleHoverY;
    private float mainCircleHoverAlphaFactor;
    private float mainCircleHoverRadius;
    private int mainCircleHoverRippleColor;
    private boolean mainCircleHoverIsAnimating = false;
    private RadialGradient mainCircleHoverRadialGradient;
    private Paint mainCircleHoverPaint;
    private ObjectAnimator mainCircleHoverRadiusAnimator;

    //circle ratios
    private static final float MAIN_CIRCLE_RATIO = 3.5f;
    private static final float MEDIUM_CIRCLE_RATIO = 3.8f;
    private static final float SMALL_CIRCLE_RATIO = 63f;
    private static final float OUTER_RADIUS_RATIO = 9f;
    private boolean isFromUser = false;

    public static class ViewData {
        private int typeRedColor;
        private int typeGreenColor;
        private int typeYellowColor;
        private int typeGrayColor;

        /**
         * @param typeRedColor:    type red color, if don't want to change it, set to @{#-1}
         * @param typeGreenColor:  type green color, if don't want to change it, set to @{#-1}
         * @param typeYellowColor: type yellow color, if don't want to change it, set to @{#-1}
         * @param typeGrayColor:   type gray color, if don't want to change it, set to @{#-1}
         */
        public ViewData(int typeRedColor, int typeGreenColor, int typeYellowColor, int typeGrayColor) {
            this.typeRedColor = typeRedColor;
            this.typeGreenColor = typeGreenColor;
            this.typeYellowColor = typeYellowColor;
            this.typeGrayColor = typeGrayColor;
        }

        public int getTypeRedColor() {
            return typeRedColor;
        }

        public void setTypeRedColor(int typeRedColor) {
            this.typeRedColor = typeRedColor;
        }

        public int getTypeGreenColor() {
            return typeGreenColor;
        }

        public void setTypeGreenColor(int typeGreenColor) {
            this.typeGreenColor = typeGreenColor;
        }

        public int getTypeYellowColor() {
            return typeYellowColor;
        }

        public void setTypeYellowColor(int typeYellowColor) {
            this.typeYellowColor = typeYellowColor;
        }

        public int getTypeGrayColor() {
            return typeGrayColor;
        }

        public void setTypeGrayColor(int typeGrayColor) {
            this.typeGrayColor = typeGrayColor;
        }
    }

    //////////////////////////////////////////////
    //Clue related variables (index start from 1)
    private ClueData clueData;
    private ViewData viewData;
    private OnDayChangedListener onDayChangedListener;
    private OnButtonClickListener onButtonClickListener;

    public static class ClueData {
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

    public static final class Builder {
        private ClueData clueData;
        private ViewData viewData;
        private Context context;
        private OnDayChangedListener onDayChangedListener;
        private OnButtonClickListener onButtonClickListener;

        public Builder(Context context) {
            this.context = context;
            viewData = new ViewData(-1, -1, -1, -1);
        }

        public ClueData getClueData() {
            return clueData;
        }

        public Builder setClueData(ClueData clueData) {
            this.clueData = clueData;
            return this;
        }

        public Context getContext() {
            return context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public ViewData getViewData() {
            return viewData;
        }

        public OnDayChangedListener getOnDayChangedListener() {
            return onDayChangedListener;
        }

        public Builder setOnDayChangedListener(OnDayChangedListener onDayChangedListener) {
            this.onDayChangedListener = onDayChangedListener;
            return this;
        }

        public Builder setViewData(ViewData viewData) {
            this.viewData = viewData;
            return this;
        }

        public ClueView build() throws Exception {
            if (clueData == null) {
                throw new Exception("null clue data !");
            }
            if (viewData == null) {
                if (onDayChangedListener == null) {
                    throw new Exception("OnDayChangedListener object can not be null !");
                }
                if (onButtonClickListener == null) {
                    throw new Exception("OnButtonClickListener object can not be null !");
                }
                return new ClueView(this, clueData, onDayChangedListener, onButtonClickListener);
            }
            if (onDayChangedListener == null) {
                throw new Exception("OnDayChangedListener object can not be null !");
            }
            if (onButtonClickListener == null) {
                throw new Exception("OnButtonClickListener object can not be null !");
            }
            return new ClueView(this, clueData, viewData, onDayChangedListener, onButtonClickListener);
        }
    }

    public interface OnDayChangedListener {
        void onDayChanged(int day, int dayType, OnViewDataChangedListener onViewDataChangedListener);
    }

    public interface OnButtonClickListener {
        void onButtonChangeClick();
    }


    //////////////////////////////////////////////

    public ClueView(Builder builder, ClueData clueData, OnDayChangedListener onDayChangedListener, OnButtonClickListener onButtonClickListener) {
        super(builder.context);
        setClueDate(clueData, onDayChangedListener, onButtonClickListener);
    }

    public ClueView(Builder builder, ClueData clueData, ViewData viewData, OnDayChangedListener onDayChangedListener, OnButtonClickListener onButtonClickListener) {
        super(builder.context);
        setClueAndViewData(clueData, viewData, onDayChangedListener, onButtonClickListener);
    }

    public ClueView(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public ClueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public ClueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClueView);
        Resources resources = getResources();

        //circle colors
        mainCircleColor = typedArray.getColor(R.styleable.ClueView_main_circle_color, resources.getColor(R.color.type_red));
        mediumCircleColor = typedArray.getColor(R.styleable.ClueView_medium_circle_color, resources.getColor(R.color.type_red));
        smallCircleTypeRedColor = typedArray.getColor(R.styleable.ClueView_small_circle_color_type1, resources.getColor(R.color.type_red));
        smallCircleTypeGreenColor = typedArray.getColor(R.styleable.ClueView_small_circle_color_type2, resources.getColor(R.color.type_green));
        smallCircleTypeGreen2Color = typedArray.getColor(R.styleable.ClueView_small_circle_color_type2, resources.getColor(R.color.type_green));
        smallCircleTypeGrayColor = typedArray.getColor(R.styleable.ClueView_small_circle_color_type3, resources.getColor(R.color.type_gray));
        smallCircleTypeYellowColor = typedArray.getColor(R.styleable.ClueView_small_circle_color_type4, resources.getColor(R.color.type_yellow));


        //btn change
        btnChangeBkgColor = typedArray.getColor(R.styleable.ClueView_main_circle_color, resources.getColor(R.color.white));
        btnChangeBkgShadowColor = resources.getColor(R.color.shadow);
        btnChangeTextColor = mainCircleColor;

        //optional textView
        tvOptionalTextColor = resources.getColor(R.color.white);

        //main day number textView
        tvMainDayNumberTextColor = resources.getColor(R.color.white);

        //week day name textView
        tvWeekDayNameTextColor = resources.getColor(R.color.white);

        //month day number textView
        tvMonthDayNumberTextColor = resources.getColor(R.color.white);

        //month name textView
        tvMonthNameTextColor = resources.getColor(R.color.white);

        btnChangeText = resources.getString(R.string.btn_change_text);

        typedArray.recycle();
    }

    private void init() {
        //circle paints
        mainCirclePaint = new Paint();
        mainCirclePaint.setAntiAlias(true);
        mainCirclePaint.setColor(mainCircleColor);
        mainCirclePaint.setStyle(Paint.Style.FILL);

        mediumCirclePaint = new Paint();
        mediumCirclePaint.setAntiAlias(true);
        mediumCirclePaint.setColor(mediumCircleColor);
        mediumCirclePaint.setStyle(Paint.Style.FILL);

        smallCircleTypeRedPaint = new Paint();
        smallCircleTypeRedPaint.setAntiAlias(true);
        smallCircleTypeRedPaint.setColor(smallCircleTypeRedColor);
        smallCircleTypeRedPaint.setStyle(Paint.Style.FILL);

        smallCircleTypeGrayPaint = new Paint();
        smallCircleTypeGrayPaint.setAntiAlias(true);
        smallCircleTypeGrayPaint.setColor(smallCircleTypeGrayColor);
        smallCircleTypeGrayPaint.setStyle(Paint.Style.FILL);

        smallCircleTypeGreenPaint = new Paint();
        smallCircleTypeGreenPaint.setAntiAlias(true);
        smallCircleTypeGreenPaint.setColor(smallCircleTypeGreenColor);
        smallCircleTypeGreenPaint.setStyle(Paint.Style.FILL);

        smallCircleTypeGreen2Paint = new Paint();
        smallCircleTypeGreen2Paint.setAntiAlias(true);
        smallCircleTypeGreen2Paint.setColor(smallCircleTypeGreen2Color);
        smallCircleTypeGreen2Paint.setStyle(Paint.Style.STROKE);
        smallCircleTypeGreen2Paint.setStrokeWidth(dp2px(2));

        smallCircleTypeYellowPaint = new Paint();
        smallCircleTypeYellowPaint.setAntiAlias(true);
        smallCircleTypeYellowPaint.setColor(smallCircleTypeYellowColor);
        smallCircleTypeYellowPaint.setStyle(Paint.Style.FILL);

        //button change
        btnChangeBkgPaint = new Paint();
        btnChangeBkgPaint.setAntiAlias(true);
        btnChangeBkgPaint.setColor(btnChangeBkgColor);
        btnChangeBkgPaint.setStyle(Paint.Style.FILL);
        btnChangeBkgPaint.setTextAlign(Paint.Align.CENTER);

        btnChangeBkgRectF = new RectF();

        btnChangeTextPaint = new Paint();
        btnChangeTextPaint.setAntiAlias(true);
        btnChangeTextPaint.setColor(btnChangeTextColor);
        btnChangeTextPaint.setStyle(Paint.Style.FILL);
        btnChangeTextPaint.setTextAlign(Paint.Align.CENTER);
        btnChangeTextPaint.setFakeBoldText(true);
        btnChangeTextPaint.setTextSize(btnChangeTextSize);

        //optional textView
        tvOptionalPaint = new Paint();
        tvOptionalPaint.setAntiAlias(true);
        tvOptionalPaint.setColor(tvOptionalTextColor);
        tvOptionalPaint.setStyle(Paint.Style.FILL);
        tvOptionalPaint.setFakeBoldText(true);
        tvOptionalPaint.setTextAlign(Paint.Align.CENTER);
        tvOptionalPaint.setTextSize(tvOptionalTextSize);

        //main day number textView
        tvMainDayNumberPaint = new Paint();
        tvMainDayNumberPaint.setAntiAlias(true);
        tvMainDayNumberPaint.setColor(tvMainDayNumberTextColor);
        tvMainDayNumberPaint.setStyle(Paint.Style.FILL);
        tvMainDayNumberPaint.setFakeBoldText(true);
        tvMainDayNumberPaint.setTextAlign(Paint.Align.CENTER);
        tvMainDayNumberPaint.setTextSize(tvMainDayNumberTextSize);

        //weekday name textView
        tvWeekDayNamePaint = new Paint();
        tvWeekDayNamePaint.setAntiAlias(true);
        tvWeekDayNamePaint.setColor(tvWeekDayNameTextColor);
        tvWeekDayNamePaint.setStyle(Paint.Style.FILL);
        tvWeekDayNamePaint.setFakeBoldText(true);
        tvWeekDayNamePaint.setTextAlign(Paint.Align.CENTER);
        tvWeekDayNamePaint.setTextSize(tvWeekDayNameTextSize);

        //month day number textView
        tvMonthDayNumberPaint = new Paint();
        tvMonthDayNumberPaint.setAntiAlias(true);
        tvMonthDayNumberPaint.setColor(tvMonthDayNumberTextColor);
        tvMonthDayNumberPaint.setStyle(Paint.Style.FILL);
        tvMonthDayNumberPaint.setTextAlign(Paint.Align.CENTER);
        tvMonthDayNumberPaint.setFakeBoldText(true);
        tvMonthDayNumberPaint.setTextSize(tvMonthDayNumberTextSize);

        //month name textView
        tvMonthNamePaint = new Paint();
        tvMonthNamePaint.setAntiAlias(true);
        tvMonthNamePaint.setColor(tvMonthNameTextColor);
        tvMonthNamePaint.setStyle(Paint.Style.FILL);
        tvMonthNamePaint.setFakeBoldText(true);
        tvMonthNamePaint.setTextAlign(Paint.Align.CENTER);
        tvMonthNamePaint.setTextSize(tvMonthNameTextSize);

        //init the array to save the angles
        if (clueData != null) {
        }

        clueData = new ClueData(3, 26,1);

        //init the hover variables
        mediumCircleHoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mediumCircleHoverPaint.setAlpha(100);
        setMediumCircleHoverRippleColor(Color.WHITE, 0.2f);

        mainCircleHoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainCircleHoverPaint.setAlpha(100);
        setMainCircleHoverRippleColor(Color.WHITE, 0.2f);


        //generate typeface for all texts
        setTypeFaceForAllTexts();
    }

    private void setTypeFaceForAllTexts() {
        AssetManager assetMgr = getContext().getAssets();
        typeface = Typeface.createFromAsset(assetMgr, "fonts/vazir_medium.ttf");

        btnChangeTextPaint.setTypeface(typeface);
        tvMainDayNumberPaint.setTypeface(typeface);
        tvWeekDayNamePaint.setTypeface(typeface);
        tvMonthDayNumberPaint.setTypeface(typeface);
        tvMonthNamePaint.setTypeface(typeface);
        tvOptionalPaint.setTypeface(typeface);
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

        setMeasuredDimension((int) (width + paddingWidth + dp2px(50)), (int) (height + paddingHeight + dp2px(50)));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        midX = getWidth() >> 1;
        midY = getHeight() >> 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initVariablesForDraw();

        drawMainCircle(canvas);

        drawSmallCircles(canvas);

        drawMediumCircle(canvas);

    }

    Paint temp = null;

    private void drawMainCircle(Canvas canvas) {
        //draw the main circle
        canvas.drawCircle(midX, midY, mainCircleRadius, mainCirclePaint);

        if (mainCircleHoverIsAnimating) {
            //draw the medium circle hover and clip
            canvas.save();
            clipPath.reset();
            clipPath.addCircle(midX, midY, mainCircleRadius, Path.Direction.CCW);
            canvas.clipPath(clipPath, Region.Op.INTERSECT);
            if (mainCircleHoverRadiusAnimator != null) {
                canvas.drawCircle(mainCircleHoverX, mainCircleHoverY, mainCircleHoverRadius, getCurrentDayPaint());
            }
            canvas.restore();
        }

        btnChangeTextColor = getCurrentDayPaint().getColor();
        btnChangeTextPaint.setColor(btnChangeTextColor);
        //draw the main circle texts
        if (pressedBtnChange) {
            btnChangeBkgPaint.setShadowLayer(dp2px(20), dp2px(10), dp2px(10), btnChangeBkgShadowColor);
            btnChangeTextPaint.setShadowLayer(dp2px(20), dp2px(10), dp2px(10), btnChangeBkgShadowColor);
            btnChangeBkgRectF.set(btnChangeBkgRectLeft - 3, btnChangeBkgRectTop - 3, btnChangeBkgRectRight + 3, btnChangeBkgRectBottom + 3);
            canvas.drawRoundRect(btnChangeBkgRectF, btnChangeBkgRectRoundnessRadius, btnChangeBkgRectRoundnessRadius, btnChangeBkgPaint);
            btnChangeTextPaint.setTextSize(btnChangeTextPaint.getTextSize() + 1);
            canvas.drawText(btnChangeText, btnChangeTextX, btnChangeTextY, btnChangeTextPaint);
            btnChangeTextPaint.setTextSize(btnChangeTextPaint.getTextSize() - 1);
            if (onButtonClickListener != null) {
                onButtonClickListener.onButtonChangeClick();
            }
        } else {
            btnChangeBkgPaint.setShadowLayer(dp2px(0), dp2px(10), dp2px(10), Color.BLACK);
            btnChangeTextPaint.setShadowLayer(dp2px(20), dp2px(10), dp2px(10), btnChangeBkgShadowColor);
            btnChangeBkgRectF.set(btnChangeBkgRectLeft, btnChangeBkgRectTop, btnChangeBkgRectRight, btnChangeBkgRectBottom);
            canvas.drawRoundRect(btnChangeBkgRectF, btnChangeBkgRectRoundnessRadius, btnChangeBkgRectRoundnessRadius, btnChangeBkgPaint);
            canvas.drawText(btnChangeText, btnChangeTextX, btnChangeTextY, btnChangeTextPaint);
        }


        canvas.drawText(tvWeekDayNameText, tvWeekDayNameX, tvWeekDayNameY, tvWeekDayNamePaint);
        canvas.drawText(tvMainDayNumberText, tvMainDayNumberX, tvMainDayNumberY, tvMainDayNumberPaint);
        canvas.drawText(tvOptionalText, tvOptionalX, tvOptionalY, tvOptionalPaint);
    }

    private Paint getCurrentDayPaint() {
        if (getCurrentDay() == -1) {
            return smallCircleTypeRedPaint;
        }
        if (clueData == null) {
            return smallCircleTypeRedPaint;
        }
        if (getCurrentDay() <= clueData.redCount) {
            return smallCircleTypeRedPaint;
        } else if (getCurrentDay() <= clueData.totalDays) {
            if (getCurrentDay() >= clueData.greenStartIndex && getCurrentDay() <= clueData.greenEndIndex) {
                if (getCurrentDay() == clueData.green2Index) {
                    return smallCircleTypeGreenPaint;
                }
                return smallCircleTypeGreenPaint;
            } else if (getCurrentDay() >= clueData.yellowStartIndex && getCurrentDay() <= clueData.yellowEndIndex) {
                return smallCircleTypeYellowPaint;
            }
        }
        return smallCircleTypeGrayPaint;
    }

    private int getDayType(int day) {
        if (day == -1) {
            return TYPE_RED;
        }
        if (clueData == null) {
            return TYPE_RED;
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

    private int getDayType() {
        int day = getCurrentDay();
        if (day == -1) {
            return TYPE_RED;
        }
        if (clueData == null) {
            return TYPE_RED;
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

    private Paint getDayPaint(int day) {
        if (day == -1) {
            return smallCircleTypeRedPaint;
        }
        if (clueData == null) {
            return smallCircleTypeRedPaint;
        }
        if (day <= clueData.redCount) {
            return smallCircleTypeRedPaint;
        } else if (day <= clueData.totalDays) {
            if (day >= clueData.greenStartIndex && day <= clueData.greenEndIndex) {
                if (day == clueData.green2Index) {
                    return smallCircleTypeGreenPaint;
                }
                return smallCircleTypeGreenPaint;
            } else if (day >= clueData.yellowStartIndex && day <= clueData.yellowEndIndex) {
                return smallCircleTypeYellowPaint;
            }
        }
        return smallCircleTypeGrayPaint;
    }

    private void drawSmallCircles(Canvas canvas) {
        float dayAng;
        for (int i = 1; i <= clueData.getTotalDays(); i++) {
            dayAng = calculateAngleForDay(i);
            float drawX = (float) Math.cos(Math.toRadians(dayAng));
            float drawY = (float) Math.sin(Math.toRadians(dayAng));
            drawX *= outerRadius;
            drawX += midX;

            drawY *= outerRadius;
            drawY += midY;
            canvas.drawCircle(drawX, drawY, smallCircleRadius, getDayPaint(i));
            if (getDayType(i) == TYPE_GREEN2) {
                canvas.drawCircle(drawX, drawY, smallCircleRadius + dp2px(2), smallCircleTypeGreen2Paint);
            }
        }
    }

    private void drawMediumCircle(Canvas canvas) {
        if (pressedMediumCircle) {
            mediumCirclePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mediumCircleX, mediumCircleY, mediumCircleRadius + dp2px(3), getCurrentDayPaint());

            //draw the medium circle hover and clip
            canvas.save();
            clipPath.reset();
            clipPath.addCircle(mediumCircleX, mediumCircleY, mediumCircleRadius + dp2px(3), Path.Direction.CCW);
            canvas.clipPath(clipPath, Region.Op.INTERSECT);
            canvas.drawCircle(mediumCircleHoverX, mediumCircleHoverY, 1.2f * mediumCircleRadius, mediumCircleHoverPaint);
            canvas.restore();

            int color = mediumCirclePaint.getColor();
            mediumCirclePaint.setStyle(Paint.Style.STROKE);
            mediumCirclePaint.setColor(Color.WHITE);
            mediumCirclePaint.setStrokeWidth(dp2px(3));
            canvas.drawCircle(mediumCircleX, mediumCircleY, mediumCircleRadius + dp2px(3), mediumCirclePaint);
            mediumCirclePaint.setColor(color);
        } else {
            mediumCirclePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mediumCircleX, mediumCircleY, mediumCircleRadius, getCurrentDayPaint());
            int color = mediumCirclePaint.getColor();
            mediumCirclePaint.setStyle(Paint.Style.STROKE);
            mediumCirclePaint.setColor(Color.WHITE);
            mediumCirclePaint.setStrokeWidth(dp2px(3));
            canvas.drawCircle(mediumCircleX, mediumCircleY, mediumCircleRadius, mediumCirclePaint);
            mediumCirclePaint.setColor(color);
        }

        canvas.drawText(tvMonthDayNumberText, tvMonthDayNumberX, tvMonthDayNumberY, tvMonthDayNumberPaint);
        canvas.drawText(tvMonthNameText, tvMonthNameX, tvMonthNameY, tvMonthNamePaint);
        if (isFromUser) {
            if (onDayChangedListener != null) {
                if (getDayFromAngle(angle) != -1) {
                    onDayChangedListener.onDayChanged(getDayFromAngle(angle), getDayType(), this);
                } else {
                    Toast.makeText(getContext(), "bad angle", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initVariablesForDraw() {
        if (isFirstDraw) {
            isFirstDraw = false;

            if (offsetAngle == -1) {
                offsetAngle = DEFAULT_START_OFFSET_ANGLE;
            }
            if (mainCircleRadius == -1) {
                mainCircleRadius = (int) (getMinHeightWidth() / MAIN_CIRCLE_RATIO);
            }
            if (mediumCircleRadius == -1) {
                mediumCircleRadius = (int) (mainCircleRadius / MEDIUM_CIRCLE_RATIO);
            }
            if (smallCircleRadius == -1) {
                smallCircleRadius = (int) (getMinHeightWidth() / SMALL_CIRCLE_RATIO);
            }
            if (outerRadius == -1) {
                outerRadius = (int) (mainCircleRadius + getMinHeightWidth() / OUTER_RADIUS_RATIO);
            }


            //button change background
            btnChangeBkgRectLeft = (int) (midX - (mainCircleRadius >> 1) - dp2px(BTN_CHANGE_LEFT_OFFSET));
            btnChangeBkgRectRight = (int) (midX + (mainCircleRadius >> 1) + dp2px(BTN_CHANGE_RIGHT_OFFSET));
            btnChangeBkgRectTop = (int) (midY + mainCircleRadius / BTN_CHANGE_TOP_OFFSET);
            btnChangeBkgRectBottom = (int) (midY + mainCircleRadius / BTN_CHANGE_BOTTOM_OFFSET);
            btnChangeBkgRectRoundnessRadius = dp2px((mainCircleRadius / BTN_CHANGE_ROUNDNESS_RATIO));
            btnChangeBkgRectF.set(btnChangeBkgRectLeft, btnChangeBkgRectTop, btnChangeBkgRectRight, btnChangeBkgRectBottom);
            btnChangeBkgPaint.setShadowLayer(dp2px(BTN_CHANGE_SHADOW_RADIUS), dp2px(BTN_CHANGE_SHADOW_X_OFFSET), dp2px(BTN_CHANGE_SHADOW_Y_OFFSET), btnChangeBkgShadowColor);

            //button change textView
            btnChangeTextY = BTN_CHANGE_TEXT_Y_OFFSET + ((btnChangeBkgRectTop + btnChangeBkgRectBottom) >> 1);
            btnChangeTextX = (btnChangeBkgRectRight + btnChangeBkgRectLeft) >> 1;
            btnChangeTextSize = sp2px(px2dp(mainCircleRadius) / BTN_CHANGE_TEXT_SIZE_RATIO);
            btnChangeTextPaint.setTextSize(btnChangeTextSize);


            //main day number textView
            tvMainDayNumberY = midY - dp2px((px2dp(mainCircleRadius) / MAIN_DAY_NUMBER_TV_Y_RATIO)) + tvMainDayNumberPaint.getFontMetrics().descent;
            tvMainDayNumberX = midX;
            tvMainDayNumberTextSize = sp2px(px2dp(mainCircleRadius) / MAIN_DAY_NUMBER_TV_TEXT_SIZE_RATIO);
            tvMainDayNumberPaint.setTextSize(tvMainDayNumberTextSize);


            //week day name textView
            tvWeekDayNameY = midY - dp2px((px2dp(mainCircleRadius) / WEEK_DAY_NAME_TV_Y_RATIO)) + tvWeekDayNamePaint.getFontMetrics().descent;
            tvWeekDayNameX = midX;
            tvWeekDayNameTextSize = sp2px((px2dp(mainCircleRadius) / WEEK_DAY_NAME_TV_TEXT_SIZE_RATIO));
            tvWeekDayNamePaint.setTextSize(tvWeekDayNameTextSize);


            //optional textView
            tvOptionalY = midY + dp2px((px2dp(mainCircleRadius) / OPTIONAL_TV_Y_RATIO)) + tvOptionalPaint.getFontMetrics().descent;
            tvOptionalX = midX;
            tvOptionalTextSize = sp2px((px2dp(mainCircleRadius) / OPTIONAL_TV_TEXT_SIZE_RATIO));
            tvOptionalPaint.setTextSize(tvOptionalTextSize);


            //month name textView
            tvMonthNameTextSize = sp2px((px2dp(mediumCircleRadius) / MONTH_NAME_TV_TEXT_SIZE_RATIO));
            tvMonthNamePaint.setTextSize(tvMonthNameTextSize);
            tvMonthDayNumberTextSize = sp2px((px2dp(mediumCircleRadius) / DAY_OF_MONTH_TV_TEXT_SIZE_RATIO));
            tvMonthDayNumberPaint.setTextSize(tvMonthDayNumberTextSize);

            //init the angle for the first time
//            angle = calculateAngleForDay(25);
        }

        calculateMediumCircleParamsXY();
    }

    private void calculateMediumCircleParamsXY() {
        //medium circle x and y
        mediumCircleX = (float) Math.cos(Math.toRadians(angle));
        mediumCircleY = (float) Math.sin(Math.toRadians(angle));
        mediumCircleX *= outerRadius;
        mediumCircleX += midX;
        mediumCircleY *= outerRadius;
        mediumCircleY += midY;

        //month name textView
        tvMonthNameY = mediumCircleY + dp2px((int) (mediumCircleRadius / MONTH_NAME_TV_Y_RATIO)) + tvMonthNamePaint.getFontMetrics().descent;
        tvMonthNameX = mediumCircleX;

        //month day number textView
        tvMonthDayNumberY = mediumCircleY - dp2px((int) (mediumCircleRadius / DAY_OF_MONTH_TV_Y_RATIO)) + tvMonthDayNumberPaint.getFontMetrics().descent;
        tvMonthDayNumberX = mediumCircleX;
    }

    private void setClueAndViewData(ClueData clueData, ViewData viewData, OnDayChangedListener onDayChangedListener, OnButtonClickListener onButtonClickListener) {
        this.clueData = clueData;
        this.viewData = viewData;
        this.onDayChangedListener = onDayChangedListener;
        if (clueData != null) {
        }
        invalidate();
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        setTypeFaceForAllTexts();
        invalidate();
    }

    public ClueData getClueData() {
        return clueData;
    }

    public void setClueData(ClueData clueData) {
        this.clueData = clueData;
        invalidate();
    }

    public void setClueDate(ClueData clueDate, OnDayChangedListener onDayChangedListener, OnButtonClickListener onButtonClickListener) {
        this.clueData = clueDate;
        this.onDayChangedListener = onDayChangedListener;
        invalidate();
    }

    public ViewData getViewData() {
        return viewData;
    }

    public void setViewData(ViewData viewData) {
        this.viewData = viewData;
        invalidate();
    }

    public String getBtnChangeText() {
        return btnChangeText;
    }

    public void setBtnChangeText(String btnChangeText) {
        this.btnChangeText = btnChangeText;
        invalidate();
    }

    public OnDayChangedListener getOnDayChangedListener() {
        return onDayChangedListener;
    }

    public OnButtonClickListener getOnButtonClickListener() {
        return onButtonClickListener;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public void setOnDayChangedListener(OnDayChangedListener onDayChangedListener) {
        this.onDayChangedListener = onDayChangedListener;
    }

    private int getMinHeightWidth() {
        return Math.min(getHeight(), getWidth());
    }

    private float calculateAngleForDay(int dayNumber) {
        if (clueData == null) {
            if (offsetAngle == -1) {
                return -90 + DEFAULT_START_OFFSET_ANGLE;
            }
            return offsetAngle - 90;
        }
        float angleUnit = (360 - 2 * offsetAngle) / (float) clueData.getTotalDays();
        return angleUnit * dayNumber + offsetAngle - 90;
    }

    private int getDayFromAngle(float currentAngle) {
        if (clueData == null) {
            return 1;
        }
        if (currentAngle > 360) {
            currentAngle = currentAngle % 360;
            if (currentAngle > 270) {
                currentAngle = -(360 - currentAngle);
            }
        }
        if (currentAngle > 270) {
            currentAngle = -(360 - currentAngle);
        }
        float angleUnit = (360 - 2 * offsetAngle) / (float) clueData.getTotalDays();
        if (ANGLE_UNIT == -1) {
            ANGLE_UNIT = angleUnit / 2;
        }
        float temp = ((currentAngle + 90 - offsetAngle) / angleUnit);
        int currentDay;
        if (temp - Math.floor(temp) <= 0.5) {
            temp = (float) Math.floor(temp);
            currentDay = (int) temp;
            if (currentDay == 0) {
                currentDay++;
            }
        } else {
            temp = (float) Math.ceil(temp);
            currentDay = (int) temp;
        }
        if (currentDay < 1) {
            return -1;
        }
        return currentDay;
    }


    public int getCurrentDay() {
        return getDayFromAngle(angle);
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

    public void setMainCircleHoverRippleColor(int rippleColor, float alphaFactor) {
        mainCircleHoverRippleColor = rippleColor;
        mainCircleHoverAlphaFactor = alphaFactor;
    }

    public void setMediumCircleHoverRippleColor(int rippleColor, float alphaFactor) {
        mediumCircleHoverRippleColor = rippleColor;
        mediumCircleHoverAlphaFactor = alphaFactor;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mediumCircleHoverMaxRadius = (float) Math.sqrt(w * w + h * h);
    }

    public int adjustHoverAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public void setMainCircleHoverRadius(final float radius) {
        mainCircleHoverRadius = radius;
        if (mainCircleHoverRadius > 0) {
            mainCircleHoverRadialGradient = new RadialGradient(mainCircleHoverX, mainCircleHoverY, 2 * mainCircleHoverRadius, adjustHoverAlpha(mainCircleHoverRippleColor, mainCircleHoverAlphaFactor), mainCircleHoverRippleColor,
                    Shader.TileMode.CLAMP);
            mainCircleHoverPaint.setShader(mainCircleHoverRadialGradient);
        }
        invalidate();
    }

    public void setMediumCircleHoverRadius(final float radius) {
        mediumCircleHoverRadius = radius;
        if (mediumCircleHoverRadius > 0) {
            mediumCircleHoverRadialGradient = new RadialGradient(mediumCircleHoverX, mediumCircleHoverY, 2 * mediumCircleHoverRadius, adjustHoverAlpha(mediumCircleHoverRippleColor, mediumCircleHoverAlphaFactor), mediumCircleHoverRippleColor,
                    Shader.TileMode.CLAMP);
            mediumCircleHoverPaint.setShader(mediumCircleHoverRadialGradient);
        }
        invalidate();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float tempAngle = angle;
        ValueAnimator animator = ValueAnimator.ofFloat(tempAngle, calculateAngleForDay(getDayFromAngle(angle)));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (pressedChangeButton(event)) {
                    pressedBtnChange = true;
                }
                if (pressedMediumCircle(event)) {
                    pressedMediumCircle = true;
                    isFromUser = true;
                    mediumCircleHoverActionDown(event);
                }
                tempAngle = calculateAngleForDay(getDayFromAngle(angle));
                computeAngle(event.getX(), event.getY());
                runAnim(tempAngle, animator);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                pressedBtnChange = false;
                pressedMediumCircle = false;
                if (!isAnimRunning) {
                    isFromUser = false;
                }
                mediumCircleHoverActionUp(event);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (animator != null) {
                    animator.cancel();
                }
                if (pressedMediumCircle) {
                    computeAngle(event.getX(), event.getY());
                    mediumCircleHoverActionMove(event);
                }
                invalidate();
                return true;
            default:
                return false;
        }
    }

    private void mediumCircleHoverActionMove(MotionEvent event) {
        if (mediumCircleHover) {
            mediumCircleHoverX = event.getX();
            mediumCircleHoverY = event.getY();

            // Cancel the ripple animation when moved outside
            if (!pressedMediumCircle(event)) {
                setMediumCircleHoverRadius(0);
            } else {
                setMediumCircleHoverRadius(mediumCircleRadius);
            }
        }
    }

    private void mediumCircleHoverActionUp(MotionEvent event) {
        if (pressedMediumCircle(event) && mediumCircleHover) {
            mediumCircleHoverX = event.getX();
            mediumCircleHoverY = event.getY();

            final float tempRadius = (float) Math.sqrt(mediumCircleHoverX * mediumCircleHoverX + mediumCircleHoverY * mediumCircleHoverY);
            float targetRadius = Math.max(tempRadius, mediumCircleHoverMaxRadius);

            if (mediumCircleHoverIsAnimating) {
                mediumCircleHoverRadiusAnimator.cancel();
            }
            mediumCircleHoverRadiusAnimator = ObjectAnimator.ofFloat(this, "mediumCircleHoverRadius", mediumCircleRadius, targetRadius);
            mediumCircleHoverRadiusAnimator.setDuration(300);
            mediumCircleHoverRadiusAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mediumCircleHoverRadiusAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mediumCircleHoverIsAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    setMediumCircleHoverRadius(0);
                    mediumCircleHoverIsAnimating = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            mediumCircleHoverRadiusAnimator.start();
        }
    }

    private void mediumCircleHoverActionDown(MotionEvent event) {
        if (mediumCircleHover) {
            mediumCircleHoverX = event.getX();
            mediumCircleHoverY = event.getY();
            mediumCircleHoverRadiusAnimator = ObjectAnimator.ofFloat(this, "mediumCircleHoverRadius", 0, mediumCircleRadius).setDuration(400);
            mediumCircleHoverRadiusAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mediumCircleHoverRadiusAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mediumCircleHoverIsAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    setMediumCircleHoverRadius(0);
                    mediumCircleHoverIsAnimating = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            mediumCircleHoverRadiusAnimator.start();

        }
    }

    private void runAnim(float tempAngle, ValueAnimator animator) {
        float targetAngle = calculateAngleForDay(getDayFromAngle(angle));
        animator = ValueAnimator.ofFloat(tempAngle, targetAngle);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(200);
        animator.setRepeatCount(0);
        final ValueAnimator finalAnimator = animator;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                angle = (float) finalAnimator.getAnimatedValue();
                isAnimRunning = true;
                isFromUser = true;
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
            }

            @Override
            public void onAnimationResume(Animator animation) {
                super.onAnimationResume(animation);
            }
        });
        if (Math.abs(Math.abs(tempAngle) - Math.abs(targetAngle)) >= ANGLE_UNIT) {
            animator.start();
        }
    }

    private boolean pressedChangeButton(MotionEvent event) {
        return event.getX() > btnChangeBkgRectLeft && event.getX() < btnChangeBkgRectRight
                && event.getY() > btnChangeBkgRectTop && event.getY() < btnChangeBkgRectBottom;
    }

    private boolean pressedMediumCircle(MotionEvent event) {
        return Math.sqrt(Math.pow(Math.abs(event.getX() - mediumCircleX), 2) + Math.pow(Math.abs(event.getY() - mediumCircleY), 2)) < mediumCircleRadius;
    }

    private void computeAngle(float x, float y) {
        x -= getWidth() >> 1;
        y -= getHeight() >> 1;
        double radius = Math.sqrt(x * x + y * y);
        if (!pressedMediumCircle) {
            if (radius > outerRadius + 2 * mediumCircleRadius) return;
            if (radius < outerRadius / 1.5) return;
        }
        float selectedAngle = (int) (180.0 * Math.atan2(y, x) / Math.PI);
        selectedAngle = ((selectedAngle > 0) ? selectedAngle : 360 + selectedAngle);
        if (isValidAngle(selectedAngle)) {
            angle = selectedAngle;
            isFromUser = true;
        }
    }

    private boolean isValidAngle(float currentAngle) {
        return !(currentAngle > 270 - Math.abs(offsetAngle) / 2) || !(currentAngle < 270 + (90 - Math.abs(calculateAngleForDay(1))));
    }

    @Override
    public void onViewDataChanged(String weekDayName, String optionalText, String mainDayNumber, String monthDayNumber, String monthName, boolean isOptionalVisible, int today) {
        if (clueData != null) {
            if (today > clueData.getTotalDays()) {
                today = 1;
            }
        }
        tvWeekDayNameText = weekDayName;
        if (isOptionalVisible) {
            tvOptionalText = optionalText;
        } else {
            tvOptionalText = "";
        }
        tvMainDayNumberText = mainDayNumber;
        tvMonthDayNumberText = monthDayNumber;
        tvMonthDayNumberText = today + "";
        tvMonthNameText = monthName;
        if (lastDay != today && !isAnimRunning) {
            if (!mainCircleHoverIsAnimating) {
                lastDay = today;
                mainCircleHoverX = mediumCircleX;
                mainCircleHoverY = mediumCircleY;
                mainCircleHoverRadiusAnimator = ObjectAnimator.ofFloat(this, "mainCircleHoverRadius", 0, 2 * mainCircleRadius).setDuration(200);
                mainCircleHoverRadiusAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                mainCircleHoverRadiusAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        mainCircleHoverIsAnimating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        setMainCircleHoverRadius(0);
                        mainCircleHoverIsAnimating = false;
                        mainCircleColor = getCurrentDayPaint().getColor();
                        mainCirclePaint.setColor(mainCircleColor);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                mainCircleHoverRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mainCircleHoverRadius = (float) mainCircleHoverRadiusAnimator.getAnimatedValue();
                        invalidate();
                    }
                });
                mainCircleHoverRadiusAnimator.start();
            }
        }
        invalidate();
    }
}
