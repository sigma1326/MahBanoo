package com.simorgh.redcalendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class QuestionsActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private Button nextButton;
    private TextView stepTitle;
    private TextView stepFractionTitle;
    private ProgressBar progressBar;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        nextButton = findViewById(R.id.ms_btn_next);
        stepTitle = findViewById(R.id.ms_tvStepTitle);
        stepFractionTitle = findViewById(R.id.ms_tvStepFraction);
        progressBar = findViewById(R.id.ms_progressBar);
        progressBar.setProgress(20, true);
        navController = Navigation.findNavController(QuestionsActivity.this, R.id.stepper_nav_host_fragment);
        Navigation.findNavController(QuestionsActivity.this, R.id.stepper_nav_host_fragment).addOnDestinationChangedListener(this);
        nextButton.setOnClickListener(v -> {
            switch (Objects.requireNonNull(Objects.requireNonNull(navController.getCurrentDestination()).getLabel()).toString()) {
                case "SetLastCycleDayFragment":
                    navController.navigate(R.id.action_test);
//                    progressBar.setProgress(40, true);

                    break;
                case "test":
                    navController.navigate(R.id.action_test_to_mainActivity);
//                    progressBar.setProgress(60, true);

                    break;
                default:
            }
//            action.setShowId(id)
//            NavHostFragment.findNavController(this @SearchFragment).navigate(action)
        });


    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        switch (Objects.requireNonNull(Objects.requireNonNull(controller.getCurrentDestination()).getLabel()).toString()) {
            case "step_set_last_cycle":
                progressBar.setProgress(20, true);
                break;
            case "test":
                progressBar.setProgress(40, true);
                break;
            default:
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}
