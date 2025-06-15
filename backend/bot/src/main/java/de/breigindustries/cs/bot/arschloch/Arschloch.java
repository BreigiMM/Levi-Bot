package de.breigindustries.cs.bot.arschloch;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Arschloch {

    public enum ExchangeMode {
        FREE,
        HIGHEST
    }

    public ArrayList<Player> players = new ArrayList<>();
    public boolean roundOngoing;
    
    public Arschloch(Player player) {
        players.add(player);
    }

    public boolean join(Player player) {
        if (roundOngoing) return false;
        if (players.size() >= 5) return false;

        return players.add(player);
    }

    public boolean leave(Player player) {
        if (roundOngoing) return false;
        return players.remove(player);
    }

    public boolean startRound() {
        if (roundOngoing) return false;
        if (players.size() < 3) return false;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            playRound();
        });
        return true;
    }

    public void playRound() {
        roundOngoing = true;

        // Deal
        // Card[] deck = Card.getShuffledDeck();


        // Game loop
        while(roundOngoing) {
            break;
        }
    }

}
