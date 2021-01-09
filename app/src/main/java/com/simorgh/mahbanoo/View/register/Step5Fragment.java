package com.simorgh.mahbanoo.View.register;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.register.Step5ViewModel;
import com.simorgh.numberpicker.NumberPicker;

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
        typeface = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/iransans_medium.ttf");
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
        onBirthDaySelectedListener = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        numberPicker = null;
        super.onDestroyView();
    }

    public interface OnBirthDaySelectedListener {
        void onBirthDaySelected(int year);
    }

}
