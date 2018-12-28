package com.simorgh.redcalendar.View.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.View.register.QuestionsActivity;
import com.simorgh.redcalendar.ViewModel.main.CycleViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static com.simorgh.redcalendar.Model.AppManager.TAG;

public class SettingsFragment extends Fragment {

    private Switch showPregnancyProb;
    private Switch showCycleDays;

    private TextView tvClearData;
    private TextView tvShareApp;
    private TextView tvMakeReport;
    private TextView tvAboutUs;
    private NavController navController;


    private CycleViewModel mViewModel;


    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);
        showPregnancyProb = v.findViewById(R.id.switch_showPregnancyProb);
        showCycleDays = v.findViewById(R.id.switch_showCycleDays);
        tvAboutUs = v.findViewById(R.id.tv_about_us);
        tvClearData = v.findViewById(R.id.tv_clear_data);
        tvShareApp = v.findViewById(R.id.tv_share_app);
        tvMakeReport = v.findViewById(R.id.tv_make_report);
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment);


        showPregnancyProb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Cycle cycle = mViewModel.getCycleLiveData().getValue();
            if (cycle != null) {
                cycle.setShowPregnancyProb(isChecked);
                mViewModel.updateCycle(cycle);
            }
        });


        showCycleDays.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Cycle cycle = mViewModel.getCycleLiveData().getValue();
            if (cycle != null) {
                cycle.setShowCycleDays(isChecked);
                mViewModel.updateCycle(cycle);
            }
        });

        tvClearData.setOnClickListener(v1 -> {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(v1.getContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(v1.getContext());
            }
            builder.setTitle("پاک کردن اطلاعات برنامه")
                    .setMessage("آیا اطمینان دارید؟")
                    .setPositiveButton("بله", (dialog, which) -> {
                        mViewModel.clearData();
                        new Handler(Looper.getMainLooper()).post(() -> {
                            v1.getContext().startActivity(new Intent(SettingsFragment.this.getActivity(), QuestionsActivity.class));
                        });
                    })
                    .setNegativeButton("خیر", (dialog, which) -> {
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        tvMakeReport.setOnClickListener(v1 -> {
            navController.navigate(R.id.action_settings_to_make_report);
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        mViewModel.getCycleLiveData().observe(this, cycle -> {
            if (showCycleDays != null && cycle != null) {
                showCycleDays.setChecked(cycle.isShowCycleDays());
                showPregnancyProb.setChecked(cycle.isShowPregnancyProb());
                Log.d(TAG, cycle.toString());
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
