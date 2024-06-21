package com.example.exe1;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView; // This is used for the main car image

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_OF_LANES = 3;
    private static final int NUM_OF_ROWS = 6;

    private ExtendedFloatingActionButton button_Left;
    private ExtendedFloatingActionButton button_Right;

    private AppCompatImageView[] main_car;
    private AppCompatImageView[][] car;
    private AppCompatImageView[] main_IMG_hearts;


    private LogicManager logic;


    private boolean timerOn;
    private long startTime;




    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 1000L);
            logic.updateTime(startTime);// update the time every 1 second
            updateGame();// update the visuals according to the changes
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();
        logic = new LogicManager(NUM_OF_LANES, NUM_OF_ROWS, main_IMG_hearts.length);

        
    }

    private void findViews() {
        button_Left = findViewById(R.id.button_Left);
        button_Right = findViewById(R.id.button_Right);
        main_IMG_hearts = new AppCompatImageView[]{ findViewById(R.id.main_IMG_heart1), findViewById(R.id.main_IMG_heart2), findViewById(R.id.main_IMG_heart3)};
        main_car = new AppCompatImageView[]{ findViewById(R.id.main_car0), findViewById(R.id.main_car1), findViewById(R.id.main_car2)};
        car = new AppCompatImageView[][]{ {findViewById(R.id.car0), findViewById(R.id.car1), findViewById(R.id.car2)},
                                            {findViewById(R.id.car3), findViewById(R.id.car4), findViewById(R.id.car5)},
                                            {findViewById(R.id.car6), findViewById(R.id.car7), findViewById(R.id.car8)},
                                            {findViewById(R.id.car9), findViewById(R.id.car10), findViewById(R.id.car11)},
                                            {findViewById(R.id.car12), findViewById(R.id.car13), findViewById(R.id.car14)},
                                            {findViewById(R.id.car15), findViewById(R.id.car16), findViewById(R.id.car17)}
        };


    }

    private void initViews() {

        button_Left.setOnClickListener(new View.OnClickListener() {// everytime we press the left button
            @Override
            public void onClick(View v) {
                logic.moveLeft();// change the info of the main car location
                updateGame();// update the images with this change
            }
        });


        button_Right.setOnClickListener(new View.OnClickListener() { //everytime we press the left button
            @Override
            public void onClick(View v) {
                logic.moveRight();// change the info of the main car location
                updateGame();// update the images with this change
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void startTimer() {// if the timer is off and the game hasn't ended turn the timer on, set the time of start and run
            if(!timerOn && !logic.isGameOver()) {
                Log.d("startTimer", "startTimer: Timer Started");
                startTime = System.currentTimeMillis();
                timerOn = true;
                handler.postDelayed(runnable, 0);
            }
        }


    private void stopTimer() {// stops the timer at the end
        timerOn = false;
        Log.d("stopTimer", "stopTimer: Timer Stopped");
        handler.removeCallbacks(runnable);
    }



    private void updateGame(){
        if(timerOn) {// the game is updating as long as the timer hasn't stopped

            // checks the car matrix and see if a certain spot is false then there's not supposed to be a car there and if it's true then there supposed to be
            for (int i = 0; i < NUM_OF_ROWS; i++) {
                for (int j = 0; j < NUM_OF_LANES; j++) {
                    if (!logic.carsMatrix[i][j])
                        car[i][j].setVisibility(View.INVISIBLE);
                    else
                        car[i][j].setVisibility(View.VISIBLE);
                }
            }
            // do the same for the main car
            for (int i = 0; i < NUM_OF_LANES; i++) {
                if (!logic.mainCarArray[i])
                    main_car[i].setVisibility(View.INVISIBLE);
                else
                    main_car[i].setVisibility(View.VISIBLE);
            }
            // send a toast message if there was a crush and change the hearts icons
            if (logic.crash()) {
                toastMessage();
                main_IMG_hearts[logic.getNumOfCrashes() - 1].setVisibility(View.INVISIBLE);
            }
            gameOver();
        }
    }

    private void toastMessage() {
        Toast.makeText(this, "oops you crashed", Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

    }

    private void gameOver() {// if the terms for ending a game happened then stop the timer
        if(logic.isGameOver()){
            stopTimer();
        }
    }






}