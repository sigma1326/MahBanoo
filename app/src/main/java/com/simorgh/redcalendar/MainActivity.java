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

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class MainActivity extends AppCompatActivity implements ClueView.OnDayChangedListener, ClueView.OnButtonClickListener, BaseMonthView.OnDayClickListener, ShowInfoMonthView.IsDayMarkedListener, BottomBar.OnItemClickListener, BottomBar.OnCircleItemClickListener {

    private AppCompatTextView titleText;


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

        titleText = findViewById(R.id.tv_toolbarTitle);

        // Lock orientation into landscape.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        SimpleMonthView simpleMonthView = new SimpleMonthView(this);
//        setContentView(simpleMonthView);
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setItemClickListener(this);
        bottomBar.setCircleItemClickListener(this);
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
}
