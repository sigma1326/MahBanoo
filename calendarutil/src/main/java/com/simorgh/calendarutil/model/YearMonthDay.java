package com.simorgh.calendarutil.model;


import com.simorgh.calendarutil.hijricalendar.UmmalquraCalendar;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;

import org.jetbrains.annotations.NotNull;

public class YearMonthDay {
    private int year;
    private int month;
    private int day;
    private int calendarType;

    /**
     * @param year         : year of date, start from 1
     * @param month        : month of date, range is between 0 to 11
     * @param day          : day of date, range is between 1 to 31
     * @param calendarType : type of calendar, see {@link CalendarType}
     */
    public YearMonthDay(int year, int month, int day, int calendarType) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.calendarType = calendarType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int date) {
        this.day = date;
    }

    public int getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(int calendarType) {
        this.calendarType = calendarType;
    }

    @NotNull
    public String toString() {
        return getYear() + "/" + (getMonth() + 1) + "/" + getDay();
    }

    /**
     * @param persianCalendar : gets {@link com.simorgh.calendarutil.persiancalendar.PersianCalendar} class and
     *                        sets current {@link YearMonthDay} object
     *                        based on it.
     */
    public void setPersianDate(PersianCalendar persianCalendar) {
        year = persianCalendar.getPersianYear();
        month = persianCalendar.getPersianMonth();
        day = persianCalendar.getPersianDay();
    }

    /**
     * @param hijri :gets {@link UmmalquraCalendar} class and
     *              sets current {@link YearMonthDay} object
     *              based on it.
     */
    public void setArabicDate(UmmalquraCalendar hijri) {
        year = hijri.get(UmmalquraCalendar.YEAR);
        month = hijri.get(UmmalquraCalendar.MONTH);
        day = hijri.get(UmmalquraCalendar.DAY_OF_MONTH);
    }
}
