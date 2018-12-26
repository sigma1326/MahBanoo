package com.simorgh.redcalendar.View.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.calendarutil.model.YearMonthDay;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.moodview.MoodView;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.Model.database.model.DayMood;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.main.CycleViewModel;
import com.simorgh.weekdaypicker.WeekDayPicker;

import java.util.Calendar;

public class AddMoodFragment extends Fragment {

    private CycleViewModel mViewModel;
    private WeekDayPicker weekDayPicker;
    private MoodView mvBleeding;
    private MoodView mvEmotion;
    private MoodView mvPain;
    private MoodView mvEatingDesire;
    private MoodView mvHairStyle;


    public static AddMoodFragment newInstance() {
        return new AddMoodFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        mViewModel.getCycleLiveData().observe(this, cycle -> {
            if (weekDayPicker != null && cycle != null) {
                weekDayPicker.setCycleData(new CycleData(cycle.getRedDaysCount(),
                        cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate()));
                Log.d(AppManager.TAG, cycle.toString());
                updateViews();
            }
        });
        mViewModel.getMoodsLiveData().observe(this, dayMoods -> {
            updateViews();
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_note_fragment, container, false);
        mvBleeding = v.findViewById(R.id.moodViewBleeding);
        mvEmotion = v.findViewById(R.id.moodViewEmotion);
        mvPain = v.findViewById(R.id.moodViewPain);
        mvEatingDesire = v.findViewById(R.id.moodViewEatingDesire);
        mvHairStyle = v.findViewById(R.id.moodViewHairStyle);

        if (getArguments() != null) {
            YearMonthDay yearMonthDay = AddMoodFragmentArgs.fromBundle(getArguments()).getSelectedDay();
            if (yearMonthDay != null) {
                Calendar calendar = AppManager.getCalendarInstance();
                calendar.set(Calendar.DAY_OF_MONTH, yearMonthDay.getDay());
                calendar.set(Calendar.MONTH, yearMonthDay.getMonth());
                calendar.set(Calendar.YEAR, yearMonthDay.getYear());
                mViewModel.setSelectedDateMood(calendar);
                Toast.makeText(getContext(), yearMonthDay.toString(), Toast.LENGTH_SHORT).show();
            }
        }


        mvBleeding.setOnItemSelectedListener(selectedItems -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood == null) {
                dayMood = new DayMood();
            }
            dayMood.setId(mViewModel.getSelectedDateMood());
            if (selectedItems.isEmpty()) {
                dayMood.setTypeBleedingSelectedIndex(-1);
            } else {
                dayMood.setTypeBleedingSelectedIndex(selectedItems.get(0));
            }
            mViewModel.updateDayMood(dayMood);
        });

        mvEmotion.setOnItemSelectedListener(selectedItems -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood == null) {
                dayMood = new DayMood();
            }
            dayMood.setId(mViewModel.getSelectedDateMood());
            if (selectedItems.isEmpty()) {
                dayMood.setTypeEmotionSelectedIndices(null);
            } else {
                dayMood.setTypeEmotionSelectedIndices(selectedItems);
            }
            mViewModel.updateDayMood(dayMood);
        });

        mvPain.setOnItemSelectedListener(selectedItems -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood == null) {
                dayMood = new DayMood();
            }
            dayMood.setId(mViewModel.getSelectedDateMood());
            if (selectedItems.isEmpty()) {
                dayMood.setTypePainSelectedIndices(null);
            } else {
                dayMood.setTypePainSelectedIndices(selectedItems);
            }
            mViewModel.updateDayMood(dayMood);
        });

        mvEatingDesire.setOnItemSelectedListener(selectedItems -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood == null) {
                dayMood = new DayMood();
            }
            dayMood.setId(mViewModel.getSelectedDateMood());
            if (selectedItems.isEmpty()) {
                dayMood.setTypeEatingDesireSelectedIndices(null);
            } else {
                dayMood.setTypeEatingDesireSelectedIndices(selectedItems);
            }
            mViewModel.updateDayMood(dayMood);
        });

        mvHairStyle.setOnItemSelectedListener(selectedItems -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood == null) {
                dayMood = new DayMood();
            }
            dayMood.setId(mViewModel.getSelectedDateMood());
            if (selectedItems.isEmpty()) {
                dayMood.setTypeHairStyleSelectedIndices(null);
            } else {
                dayMood.setTypeHairStyleSelectedIndices(selectedItems);
            }
            mViewModel.updateDayMood(dayMood);
        });

        weekDayPicker = v.findViewById(R.id.weekDayPicker);
        weekDayPicker.setSelectedDate(AppManager.getCalendarInstance());
        weekDayPicker.setOnDaySelectedListener(day -> {
            mViewModel.setSelectedDateMood(day);
            updateViews();
        });
        return v;
    }

    private void updateViews() {
        mvBleeding.setSelectedItem(null);
        mvEmotion.setSelectedItems(null);
        mvPain.setSelectedItems(null);
        mvEatingDesire.setSelectedItems(null);
        mvHairStyle.setSelectedItems(null);

        weekDayPicker.setSelectedDate(mViewModel.getSelectedDateMood());
        DayMood dayMood = mViewModel.getDayMood();
        if (dayMood != null) {
            mvBleeding.setSelectedItem(dayMood.getTypeBleedingSelectedIndex());
            mvEmotion.setSelectedItems(dayMood.getTypeEmotionSelectedIndices());
            mvPain.setSelectedItems(dayMood.getTypePainSelectedIndices());
            mvEatingDesire.setSelectedItems(dayMood.getTypeEatingDesireSelectedIndices());
            mvHairStyle.setSelectedItems(dayMood.getTypeHairStyleSelectedIndices());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
