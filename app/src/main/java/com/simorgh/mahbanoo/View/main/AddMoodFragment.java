package com.simorgh.mahbanoo.View.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.simorgh.calendarutil.model.YearMonthDay;
import com.simorgh.databaseutils.model.DayMood;
import com.simorgh.mahbanoo.Model.AndroidUtils;
import com.simorgh.mahbanoo.Model.AppManager;
import com.simorgh.mahbanoo.Model.DrugItem;
import com.simorgh.mahbanoo.Model.DrugListAdapter;
import com.simorgh.mahbanoo.Model.Logger;
import com.simorgh.mahbanoo.R;
import com.simorgh.mahbanoo.ViewModel.main.CycleViewModel;
import com.simorgh.moodview.MoodView;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddMoodFragment extends Fragment {

    private CycleViewModel mViewModel;
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
        getArgs();
        mViewModel.getMoodsLiveData().observe(this, dayMoods -> {
            updateViews();
        });

    }

    @Override
    public void onDestroyView() {
        mvBleeding = null;
        mvEmotion = null;
        mvPain = null;
        mvEatingDesire = null;
        mvHairStyle = null;

        btnApplyWeight = null;
        btnAddDrug = null;
        etWeight = null;
        etDrugName = null;

        rvDrugs = null;

        super.onDestroyView();
    }

    private void getArgs() {
        if (getArguments() != null) {
            YearMonthDay yearMonthDay = AddMoodFragmentArgs.fromBundle(getArguments()).getSelectedDay();
            if (yearMonthDay != null) {
                Calendar calendar = AppManager.getCalendarInstance();
                calendar.set(Calendar.DAY_OF_MONTH, yearMonthDay.getDay());
                calendar.set(Calendar.MONTH, yearMonthDay.getMonth());
                calendar.set(Calendar.YEAR, yearMonthDay.getYear());
                mViewModel.setSelectedDateMood(calendar);
                Logger.d("getArgs: " + calendar.getTime());
            }
        }
    }

    private void hideKeyboard(@NonNull Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusView = activity.getCurrentFocus();
        if (focusView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (activity.getCurrentFocus() != null) {
            activity.getCurrentFocus().clearFocus();
        }
    }

    private void showKeyboard(@NonNull Activity activity, View v1) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v1, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_note_fragment, container, false);

        mvBleeding = view.findViewById(R.id.moodViewBleeding);
        mvEmotion = view.findViewById(R.id.moodViewEmotion);
        mvPain = view.findViewById(R.id.moodViewPain);
        mvEatingDesire = view.findViewById(R.id.moodViewEatingDesire);
        mvHairStyle = view.findViewById(R.id.moodViewHairStyle);


        btnApplyWeight = view.findViewById(R.id.btn_weight_apply);
        etWeight = view.findViewById(R.id.et_weight);
        btnAddDrug = view.findViewById(R.id.btn_drug_apply);
        etDrugName = view.findViewById(R.id.et_drug);
        rvDrugs = view.findViewById(R.id.rv_drugs);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getArgs();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvDrugs.setLayoutManager(gridLayoutManager);
        rvDrugs.setAdapter(new DrugListAdapter(new DrugListAdapter.DrugDiffCallBack()));
        rvDrugs.setNestedScrollingEnabled(false);
        rvDrugs.setHasFixedSize(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            rvDrugs.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        etWeight.setOnClickListener(v1 -> {
            v1.setFocusable(true);
            v1.setFocusableInTouchMode(true);
            v1.requestFocus();
            showKeyboard(Objects.requireNonNull(getActivity()), v1);

        });

        etDrugName.setOnClickListener(v1 -> {
            v1.setFocusable(true);
            v1.setFocusableInTouchMode(true);
            v1.requestFocus();
            showKeyboard(Objects.requireNonNull(getActivity()), v1);
        });


        btnAddDrug.setOnClickListener(v1 -> {
            if (etDrugName != null && etDrugName.getText().length() > 0) {
                AppManager.getExecutor().execute(() -> {
                    if (mViewModel.getDayMood() != null) {
                        DayMood dayMood = mViewModel.getDayMood();
                        if (mViewModel.getDayMood().getDrugs() != null) {
                            dayMood.getDrugs().add(etDrugName.getText().toString());
                            clearFocus();
                            mViewModel.updateDayMood(dayMood);
                        } else {
                            List<String> drugs = new LinkedList<>();
                            drugs.add(etDrugName.getText().toString());
                            dayMood.setDrugs(drugs);
                            clearFocus();
                            mViewModel.updateDayMood(dayMood);
                        }
                    } else {
                        DayMood dayMood = new DayMood();
                        dayMood.setId(mViewModel.getSelectedDateMood());
                        List<String> drugs = new LinkedList<>();
                        drugs.add(etDrugName.getText().toString());
                        clearFocus();
                        dayMood.setDrugs(drugs);
                        mViewModel.updateDayMood(dayMood);
                    }
                });
            }
        });

        btnApplyWeight.setOnClickListener(v1 -> {
            if (etDrugName != null && etWeight.getText().length() > 0) {
                AppManager.getExecutor().execute(() -> {
                    if (mViewModel.getDayMood() != null) {
                        DayMood dayMood = mViewModel.getDayMood();
                        try {
                            dayMood.setWeight(Float.parseFloat(etWeight.getText().toString()));
                            clearFocus();
                            mViewModel.updateDayMood(dayMood);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        DayMood dayMood = new DayMood();
                        dayMood.setId(mViewModel.getSelectedDateMood());
                        try {
                            dayMood.setWeight(Float.parseFloat(etWeight.getText().toString()));
                            clearFocus();
                            mViewModel.updateDayMood(dayMood);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        mvBleeding.setOnItemSelectedListener(selectedItems -> {
            AppManager.getExecutor().execute(() -> {
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
        });

        mvEmotion.setOnItemSelectedListener(selectedItems -> {
            AppManager.getExecutor().execute(() -> {
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
        });

        mvPain.setOnItemSelectedListener(selectedItems -> {
            AppManager.getExecutor().execute(() -> {
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
        });

        mvEatingDesire.setOnItemSelectedListener(selectedItems -> {
            AppManager.getExecutor().execute(() -> {
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
        });

        mvHairStyle.setOnItemSelectedListener(selectedItems -> {
            AppManager.getExecutor().execute(() -> {
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
        });
    }

    private void clearFocus() {
        AndroidUtils.runOnUIThread(() -> {
            hideKeyboard(Objects.requireNonNull(getActivity()));
            etDrugName.setText("");
            etDrugName.setFocusableInTouchMode(false);
            etDrugName.setFocusable(false);

            etWeight.setText("");
            etWeight.setFocusableInTouchMode(false);
            etWeight.setFocusable(false);
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateViews() {
        mvBleeding.setSelectedItem(null);
        mvEmotion.setSelectedItems(null);
        mvPain.setSelectedItems(null);
        mvEatingDesire.setSelectedItems(null);
        mvHairStyle.setSelectedItems(null);

        AppManager.getExecutor().execute(() -> {
            DayMood dayMood = mViewModel.getDayMood();
            if (dayMood != null && rvDrugs != null) {
                AndroidUtils.runOnUIThread(() -> {
                    mvBleeding.setSelectedItem(dayMood.getTypeBleedingSelectedIndex());
                    mvEmotion.setSelectedItems(dayMood.getTypeEmotionSelectedIndices());
                    mvPain.setSelectedItems(dayMood.getTypePainSelectedIndices());
                    mvEatingDesire.setSelectedItems(dayMood.getTypeEatingDesireSelectedIndices());
                    mvHairStyle.setSelectedItems(dayMood.getTypeHairStyleSelectedIndices());

                    etWeight.setHint(String.format("%3.3f", dayMood.getWeight()));
                });

                List<DrugItem> drugItemList = new LinkedList<>();
                if (dayMood.getDrugs() != null) {
                    DrugItem drugItem;
                    for (int i = 0; i < dayMood.getDrugs().size(); i++) {
                        drugItem = new DrugItem(dayMood.getDrugs().get(i));
                        drugItem.setId(mViewModel.getSelectedDateMood());
                        drugItemList.add(drugItem);
                    }
                }

                AndroidUtils.runOnUIThread(() -> {
                    if (!drugItemList.isEmpty()) {
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                        rvDrugs.setLayoutManager(gridLayoutManager);
                        if (rvDrugs.getAdapter() == null) {
                            rvDrugs.setAdapter(new DrugListAdapter(new DrugListAdapter.DrugDiffCallBack()));
                        }
                        ((DrugListAdapter) (Objects.requireNonNull(rvDrugs.getAdapter()))).submitList(drugItemList);
                    } else {
                        rvDrugs.setAdapter(null);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
