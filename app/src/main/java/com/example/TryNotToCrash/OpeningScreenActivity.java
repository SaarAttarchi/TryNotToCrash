package com.example.TryNotToCrash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class OpeningScreenActivity extends AppCompatActivity {

    private static final String gameName = "Try Not To Crash";
    private static final String optionsQuestion = "how would you like to play?";
    private static final String fast = "fast";
    private static final String slow = "slow";

    private int typeOfMove;

    private MaterialTextView game_name;
    private MaterialTextView options;
    private MaterialTextView fast_opt;
    private MaterialTextView slow_opt;
    private MaterialButton buttons_opt;
    private MaterialButton sensor_opt;
    private SwitchCompat speed_mode;
    private boolean switchPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opening_screen);
        findViews();
        initViews();
    }

    private void findViews() {
        game_name = findViewById(R.id.game_name);
        options = findViewById(R.id.options);
        buttons_opt = findViewById(R.id.buttons_opt);
        sensor_opt = findViewById(R.id.sensor_opt);
        speed_mode = findViewById(R.id.speed_mode);
        fast_opt = findViewById(R.id.fast_opt);
        slow_opt = findViewById(R.id.slow_opt);
    }

    private void initViews() {

        game_name.setText(gameName);
        options.setText(optionsQuestion);
        fast_opt.setText(fast);
        slow_opt.setText(slow);

        // create new intent to go to main activity
        Intent intent = new Intent(this, MainActivity.class);

        // when we press the switch it set a Boolean variable if checked or not
        speed_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchPressed = isChecked;

            }
        });

        // if we press the button opt we go to the intent with the buttons type of movement and the type of speed based on the switch
        buttons_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMove = 1;
                intent.putExtra(MainActivity.TYPE_OF_MOVE, typeOfMove);
                intent.putExtra(MainActivity.TYPE_OF_SPEED, switchPressed);
                startActivity(intent);
                finish();
            }
        });
        // if we press the sensor opt we go to the intent with the sensors type of movement and the type of speed based on the switch
        sensor_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfMove = 2;
                intent.putExtra(MainActivity.TYPE_OF_MOVE, typeOfMove);
                intent.putExtra(MainActivity.TYPE_OF_SPEED, switchPressed);
                startActivity(intent);
                finish();
            }
        });




    }
}