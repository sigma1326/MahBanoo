package com.simorgh.redcalendar.View.main;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.simorgh.calendarutil.model.YearMonthDay;
import com.simorgh.cycleutils.CycleData;
import com.simorgh.databaseutils.model.Cycle;
import com.simorgh.moodview.MoodView;
import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.databaseutils.model.DayMood;
import com.simorgh.redcalendar.Model.DrugItem;
import com.simorgh.redcalendar.Model.DrugListAdapter;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.main.CycleViewModel;
import com.simorgh.weekdaypicker.WeekDayPicker;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AddMoodFragment extends Fragment {

    private CycleViewModel mViewModel;
    private WeekDayPicker weekDayPicker;
    private MoodView mvBleeding;
    private MoodView mvEmotion;
    private MoodView mvPain;
    private MoodView mvEatingDesire;
    private MoodView mvHairStyle;

    private Button btnApplyWeight;
    private Button btnAddDrug;

    private EditText etWeight;
    private EditText etDrugName;

    private RecyclerView rvDrugs;


    public static AddMoodFragment newInstance() {
        return new AddMoodFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CycleViewModel.class);
        mViewModel.getCycleLiveData().observe(this, cycle -> {
            if (weekDayPicker != null && cycle != null) {
                weekDayPicker.setCycleData(new CycleData(cycle.getRedDaysCount(),
                        cycle.getGrayDaysCount(), cycle.getYellowDaysCount(), cycle.getStartDate()));
                Log.d(AppManager.TAG, cycle.toString());
                updateViews();
            }
        });
        mViewModel.getMoodsLiveData().observe(this, dayMoods -> {
            updateViews();
            DayMood dayMood = mViewModel.getDayMood();
            List<DrugItem> drugItemList = new LinkedList<>();
            if (rvDrugs != null && dayMood != null) {
                if (mViewModel.getDayMood().getDrugs() != null) {
                    DrugItem drugItem;
                    for (int i = 0; i < mViewModel.getDayMood().getDrugs().size(); i++) {
                        drugItem = new DrugItem(mViewModel.getDayMood().getDrugs().get(i));
                        drugItemList.add(drugItem);
                    }
                }
                ((DrugListAdapter) Objects.requireNonNull(rvDrugs.getAdapter())).submitList(drugItemList);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_note_fragment, container, false);
        mvBleeding = v.findViewById(R.id.moodViewBleeding);
        mvEmotion = v.findViewById(R.id.moodViewEmotion);
        mvPain = v.findViewById(R.id.moodViewPain);
        mvEatingDesire = v.findViewById(R.id.moodViewEatingDesire);
        mvHairStyle = v.findViewById(R.id.moodViewHairStyle);


        btnApplyWeight = v.findViewById(R.id.btn_weight_apply);
        etWeight = v.findViewById(R.id.et_weight);
        btnAddDrug = v.findViewById(R.id.btn_drug_apply);
        etDrugName = v.findViewById(R.id.et_drug);
        rvDrugs = v.findViewById(R.id.rv_drugs);
        rvDrugs.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvDrugs.setAdapter(new DrugListAdapter(new DrugListAdapter.DrugDiffCallBack()));
        rvDrugs.setNestedScrollingEnabled(false);


        btnAddDrug.setOnClickListener(v1 -> {
            if (etDrugName != null && etDrugName.getText().length() > 0) {
                if (mViewModel.getDayMood() != null) {
                    DayMood dayMood = mViewModel.getDayMood();
                    if (mViewModel.getDayMood().getDrugs() != null) {
                        dayMood.getDrugs().add(etDrugName.getText().toString());
                        mViewModel.updateDayMood(dayMood);
                    } else {
                        List<String> drugs = new LinkedList<>();
                        drugs.add(etDrugName.getText().toString());
                        dayMood.setDrugs(drugs);
                        mViewModel.updateDayMood(dayMood);
                    }
                } else {
                    DayMood dayMood = new DayMood();
                    dayMood.setId(mViewModel.getSelectedDateMood());
                    dayMood.setDrugs(new LinkedList<>());
                    List<String> drugs = new LinkedList<>();
                    drugs.add(etDrugName.getText().toString());
                    dayMood.setDrugs(drugs);
                    mViewModel.updateDayMood(dayMood);
                }
            }
        });

        btnApplyWeight.setOnClickListener(v1 -> {
            if (etDrugName != null && etWeight.getText().length() > 0) {
                if (mViewModel.getDayMood() != null) {
                    DayMood dayMood = mViewModel.getDayMood();
                    try {
                        dayMood.setWeight(Float.parseFloat(etWeight.getText().toString()));
                        mViewModel.updateDayMood(dayMood);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    DayMood dayMood = new DayMood();
                    dayMood.setId(mViewModel.getSelectedDateMood());
                    try {
                        dayMood.setWeight(Float.parseFloat(etWeight.getText().toString()));
                        mViewModel.updateDayMood(dayMood);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (getArguments() != null) {
            YearMonthDay yearMonthDay = AddMoodFragmentArgs.fromBundle(getArguments()).getSelectedDay();
            if (yearMonthDay != null) {
                Calendar calendar = AppManager.getCalendarInstance();
                calendar.set(Calendar.DAY_OF_MONTH, yearMonthDay.getDay());
                calendar.set(Calendar.MONTH, yearMonthDay.getMonth());
                calendar.set(Calendar.YEAR, yearMonthDay.getYear());
                mViewModel.setSelectedDateMood(calendar);
            }
        }


        mvBleeding.setOnItemSelectedListener(selectedItems -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood == null) {
                dayMood = new DayMood();
                dayMood.setId(mViewModel.getSelectedDateMood());
            }
            if (selectedItems.isEmpty()) {
                dayMood.setTypeBleedingSelectedIndex(-1);
            } else {
                dayMood.setTypeBleedingSelectedIndex(selectedItems.get(0));
            }
            mViewModel.updateDayMood(dayMood);
        });

        mvEmotion.setOnItemSelectedListener(selectedItems -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood == null) {
                dayMood = new DayMood();
                dayMood.setId(mViewModel.getSelectedDateMood());
            }
            if (selectedItems.isEmpty()) {
                dayMood.setTypeEmotionSelectedIndices(null);
            } else {
                dayMood.setTypeEmotionSelectedIndices(selectedItems);
            }
            mViewModel.updateDayMood(dayMood);
        });

        mvPain.setOnItemSelectedListener(selectedItems -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood == null) {
                dayMood = new DayMood();
                dayMood.setId(mViewModel.getSelectedDateMood());
            }
            if (selectedItems.isEmpty()) {
                dayMood.setTypePainSelectedIndices(null);
            } else {
                dayMood.setTypePainSelectedIndices(selectedItems);
            }
            mViewModel.updateDayMood(dayMood);
        });

        mvEatingDesire.setOnItemSelectedListener(selectedItems -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood == null) {
                dayMood = new DayMood();
                dayMood.setId(mViewModel.getSelectedDateMood());
            }
            if (selectedItems.isEmpty()) {
                dayMood.setTypeEatingDesireSelectedIndices(null);
            } else {
                dayMood.setTypeEatingDesireSelectedIndices(selectedItems);
            }
            mViewModel.updateDayMood(dayMood);
        });

        mvHairStyle.setOnItemSelectedListener(selectedItems -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood == null) {
                dayMood = new DayMood();
                dayMood.setId(mViewModel.getSelectedDateMood());
            }
            if (selectedItems.isEmpty()) {
                dayMood.setTypeHairStyleSelectedIndices(null);
            } else {
                dayMood.setTypeHairStyleSelectedIndices(selectedItems);
            }
            mViewModel.updateDayMood(dayMood);
        });

        weekDayPicker = v.findViewById(R.id.weekDayPicker);
        weekDayPicker.setSelectedDate(AppManager.getCalendarInstance());
        weekDayPicker.setOnDaySelectedListener(day -> {
            mViewModel.setSelectedDateMood(day);
            updateViews();
        });
        return v;
    }

    @SuppressLint("DefaultLocale")
    private void updateViews() {
        mvBleeding.setSelectedItem(null);
        mvEmotion.setSelectedItems(null);
        mvPain.setSelectedItems(null);
        mvEatingDesire.setSelectedItems(null);
        mvHairStyle.setSelectedItems(null);

        weekDayPicker.setSelectedDate(mViewModel.getSelectedDateMood());
        DayMood dayMood = mViewModel.getDayMood();
        if (dayMood != null) {
            mvBleeding.setSelectedItem(dayMood.getTypeBleedingSelectedIndex());
            mvEmotion.setSelectedItems(dayMood.getTypeEmotionSelectedIndices());
            mvPain.setSelectedItems(dayMood.getTypePainSelectedIndices());
            mvEatingDesire.setSelectedItems(dayMood.getTypeEatingDesireSelectedIndices());
            mvHairStyle.setSelectedItems(dayMood.getTypeHairStyleSelectedIndices());

            etWeight.setHint(String.format("%3.3f", dayMood.getWeight()));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
