package com.simorgh.redcalendar.Model.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simorgh.redcalendar.Model.AppManager;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import androidx.room.TypeConverter;

public class TypeConverters {
    @TypeConverter
    public static Calendar toCalendar(Long value) {
        if (value == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value);
            return calendar;
        }
    }

    @TypeConverter
    public static Long toTimeInMillis(Calendar calendar) {
        return calendar == null ? null : calendar.getTimeInMillis();
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
