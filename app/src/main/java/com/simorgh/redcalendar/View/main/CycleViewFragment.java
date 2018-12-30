package com.simorgh.redcalendar.View.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.calendarutil.persiancalendar.PersianDate;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.cycleview.CycleView;
import com.simorgh.cycleview.OnViewDataChangedListener;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.databaseutils.model.DayMood;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.main.CycleViewModel;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class CycleViewFragment extends Fragment implements CycleView.OnButtonClickListener, CycleView.OnDayChangedListener {

    private CycleViewModel mViewModel;
    private CycleView cycleView;
    private Calendar start = AppManager.getCalendarInstance();
    private Calendar temp = AppManager.getCalendarInstance();
    private Calendar today = AppManager.getCalendarInstance();
    private Calendar currentCycleStartDate = AppManager.getCalendarInstance();
    private boolean isFirstDraw = true;
    private OnButtonChangeClickListener onButtonChangeClickListener;
    private OnDayTypeChangedListener onDayTypeChangedListener;

    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private LinkedList<ImageView> items = new LinkedList<>();


    public static CycleViewFragment newInstance() {
        return new CycleViewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cycle_view_fragment, container, false);
        cycleView = v.findViewById(R.id.clueView);
        cycleView.setOnButtonClickListener(this);
        cycleView.setOnDayChangedListener(this);

        item1 = v.findViewById(R.id.img_item1);
        item2 = v.findViewById(R.id.img_item2);
        item3 = v.findViewById(R.id.img_item3);
        items.add(item1);
        items.add(item2);
        items.add(item3);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        mViewModel.getCycleLiveData().observe(this, cycle -> {
            if (cycleView != null && cycle != null) {
                mViewModel.setCycle(cycle);
                cycleView.setCycleData(new CycleData(cycle.getRedDaysCount(),
                        cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate()));
                if (isFirstDraw) {
                    isFirstDraw = false;
                    cycleView.showToday((CalendarTool.PersianToGregorian(new PersianDate())));
                } else {
                    cycleView.showToday(mViewModel.getSelectedDate());
                }
                start.setTimeInMillis(cycle.getStartDate().getTimeInMillis());
                Log.d(AppManager.TAG, cycle.toString());
            }
        });
    }

    @Override
    public void onButtonChangeClick() {
        if (onButtonChangeClickListener != null) {
            onButtonChangeClickListener.onButtonChangeClicked();
        }
    }

    public interface OnButtonChangeClickListener {
        void onButtonChangeClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onButtonChangeClickListener = (OnButtonChangeClickListener) context;
        onDayTypeChangedListener = (OnDayTypeChangedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onButtonChangeClickListener = null;
        onDayTypeChangedListener = null;
    }

    @Override
    public void onDayChanged(int day, int dayType, OnViewDataChangedListener listener) {
        if (day == -1) {
            day = mViewModel.getSelectedDay();
        }
        mViewModel.setSelectedDay(day);
        long diffDays;
        int day2;
        diffDays = CalendarTool.getDaysFromDiff(today, start);
        if (diffDays >= 0 && mViewModel.getCycle() != null) {
            day2 = (int) ((diffDays) % mViewModel.getCycle().getTotalDaysCount()) + 1;
            currentCycleStartDate.setTimeInMillis(today.getTimeInMillis());
            currentCycleStartDate.add(Calendar.DAY_OF_MONTH, -1 * day2 + 1);
            temp.setTimeInMillis(today.getTimeInMillis());
            temp.add(Calendar.DAY_OF_MONTH, -1 * day2 + day);

            showDayMoods();

            mViewModel.setSelectedDate(temp);
//            mViewModel.setSelectedDateMood(temp);
            if (onDayTypeChangedListener != null) {
                onDayTypeChangedListener.onDayChanged(temp);
            }
        } else {
            throw new UnsupportedOperationException("invalid cycle date");
        }

        temp.setTimeInMillis(currentCycleStartDate.getTimeInMillis());
        temp.add(Calendar.DAY_OF_MONTH, day - 1);


        PersianCalendar persianCalendar = CalendarTool.GregorianToPersian(temp);

        String optionalText = "احتمال بارداری: ";
        switch (dayType) {
            case CycleView.TYPE_RED:
                break;
            case CycleView.TYPE_GREEN:
                optionalText += "متوسط";
                break;
            case CycleView.TYPE_GREEN2:
                optionalText += "زیاد";
                break;
            case CycleView.TYPE_GRAY:
                optionalText += "کم";
                break;
            case CycleView.TYPE_YELLOW:
                break;
        }
        listener.onViewDataChanged(persianCalendar.getPersianWeekDayName()
                , optionalText, days[day - 1], persianCalendar.getPersianDay() + ""
                , CalendarTool.getIranianMonthName(persianCalendar.getPersianMonth() + 1)
                , Objects.requireNonNull(mViewModel.getCycleLiveData().getValue())
                        .isShowPregnancyProb(), day);

        if (onDayTypeChangedListener != null) {
            onDayTypeChangedListener.onDayTypeChanged(dayType);
        }

    }

    private void showDayMoods() {
        DayMood dayMood = mViewModel.getDayMood(temp);
        item1.setImageResource(0);
        item1.setVisibility(View.GONE);
        item2.setImageResource(0);
        item2.setVisibility(View.GONE);
        item3.setImageResource(0);
        item3.setVisibility(View.GONE);
        if (dayMood != null) {
            int index = 0;
            ImageView imageView = item2;
            if (dayMood.getTypeBleedingSelectedIndex() != -1) {
                switch (dayMood.getTypeBleedingSelectedIndex()) {
                    case 0:
                        imageView.setImageResource(R.drawable.item_bleeding1);
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.item_bleeding2);
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.item_bleeding3);
                        break;
                    case 3:
                        imageView.setImageResource(R.drawable.item_bleeding4);
                        break;
                }
                imageView.setVisibility(View.VISIBLE);
                index++;
                imageView = item1;
            }
            if (dayMood.getTypeEmotionSelectedIndices() != null) {
                if (dayMood.getTypeEmotionSelectedIndices().size() > 0) {
                    switch (dayMood.getTypeEmotionSelectedIndices().get(0)) {
                        case 0:
                            imageView.setImageResource(R.drawable.item_emotion1);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.item_emotion2);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.item_emotion3);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.item_emotion4);
                            break;
                    }
                    imageView.setVisibility(View.VISIBLE);
                    ++index;
                    imageView = item3;
                }
            }
            if (dayMood.getTypePainSelectedIndices() != null) {
                if (dayMood.getTypePainSelectedIndices().size() > 0 && index <= 2) {
                    if (index == 0) {
                        imageView = item2;
                    } else if (index == 1) {
                        imageView = item1;
                    } else if (index == 2) {
                        imageView = item3;
                    }
                    switch (dayMood.getTypePainSelectedIndices().get(0)) {
                        case 0:
                            imageView.setImageResource(R.drawable.item_pain1);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.item_pain2);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.item_pain3);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.item_pain4);
                            break;
                    }
                    imageView.setVisibility(View.VISIBLE);
                    ++index;
                }
            }
            if (dayMood.getTypeEatingDesireSelectedIndices() != null) {
                if (dayMood.getTypeEatingDesireSelectedIndices().size() > 0 && index <= 2) {
                    if (index == 0) {
                        imageView = item2;
                    } else if (index == 1) {
                        imageView = item1;
                    } else if (index == 2) {
                        imageView = item3;
                    }
                    switch (dayMood.getTypeEatingDesireSelectedIndices().get(0)) {
                        case 0:
                            imageView.setImageResource(R.drawable.item_eating_desire1);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.item_eating_desire2);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.item_eating_desire3);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.item_eating_desire4);
                            break;
                    }
                    imageView.setVisibility(View.VISIBLE);
                    ++index;
                }
            }
            if (dayMood.getTypeHairStyleSelectedIndices() != null) {
                if (dayMood.getTypeHairStyleSelectedIndices().size() > 0 && index <= 2) {
                    if (index == 0) {
                        imageView = item2;
                    } else if (index == 1) {
                        imageView = item1;
                    } else if (index == 2) {
                        imageView = item3;
                    }
                    switch (dayMood.getTypeHairStyleSelectedIndices().get(0)) {
                        case 0:
                            imageView.setImageResource(R.drawable.item_hair_style1);
                            break;
                        case 1:
                            imageView.setImageResource(R.drawable.item_hair_style2);
                            break;
                        case 2:
                            imageView.setImageResource(R.drawable.item_hair_style3);
                            break;
                        case 3:
                            imageView.setImageResource(R.drawable.item_hair_style4);
                            break;
                    }
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public interface OnDayTypeChangedListener {
        public void onDayTypeChanged(int dayType);

        public void onDayChanged(Calendar day);
    }


    private static String[] days = {
            "روز اول",
            "روز دوم",
            "روز سوم",
            "روز چهارم",
            "روز پنجم",
            "روز ششم",
            "روز هفتم",
            "روز هشتم",
            "روز نهم",
            "روز دهم",
            "روز یازدهم",
            "روز دوازدهم",
            "روز سیزدهم",
            "روز چهاردهم",
            "روز پانزدهم",
            "روز شانزدهم",
            "روز هفدهم",
            "روز هجدهم",
            "روز نوزدهم",
            "روز بیستم",
            "روز بیست و یکم",
            "روز بیست و دوم",
            "روز بیست و سوم",
            "روز بیست و چهارم",
            "روز بیست و پنجم",
            "روز بیست و ششم",
            "روز بیست و هفتم",
            "روز بیست و هشتم",
            "روز بیست و نهم",
            "روز سی‌ام",
            "روز سی و یکم",
            "روز سی و دوم",
            "روز سی و سوم",
            "روز سی و چهارم",
            "روز سی و پنجم",
            "روز سی و ششم",
            "روز سی و هفتم",
            "روز سی و هشتم",
            "روز سی و نهم",
            "روز چهلم",
    };
}
