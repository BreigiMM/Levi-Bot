package de.breigindustries.cs.bot.arschloch;

public enum Role {
    NONE(""),
    ARSCHLOCH("Arschloch"),
    VARSCHLOCH("Vize-Arschloch"),
    BAUER("Bauer"),
    VPRAESIDENT("Vize-Präsident"),
    PRAESIDENT("Präsident");

    public String title;

    Role(String title) {
        this.title = title;
    }
}
