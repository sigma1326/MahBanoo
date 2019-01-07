package com.simorgh.calendarutil;


import android.annotation.SuppressLint;

import com.simorgh.calendarutil.hijricalendar.UmmalquraCalendar;
import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.calendarutil.model.YearMonthDay;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.calendarutil.persiancalendar.PersianDate;

import java.util.Calendar;

public class CalendarTool {
    //Saturday to Friday
    private final String[] persianDayNames = {"ش", "ی", "د", "س", "چ", "پ", "ج"};
    private static final String[] arabicDayNames = {"س", "ح", "ن", "ث", "ر", "خ", "ج"};
    private final String[] gregorianDayNames = {"S", "M", "T", "W", "T", "F", "S"};

    private static final String[] persianMonthNames = {"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};
    private static final String[] persianMonthNamesInEnglish = {"Farvardin", "Ordibehesht", "Khordad", "Tir", "Mordad", "Shahrivar", "Mehr", "Aban", "Azar", "Dey", "Bahman", "Esfand"};
    private static final String[] gregorianMonthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] gregorianMonthNamesInPersian = {"ژانویه", "فوریه", "مارس", "آوریل", "مه", "ژوئن", "ژوئیه", "اوت", "سپتامبر", "اکتبر", "نوامبر", "دسامبر"};
    private static final String[] arabicMonthNames = {"محرم", "صفر", "ربیع الاولی", "ربیع الثانیه", "جمادی الاولی", "جمادی الثانیه", "رجب", "شعبان", "رمضان", "شوال", "ذوالقعده", "ذوالحجه"};
    private static final String[] arabicMonthNamesInEnglish = {"Muḥarram", "Ṣafar", "Rabīʿ al-Awwal", "Rabīʿ ath-Thānī", "Jumādá al-Ūlá", "Jumādá al-Ākhirah", "Rajab", "Sha‘bān", "Ramaḍān", "Shawwāl", "Dhū al-Qa‘dah", "Dhū al-Ḥijjah"};

    public static boolean isRTLLanguage(int calendarType) {
        boolean isRTLLanguage;
        switch (calendarType) {
            case CalendarType.PERSIAN:
            case CalendarType.ARABIC:
                isRTLLanguage = true;
                break;
            case CalendarType.GREGORIAN:
            default:
                isRTLLanguage = false;
        }
        return isRTLLanguage;
    }

    public static String getPersianDayName(String substring) {
        switch (substring) {
            case "SAT":
                return "ش";
            case "SUN":
                return "ی";
            case "MON":
                return "د";
            case "TUE":
                return "س";
            case "WED":
                return "چ";
            case "THU":
                return "پ";
            case "FRI":
                return "ج";
        }
        return "";
    }

    public static String getArabicDayName(String substring) {
        switch (substring) {
            case "SAT":
                return arabicDayNames[0];
            case "SUN":
                return arabicDayNames[1];
            case "MON":
                return arabicDayNames[2];
            case "TUE":
                return arabicDayNames[3];
            case "WED":
                return arabicDayNames[4];
            case "THU":
                return arabicDayNames[5];
            case "FRI":
                return arabicDayNames[6];
        }
        return "";
    }

    private volatile static Calendar calendar = Calendar.getInstance();

