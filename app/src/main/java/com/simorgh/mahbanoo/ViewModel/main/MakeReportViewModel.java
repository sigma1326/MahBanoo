package com.simorgh.mahbanoo.ViewModel.main;

import com.simorgh.mahbanoo.Model.AppManager;

import java.util.Calendar;
import java.util.Objects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MakeReportViewModel extends ViewModel {
    private boolean isRangeStart;
    private MutableLiveData<Calendar> rangeStart = new MutableLiveData<>();
    private MutableLiveData<Calendar> rangeEnd = new MutableLiveData<>();

    public MakeReportViewModel() {
        rangeStart.setValue(AppManager.getCalendarInstance());
        rangeEnd.setValue(AppManager.getCalendarInstance());
    }

    public boolean isRangeStart() {
        return isRangeStart;
    }

    public void setRangeStart(boolean rangeStart) {
        isRangeStart = rangeStart;
    }

    public LiveData<Calendar> getRangeStartLive() {
        return rangeStart;
    }

    public LiveData<Calendar> getRangeEndLive() {
        return rangeEnd;
    }

    public void setRangeStart(Calendar rangeStart) {
        Objects.requireNonNull(this.rangeStart.getValue()).setTimeInMillis(rangeStart.getTimeInMillis());

    }

    public void setRangeEnd(Calendar rangeEnd) {
        Objects.requireNonNull(this.rangeEnd.getValue()).setTimeInMillis(rangeEnd.getTimeInMillis());
    }
}
