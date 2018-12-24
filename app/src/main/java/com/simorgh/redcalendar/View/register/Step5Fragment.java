package com.simorgh.redcalendar.View.register;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.numberpicker.NumberPicker;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.register.Step5ViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class Step5Fragment extends Fragment {

    private Step5ViewModel mViewModel;
    private Typeface typeface;
    private OnBirthDaySelectedListener onBirthDaySelectedListener;
    private NumberPicker numberPicker;


    public static Step5Fragment newInstance() {
        return new Step5Fragment();
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/iransans_medium.ttf");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step5_fragment, container, false);
        numberPicker = v.findViewById(R.id.np_birth_date);
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            mViewModel.setYear(newVal);
            if (onBirthDaySelectedListener != null) {
                onBirthDaySelectedListener.onBirthDaySelected(mViewModel.getYear());
            }
        });
        numberPicker.setTypeface(typeface);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Step5ViewModel.class);
        mViewModel.setYear(numberPicker.getValue());
        if (onBirthDaySelectedListener != null) {
            onBirthDaySelectedListener.onBirthDaySelected(mViewModel.getYear());
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onBirthDaySelectedListener = (OnBirthDaySelectedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onBirthDaySelectedListener = null;
    }

    public interface OnBirthDaySelectedListener {
        public void onBirthDaySelected(int year);
    }

}
