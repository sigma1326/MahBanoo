package com.simorgh.redcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.widget.Button;

import com.simorgh.redcalendar.ui.SetLastCycleDayFragment;

public class QuestionsActivity extends AppCompatActivity {

    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        nextButton = findViewById(R.id.ms_btn_next);
        nextButton.setOnClickListener(v -> {
            Navigation.findNavController(QuestionsActivity.this,R.id.stepper_nav_host_fragment).navigate(R.id.action_test);
//            action.setShowId(id)
//            NavHostFragment.findNavController(this @SearchFragment).navigate(action)
        });

    }
}
