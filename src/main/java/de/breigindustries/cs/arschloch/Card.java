package de.breigindustries.cs.arschloch;

import java.util.Random;

/**
 * Cards are represented as integers, 0 being the seven of diamonds, 31 being the ace of spades
 * Ranks 7 to ace, suits diamonds -> hearts -> clubs -> spades
 */
public class Card {

    public final int value;

    public Card(int value) {
        this.value = value;
    }

    public static Card[] getShuffledDeck() {
        
        // Create new sorted deck
        Card[] deck = new Card[32];
        for (int i = 0; i < deck.length; i++) {
            deck[i] = new Card(i);
        }

        // Fisher-Yates
        Random rand = new Random();
        for (int i = deck.length - 1; i > 0; i--) {
            int j = rand.nextInt(i+1); // 0 to i (inclusive)
            // Swap deck[i] with deck[j]
            Card temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }

        return deck;
    }
}
