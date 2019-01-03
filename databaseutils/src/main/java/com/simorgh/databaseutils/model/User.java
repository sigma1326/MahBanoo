package com.simorgh.databaseutils.model;

import android.annotation.SuppressLint;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    private int id;

    @ColumnInfo(name = "current_cycle")
    private Calendar currentCycle;

    @ColumnInfo(name = "show_cycle_days")
    private boolean showCycleDays;

    @ColumnInfo(name = "show_pregnancy_prob")
    private boolean showPregnancyProb;


    @ColumnInfo(name = "birth_year")
    private int birthYear;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getCurrentCycle() {
        return currentCycle;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public void setCurrentCycle(Calendar currentCycle) {
        if (this.currentCycle == null) {
            this.currentCycle = Calendar.getInstance();
            this.currentCycle.clear();
        }
        this.currentCycle.setTimeInMillis(currentCycle.getTimeInMillis());
    }

    public boolean isShowCycleDays() {
        return showCycleDays;
    }

    public void setShowCycleDays(boolean showCycleDays) {
        this.showCycleDays = showCycleDays;
    }

    public boolean isShowPregnancyProb() {
        return showPregnancyProb;
    }

    public void setShowPregnancyProb(boolean showPregnancyProb) {
        this.showPregnancyProb = showPregnancyProb;
    }

    public User() {
        id = 1;
        showPregnancyProb = true;
        showCycleDays = true;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("user: {%d , %s , %b , %b}", id, currentCycle.getTime().toString(), isShowPregnancyProb(), isShowCycleDays());
    }
}
