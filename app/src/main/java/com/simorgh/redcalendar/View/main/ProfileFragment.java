package com.simorgh.redcalendar.View.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.cyclebar.CycleBar;
import com.simorgh.cycleutils.ClueData;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.main.CycleViewModel;
import com.simorgh.redcalendar.ViewModel.main.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private CycleBar cycleBar;
    private CycleViewModel cycleViewModel;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);
        cycleBar = v.findViewById(R.id.cycleBar);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        cycleViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        cycleViewModel.getCycleLiveData().observe(this, cycle -> {
            if (cycleBar != null && cycle != null) {
                cycleBar.setClueData(new ClueData(cycle.getRedDaysCount(),
                        cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate()));
                Log.d(AppManager.TAG, cycle.toString());
            }
        });
    }

}