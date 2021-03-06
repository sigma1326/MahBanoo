package com.simorgh.mahbanoo.View.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.cyclebar.CycleBar;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.databaseutils.model.User;
import com.simorgh.mahbanoo.Model.AppManager;
import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.main.CycleViewModel;
import com.simorgh.spinner.NiceSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ProfileFragment extends Fragment {

    private CycleViewModel mViewModel;
    private CycleBar cycleBar;
    private NiceSpinner spinnerRedDays;
    private NiceSpinner spinnerGrayDays;
    private NiceSpinner spinnerYellowDays;
    private NiceSpinner spinnerBirthYear;
    private TextView tvThisCycleStartEnd;
    private TextView tvCurrentCycle;
    private TextView tvRedDays;
    private TextView tvGrayDays;
    private TextView tvYellowDays;
    private TextView tvBirthYear;

    private Calendar calendar = AppManager.getCalendarInstance();
    private PersianCalendar persianCalendar = CalendarTool.GregorianToPersian(calendar);
    private PersianCalendar persianCalendarStartEnd = CalendarTool.GregorianToPersian(calendar);
    private List<Integer> datasetRed = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private List<Integer> datasetGray = new LinkedList<>(Arrays.asList(21, 22, 23, 24, 25, 26, 27, 28));
    private List<Integer> datasetYellow = new LinkedList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
    private LinkedList<Integer> datasetBirthYear = new LinkedList<>();
    private Typeface typeface;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/iransans_medium.ttf");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);
        cycleBar = v.findViewById(R.id.cycleBar);
        spinnerRedDays = v.findViewById(R.id.spinnerRedCount);
        spinnerGrayDays = v.findViewById(R.id.spinnerGrayCount);
        spinnerYellowDays = v.findViewById(R.id.spinnerYellowCount);
        spinnerBirthYear = v.findViewById(R.id.spinnerBirthYear);
        tvThisCycleStartEnd = v.findViewById(R.id.tv_CurrentCycleStartEnd);
        tvRedDays = v.findViewById(R.id.tv_red_count);
        tvGrayDays = v.findViewById(R.id.tv_gray_count);
        tvYellowDays = v.findViewById(R.id.tv_yellow_count);
        tvBirthYear = v.findViewById(R.id.tv_birth_year);
        tvCurrentCycle = v.findViewById(R.id.tv_current_cycle);

        tvThisCycleStartEnd.setTypeface(typeface);
        tvCurrentCycle.setTypeface(typeface);
        tvRedDays.setTypeface(typeface);
        tvGrayDays.setTypeface(typeface);
        tvYellowDays.setTypeface(typeface);
        tvBirthYear.setTypeface(typeface);

        for (int i = persianCalendar.getPersianYear() - 50; i <= persianCalendar.getPersianYear() - 13; i++) {
            datasetBirthYear.addLast(i);
        }
        spinnerRedDays.attachDataSource(datasetRed);
        spinnerGrayDays.attachDataSource(datasetGray);
        spinnerYellowDays.attachDataSource(datasetYellow);
        spinnerBirthYear.attachDataSource(datasetBirthYear);

        spinnerRedDays.addOnItemClickListener((parent, view, position, id) -> {
            if (mViewModel.getUserWithCyclesLiveData().getValue() != null) {
                Cycle cycle = mViewModel.getUserWithCyclesLiveData().getValue().getCurrentCycle();
                cycle.setRedDaysCount(datasetRed.get(position));
                mViewModel.updateCycle(cycle);
            }
        });

        spinnerGrayDays.addOnItemClickListener((parent, view, position, id) -> {
            if (mViewModel.getUserWithCyclesLiveData().getValue() != null) {
                Cycle cycle = mViewModel.getUserWithCyclesLiveData().getValue().getCurrentCycle();
                cycle.setGrayDaysCount(datasetGray.get(position));
                mViewModel.updateCycle(cycle);
            }
        });

        spinnerYellowDays.addOnItemClickListener((parent, view, position, id) -> {
            if (mViewModel.getUserWithCyclesLiveData().getValue() != null) {
                Cycle cycle = mViewModel.getUserWithCyclesLiveData().getValue().getCurrentCycle();
                cycle.setYellowDaysCount(datasetYellow.get(position));
                mViewModel.updateCycle(cycle);
            }
        });

        spinnerBirthYear.addOnItemClickListener((parent, view, position, id) -> {
            if (mViewModel.getUserWithCyclesLiveData().getValue() != null) {
                User user = mViewModel.getUserWithCyclesLiveData().getValue().getUser();
                user.setBirthYear(datasetBirthYear.get(position));
                mViewModel.updateUser(user);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        mViewModel.getUserWithCyclesLiveData().observe(this, userWithCycles -> {
            if (cycleBar != null && userWithCycles != null) {
                Cycle cycle = userWithCycles.getCurrentCycle();
                User user = userWithCycles.getUser();
                cycleBar.setCycleData(new CycleData(cycle.getRedDaysCount(),
                        cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate(), cycle.getEndDate()));

                spinnerRedDays.setSelectedIndex(cycle.getRedDaysCount() - 1);
                spinnerGrayDays.setSelectedIndex(cycle.getGrayDaysCount() - 21);
                spinnerYellowDays.setSelectedIndex(cycle.getYellowDaysCount());
                spinnerBirthYear.setSelectedIndex(user.getBirthYear() - (persianCalendar.getPersianYear() - 50));

                persianCalendarStartEnd = CalendarTool.GregorianToPersian(cycle.getCurrentCycleStart(AppManager.getCalendarInstance()));
                String start = persianCalendarStartEnd.getPersianDay() + " " + persianCalendarStartEnd.getPersianMonthName();
                persianCalendarStartEnd.addPersianDate(PersianCalendar.DAY_OF_MONTH, cycle.getTotalDaysCount() - 1);
                String end = persianCalendarStartEnd.getPersianDay() + " " + persianCalendarStartEnd.getPersianMonthName();
                tvThisCycleStartEnd.setText(String.format("%s - %s", start, end));
                Log.d(AppManager.TAG, cycle.toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        cycleBar = null;
        spinnerRedDays= null;
        spinnerGrayDays= null;
        spinnerYellowDays = null;
        spinnerBirthYear= null;
        tvThisCycleStartEnd= null;
        tvCurrentCycle= null;
        tvRedDays= null;
        tvGrayDays= null;
        tvYellowDays= null;
        tvBirthYear = null;
        super.onDestroyView();
    }
}
