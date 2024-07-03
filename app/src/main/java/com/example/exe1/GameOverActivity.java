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

public class GameOverActivity extends AppCompatActivity {

    public final static String  SCORE = "SCORE";
    public final static String  STATUS = "STATUS";
    private MaterialTextView finalScore;
    private MaterialButton restart_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        findViews();
        initViews();
    }

    private void findViews() {
        finalScore = findViewById(R.id.finalScore);
        restart_button = findViewById(R.id.restart_button);

    }

    private void initViews() {

        int score = getIntent().getIntExtra(SCORE, 0);
        String status = getIntent().getStringExtra(STATUS);
        finalScore.setText(status + "\n" + score);

        Intent intent = new Intent(this, MainActivity.class);
        restart_button.setOnClickListener(new View.OnClickListener() {// everytime we press the left button
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }
}