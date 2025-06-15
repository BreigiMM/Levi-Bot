package de.breigindustries.cs.bot.arschloch;

public class Player {
    
    public Role role;
    public Card[] hand;

    public Player(Role role, Card[] hand) {
        this.role = role;
        this.hand = hand;
    }

}
