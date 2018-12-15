package com.simorgh.redcalendar.ui.step3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.redcalendar.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class SetCycleFrequencyFragment extends Fragment {

    private SetCycleFrequencyViewModel mViewModel;

    public static SetCycleFrequencyFragment newInstance() {
        return new SetCycleFrequencyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_cycle_length_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SetCycleFrequencyViewModel.class);
        // TODO: Use the ViewModel
    }

}