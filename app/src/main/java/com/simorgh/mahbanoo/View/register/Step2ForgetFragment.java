package com.simorgh.mahbanoo.View.register;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.register.Step2ForgetViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class Step2ForgetFragment extends Fragment {

    private Step2ForgetViewModel mViewModel;
    private Step2Fragment.OnRedDaysCountSelectedListener onRedDaysCountSelected;


    public static Step2ForgetFragment newInstance() {
        return new Step2ForgetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step2forget_fragment, container, false);
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
