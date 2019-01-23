package com.simorgh.mahbanoo.View.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianDate;
import com.simorgh.databaseutils.CycleRepository;
import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.main.MakeReportViewModel;
import com.simorgh.reportutil.ReportUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
    private CycleRepository cycleRepository;
    private boolean isFirst = true;


    public static MakeReportFragment newInstance() {
        return new MakeReportFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cycleRepository = new CycleRepository(Objects.requireNonNull(getActivity()).getApplication());
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

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment);

        btnRangeStart.setOnClickListener(v1 -> {
            mViewModel.setRangeStart(true);
            navController.navigate(R.id.action_make_report_to_report_date);
        });

        btnRangeEnd.setOnClickListener(v1 -> {
            mViewModel.setRangeStart(false);
            navController.navigate(R.id.action_make_report_to_report_date);
        });


        btnMakeReport.setOnClickListener(v1 -> {
            try {
                ReportUtils.createReport(getActivity(), cycleRepository.getUserWithCycles().getCycles()
                        , Objects.requireNonNull(mViewModel.getRangeStartLive().getValue())
                        , Objects.requireNonNull(mViewModel.getRangeEndLive().getValue())
                        , chbBleeding.isChecked()
                        , chbEmotion.isChecked()
                        , chbPain.isChecked()
                        , chbEatingDesire.isChecked()
                        , chbHairStyle.isChecked()
                        , chbWeight.isChecked()
                        , chbDrugs.isChecked());
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
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MakeReportViewModel.class);
        mViewModel.getRangeStartLive().observe(this, calendar -> {
            if (calendar != null && btnRangeStart != null) {
                PersianDate persianDate = CalendarTool.GregorianToPersianDate(calendar);
                if (isFirst) {
                    persianDate.addDay(-10);
                    mViewModel.setRangeStart(CalendarTool.PersianToGregorian(persianDate));
                    isFirst = false;
                }
                btnRangeStart.setText(String.format("%s/%d/%d", String.valueOf(persianDate.getShYear())
                        , persianDate.getShMonth(), persianDate.getShDay()));
            }
        });

        mViewModel.getRangeEndLive().observe(this, calendar -> {
            if (calendar != null && btnRangeEnd != null) {
                PersianDate persianDate = CalendarTool.GregorianToPersianDate(calendar);
                btnRangeEnd.setText(String.format("%s/%d/%d", String.valueOf(persianDate.getShYear())
                        , persianDate.getShMonth(), persianDate.getShDay()));

            }
        });

    }

}
