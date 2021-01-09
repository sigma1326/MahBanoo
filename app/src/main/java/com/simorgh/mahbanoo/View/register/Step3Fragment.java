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
import com.simorgh.mahbanoo.ViewModel.register.Step3ViewModel;
import com.simorgh.numberpicker.NumberPicker;

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
        typeface = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/iransans_medium.ttf");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step3_fragment, container, false);
        NumberPicker numberPicker = v.findViewById(R.id.np_period_freq);
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> mViewModel.setGrayDayCount(newVal));
        numberPicker.setTypeface(typeface);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Step3ViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onGrayDaysCountSelected = (OnGrayDaysCountSelectedListener) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (onGrayDaysCountSelected != null) {
            onGrayDaysCountSelected.onGrayDaysCountSelected(mViewModel.getGrayDayCount());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onGrayDaysCountSelected = null;
    }

    public interface OnGrayDaysCountSelectedListener {
        void onGrayDaysCountSelected(int count);
    }
}
