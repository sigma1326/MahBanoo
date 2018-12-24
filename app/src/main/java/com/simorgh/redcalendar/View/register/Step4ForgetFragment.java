package com.simorgh.redcalendar.View.register;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.register.Step4ForgetViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class Step4ForgetFragment extends Fragment {

    private Step4ForgetViewModel mViewModel;
    private TextView tvForget;
    private Typeface typeface;

    public static Step4ForgetFragment newInstance() {
        return new Step4ForgetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step4forget_fragment, container, false);
        tvForget = v.findViewById(R.id.tv_forget);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/iransans_medium.ttf");
        tvForget.setTypeface(typeface);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(Step4ForgetViewModel.class);
        // TODO: Use the ViewModel
    }

}