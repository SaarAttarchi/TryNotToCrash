package com.example.TryNotToCrash;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import com.example.TryNotToCrash.Players.Player;
import com.example.TryNotToCrash.Players.PlayersData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;


import java.util.Collections;
import java.util.Comparator;

public class GameOverActivity extends AppCompatActivity {

    public final static String  SCORE = "SCORE";
    public final static String  STATUS = "STATUS";
    public final static String  PLAYERS_LIST = "PLAYERS_LIST";
    public final static String  TYPE_OF_MOVE = "type";
    public final static String  TYPE_OF_SPEED = "speed";
    private MaterialTextView finalScore;
    private int score;
    private int typeOfMove;
    private boolean typeOfSpeed;
    private MaterialButton restart_button;
    private MaterialButton main_menu_button;
    private MaterialButton show_location;
    private MaterialTextView players_list[];

    private PlayersData playersData;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        findViews();
        initViews();

        // sets the request to use the location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();

    }

    private void findViews() {
        finalScore = findViewById(R.id.finalScore);
        restart_button = findViewById(R.id.restart_button);
        main_menu_button = findViewById(R.id.main_menu_button);
        show_location = findViewById(R.id.show_location);
        players_list = new MaterialTextView[]{ findViewById(R.id.players_list1), findViewById(R.id.players_list2), findViewById(R.id.players_list3), findViewById(R.id.players_list4), findViewById(R.id.players_list5)};


    }

    private void initViews() {
        // gets the score, game over string, the players list, the movement type and the speed type from the main activity
        score = getIntent().getIntExtra(SCORE, 0);
        String status = getIntent().getStringExtra(STATUS);
        String playersListJson = getIntent().getStringExtra(PLAYERS_LIST);
        typeOfMove = getIntent().getIntExtra(TYPE_OF_MOVE, 0);
        typeOfSpeed = getIntent().getBooleanExtra(TYPE_OF_SPEED, false);
        finalScore.setText(status + "\n" + score);


        Gson gson = new Gson();
        // saves the list of players as a variable in the class of PlayersData
        playersData = gson.fromJson(playersListJson, PlayersData.class);

        // sorting the players list by their score
        Collections.sort(playersData.getPlayersList(), new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p2.getFinalScore(), p1.getFinalScore());
            }
        });

        // pick 5 from the players list and if there are not 5 then the amount there is
        for (int i = 0; i < Math.min(playersData.getPlayersList().size(), 5); i++) {
            Player player = playersData.getPlayersList().get(i);
            players_list[i].setText((i + 1) + ": " + player.getFinalScore()); // write the score for each player and his position
            players_list[i].setOnClickListener(v -> showPlayersLocations(player)); // show his location when pressing him
        }
        // save the new player location
        show_location.setOnClickListener(v -> savePlayerLocation());



        // create new intent to go to main activity
        Intent intentGame = new Intent(this, MainActivity.class);
        // by pressing the restart button go back to the main activity with the previous speed and movement
        restart_button.setOnClickListener(new View.OnClickListener() {// everytime we press the left button
            @Override
            public void onClick(View v) {
                intentGame.putExtra(MainActivity.TYPE_OF_MOVE, typeOfMove);
                intentGame.putExtra(MainActivity.TYPE_OF_SPEED, typeOfSpeed);
                startActivity(intentGame);
            }
        });

        // create new intent to go to main menu
        Intent intentMenu = new Intent(this, OpeningScreenActivity.class);
        // by pressing the main menu button go back to the main menu
        main_menu_button.setOnClickListener(new View.OnClickListener() {// everytime we press the left button
            @Override
            public void onClick(View v) {
                startActivity(intentMenu);
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void savePlayerLocation() { // save the player location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    for (Player player : playersData.getPlayersList()) {
                        // checks all the players in the list if they are the new player
                        if (player.getFinalScore() == score) {
                            // when found set his latitude and longitude
                            player.setLatitude(location.getLatitude());
                            player.setLongitude(location.getLongitude());
                            Log.d("tag", "Player location saved: " + location.getLatitude() + ", " + location.getLongitude());
                            Gson gson = new Gson();
                            String updatedPlayersListJson = gson.toJson(playersData); // save the update of the playersData with the new player location on a string
                            SharePreferences.putString(GameOverActivity.this, "playersList", updatedPlayersListJson); // put the new string in the share preferences
                            showPlayersLocations(player);// show his location on the map
                            break;
                        }
                    }
                }
            }
    });
    }

    // show the player location
    private void showPlayersLocations(Player player) {
        if (player.getLatitude() != 0 && player.getLongitude() != 0) { // if there is a location saved send it to the map fragment
            MapFragment mapFragment = MapFragment.newInstance(player.getLatitude(), player.getLongitude());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.map_fragment_container, mapFragment);
            transaction.commit();
        } else { // if there is no saved location
            Log.e("not tag", "Player location is not set");
        }
    }


    // checks the permissions in the manifest
    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
    }


}