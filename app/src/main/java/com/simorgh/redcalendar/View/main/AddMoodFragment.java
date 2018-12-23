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

import com.simorgh.cycleutils.ClueData;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.main.AddMoodViewModel;
import com.simorgh.redcalendar.ViewModel.main.CycleViewModel;
import com.simorgh.weekdaypicker.WeekDayPicker;

import java.util.Calendar;

public class AddMoodFragment extends Fragment implements WeekDayPicker.onDaySelectedListener {

    private AddMoodViewModel mViewModel;
    private WeekDayPicker weekDayPicker;
    private CycleViewModel cycleViewModel;


    public static AddMoodFragment newInstance() {
        return new AddMoodFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_note_fragment, container, false);
        weekDayPicker = v.findViewById(R.id.weekDayPicker);
        weekDayPicker.setOnDaySelectedListener(this);
        weekDayPicker.setSelectedDate(Calendar.getInstance());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddMoodViewModel.class);
        cycleViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        cycleViewModel.getCycleLiveData().observe(this, cycle -> {
            if (weekDayPicker != null && cycle != null) {
                weekDayPicker.setClueData(new ClueData(cycle.getRedDaysCount(),
                        cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate()));
                Log.d(AppManager.TAG, cycle.toString());
            }
        });
    }

    @Override
    public void onDaySelected(Calendar day) {

    }
}
