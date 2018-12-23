package com.simorgh.redcalendar.View;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.numberpicker.NumberPicker;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.Step2ViewModel;

public class Step2Fragment extends Fragment {

    private Step2ViewModel mViewModel;
    private Typeface typeface;

    public static Step2Fragment newInstance() {
        return new Step2Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/iransans_medium.ttf");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step2_fragment, container, false);
        NumberPicker numberPicker = v.findViewById(R.id.np_period_days);
        numberPicker.setTypeface(typeface);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Step2ViewModel.class);
        // TODO: Use the ViewModel
    }

}
