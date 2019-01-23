package com.simorgh.mahbanoo.View.register;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.register.Step3ForgetViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class Step3ForgetFragment extends Fragment {

    private Step3ForgetViewModel mViewModel;
    private Step3Fragment.OnGrayDaysCountSelectedListener onGrayDaysCountSelected;


    public static Step3ForgetFragment newInstance() {
        return new Step3ForgetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,   @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step3forget_fragment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Step3ForgetViewModel.class);
    }

    @Override
    public void onDestroyView() {
        if (onGrayDaysCountSelected != null) {
            onGrayDaysCountSelected.onGrayDaysCountSelected(24);
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onGrayDaysCountSelected = (Step3Fragment.OnGrayDaysCountSelectedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onGrayDaysCountSelected = null;
    }
}
