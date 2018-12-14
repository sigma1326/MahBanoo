package com.simorgh.redcalendar.ui.step2;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.redcalendar.R;

public class SetCycleLengthFragment extends Fragment {

    private SetCycleLengthViewModel mViewModel;

    public static SetCycleLengthFragment newInstance() {
        return new SetCycleLengthFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_cycle_length_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SetCycleLengthViewModel.class);
        // TODO: Use the ViewModel
    }

}
