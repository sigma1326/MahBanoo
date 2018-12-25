package com.simorgh.cycleview;

public interface OnViewDataChangedListener {
    void onViewDataChanged(String weekDayName, String optionalText, String mainDayNumber, String monthDayNumber, String monthName, boolean isOptionalVisible, int today);
}
