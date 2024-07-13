package com.example.TryNotToCrash;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.TryNotToCrash.Interfaces.Update;

import java.util.Random;

public class LogicManager {


    private final int MAX_CRASHES;
    private final int NUM_OF_LANES;
    private final int NUM_OF_ROWS;

    private Update update;

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private long timeDif = 0l;
    private int movementRangeTime = 200;
    public int currentLane = 1; // sets where the main car will be at the start
    private int timePlayed;
    private int numOfCrashes = 0;
    private int score = 0;

    public boolean[] mainCarArray;
    public boolean[][] carsMatrix;




    public LogicManager(int numOfLanes, int numOfRows, int maxCrashes) {

        this.NUM_OF_LANES = numOfLanes;
        this.NUM_OF_ROWS = numOfRows;
        this.MAX_CRASHES = maxCrashes;

        // creates the cars matrix and the main car array
        mainCarArray = new boolean[numOfLanes];
        carsMatrix = new boolean[numOfRows][numOfLanes];

        // make the array to be false
        for (int i = 0; i < numOfLanes; i++) {
            mainCarArray[i] = false;
        }
        // setting the spot if the current lane to be true so the car will be only there
        mainCarArray[currentLane] = true;

        // make the whole matrix to be false so it will be empty at the beginning
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfLanes; j++) {
                carsMatrix[i][j] = false;
            }
        }

    }


    public void updateTime(long startTime, long second) {// set the current time and gives us the time the game is running
        long currentTime = getCurrentTime();
        timePlayed = (int)((currentTime - startTime) / 1000); // we divide by 1000 to see the time is seconds
        Log.d("Timer", String.valueOf(timePlayed));
        updateLocation();
        score += 10;
    }

    private void updateLocation() {
        // every second we update the position of the cars
        for (int i = 0; i < NUM_OF_LANES; i++) {
            carsMatrix[NUM_OF_ROWS - 1][i] = false; // set the last row to be false so the cars at the end will disappear
        }
        // every ro we check if there is a car the so set the same lane in the lower row to be true and the current spot we change to false
        // with that update the location
        for (int i = NUM_OF_ROWS - 2; i >= 0; i--) {
            for (int j = 0; j < NUM_OF_LANES; j++) {
                if (carsMatrix[i][j]) {
                    carsMatrix[i + 1][j] = true;
                    carsMatrix[i][j] = false;
                }
            }
        }
        // every to seconds we pick a random lane and set a car there
        if ((timePlayed % 2) == 0) {
            int laneToSpawn = new Random().nextInt(NUM_OF_LANES);
            carsMatrix[0][laneToSpawn] = true;

        }

    }

    public boolean crash() {
            if(mainCarArray[currentLane] && carsMatrix[NUM_OF_ROWS - 1][currentLane]){ // if there is a car shown un the last row in the current lane the main car is then there is a crash
                numOfCrashes++;
                Log.d("crash", "crash");
                return true;
            }
        return false;
    }



    public int getNumOfCrashes() {
        return numOfCrashes;
    }
    public int getScore(){ return score;}


    public boolean isGameOver(){// if the number of crashes is the same as the max number of crashes then it will return true for the game is ending
        return getNumOfCrashes() == MAX_CRASHES;
    }

    public long getCurrentTime(){
        return System.currentTimeMillis();
    }


    //get the sensors
    public void sensorMove(Context context, Update update) {

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.update = update;
        sensorListener();

    }

    // create the sensor listener for only x changes
    private void sensorListener(){
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) { // everytime the sensor is moving check it
                float x = event.values[0];
                checkMovement(x);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }

    private void checkMovement(float x){
        if(getCurrentTime() - timeDif > 500 ){ // if the time now and the time we checked before is more than 0.5 second
            timeDif = getCurrentTime();
            // see if the sensor move the amount we want
            if (x < -4.0) { // means there was a movement to the right and update the game
                moveRight();
                Log.d("sensor move", "right");
                if (update != null)
                    update.moveBySensor();
            }

            if (x > 4.0) { // means there was a movement to the left and update the game
                moveLeft();
                Log.d("sensor move", "left");
                if (update != null)
                    update.moveBySensor();
            }
        }
    }



    public void moveLeft() {
        if(currentLane > 0) {// do it only if we are not on the left lane
            // set the current position to be false, update the number of the current lane and set the new place to be true;
            mainCarArray[currentLane] = false;
            currentLane--;
            mainCarArray[currentLane] = true;
        }
    }

    public void moveRight() {// do it only if we are not on the right lane
        if(currentLane < 4) {
            // set the current position to be false, update the number of the current lane and set the new place to be true;
            mainCarArray[currentLane] = false;
            currentLane++;
            mainCarArray[currentLane] = true;
        }
    }

    // starting the senor to check movements
    public void startSensor() {
        Log.d("sensor", "sensor start");
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );

    }
    // stop using the senor
    public void stopSensor() {
        Log.d("sensor", "sensor end");
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
