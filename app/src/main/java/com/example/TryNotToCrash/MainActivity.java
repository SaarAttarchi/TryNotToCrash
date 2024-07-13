package com.example.TryNotToCrash;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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

import com.example.TryNotToCrash.Interfaces.Update;
import com.example.TryNotToCrash.Players.Player;
import com.example.TryNotToCrash.Players.PlayersData;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private static final int NUM_OF_LANES = 5;
    private static final int NUM_OF_ROWS = 6;
    private static final long SECOND = 1000L;
    public final static String  TYPE_OF_MOVE = "type";
    public final static String  TYPE_OF_SPEED = "speed";
    private int typeOfMove;
    private boolean typeOfSpeed;
    private int speed;


    private ExtendedFloatingActionButton button_Left;
    private ExtendedFloatingActionButton button_Right;

    private AppCompatImageView[] main_car;
    private AppCompatImageView[][] car;
    private AppCompatImageView[] main_IMG_hearts;
    private MaterialTextView main_LBL_score;


    private LogicManager logic;


    private boolean timerOn;
    private long startTime;

    private Executor executor;
    private MediaPlayer mediaPlayer;




    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, SECOND / speed);
            logic.updateTime(startTime, SECOND/ speed);// update the time every 1 second
            stopSound();
            updateGame();// update the visuals according to the changes

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        findViews();
        logic = new LogicManager(NUM_OF_LANES, NUM_OF_ROWS, main_IMG_hearts.length);
        initViews();




        
    }

    private void findViews() {
        button_Left = findViewById(R.id.button_Left);
        button_Right = findViewById(R.id.button_Right);
        main_IMG_hearts = new AppCompatImageView[]{ findViewById(R.id.main_IMG_heart1), findViewById(R.id.main_IMG_heart2), findViewById(R.id.main_IMG_heart3)};
        main_car = new AppCompatImageView[]{ findViewById(R.id.main_car0), findViewById(R.id.main_car1), findViewById(R.id.main_car2), findViewById(R.id.main_car3), findViewById(R.id.main_car4)};
        car = new AppCompatImageView[][]{ {findViewById(R.id.car0_0), findViewById(R.id.car0_1), findViewById(R.id.car0_2), findViewById(R.id.car0_3), findViewById(R.id.car0_4)},
                {findViewById(R.id.car1_0), findViewById(R.id.car1_1), findViewById(R.id.car1_2), findViewById(R.id.car1_3), findViewById(R.id.car1_4)},
                {findViewById(R.id.car2_0), findViewById(R.id.car2_1), findViewById(R.id.car2_2), findViewById(R.id.car2_3), findViewById(R.id.car2_4)},
                {findViewById(R.id.car3_0), findViewById(R.id.car3_1), findViewById(R.id.car3_2), findViewById(R.id.car3_3), findViewById(R.id.car3_4)},
                {findViewById(R.id.car4_0), findViewById(R.id.car4_1), findViewById(R.id.car4_2), findViewById(R.id.car4_3), findViewById(R.id.car4_4)},
                {findViewById(R.id.car5_0), findViewById(R.id.car5_1), findViewById(R.id.car5_2), findViewById(R.id.car5_3), findViewById(R.id.car5_4)}
        };
        main_LBL_score = findViewById(R.id.main_LBL_score);


    }

    private void initViews() {
        typeOfMove = getIntent().getIntExtra(TYPE_OF_MOVE, 0); // gets the movement type from the opening screen
        typeOfSpeed = getIntent().getBooleanExtra(TYPE_OF_SPEED, false); // gets the speed type from the opening screen

        executor = Executors.newSingleThreadExecutor();

        // if the switch is press then the player want the fast option and the speed will be doubled
        // if not then the player want the slow option and the speed will be regular
        if(typeOfSpeed)
            speed = 2;
        if(!typeOfSpeed)
            speed = 1;



        // if the player chose the button movement option
        if(typeOfMove == 1){
            Log.d("type of move", String.valueOf(typeOfMove));
            buttonMove();
        }
        // if the player chose the sensors movement option
        if(typeOfMove == 2) {
            Log.d("type of move", String.valueOf(typeOfMove));
            logic.sensorMove(this, new Update(){
                // everytime the car is moved wee update the game to see it
                @Override
                public void moveBySensor() {
                    updateGame();
                    Log.d("sensor move", "car moved");
                }
            });
        }

    }

    // move the main car location based on the buttons
    private void buttonMove() {
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
                if(typeOfMove == 2)
                    logic.startSensor();
            }
        }


    private void stopTimer() {// stops the timer at the end
        timerOn = false;
        handler.removeCallbacks(runnable);
        if(typeOfMove == 2)
            logic.stopSensor();
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
                playCrashSound(R.raw.crash);
                toastMessage();
                main_IMG_hearts[logic.getNumOfCrashes() - 1].setVisibility(View.INVISIBLE);
            }
            main_LBL_score.setText(String.valueOf(logic.getScore()));
            gameOver();
        }
    }

    private void toastMessage() {
        Toast.makeText(this, "oops you crashed", Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

    }

    private void gameOver() {// if the terms for ending a game happened then stop the timer and move to the game over screen
        if(logic.isGameOver()){
            stopTimer();

            playersList();
            // get the playersList json from Share Preferences
            String playersListJson = SharePreferences.getString(this, "playersList", "");

            Intent intent = new Intent(this, GameOverActivity.class); // create new intent to go to main activity
            // sends the score, game over string, the players list, the movement type and the speed type to the game over screen
            intent.putExtra(GameOverActivity.SCORE, logic.getScore());
            intent.putExtra(GameOverActivity.STATUS, "GAME OVER");
            intent.putExtra(GameOverActivity.PLAYERS_LIST, playersListJson);
            intent.putExtra(MainActivity.TYPE_OF_MOVE, typeOfMove);
            intent.putExtra(MainActivity.TYPE_OF_SPEED, typeOfSpeed);
            startActivity(intent);
            finish();
        }
    }

    // set the players list
    private void playersList(){

        PlayersData playersList = loadPlayersList();
        // add a new player to the list
        playersList.addPlayer( new Player().setFinalScore(logic.getScore()));

        Gson gson = new Gson();
        String playersListAsJson = gson.toJson(playersList); // save the new list as a string
        SharePreferences.putString(this, "playersList", playersListAsJson); // put the new string as playersList in the Share Preferences

        Log.d("JSON",playersListAsJson);



    }

    // get the last players list
    private PlayersData loadPlayersList() {
        Gson gson = new Gson();
        // get the players list from the Share Preferences
        String playersListAsJson  = SharePreferences.getString(this, "playersList", "");
        if (playersListAsJson.isEmpty()) { // if its empty so create one and if not than return it
            return new PlayersData();
        } else {
            return gson.fromJson(playersListAsJson, PlayersData.class);
        }
    }

    public void playCrashSound(int resID) {
        if (mediaPlayer == null){
            executor.execute(() -> {
                mediaPlayer = MediaPlayer.create(this, resID);
                mediaPlayer.setVolume(1.0f,1.0f);
                mediaPlayer.start(); // no need to call prepare(); create() does that for you
                Log.d("sound", "crash sound played");
            });

        }



    }


    public void stopSound() {
        if (mediaPlayer != null){
            executor.execute(() -> {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                Log.d("sound", "crash sound stopped");
            });
        }
    }





}