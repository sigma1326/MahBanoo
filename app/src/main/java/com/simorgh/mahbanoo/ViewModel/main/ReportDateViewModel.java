package com.simorgh.mahbanoo.ViewModel.main;

import androidx.lifecycle.ViewModel;

public class ReportDateViewModel extends ViewModel {
    private boolean isRangeStart;

    public ReportDateViewModel() {
        isRangeStart = true;
    }

    public boolean isRangeStart() {
        return isRangeStart;
    }

    public void setRangeStart(boolean rangeStart) {
        isRangeStart = rangeStart;
    }
}