    public static Calendar PersianToGregorian(PersianCalendar p) {
        try {
            calendar.setTimeInMillis(p.getTimeInMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static PersianCalendar GregorianToPersian(Calendar c) {
        return new PersianCalendar(c.getTimeInMillis());
    }

    public static PersianDate GregorianToPersianDate(Calendar c) {
        return new PersianDate(c.getTimeInMillis());
    }

    public static void convertToOriginalDateType(YearMonthDay yearMonthDay) {
        switch (yearMonthDay.getCalendarType()) {
            case CalendarType.PERSIAN:
                PersianCalendar persianCalendar = GregorianToPersian(yearMonthDay);
                yearMonthDay.setPersianDate(persianCalendar);
                break;
            case CalendarType.ARABIC:
                UmmalquraCalendar hijri = GregorianToHijri(yearMonthDay);
                yearMonthDay.setArabicDate(hijri);
                break;
            case CalendarType.GREGORIAN:
                break;
            default:
        }
    }

    private static UmmalquraCalendar GregorianToHijri(YearMonthDay yearMonthDay) {
        calendar.clear();
        calendar.set(yearMonthDay.getYear(), yearMonthDay.getMonth(), yearMonthDay.getDay());
        return GregorianToHijri(calendar);
    }

    private static PersianCalendar GregorianToPersian(YearMonthDay yearMonthDay) {
        calendar.clear();
        calendar.set(yearMonthDay.getYear(), yearMonthDay.getMonth(), yearMonthDay.getDay());
        return GregorianToPersian(calendar);
    }

    public static Calendar PersianToGregorian(PersianDate persianDate) {
        calendar.clear();
        calendar.setTimeInMillis(persianDate.getTime());
        return calendar;
    }

    public static Calendar HijriToGregorian(UmmalquraCalendar hijri) {
        calendar.clear();
        calendar.setTimeInMillis(hijri.getTimeInMillis());
        return calendar;
    }

    static volatile UmmalquraCalendar hijri = new UmmalquraCalendar();

    public static UmmalquraCalendar GregorianToHijri(Calendar calendar) {
        hijri.clear();
        hijri.setTimeInMillis(calendar.getTimeInMillis());
        return hijri;
    }

    public static String getIranianMonthName(int irMonth) {
        String name;
        switch (irMonth) {
            case 1:
                name = "فروردین";
                break;
            case 2:
                name = "اردیبهشت";
                break;
            case 3:
                name = "خرداد";
                break;
            case 4:
                name = "تیر";
                break;
            case 5:
                name = "مرداد";
                break;
            case 6:
                name = "شهریور";
                break;
            case 7:
                name = "مهر";
                break;
            case 8:
                name = "آبان";
                break;
            case 9:
                name = "آذر";
                break;
            case 10:
                name = "دی";
                break;
            case 11:
                name = "بهمن";
                break;
            case 12:
                name = "اسفند";
                break;
            default:
                name = "";
        }
        return name;
    }

    public static String getGregorianMonthName(int gMonth) {
        String name;
        switch (gMonth) {
            case 1:
                name = "ژانویه";
                break;
            case 2:
                name = "فوریه";
                break;
            case 3:
                name = "مارس";
                break;
            case 4:
                name = "آوریل";
                break;
            case 5:
                name = "مه";
                break;
            case 6:
                name = "ژوئن";
                break;
            case 7:
                name = "ژوئیه";
                break;
            case 8:
                name = "اوت";
                break;
            case 9:
                name = "سپتامبر";
                break;
            case 10:
                name = "اکتبر";
                break;
            case 11:
                name = "نوامبر";
                break;
            case 12:
                name = "دسامبر";
                break;
            default:
                name = "";
        }
        return name;
    }

    public static String getGregorianFormalDate(Calendar grgDate) {
        StringBuilder s = new StringBuilder();
        int day = grgDate.get(Calendar.DAY_OF_MONTH);
        int month = grgDate.get(Calendar.MONTH);
        int year = grgDate.get(Calendar.YEAR);
        s.append(format(day, gregorianMonthNames[month], year));
        return s.toString();
    }

    @SuppressLint("DefaultLocale")
    public static String format(int day, String monthName, int year) {
        return String.format("%02d  %-15s  %d", day, monthName, year);
    }

    public static String getGregorianFormalDateInPersian(Calendar grgDate) {
        StringBuilder s = new StringBuilder();
        int day = grgDate.get(Calendar.DAY_OF_MONTH);
        int month = grgDate.get(Calendar.MONTH);
        int year = grgDate.get(Calendar.YEAR);
        s.append(format(day, gregorianMonthNamesInPersian[month], year));
        return s.toString();
    }

    public static String getPersianFormalDate(Calendar grgDate) {
        StringBuilder s = new StringBuilder();
        PersianCalendar persianCalendar = GregorianToPersian(grgDate);
        int day = persianCalendar.getPersianDay();
        int month = persianCalendar.getPersianMonth();
        int year = persianCalendar.getPersianYear();
        s.append(format(day, persianMonthNames[month], year));
        return s.toString();
    }


    public static String getPersianFormalDateInEnglish(Calendar grgDate) {
        StringBuilder s = new StringBuilder();
        PersianCalendar persianCalendar = GregorianToPersian(grgDate);
        int day = persianCalendar.getPersianDay();
        int month = persianCalendar.getPersianMonth();
        int year = persianCalendar.getPersianYear();
        s.append(format(day, persianMonthNamesInEnglish[month], year));
        return s.toString();
    }

    public static String getArabicFormalDate(Calendar grgDate) {
        StringBuilder s = new StringBuilder();
        UmmalquraCalendar hijri = GregorianToHijri(grgDate);
        int day = hijri.get(UmmalquraCalendar.DAY_OF_MONTH);
        int month = hijri.get(UmmalquraCalendar.MONTH);
        int year = hijri.get(UmmalquraCalendar.YEAR);
        s.append(format(day, arabicMonthNames[month], year));
        return s.toString();
    }

    public static String getArabicFormalDateInEnglish(Calendar grgDate) {
        StringBuilder s = new StringBuilder();
        UmmalquraCalendar hijri = GregorianToHijri(grgDate);
        int day = hijri.get(UmmalquraCalendar.DAY_OF_MONTH);
        int month = hijri.get(UmmalquraCalendar.MONTH);
        int year = hijri.get(UmmalquraCalendar.YEAR);
        s.append(format(day, arabicMonthNamesInEnglish[month], year));
        return s.toString();
    }

    static volatile PersianCalendar p = new PersianCalendar();

    public static int getDaysInMonth(int month, int year, int calendarType) {
        switch (calendarType) {
            case CalendarType.PERSIAN:
                if (month <= 5) {
                    return 31;
                } else if (month < 11) {
                    return 30;
                } else {
                    p.clear();
                    p.setPersianDate(year, month, 1);
                    if (p.isPersianLeapYear()) {
                        return 30;
                    } else {
                        return 29;
                    }
                }
            case CalendarType.ARABIC:
                return 30;
            case CalendarType.GREGORIAN:
                switch (month) {
                    case Calendar.JANUARY:
                    case Calendar.MARCH:
                    case Calendar.MAY:
                    case Calendar.JULY:
                    case Calendar.AUGUST:
                    case Calendar.OCTOBER:
                    case Calendar.DECEMBER:
                        return 31;
                    case Calendar.APRIL:
                    case Calendar.JUNE:
                    case Calendar.SEPTEMBER:
                    case Calendar.NOVEMBER:
                        return 30;
                    case Calendar.FEBRUARY:
                        return (year % 4 == 0) ? 29 : 28;
                    default:
                        return 30;
                }
        }
        return 30;
    }

    public static String getMonthName(int month, int calendarType) {
        switch (calendarType) {
            case CalendarType.PERSIAN:
                return persianMonthNames[month];
            case CalendarType.ARABIC:
                return arabicMonthNames[month];
            case CalendarType.GREGORIAN:
                return gregorianMonthNames[month];
        }
        return "";
    }


    public static long getDaysFromDiff(PersianCalendar input, Calendar startDate) {
        float diffDays;
        in.clear();
        in = CalendarTool.PersianToGregorian(input);
//        in.set(input.get(Calendar.YEAR), input.get(Calendar.MONTH), input.get(Calendar.DAY_OF_MONTH));
        start.clear();
        start.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
        if (in.compareTo(start) == 0) {
            return 0L;
        }
        if (in.before(start)) {
            return -1L;
        }
        diffDays = (float) (in.getTimeInMillis() - start.getTimeInMillis()) / (60 * 60 * 24 * 1000);
        if (diffDays < 0) {
            return (long) -3;
        }
        return (long) diffDays;
    }

    private static Calendar in = Calendar.getInstance();
    private static Calendar start = Calendar.getInstance();

    public static long getDaysFromDiff(Calendar input, Calendar startDate) {
        double diffDays;
        in.clear();
        in.set(input.get(Calendar.YEAR), input.get(Calendar.MONTH), input.get(Calendar.DAY_OF_MONTH),0,0,0);
        start.clear();
        start.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH),0,0,0);
        if (in.compareTo(start) == 0) {
            return 0L;
        }
        if (in.before(start)) {
            return -1L;
        }
        diffDays = (in.getTimeInMillis() - start.getTimeInMillis()) / (60d * 60d * 24d * 1000d);
        diffDays = Math.ceil(diffDays);
        if (diffDays < 0) {
            return (long) -3;
        }
        return (long) diffDays;
    }


}