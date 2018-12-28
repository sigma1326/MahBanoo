package com.simorgh.redcalendar.View.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.calendarutil.persiancalendar.PersianDate;
import com.simorgh.moodview.MoodView;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.Model.database.model.Cycle;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.main.MakeReportViewModel;

import java.util.Objects;

import static com.simorgh.redcalendar.Model.AppManager.TAG;

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

        });


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MakeReportViewModel.class);
        mViewModel.getRangeStartLive().observe(this, calendar -> {
            if (calendar != null && btnRangeStart != null) {
                PersianDate persianDate = CalendarTool.GregorianToPersianDate(calendar);
                btnRangeStart.setText(new StringBuilder()
                        .append(persianDate.getShYear()).append("/")
                        .append(persianDate.getShMonth()).append("/")
                        .append(persianDate.getShDay()).toString());
                Log.d(TAG, "start: " + calendar.getTime().toString());
            }
        });

        mViewModel.getRangeEndLive().observe(this, calendar -> {
            if (calendar != null && btnRangeEnd != null) {
                PersianDate persianDate = CalendarTool.GregorianToPersianDate(calendar);
                btnRangeEnd.setText(new StringBuilder()
                        .append(persianDate.getShYear()).append("/")
                        .append(persianDate.getShMonth()).append("/")
                        .append(persianDate.getShDay()).toString());

                Log.d(TAG, "end: " + calendar.getTime().toString());

            }
        });

    }

}
