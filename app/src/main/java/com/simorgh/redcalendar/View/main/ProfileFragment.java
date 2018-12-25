package com.simorgh.redcalendar.View.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.cyclebar.CycleBar;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.Model.database.model.Cycle;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.main.CycleViewModel;
import com.simorgh.spinner.NiceSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private CycleViewModel mViewModel;
    private CycleBar cycleBar;
    private NiceSpinner spinnerRedDays;
    private NiceSpinner spinnerGrayDays;
    private NiceSpinner spinnerYellowDays;
    private NiceSpinner spinnerBirthYear;
    private Calendar calendar = AppManager.getCalendarInstance();
    private PersianCalendar persianCalendar = CalendarTool.GregorianToPersian(calendar);
    private List<Integer> datasetRed = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private List<Integer> datasetGray = new LinkedList<>(Arrays.asList(21, 22, 23, 24, 25, 26, 27, 28));
    private List<Integer> datasetYellow = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
    private LinkedList<Integer> datasetBirthYear = new LinkedList<>();

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);
        cycleBar = v.findViewById(R.id.cycleBar);
        spinnerRedDays = v.findViewById(R.id.spinnerRedCount);
        spinnerGrayDays = v.findViewById(R.id.spinnerGrayCount);
        spinnerYellowDays = v.findViewById(R.id.spinnerYellowCount);
        spinnerBirthYear = v.findViewById(R.id.spinnerBirthYear);


        for (int i = persianCalendar.getPersianYear() - 50; i <= persianCalendar.getPersianYear() - 13; i++) {
            datasetBirthYear.addLast(i);
        }
        spinnerRedDays.attachDataSource(datasetRed);
        spinnerGrayDays.attachDataSource(datasetGray);
        spinnerYellowDays.attachDataSource(datasetYellow);
        spinnerBirthYear.attachDataSource(datasetBirthYear);

        spinnerRedDays.addOnItemClickListener((parent, view, position, id) -> {
            if (mViewModel.getCycleLiveData().getValue() != null) {
                Cycle cycle = mViewModel.getCycleLiveData().getValue();
                cycle.setRedDaysCount(datasetRed.get(position));
                mViewModel.updateCycle(cycle);
            }
        });

        spinnerGrayDays.addOnItemClickListener((parent, view, position, id) -> {
            if (mViewModel.getCycleLiveData().getValue() != null) {
                Cycle cycle = mViewModel.getCycleLiveData().getValue();
                cycle.setGrayDaysCount(datasetGray.get(position));
                mViewModel.updateCycle(cycle);
            }
        });

        spinnerYellowDays.addOnItemClickListener((parent, view, position, id) -> {
            if (mViewModel.getCycleLiveData().getValue() != null) {
                Cycle cycle = mViewModel.getCycleLiveData().getValue();
                cycle.setYellowDaysCount(datasetYellow.get(position));
                mViewModel.updateCycle(cycle);
            }
        });

        spinnerBirthYear.addOnItemClickListener((parent, view, position, id) -> {
            if (mViewModel.getCycleLiveData().getValue() != null) {
                Cycle cycle = mViewModel.getCycleLiveData().getValue();
                cycle.setBirthYear(datasetBirthYear.get(position));
                mViewModel.updateCycle(cycle);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        mViewModel.getCycleLiveData().observe(this, cycle -> {
            if (cycleBar != null && cycle != null) {
                cycleBar.setCycleData(new CycleData(cycle.getRedDaysCount(),
                        cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate()));

                spinnerRedDays.setSelectedIndex(cycle.getRedDaysCount() - 1);
                spinnerGrayDays.setSelectedIndex(cycle.getGrayDaysCount() - 21);
                spinnerYellowDays.setSelectedIndex(cycle.getYellowDaysCount() - 1);
                spinnerBirthYear.setSelectedIndex(cycle.getBirthYear() - (persianCalendar.getPersianYear() - 50));

                Log.d(AppManager.TAG, cycle.toString());
            }
        });
    }

}
