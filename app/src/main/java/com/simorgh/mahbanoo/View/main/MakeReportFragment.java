package com.simorgh.mahbanoo.View.main;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianDate;
import com.simorgh.mahbanoo.Model.AppManager;
import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.main.MakeReportViewModel;
import com.simorgh.reportutil.ReportUtils;

import java.util.Objects;

public class MakeReportFragment extends Fragment {

    private MakeReportViewModel mViewModel;
    private CheckBox chbBleeding;
    private CheckBox chbEatingDesire;
    private CheckBox chbEmotion;
    private CheckBox chbHairStyle;
    private CheckBox chbPain;
    private CheckBox chbWeight;
    private CheckBox chbDrugs;

    private Button btnRangeStart;
    private Button btnRangeEnd;
    private Button btnMakeReport;

    private NavController navController;
    private boolean isFirst = true;


    public static MakeReportFragment newInstance() {
        return new MakeReportFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.make_report_fragment, container, false);

        chbBleeding = v.findViewById(R.id.chb_bleeding);
        chbEatingDesire = v.findViewById(R.id.chb_eating_desire);
        chbEmotion = v.findViewById(R.id.chb_emotion);
        chbHairStyle = v.findViewById(R.id.chb_hair_style);
        chbPain = v.findViewById(R.id.chb_pain);
        chbWeight = v.findViewById(R.id.chb_weight);
        chbDrugs = v.findViewById(R.id.chb_drugs);


        btnRangeStart = v.findViewById(R.id.btn_start_range);
        btnRangeEnd = v.findViewById(R.id.btn_end_range);
        btnMakeReport = v.findViewById(R.id.btn_make_report);

        navController = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment);

        btnRangeStart.setOnClickListener(v1 -> {
            mViewModel.setRangeStart(true);
            navController.navigate(R.id.action_make_report_to_report_date);
        });

        btnRangeEnd.setOnClickListener(v1 -> {
            mViewModel.setRangeStart(false);
            navController.navigate(R.id.action_make_report_to_report_date);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnMakeReport.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity()
                    , R.drawable.btn_make_report_ripple_background));
        } else {
            btnMakeReport.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity()
                    , R.drawable.btn_make_report_ripple_background_api19));
        }


        btnMakeReport.setOnClickListener(v1 -> {
            try {
                boolean bleeding = chbBleeding.isChecked();
                boolean motion = chbEmotion.isChecked();
                boolean pain = chbPain.isChecked();
                boolean eatingDesire = chbEatingDesire.isChecked();
                boolean hairStyle = chbHairStyle.isChecked();
                boolean weight = chbWeight.isChecked();
                boolean drugs = chbDrugs.isChecked();
                ReportUtils.createReport(getActivity(), AppManager.getExecutor(), AppManager.getCycleRepository()
                        , Objects.requireNonNull(mViewModel.getRangeStartLive().getValue())
                        , Objects.requireNonNull(mViewModel.getRangeEndLive().getValue())
                        , bleeding
                        , motion
                        , pain
                        , eatingDesire
                        , hairStyle
                        , weight
                        , drugs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        return v;
    }

    private void setRTL(final View view) {
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_RTL);
    }


    @Override
    public void onDestroyView() {
        chbBleeding = null;
        chbEatingDesire = null;
        chbEmotion = null;
        chbHairStyle = null;
        chbPain = null;
        chbWeight = null;
        chbDrugs = null;

        btnRangeStart = null;
        btnRangeEnd = null;
        btnMakeReport = null;

        navController = null;

        super.onDestroyView();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(MakeReportViewModel.class);
        mViewModel.getRangeStartLive().observe(getViewLifecycleOwner(), calendar -> {
            if (calendar != null && btnRangeStart != null) {
                PersianDate persianDate = CalendarTool.GregorianToPersianDate(calendar);
                if (isFirst) {
                    persianDate.addDay(-10);
                    mViewModel.setRangeStart(CalendarTool.PersianToGregorian(persianDate));
                    isFirst = false;
                }
                btnRangeStart.setText(String.format("%s/%d/%d", persianDate.getShYear()
                        , persianDate.getShMonth(), persianDate.getShDay()));
            }
        });

        mViewModel.getRangeEndLive().observe(getViewLifecycleOwner(), calendar -> {
            if (calendar != null && btnRangeEnd != null) {
                PersianDate persianDate = CalendarTool.GregorianToPersianDate(calendar);
                btnRangeEnd.setText(String.format("%s/%d/%d", persianDate.getShYear()
                        , persianDate.getShMonth(), persianDate.getShDay()));

            }
        });

    }

}
