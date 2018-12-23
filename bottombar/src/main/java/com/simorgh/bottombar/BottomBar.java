package com.simorgh.bottombar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class BottomBar extends View {

    private ArrayList<BottomItem> items = new ArrayList<>();

    private float realHeight = -1;
    private float realWidth = -1;

    private float circleRadius = -1;
    private float circleX = -1;
    private float circleY = -1;
    private float circleIconWidth = -1;
    private float circleIconHeight = -1;

    private float itemIconWidth = -1;
    private float itemIconHeight = -1;
    private float itemTextX = -1;
    private float itemTextY = -1;
    private float itemTextSize = -1;


    private Paint backgroundPaint;
    private int backgroundColor;
    private float backgroundLeftY;

    private Paint mainBackgroundPaint;
    private int mainBackgroundColor;

    private Paint circlePaint;
    private int circleColor;
    private Paint circleIconPaint;
    private int circleIconColor;

    private Paint selectedIconPaint;
    private Paint unSelectedIconPaint;
    private TextPaint itemTextPaint;
    private static Bitmap circleIcon;
    private int selectedIconColor;
    private int unSelectedIconColor;

    //default view height and width
    private static final int DEFAULT_HEIGHT = 90;
    private static final int DEFAULT_WIDTH = 360;


    private Typeface typeface;


    private Bitmap icon_settings;
    private Bitmap icon_account;
    private Bitmap icon_calendar;
    private Bitmap icon_cycle;

    private Rect circleIconRect = new Rect();
    private Rect settingsIconRect = new Rect();
    private Rect accountIconRect = new Rect();
    private Rect calendarIconRect = new Rect();
    private Rect cycleIconRect = new Rect();
    private float icons_y_center = -1;
    private float icons_x_center = -1;

    private int selectedIndex = 4;

    private OnItemClickListener itemClickListener;
    private OnCircleItemClickListener circleItemClickListener;

    public class BottomItem {
        private int index;
        private String text;
        private Bitmap icon;

        public BottomItem(int index, String text, Bitmap icon) {
            this.index = index;
            this.text = text;
            this.icon = icon;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Bitmap getIcon() {
            return icon;
        }

        public void setIcon(Bitmap icon) {
            this.icon = icon;
        }
    }


    public BottomBar(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomBar);
        Resources resources = getResources();

        backgroundColor = typedArray.getColor(R.styleable.BottomBar_backgroundColor, resources.getColor(R.color.background_color));
        mainBackgroundColor = typedArray.getColor(R.styleable.BottomBar_mainBackgroundColor, resources.getColor(R.color.main_background_color));

        circleColor = typedArray.getColor(R.styleable.BottomBar_circleColor, resources.getColor(R.color.circle_color));
        circleIconColor = typedArray.getColor(R.styleable.BottomBar_circleColor, resources.getColor(R.color.circle_icon_color));

        selectedIconColor = typedArray.getColor(R.styleable.BottomBar_selectedColor, resources.getColor(R.color.selected_color));
        unSelectedIconColor = typedArray.getColor(R.styleable.BottomBar_unSelectedColor, resources.getColor(R.color.unselected_color));

        itemTextSize = typedArray.getDimension(R.styleable.BottomBar_textSize, resources.getDimension(R.dimen.textSize));

        typedArray.recycle();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);

        mainBackgroundPaint = new Paint();
        mainBackgroundPaint.setAntiAlias(true);
        mainBackgroundPaint.setStyle(Paint.Style.FILL);
        mainBackgroundPaint.setColor(mainBackgroundColor);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleColor);

        circleIconPaint = new Paint();
        circleIconPaint.setAntiAlias(true);
        circleIconPaint.setStyle(Paint.Style.FILL);
        circleIconPaint.setColor(circleIconColor);

        ColorFilter colorFilter = new LightingColorFilter(Color.TRANSPARENT, selectedIconColor);

        selectedIconPaint = new Paint();
        selectedIconPaint.setAntiAlias(true);
        selectedIconPaint.setStyle(Paint.Style.FILL);
        selectedIconPaint.setColor(selectedIconColor);
        selectedIconPaint.setColorFilter(colorFilter);

        colorFilter = new LightingColorFilter(Color.TRANSPARENT, unSelectedIconColor);
        unSelectedIconPaint = new Paint();
        unSelectedIconPaint.setAntiAlias(true);
        unSelectedIconPaint.setStyle(Paint.Style.FILL);
        unSelectedIconPaint.setColor(unSelectedIconColor);
        unSelectedIconPaint.setColorFilter(colorFilter);


        AssetManager assetMgr = getContext().getAssets();
        typeface = Typeface.createFromAsset(assetMgr, "fonts/iransans_medium.ttf");

        itemTextPaint = new TextPaint();
        itemTextPaint.setTextSize(itemTextSize);
        itemTextPaint.setColor(selectedIconColor);
        itemTextPaint.setFakeBoldText(true);
        itemTextPaint.setTextAlign(Paint.Align.CENTER);
        itemTextPaint.setTypeface(typeface);

        circleIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_pen);
        icon_settings = BitmapFactory.decodeResource(getResources(), R.drawable.icon_setting);
        icon_account = BitmapFactory.decodeResource(getResources(), R.drawable.icon_account);
        icon_calendar = BitmapFactory.decodeResource(getResources(), R.drawable.icon_calendar);
        icon_cycle = BitmapFactory.decodeResource(getResources(), R.drawable.icon_cycle);

        BottomItem bottomItem;
        bottomItem = new BottomItem(1, "تنظیمات", icon_settings);
        items.add(bottomItem);

        bottomItem = new BottomItem(2, "حساب کاربری", icon_account);
        items.add(bottomItem);

        bottomItem = new BottomItem(3, "تقویم", icon_calendar);
        items.add(bottomItem);

        bottomItem = new BottomItem(4, "دوره فعلی", icon_cycle);
        items.add(bottomItem);
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

        circleRadius = realHeight / 3f;
        circleX = realWidth / 2f;
        circleY = realHeight / 3f + 5;

        circleIconWidth = realWidth / 10.6f;
        circleIconHeight = realWidth / 10.6f;

        itemIconHeight = realWidth / 18f;
        itemIconWidth = realWidth / 18f;
        backgroundLeftY = realHeight / 3f;
        icons_y_center = backgroundLeftY + 22;
        icons_x_center = realWidth / 8.4f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.DST);

        drawBackground(canvas);

        drawMainCircle(canvas);

        drawItems(canvas);

    }

    private void drawBackground(Canvas canvas) {
//        mainBackgroundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(0, 0, dp2px(realWidth), dp2px(realHeight / 3f), mainBackgroundPaint);
        canvas.drawRect(0, dp2px(backgroundLeftY), dp2px(realWidth), dp2px(realHeight), backgroundPaint);
    }

    private void drawMainCircle(Canvas canvas) {
        circleIconRect.set((int) dp2px(circleX - 17), (int) dp2px(circleY - 17), (int) dp2px(circleX + 17), (int) dp2px(circleY + 17));
        canvas.drawCircle(dp2px(circleX), dp2px(circleY), dp2px(circleRadius), circlePaint);
        canvas.drawBitmap(circleIcon, null, circleIconRect, circlePaint);
    }

    private void drawItems(Canvas canvas) {
        settingsIconRect.set((int) dp2px(icons_x_center - 10), (int) dp2px(icons_y_center - 10)
                , (int) dp2px(icons_x_center + 10), (int) dp2px(icons_y_center + 10));

        accountIconRect.set((int) dp2px(icons_x_center * 2.7f - 10), (int) dp2px(icons_y_center - 12)
                , (int) dp2px(icons_x_center * 2.7f + 10), (int) dp2px(icons_y_center + 12));

        cycleIconRect.set((int) dp2px(realWidth - icons_x_center - 10), (int) dp2px(icons_y_center - 10)
                , (int) dp2px(realWidth - icons_x_center + 10), (int) dp2px(icons_y_center + 10));

        calendarIconRect.set((int) dp2px(realWidth - icons_x_center * 2.7f - 10), (int) dp2px(icons_y_center - 10)
                , (int) dp2px(realWidth - icons_x_center * 2.7f + 10), (int) dp2px(icons_y_center + 10));

        for (int i = 1; i <= items.size(); i++) {
            canvas.drawBitmap(items.get(i - 1).getIcon(), null, getIconRect(i), getItemIconPaint(i));
            canvas.drawText(items.get(i - 1).getText(), getIconRect(i).centerX(), dp2px(realHeight - 10), getItemTextPaint(i));
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Math.sqrt(Math.pow(Math.abs(event.getX() - dp2px(circleX)), 2) + Math.pow(Math.abs(event.getY() - dp2px(circleY)), 2))
                        < (getIconRect(2).centerX() - getIconRect(1).centerX() - 50)) {
                    if (circleItemClickListener != null) {
                        circleItemClickListener.onClick(true);
                    }
                } else {
                    for (int i = 0; i < items.size(); i++) {
                        if (Math.sqrt(Math.pow(Math.abs(event.getX() - getIconRect(i + 1).centerX()), 2) + Math.pow(Math.abs(event.getY() - getIconRect(i + 1).centerY()), 2))
                                < (getIconRect(2).centerX() - getIconRect(1).centerX() - 50)) {
                            selectedIndex = i + 1;
                            if (itemClickListener != null) {
                                itemClickListener.onClick(items.get(selectedIndex - 1), true);
                            }
                            break;
                        }
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

    private Rect getIconRect(final int i) {
        switch (i) {
            case 1:
                return settingsIconRect;
            case 2:
                return accountIconRect;
            case 3:
                return calendarIconRect;
            case 4:
                return cycleIconRect;
            default:
                return settingsIconRect;
        }
    }

    private Paint getItemIconPaint(final int i) {
        if (i == selectedIndex) {
            return selectedIconPaint;
        } else {
            return unSelectedIconPaint;
        }
    }

    private Paint getItemTextPaint(final int i) {
        if (i == selectedIndex) {
            itemTextPaint.setColor(selectedIconColor);
        } else {
            itemTextPaint.setColor(unSelectedIconColor);
        }
        return itemTextPaint;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * @param selectedIndex: starts from left to right (1 to 4) without counting the main circle
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        if (itemClickListener != null) {
            if (selectedIndex == -1) {
                circleItemClickListener.onClick(false);
            } else {
                itemClickListener.onClick(items.get(selectedIndex - 1), false);
            }
        }
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


    public OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OnCircleItemClickListener getCircleItemClickListener() {
        return circleItemClickListener;
    }

    public void setCircleItemClickListener(OnCircleItemClickListener circleItemClickListener) {
        this.circleItemClickListener = circleItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(BottomItem item, boolean fromUser);
    }

    public interface OnCircleItemClickListener {
        void onClick(boolean fromUser);
    }

}
