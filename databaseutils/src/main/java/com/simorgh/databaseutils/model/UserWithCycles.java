package com.simorgh.databaseutils.model;

import android.util.Log;

import com.simorgh.databaseutils.TypeConverters;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

public class UserWithCycles {
    @Embedded
    private User user;

    @Relation(parentColumn = "id", entityColumn = "user_id", entity = Cycle.class)
    private List<Cycle> cycles;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Cycle> getCycles() {
        return cycles;
    }

    public void setCycles(@NonNull List<Cycle> cycles) {
        if (this.cycles == null) {
            this.cycles = new LinkedList<>();
        }
        this.cycles.addAll(cycles);
    }

    public Cycle getCurrentCycle() {
        if (cycles == null) {
            return null;
        }
        if (cycles.size() == 0) {
            return null;
        }
        for (Cycle cycle : cycles) {
            if (TypeConverters.toTimeInMillis(cycle.getStartDate()) == TypeConverters.toTimeInMillis(user.getCurrentCycle())) {
                Log.d("debug13", "getCurrentCycle: " + TypeConverters.toTimeInMillis(cycle.getStartDate())
                        + ":" + TypeConverters.toTimeInMillis(user.getCurrentCycle()));
                return cycle;
            }
        }
//        return cycles.get(0);
        return null;
    }
}
