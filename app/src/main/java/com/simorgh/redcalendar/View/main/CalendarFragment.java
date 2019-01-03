package com.simorgh.redcalendar.View.main;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.calendarutil.CalendarTool;
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
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.main.CycleViewModel;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

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

    RecyclerView recyclerView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calendarView = new CalendarView(getActivity()
                , BaseMonthView.MonthViewTypeShowCalendar
                , CalendarType.PERSIAN, null
                , AppManager.minDate, AppManager.maxDate);

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment);


        calendarView.setIsDayMarkedListener(this);
        calendarView.setOnDayClickListener(this);
        calendarView.scrollToCurrentDate(AppManager.getCalendarInstance());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            calendarView.setElevation(10);
        }
//        View v = LayoutInflater.from(getContext()).inflate(R.layout.calendar_fragment, container, false);
//        recyclerView = v.findViewById(R.id.rv_month);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
//        recyclerView.setAdapter(new MonthListAdapter(new MonthListAdapter.MonthDiffCallBack()));
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setHasFixedSize(true);

        return calendarView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        List<MonthItem> monthItems = new LinkedList<>();
//      new Thread(() -> {
//          for (int i = 0; i < 10; i++) {
//              monthItems.add(new MonthItem());
//          }
////          new Handler(Looper.getMainLooper()).post(() ->
////                  );
//          ((MonthListAdapter) Objects.requireNonNull(recyclerView.getAdapter())).submitList(monthItems);
//
//      }).run();
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

    @Override
    public boolean isDayMarked(Calendar day) {
        DayMood dayMood = cycleRepository.getDayMood(day);
        if (dayMood == null) {
            return false;
        }
        if (dayMood.getTypeBleedingSelectedIndex() != -1) {
            return true;
        } else if (dayMood.getTypeEmotionSelectedIndices() != null) {
            return dayMood.getTypeEmotionSelectedIndices().size() > 0;
        } else if (dayMood.getTypePainSelectedIndices() != null) {
            return dayMood.getTypePainSelectedIndices().size() > 0;
        } else if (dayMood.getTypeEatingDesireSelectedIndices() != null) {
            return dayMood.getTypeEatingDesireSelectedIndices().size() > 0;
        } else if (dayMood.getTypeHairStyleSelectedIndices() != null) {
            return dayMood.getTypeHairStyleSelectedIndices().size() > 0;
        }

        return false;
    }

    @Override
    public void onDayClick(BaseMonthView view, Calendar day) {
        if (!day.after(AppManager.getCalendarInstance())) {
            mViewModel.setSelectedDateCalendar(day);
            navController.navigate(CalendarFragmentDirections.actionCalendarToAddNote().setSelectedDay(new YearMonthDay(day)));
        }
    }
}
