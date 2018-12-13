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
        setContentView(R.layout.stepper_layout);

//        ClueView c = findViewById(R.id.clue_view);
//        c.setOnDayChangedListener(this);
//        c.setOnButtonClickListener(this);
//        try {
//            c.onViewDataChanged("شنبه", "متوسط", "روز اول", "1", "آذر", true, 12);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        c.setClueData(new ClueView.ClueData(6,26));


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
//
//        calendarView.setRange(min, max);
//        calendarView.scrollToCurrentDate(Calendar.getInstance());
//        calendarView.setIsDayMarkedListener(this);


//        StepperLayout stepperLayout = findViewById(R.id.stepperLayout);
//        stepperLayout.setListener(this);
//        stepperLayout.setAdapter(new SampleFragmentStepAdapter(getSupportFragmentManager(), this));

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
    public boolean isDayMarked(int day) {
        return day % 5 == 0;
    }

}
