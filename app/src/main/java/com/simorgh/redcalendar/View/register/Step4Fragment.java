package com.simorgh.redcalendar.View.register;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.numberpicker.NumberPicker;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.register.Step4ViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class Step4Fragment extends Fragment {

    private Step4ViewModel mViewModel;
    private Typeface typeface;
    private OnYellowDaysCountSelectedListener onYellowDaysCountSelected;


    public static Step4Fragment newInstance() {
        return new Step4Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/iransans_medium.ttf");
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step4_fragment, container, false);
        NumberPicker numberPicker = v.findViewById(R.id.np_pms);
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> mViewModel.setYellowDayCount(newVal));
        numberPicker.setTypeface(typeface);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Step4ViewModel.class);
    }

    @Override
    public void onDestroyView() {
        if (onYellowDaysCountSelected != null) {
            onYellowDaysCountSelected.onYellowDaysCountSelected(mViewModel.getYellowDayCount());
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onYellowDaysCountSelected = (OnYellowDaysCountSelectedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onYellowDaysCountSelected = null;
    }

    public interface OnYellowDaysCountSelectedListener {
        public void onYellowDaysCountSelected(int count);
    }

}
