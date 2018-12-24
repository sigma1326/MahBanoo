package com.simorgh.redcalendar.View.register;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.numberpicker.NumberPicker;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.register.Step3ViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class Step3Fragment extends Fragment {

    private Step3ViewModel mViewModel;
    private Typeface typeface;
    private OnGrayDaysCountSelectedListener onGrayDaysCountSelected;

    public static Step3Fragment newInstance() {
        return new Step3Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/iransans_medium.ttf");
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step3_fragment, container, false);
        NumberPicker numberPicker = v.findViewById(R.id.np_period_freq);
        numberPicker.setTypeface(typeface);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Step3ViewModel.class);
        // TODO: Use the ViewModel
    }


    public interface OnGrayDaysCountSelectedListener {
        public void onGrayDaysCountSelected(int count);
    }
}
