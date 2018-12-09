package com.simorgh.redcalendar;

import android.os.Bundle;
import android.widget.Toast;

import com.simorgh.cluecalendar.model.CalendarType;
import com.simorgh.cluecalendar.util.MonthViewAdapter;
import com.simorgh.cluecalendar.view.WeekDaysLabelView;
import com.simorgh.clueview.ClueView;
import com.simorgh.clueview.OnViewDataChangedListener;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements ClueView.OnDayChangedListener, ClueView.OnButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ClueView c = findViewById(R.id.clue_view);
//        c.setOnDayChangedListener(this);
//        c.setOnButtonClickListener(this);
//        try {
//            c.onViewDataChanged("شنبه", "متوسط", "روز اول", "1", "آذر", true, 12);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        c.setClueData(new ClueView.ClueData(6,26));

//        MonthView monthView = findViewById(R.id.month_view);
//        monthView.setMonthParams(5, 8, 1397, 1, 1, 31, 1);

        WeekDaysLabelView labelView = findViewById(R.id.weekDaysLabelView);
        labelView.setCalendarType(CalendarType.PERSIAN);

        final Calendar minDate = Calendar.getInstance();
        final Calendar maxDate = Calendar.getInstance();
        minDate.set(Calendar.YEAR, 2018);
        maxDate.set(Calendar.YEAR, 2019);

        RecyclerView recyclerView = findViewById(R.id.rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        MonthViewAdapter adapter = new MonthViewAdapter(this, CalendarType.PERSIAN);
//        adapter.setRange(minDate,maxDate);
        recyclerView.setAdapter(adapter);
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
}
