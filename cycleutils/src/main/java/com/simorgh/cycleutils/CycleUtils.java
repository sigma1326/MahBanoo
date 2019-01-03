package com.simorgh.cycleutils;

import com.simorgh.databaseutils.model.Cycle;

import java.util.LinkedList;
import java.util.List;

public final class CycleUtils {
    public static List<CycleData> getCycleDataList(List<Cycle> cycleList) {
        List<CycleData> cycleData = new LinkedList<>();

        for (Cycle cycle : cycleList) {
            cycleData.add(new CycleData(cycle.getRedDaysCount(),
                    cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate(), cycle.getEndDate()));
        }
        return cycleData;
    }
}
