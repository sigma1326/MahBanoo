package com.simorgh.mahbanoo.View.main;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.calendarutil.model.YearMonthDay;
import com.simorgh.cyclecalendar.view.BaseMonthView;
import com.simorgh.cyclecalendar.view.CalendarView;
import com.simorgh.cyclecalendar.view.ShowInfoMonthView;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.cycleutils.CycleUtils;
import com.simorgh.databaseutils.CycleRepository;
import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.databaseutils.model.DayMood;
import com.simorgh.databaseutils.model.User;
import com.simorgh.mahbanoo.Model.AppManager;
import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.main.CycleViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class CalendarFragment extends Fragment implements ShowInfoMonthView.IsDayMarkedListener, BaseMonthView.OnDayClickListener {

    private CycleViewModel mViewModel;
    private CalendarView calendarView;
    private CycleRepository cycleRepository;
    private NavController navController;


    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cycleRepository = new CycleRepository(Objects.requireNonNull(getActivity()).getApplication());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (calendarView == null) {
            calendarView = new CalendarView(getActivity()
                    , BaseMonthView.MonthViewTypeShowCalendar
                    , CalendarType.PERSIAN, null
                    , AppManager.minDate, AppManager.maxDate);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                calendarView.setElevation(10);
            }
        }
        return calendarView;
    }

    @Override
    public void onDestroyView() {
        calendarView = null;
        navController = null;
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment);


        calendarView.setIsDayMarkedListener(this);
        calendarView.setOnDayClickListener(this);
        calendarView.scrollToCurrentDate(AppManager.getCalendarInstance());

        mViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        mViewModel.getUserWithCyclesLiveData().observe(this, userWithCycles -> {
            if (calendarView != null && userWithCycles != null) {
                Cycle cycle = userWithCycles.getCurrentCycle();
                User user = userWithCycles.getUser();
                calendarView.setCycleData(new CycleData(cycle.getRedDaysCount(),
                        cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate(), cycle.getEndDate()));

                calendarView.setCycleDataList(CycleUtils.getCycleDataList(userWithCycles.getCycles()));
                calendarView.scrollToCurrentDate(mViewModel.getSelectedDateCalendar());
                Log.d(AppManager.TAG, cycle.toString());
                mViewModel.setUser(user);
                calendarView.setShowInfo(user.isShowCycleDays());
            }
        });
    }

    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public List<Integer> getMarkedDays(Calendar day) {
        try {
            return Executors.newCachedThreadPool().submit(() -> {
                List<DayMood> dayMoods = cycleRepository.getMonthMarkedDays(day);
                List<Integer> days = new ArrayList<>();
                for (DayMood dayMood : dayMoods) {
                    boolean add = false;
                    if (dayMood != null) {
                        if (dayMood.getTypeBleedingSelectedIndex() != -1) {
                            add = true;
                        } else if (dayMood.getTypeEmotionSelectedIndices() != null) {
                            add = dayMood.getTypeEmotionSelectedIndices().size() > 0;
                        } else if (dayMood.getTypePainSelectedIndices() != null) {
                            add = dayMood.getTypePainSelectedIndices().size() > 0;
                        } else if (dayMood.getTypeEatingDesireSelectedIndices() != null) {
                            add = dayMood.getTypeEatingDesireSelectedIndices().size() > 0;
                        } else if (dayMood.getTypeHairStyleSelectedIndices() != null) {
                            add = dayMood.getTypeHairStyleSelectedIndices().size() > 0;
                        } else if (dayMood.getDrugs() != null) {
                            add = dayMood.getDrugs().size() > 0;
                        } else if (dayMood.getWeight() != 0) {
                            add = dayMood.getWeight() > 0;
                        }
                        if (add) {
                            days.add(dayMood.getId().get(Calendar.DAY_OF_MONTH));
                        }
                    }

                }
                return days;
            }).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void onDayClick(BaseMonthView view, Calendar day) {
        if (!day.after(AppManager.getCalendarInstance())) {
            mViewModel.setSelectedDateCalendar(day);
            navController.navigate(CalendarFragmentDirections.actionCalendarToAddNote().setSelectedDay(new YearMonthDay(day)));
        }
    }
}
