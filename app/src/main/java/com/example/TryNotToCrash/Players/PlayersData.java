package com.example.TryNotToCrash.Players;

import java.util.ArrayList;

public class PlayersData {

    private ArrayList<Player> playersList = new ArrayList<>();


    public PlayersData(){

    }

    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    public PlayersData setPlayersList(ArrayList<Player> playersList) {
        this.playersList = playersList;
        return this;
    }



    public void addPlayer(Player player) {
        playersList.add(player);
    }

    public String toString() {
        return "Players{" +
                "PLAYERS=" + playersList.toString() +
                '}';
    }

}
