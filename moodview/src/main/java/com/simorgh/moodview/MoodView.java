package com.simorgh.moodview;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;

public class MoodView extends View {
    public static final int TYPE_BLEEDING = 0;
    public static final int TYPE_EMOTION = 1;
    public static final int TYPE_PAIN = 2;
    public static final int TYPE_EATING_DESIRE = 3;
    public static final int TYPE_HAIR_STYLE = 4;

    //default view height and width
    private static final int DEFAULT_HEIGHT = 150;
    private static final int DEFAULT_WIDTH = 360;

    private float realHeight = -1;
    private float realWidth = -1;

    private int moodType = TYPE_BLEEDING;

    private String title = "";
    private String[] itemStrings = new String[4];
    private int itemSize = 4;

    private TextPaint itemTextPaint;
    private int itemTextColor;
    private float itemTextSize;

    private TextPaint titleTextPaint;
    private int titleTextColor;
    private float titleTextSize;

    private Paint linePaint;
    private int lineColor;
    private float lineLength;

    private Typeface typeface;

    private float itemIconSize;
    private float checkIconSize;

    private boolean isMultiSelect = false;
    private RectF iconRect = new RectF();
    private RectF iconCheckRect = new RectF();

    private List<Integer> selectedItems = new ArrayList<>();
    private List<Bitmap> itemIcons = new ArrayList<>();

    private Bitmap checkIcon;

    private OnItemSelectedListener onItemSelectedListener;

    public MoodView(Context context) {
        super(context);
        initAttrs(context, null);
        init();
    }

