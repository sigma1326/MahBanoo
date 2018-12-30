package com.simorgh.redcalendar;

import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.calendarutil.persiancalendar.PersianDate;

import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testCalendar() {
        PersianDate persianDate = new PersianDate();
        PersianCalendar persianCalendar = new PersianCalendar();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR,0);
        persianCalendar.setTimeInMillis(calendar.getTimeInMillis());
        persianCalendar.setTimeZone(TimeZone.getDefault());
        persianDate.setHour(0);

        int pYear = persianDate.getShYear();
        int pMonth = persianDate.getShMonth();
        int pDay = persianDate.getShDay();
        System.out.println(pYear + ":" + pMonth + ":" + pDay);
        System.out.println(persianCalendar.getPersianShortDateTime());

    }
}