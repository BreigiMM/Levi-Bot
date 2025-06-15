package de.breigindustries.cs.arschloch;

public class Player {
    
    public Role role;
    public Card[] hand;

    public Player(Role role, Card[] hand) {
        this.role = role;
        this.hand = hand;
    }

}