    public MoodView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public MoodView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MoodView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MoodView);
        Resources resources = getResources();

        moodType = typedArray.getInt(R.styleable.MoodView_itemType, TYPE_BLEEDING);

        if (moodType == TYPE_EATING_DESIRE) {
            itemSize = 4;
        } else {
            itemSize = 4;
        }
        isMultiSelect = moodType != TYPE_BLEEDING;

        itemTextColor = typedArray.getColor(R.styleable.MoodView_itemTextColor, getItemTextColor(moodType, resources));
        titleTextColor = typedArray.getColor(R.styleable.MoodView_titleTextColor, resources.getColor(R.color.titleTextColor));
        lineColor = typedArray.getColor(R.styleable.MoodView_lineColor, resources.getColor(R.color.lineColor));

        itemTextSize = typedArray.getDimension(R.styleable.MoodView_itemTextSize, resources.getDimension(R.dimen.itemTextSize));
        titleTextSize = typedArray.getDimension(R.styleable.MoodView_titleTextSize, resources.getDimension(R.dimen.titleTextSize));

        checkIcon = BitmapFactory.decodeResource(getResources(), R.drawable.check);

        initItemStrings(resources);

        initItemIcons();

        title = getTitleByType(resources);

        typedArray.recycle();
    }

    private void initItemIcons() {
        Bitmap bitmap;
        switch (moodType) {
            case TYPE_BLEEDING:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_bleeding1);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_bleeding2);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_bleeding3);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_bleeding4);
                itemIcons.add(bitmap);
                break;
            case TYPE_EMOTION:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_emotion1);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_emotion2);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_emotion3);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_emotion4);
                itemIcons.add(bitmap);
                break;
            case TYPE_PAIN:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_pain1);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_pain2);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_pain3);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_pain4);
                itemIcons.add(bitmap);
                break;
            case TYPE_EATING_DESIRE:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_eating_desire1);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_eating_desire2);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_eating_desire3);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_eating_desire4);
                itemIcons.add(bitmap);
                break;
            case TYPE_HAIR_STYLE:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_hair_style1);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_hair_style2);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_hair_style3);
                itemIcons.add(bitmap);

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.item_hair_style4);
                itemIcons.add(bitmap);
                break;
            default:
        }
    }

    private String getTitleByType(Resources resources) {
        switch (moodType) {
            case TYPE_BLEEDING:
                return resources.getString(R.string.type_bleeding_title);
            case TYPE_EMOTION:
                return resources.getString(R.string.type_emotion_title);
            case TYPE_PAIN:
                return resources.getString(R.string.type_pain_title);
            case TYPE_EATING_DESIRE:
                return resources.getString(R.string.type_eating_desire_title);
            case TYPE_HAIR_STYLE:
                return resources.getString(R.string.type_hair_style_title);
            default:
                return "";
        }
    }

    private void initItemStrings(Resources resources) {
        for (int i = 0; i < itemSize; i++) {
            switch (moodType) {
                case TYPE_BLEEDING:
                    itemStrings[i] = resources.getStringArray(R.array.type_bleeding_strings)[i];
                    break;
                case TYPE_EMOTION:
                    itemStrings[i] = resources.getStringArray(R.array.type_emotion_strings)[i];
                    break;
                case TYPE_PAIN:
                    itemStrings[i] = resources.getStringArray(R.array.type_pain_strings)[i];
                    break;
                case TYPE_EATING_DESIRE:
                    itemStrings[i] = resources.getStringArray(R.array.type_eating_desire_strings)[i];
                    break;
                case TYPE_HAIR_STYLE:
                    itemStrings[i] = resources.getStringArray(R.array.type_hair_style_strings)[i];
                    break;
                default:
                    itemStrings[i] = "";
            }
        }
    }

    private int getItemTextColor(int moodType, Resources resources) {
        switch (moodType) {
            case TYPE_BLEEDING:
                return resources.getColor(R.color.typeBleedingTextColor);
            case TYPE_EMOTION:
                return resources.getColor(R.color.typeEmotionTextColor);
            case TYPE_PAIN:
                return resources.getColor(R.color.typePainTextColor);
            case TYPE_EATING_DESIRE:
                return resources.getColor(R.color.typeEatingDesireTextColor);
            case TYPE_HAIR_STYLE:
                return resources.getColor(R.color.typeHairStyleTextColor);
            default:
                return resources.getColor(R.color.typeBleedingTextColor);
        }
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(dp2px(1));
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(lineColor);

        AssetManager assetMgr = getContext().getAssets();
        typeface = Typeface.createFromAsset(assetMgr, "fonts/iransans_medium.ttf");

        itemTextPaint = new TextPaint();
        itemTextPaint.setAntiAlias(true);
        itemTextPaint.setStyle(Paint.Style.FILL);
        itemTextPaint.setTextSize(itemTextSize);
        itemTextPaint.setTextAlign(Paint.Align.CENTER);
        itemTextPaint.setColor(itemTextColor);
        itemTextPaint.setTypeface(typeface);

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
        itemIconSize = (realHeight - 2) / 2.05f;
        checkIconSize = (itemIconSize) / 3f;
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
        canvas.drawText(getTitleByType(getResources())
                , dp2px(realWidth - 32), titleTextPaint.getFontMetrics().descent / 2f + dp2px(12), titleTextPaint);

        for (int j = 0; j < selectedItems.size(); j++) {
            if (moodType == TYPE_BLEEDING) {
                Log.d("debug13", "bleed: " + selectedItems.get(j));
            }
            if (moodType == TYPE_EMOTION) {
                Log.d("debug13", "emotion: " + selectedItems.get(j));
            }
        }

        float y = dp2px(realHeight / 2.08f);
        int i = 0;
        for (float x = dp2px(realWidth) - dp2px(realWidth) / 8f; x > 0 && i < itemSize; x -= dp2px(realWidth) / 4f, i++) {
            iconRect.set(x - dp2px(itemIconSize / 2), y - dp2px(itemIconSize / 2), x + dp2px(itemIconSize / 2), y + dp2px(itemIconSize / 2));
            iconCheckRect.set(x + dp2px(itemIconSize / 2) - dp2px(checkIconSize)
                    , y + dp2px(itemIconSize / 2) - dp2px(checkIconSize)
                    , x + dp2px(itemIconSize / 2), y + dp2px(itemIconSize / 2));
            canvas.drawBitmap(itemIcons.get(i), null, iconRect, null);
            if (selectedItems.contains(i)) {
                canvas.drawBitmap(checkIcon, null, iconCheckRect, null);
            }
            canvas.drawText(itemStrings[i], x, y + dp2px(60), itemTextPaint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return false;
                }
                float y = dp2px(realHeight / 2f);
                float radius = dp2px(itemIconSize);
                int i = 0;
                for (float x = dp2px(realWidth) - dp2px(realWidth) / 8f; x > 0 && i < itemSize; x -= dp2px(realWidth) / 4f, i++) {
                    if (Math.sqrt(Math.pow(Math.abs(event.getX() - x), 2) + Math.pow(Math.abs(event.getY() - y), 2)) < radius) {
                        if (isMultiSelect) {
                            if (selectedItems.contains(i)) {
                                selectedItems.remove(new Integer(i));
                            } else {
                                selectedItems.add(new Integer(i));
                            }
                        } else {
                            if (selectedItems.contains(i)) {
                                selectedItems.remove(new Integer(i));
                            } else {
                                selectedItems.clear();
                                selectedItems.add(new Integer(i));
                            }
                        }
                        if (onItemSelectedListener != null) {
                            onItemSelectedListener.onItemSelected(selectedItems);
                        }
                        break;
                    }
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_DOWN:
                invalidate();
                return true;
            default:
                return false;
        }
    }

    public OnItemSelectedListener getOnItemSelectedListener() {
        return onItemSelectedListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public int getMoodType() {
        return moodType;
    }

    public void setMoodType(int moodType) {
        this.moodType = moodType;
        initAttrs(getContext(), null);
        init();
        postInvalidate();
    }


    public List<Integer> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Integer> selectedItems) {
        if (selectedItems == null || selectedItems.size() == 0) {
            this.selectedItems.clear();
        } else {
            this.selectedItems.clear();
            this.selectedItems.addAll(selectedItems);
        }
        postInvalidate();
    }

    public void setSelectedItem(Integer selectedItem) {
        if (selectedItem == null) {
            this.selectedItems.clear();
        } else {
            this.selectedItems.clear();
            this.selectedItems.add(selectedItem);
        }
        invalidate();
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

    public interface OnItemSelectedListener {
        public void onItemSelected(List<Integer> selectedItems);
    }

}
