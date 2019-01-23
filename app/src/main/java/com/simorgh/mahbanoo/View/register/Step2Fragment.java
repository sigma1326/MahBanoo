package com.simorgh.mahbanoo.View.register;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.register.Step2ViewModel;
import com.simorgh.numberpicker.NumberPicker;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class Step2Fragment extends Fragment {

    private Step2ViewModel mViewModel;
    private Typeface typeface;
    private OnRedDaysCountSelectedListener onRedDaysCountSelected;

    public static Step2Fragment newInstance() {
        return new Step2Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/iransans_medium.ttf");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step2_fragment, container, false);
        NumberPicker numberPicker = v.findViewById(R.id.np_period_days);
        numberPicker.setTypeface(typeface);
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            mViewModel.setRedDayCount(newVal);
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Step2ViewModel.class);
    }


    @Override
    public void onDestroyView() {
        if (onRedDaysCountSelected != null) {
            onRedDaysCountSelected.onRedDaysCountSelected(mViewModel.getRedDayCount());
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onRedDaysCountSelected = (OnRedDaysCountSelectedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRedDaysCountSelected = null;
    }

    public interface OnRedDaysCountSelectedListener {
        void onRedDaysCountSelected(int count);
    }

}
