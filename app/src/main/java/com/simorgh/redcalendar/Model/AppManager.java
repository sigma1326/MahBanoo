package com.simorgh.redcalendar.Model;

import android.app.Application;
import android.util.Log;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.calendarutil.persiancalendar.PersianDate;
import com.simorgh.databaseutils.CycleRepository;
import com.simorgh.redcalendar.R;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class AppManager extends Application {
    public static final String TAG = "debug13";
    public static Calendar minDate;
    public static Calendar maxDate;


    private CycleRepository cycleRepository;

    public static Locale mLocale;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocale = getResources().getConfiguration().locale;

        minDate = getCalendarInstance();
        minDate.set(Calendar.YEAR, 2017);

        maxDate = getCalendarInstance();
        maxDate.set(Calendar.YEAR, 2020);

        PersianCalendar persianCalendar = CalendarTool.GregorianToPersian(getCalendarInstance());
        PersianDate persianDate = new PersianDate();
        persianDate.setGrgDay(25);
        Log.d(TAG, persianCalendar.getPersianLongDate());
        Log.d(TAG, persianDate.toDate().toString());
        Log.d(TAG, persianDate.getShDay() + ":" + persianDate.getShMonth() + ":" + persianDate.getShYear());

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/iransans_medium.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

    public static Calendar getCalendarInstance() {
        return Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"), mLocale);
    }
}
