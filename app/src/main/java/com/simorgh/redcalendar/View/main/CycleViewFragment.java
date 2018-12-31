package com.simorgh.redcalendar.View.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.calendarutil.persiancalendar.PersianDate;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.cycleview.CycleView;
import com.simorgh.cycleview.OnViewDataChangedListener;
import com.simorgh.cycleview.SizeConverter;
import com.simorgh.databaseutils.model.DayMood;
import com.simorgh.moodview.MoodView;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.Model.MoodItem;
import com.simorgh.redcalendar.Model.MoodListAdapter;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.main.CycleViewModel;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private LinkedList<ImageView> items = new LinkedList<>();
    private RecyclerView rvDayMoods;

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

        rvDayMoods = v.findViewById(R.id.rv_day_moods);
        initMoodRecyclerView();

        return v;
    }

    FlexboxLayoutManager layoutManager;
    LinearLayoutManager linearLayoutManager;

    private void initMoodRecyclerView() {
        layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        rvDayMoods.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rvDayMoods.setLayoutManager(linearLayoutManager);
        rvDayMoods.setNestedScrollingEnabled(false);
        rvDayMoods.setAdapter(new MoodListAdapter(new MoodListAdapter.ItemDiffCallBack()));
        rvDayMoods.setHasFixedSize(true);
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

    private List<MoodItem> moodItems = new LinkedList<>();

    private void showDayMoods() {
        DayMood dayMood = mViewModel.getDayMood(temp);
        moodItems = new LinkedList<>();
        if (rvDayMoods != null) {
//            rvDayMoods.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (dayMood != null && rvDayMoods != null) {
            if (dayMood.getTypeBleedingSelectedIndex() != -1) {
                moodItems.add(new MoodItem(dayMood.getId(), MoodView.TYPE_BLEEDING, dayMood.getTypeBleedingSelectedIndex()));
            }
            if (dayMood.getTypeEmotionSelectedIndices() != null) {
                for (int i = 0; i < dayMood.getTypeEmotionSelectedIndices().size(); i++) {
                    moodItems.add(new MoodItem(dayMood.getId(), MoodView.TYPE_EMOTION, dayMood.getTypeEmotionSelectedIndices().get(i)));
                }
            }
            if (dayMood.getTypePainSelectedIndices() != null) {
                for (int i = 0; i < dayMood.getTypePainSelectedIndices().size(); i++) {
                    moodItems.add(new MoodItem(dayMood.getId(), MoodView.TYPE_PAIN, dayMood.getTypePainSelectedIndices().get(i)));
                }
            }
            if (dayMood.getTypeEatingDesireSelectedIndices() != null) {
                for (int i = 0; i < dayMood.getTypeEatingDesireSelectedIndices().size(); i++) {
                    moodItems.add(new MoodItem(dayMood.getId(), MoodView.TYPE_EATING_DESIRE, dayMood.getTypeEatingDesireSelectedIndices().get(i)));
                }
            }
            if (dayMood.getTypeHairStyleSelectedIndices() != null) {
                for (int i = 0; i < dayMood.getTypeHairStyleSelectedIndices().size(); i++) {
                    moodItems.add(new MoodItem(dayMood.getId(), MoodView.TYPE_HAIR_STYLE, dayMood.getTypeHairStyleSelectedIndices().get(i)));
                }
            }
            Log.d("debug13", "showDayMoods: " + moodItems.size());
        }
        assert rvDayMoods != null;
        if (rvDayMoods != null) {
            ViewGroup.LayoutParams params = rvDayMoods.getLayoutParams();
            params.width = (int) (moodItems.size() * SizeConverter.dpToPx(Objects.requireNonNull(getContext()), 79));
            rvDayMoods.setLayoutParams(params);
//            rvDayMoods.invalidate();
        }
        ((MoodListAdapter) Objects.requireNonNull(rvDayMoods.getAdapter())).submitList(moodItems);
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
