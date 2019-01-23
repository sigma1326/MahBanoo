package com.simorgh.mahbanoo.View.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.simorgh.databaseutils.model.User;
import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.View.register.QuestionsActivity;
import com.simorgh.mahbanoo.ViewModel.main.CycleViewModel;
import com.simorgh.sweetalertdialog.SweetAlertDialog;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static com.simorgh.mahbanoo.Model.AppManager.TAG;

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
            User user = Objects.requireNonNull(mViewModel.getUserWithCyclesLiveData().getValue()).getUser();
            if (user != null) {
                user.setShowPregnancyProb(isChecked);
                mViewModel.updateUser(user);
            }
        });


        showCycleDays.setOnCheckedChangeListener((buttonView, isChecked) -> {
            User user = Objects.requireNonNull(mViewModel.getUserWithCyclesLiveData().getValue()).getUser();
            if (user != null) {
                user.setShowCycleDays(isChecked);
                mViewModel.updateUser(user);
            }
        });

        tvClearData.setOnClickListener(v1 -> {

            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("پاک کردن اطلاعات برنامه")
                    .setContentText("آیا اطمینان دارید؟")
                    .setConfirmText("باشه")
                    .setCancelText("بی‌خیال")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        mViewModel.clearData();
                        new Handler(Looper.getMainLooper()).post(() -> {
                            v1.getContext().startActivity(new Intent(SettingsFragment.this.getActivity(), QuestionsActivity.class));
                        });
                    })
                    .show();
        });

        tvMakeReport.setOnClickListener(v1 -> navController.navigate(R.id.action_settings_to_make_report));

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        mViewModel.getUserWithCyclesLiveData().observe(this, userWithCycles -> {
            if (showCycleDays != null && userWithCycles != null) {
                showCycleDays.setChecked(userWithCycles.getUser().isShowCycleDays());
                showPregnancyProb.setChecked(userWithCycles.getUser().isShowPregnancyProb());
                Log.d(TAG, userWithCycles.getUser().getCurrentCycle().toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        showPregnancyProb = null;
        showCycleDays = null;
        tvClearData = null;
        tvShareApp = null;
        tvMakeReport = null;
        tvAboutUs = null;
        navController = null;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
