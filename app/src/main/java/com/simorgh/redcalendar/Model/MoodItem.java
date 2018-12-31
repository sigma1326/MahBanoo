package com.simorgh.redcalendar.Model;

import java.util.Calendar;

public class MoodItem {
    private Calendar id = Calendar.getInstance();
    private int moodType;
    private int moodSelectedIndex = -1;

    public MoodItem() {

    }

    public MoodItem(Calendar id, int moodType, int moodSelectedIndex) {
        this.id = id;
        this.moodType = moodType;
        this.moodSelectedIndex = moodSelectedIndex;
    }

    public int getMoodType() {
        return moodType;
    }

    public void setMoodType(int moodType) {
        this.moodType = moodType;
    }

    public int getMoodSelectedIndex() {
        return moodSelectedIndex;
    }

    public void setMoodSelectedIndex(int moodSelectedIndex) {
        this.moodSelectedIndex = moodSelectedIndex;
    }

    public Calendar getId() {
        return id;
    }

    public void setId(Calendar id) {
        this.id.setTimeInMillis(id.getTimeInMillis());
    }
}
