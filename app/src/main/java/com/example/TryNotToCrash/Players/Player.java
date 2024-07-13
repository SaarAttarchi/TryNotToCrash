package com.example.TryNotToCrash.Players;

public class Player {

    private int finalScore = 0;
    private double latitude;
    private double longitude;



    public int getFinalScore() {
        return finalScore;
    }


    public Player setFinalScore(int finalScore) {
        this.finalScore = finalScore;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
