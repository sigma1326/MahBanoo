package com.simorgh.redcalendar.View;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.clueview.ClueView;
import com.simorgh.clueview.OnViewDataChangedListener;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.CycleViewViewModel;

public class CycleViewFragment extends Fragment implements ClueView.OnButtonClickListener, ClueView.OnDayChangedListener {

    private CycleViewViewModel mViewModel;
    private ClueView clueView;

    public static CycleViewFragment newInstance() {
        return new CycleViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cycle_view_fragment, container, false);
        clueView = v.findViewById(R.id.clueView);
        clueView.setOnButtonClickListener(this);
        clueView.setOnDayChangedListener(this);
        try {
            clueView.onViewDataChanged("شنبه", "متوسط", "روز اول", "1", "آذر", true, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        clueView.setClueData(AppManager.cvClueData);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CycleViewViewModel.class);
    }

    @Override
    public void onButtonChangeClick() {

    }

    @Override
    public void onDayChanged(int day, int dayType, OnViewDataChangedListener listener) {
        String dayS;
        switch (day % 7) {
            case 0:
                dayS = "شنبه";
                break;
            case 1:
                dayS = "یک‌شنبه";
                break;
            case 2:
                dayS = "دوشنبه";
                break;
            case 3:
                dayS = "سه‌شنبه";
                break;
            case 4:
                dayS = "چهارشنبه";
                break;
            case 5:
                dayS = "پنج‌شنبه";
                break;
            case 6:
                dayS = "جمعه";
                break;
            default:
                dayS = "جمعه";
        }
        listener.onViewDataChanged(dayS, "کم", "روز هفتم", "1", "آذر", true, day);

    }
}
