package com.simorgh.databaseutils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.calendarutil.model.YearMonthDay;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

public class TypeConverters {
    @TypeConverter
    public static Calendar toCalendar(Long value) {
        if (value == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            if (String.valueOf(value).length() < 8) {
                return Calendar.getInstance();
            }
            int year = Integer.parseInt(String.valueOf(value).substring(6));
            int month = Integer.parseInt(String.valueOf(value).substring(4, 6));
            int day = Integer.parseInt(String.valueOf(value).substring(0, 4));
            calendar.clear();
            calendar.set(Calendar.DAY_OF_MONTH, year);
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.YEAR, day);
            return calendar;
        }
    }

    @TypeConverter
    public static long toTimeInMillis(@NonNull Calendar calendar) {
        if (calendar == null) {
            return 0;
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        YearMonthDay yearMonthDay = new YearMonthDay(year, month, day, CalendarType.GREGORIAN);
        long ret;
        ret = yearMonthDay.getYear();
        ret = ret * 100 + (yearMonthDay.getMonth() + 1);
        ret = ret * 100 + yearMonthDay.getDay();
        return ret;
    }


    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Integer> stringToIntegerList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Integer>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String IntegerListToString(List<Integer> objects) {
        return gson.toJson(objects);
    }


    @TypeConverter
    public static List<String> stringToStringList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String StringListToString(List<String> objects) {
        return gson.toJson(objects);
    }
}
