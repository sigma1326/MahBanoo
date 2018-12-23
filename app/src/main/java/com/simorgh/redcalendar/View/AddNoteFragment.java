package com.simorgh.redcalendar.View;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.redcalendar.Model.AppManager;
import com.simorgh.redcalendar.R;
import com.simorgh.redcalendar.ViewModel.AddNoteViewModel;
import com.simorgh.weekdaypicker.WeekDayPicker;

import java.util.Calendar;

public class AddNoteFragment extends Fragment implements WeekDayPicker.onDaySelectedListener {

    private AddNoteViewModel mViewModel;
    private WeekDayPicker weekDayPicker;

    public static AddNoteFragment newInstance() {
        return new AddNoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_note_fragment, container, false);
        weekDayPicker = v.findViewById(R.id.weekDayPicker);
        weekDayPicker.setOnDaySelectedListener(this);
        weekDayPicker.setClueData(AppManager.wdpClueData);
        weekDayPicker.setSelectedDate(Calendar.getInstance());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddNoteViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDaySelected(Calendar day) {

    }
}
