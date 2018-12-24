package com.simorgh.redcalendar.View.register;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.register.Step2ForgetViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class Step2ForgetFragment extends Fragment {

    private Step2ForgetViewModel mViewModel;
    private TextView tvForget;
    private Typeface typeface;
    private Step2Fragment.OnRedDaysCountSelectedListener onRedDaysCountSelected;


    public static Step2ForgetFragment newInstance() {
        return new Step2ForgetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step2forget_fragment, container, false);
        tvForget = v.findViewById(R.id.tv_forget);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/iransans_medium.ttf");
        tvForget.setTypeface(typeface);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Step2ForgetViewModel.class);
    }


    @Override
    public void onDestroyView() {
        if (onRedDaysCountSelected != null) {
            onRedDaysCountSelected.onRedDaysCountSelected(4);
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onRedDaysCountSelected = (Step2Fragment.OnRedDaysCountSelectedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRedDaysCountSelected = null;
    }
}
