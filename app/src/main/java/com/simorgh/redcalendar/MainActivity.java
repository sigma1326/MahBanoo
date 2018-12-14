package com.simorgh.redcalendar;

import android.os.Bundle;
import android.widget.Toast;

import com.simorgh.cluecalendar.util.CalendarTool;
import com.simorgh.cluecalendar.view.BaseMonthView;
import com.simorgh.cluecalendar.view.ShowInfoMonthView;
import com.simorgh.clueview.ClueView;
import com.simorgh.clueview.OnViewDataChangedListener;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ClueView.OnDayChangedListener, ClueView.OnButtonClickListener, BaseMonthView.OnDayClickListener, ShowInfoMonthView.IsDayMarkedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClueView c = findViewById(R.id.clue_view);
        c.setOnDayChangedListener(this);
        c.setOnButtonClickListener(this);
        try {
            c.onViewDataChanged("شنبه", "متوسط", "روز اول", "1", "آذر", true, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.setClueData(new ClueView.ClueData(6, 26));

//        CalendarView calendarView = findViewById(R.id.calendarView);
//        calendarView.setOnDayClickListener(this);
//        calendarView.setMonthViewType(BaseMonthView.MonthViewTypeShowCalendar);
//        calendarView.setCalendarType(CalendarType.PERSIAN);
//        calendarView.setClueData(new ClueData(5, 28, 4, Calendar.getInstance()));
//        Calendar min = Calendar.getInstance();
//        min.set(Calendar.YEAR, 2018);
//        min.set(Calendar.MONTH, 0);
//        Calendar max = Calendar.getInstance();
//        max.set(Calendar.YEAR, 2020);
//        max.set(Calendar.MONTH, 10);

//        calendarView.setRange(min, max);
//        calendarView.scrollToCurrentDate(Calendar.getInstance());
//        calendarView.setIsDayMarkedListener(this);


//        StepperLayout stepperLayout = findViewById(R.id.stepperLayout);
//        stepperLayout.setListener(this);
//        stepperLayout.setAdapter(new SampleFragmentStepAdapter(getSupportFragmentManager(), this));

        // Lock orientation into landscape.
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Create a GameView and bind it to this activity.
        // You don't need a ViewGroup to fill the screen, because the system
        // has a FrameLayout to which this will be added.
//        mGameView = new GameView(this);
//        // Android 4.1 and higher simple way to request fullscreen.
//        mGameView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
//        setContentView(mGameView);
    }

    private GameView mGameView;

    /**
     * Pauses game when activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
//        mGameView.pause();
    }

    /**
     * Resumes game when activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
//        mGameView.resume();
    }

    @Override
    public void onDayChanged(int day, int dayType, OnViewDataChangedListener listener) {
        String dayS;
        switch (day % 7) {
            case 0:
                dayS = "شنبه";
                break;
            case 1:
                dayS = "یک‌شنبه";
                break;
            case 2:
                dayS = "دوشنبه";
                break;
            case 3:
                dayS = "سه‌شنبه";
                break;
            case 4:
                dayS = "چهارشنبه";
                break;
            case 5:
                dayS = "پنج‌شنبه";
                break;
            case 6:
                dayS = "جمعه";
                break;
            default:
                dayS = "جمعه";
        }
        listener.onViewDataChanged(dayS, "کم", "روز هفتم", "1", "آذر", true, day);

    }

    @Override
    public void onButtonChangeClick() {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDayClick(BaseMonthView view, Calendar day) {
        Toast.makeText(this, " " + CalendarTool.GregorianToPersian(day).getPersianLongDate(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isDayMarked(Calendar day) {
        return day.get(Calendar.DAY_OF_MONTH) % 5 == 0;
    }

//    @Override
//    public void onCompleted(View completeButton) {
//
//    }
//
//    @Override
//    public void onError(VerificationError verificationError) {
//
//    }
//
//    @Override
//    public void onStepSelected(int newStepPosition) {
//
//    }
//
//    @Override
//    public void onReturn() {
//
//    }
//
//    private class SampleFragmentStepAdapter implements StepAdapter {
//        private Context context;
//
//        public SampleFragmentStepAdapter(FragmentManager supportFragmentManager, Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public Step createStep(int position) {
//            return new  SetLastCycleDayFragment();
//        }
//
//        @Override
//        public Step findStep(int position) {
//            return null;
//        }
//
//        @NonNull
//        @Override
//        public StepViewModel getViewModel(int position) {
//            StepViewModel.Builder builder = new StepViewModel.Builder(context);
//            builder.setTitle("");
//            return builder.create();
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//        @Override
//        public PagerAdapter getPagerAdapter() {
//            return null;
//        }
//    }
}
