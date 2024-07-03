package com.example.exe1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class OpeningScreenActivity extends AppCompatActivity {

    private static final String gameName = "Try Not To Crash";
    private static final String optionsQuestion = "how would you like to play?";

    private int typeOfMove;

    private MaterialTextView game_name;
    private MaterialTextView options;
    private MaterialButton buttons_opt;

    private MaterialButton sensor_opt;

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
    }

    private void initViews() {

        game_name.setText(gameName);
        options.setText(optionsQuestion);


        Intent intent = new Intent(this, MainActivity.class);

        buttons_opt.setOnClickListener(new View.OnClickListener() {// everytime we press the left button
            @Override
            public void onClick(View v) {
                typeOfMove = 1;
                intent.putExtra(MainActivity.TYPE_OF_MOVE, typeOfMove);
                startActivity(intent);
                finish();
            }
        });

        sensor_opt.setOnClickListener(new View.OnClickListener() {// everytime we press the left button
            @Override
            public void onClick(View v) {
                typeOfMove = 2;
                intent.putExtra(MainActivity.TYPE_OF_MOVE, typeOfMove);
                startActivity(intent);
                finish();
            }
        });
    }
}