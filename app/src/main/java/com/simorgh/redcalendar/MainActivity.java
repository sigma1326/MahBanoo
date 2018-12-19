package com.simorgh.redcalendar;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.simorgh.bottombar.BottomBar;
import com.simorgh.cluecalendar.util.CalendarTool;
import com.simorgh.cluecalendar.view.BaseMonthView;
import com.simorgh.cluecalendar.view.ShowInfoMonthView;
import com.simorgh.clueview.ClueView;
import com.simorgh.clueview.OnViewDataChangedListener;
import com.simorgh.weekdaypicker.ClueData;
import com.simorgh.weekdaypicker.WeekDayPicker;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class MainActivity extends AppCompatActivity implements ClueView.OnDayChangedListener, ClueView.OnButtonClickListener, BaseMonthView.OnDayClickListener, ShowInfoMonthView.IsDayMarkedListener, BottomBar.OnItemClickListener, BottomBar.OnCircleItemClickListener, WeekDayPicker.onDaySelectedListener {

    private AppCompatTextView titleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lock orientation into landscape.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


//        ClueView c = findViewById(R.id.clue_view);
//        c.setOnDayChangedListener(this);
//        c.setOnButtonClickListener(this);
//        try {
//            c.onViewDataChanged("شنبه", "متوسط", "روز اول", "1", "آذر", true, 12);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        c.setClueData(new ClueView.ClueData(6, 26));

        titleText = findViewById(R.id.tv_toolbarTitle);


//        SimpleMonthView simpleMonthView = new SimpleMonthView(this);
//        setContentView(simpleMonthView);
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setItemClickListener(this);
        bottomBar.setCircleItemClickListener(this);

//        CycleBar cycleBar = findViewById(R.id.cycleBar);
        Calendar cc = Calendar.getInstance();
        cc.set(Calendar.DAY_OF_MONTH, 26);
//        cycleBar.setClueData(new ClueData(3,26,3,cc));

//        WeekDayPicker weekDayPicker = findViewById(R.id.weekDayPicker);
//        weekDayPicker.setClueData(new ClueData(3, 26, 3, Calendar.getInstance()));
//        weekDayPicker.setSelectedDate(cc);
//        weekDayPicker.setOnDaySelectedListener(this);


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

    @Override
    public void onClick(BottomBar.BottomItem item) {
        titleText.setText(item.getText());
    }

    @Override
    public void onClick() {
        Toast.makeText(this, "circle", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDaySelected(Calendar day) {
        Toast.makeText(this, day.get(Calendar.DAY_OF_MONTH) + "", Toast.LENGTH_SHORT).show();
    }
}
