package com.simorgh.databaseutils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simorgh.calendarutil.model.CalendarType;
import com.simorgh.calendarutil.model.YearMonthDay;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import androidx.room.TypeConverter;

public class TypeConverters {
    @TypeConverter
    public static Calendar toCalendar(String value) {
        if (value == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            Type listType = new TypeToken<YearMonthDay>() {
            }.getType();
            YearMonthDay yearMonthDay = gson.fromJson(value, listType);
            calendar.set(Calendar.DAY_OF_MONTH, yearMonthDay.getDay());
            calendar.set(Calendar.MONTH, yearMonthDay.getMonth());
            calendar.set(Calendar.YEAR, yearMonthDay.getYear());
            return calendar;
        }
    }

    @TypeConverter
    public static String toTimeInMillis(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        YearMonthDay yearMonthDay = new YearMonthDay(year, month, day, CalendarType.GREGORIAN);
        return gson.toJson(yearMonthDay);
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
