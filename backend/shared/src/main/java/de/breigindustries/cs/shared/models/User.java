package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private int experience;

    public User() {}

    public User(String username, int experience) {
        this.username = username;
        this.experience = experience;
    }

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
    public void setExperience(int experience) { this.experience = experience; }
    public int getExperience() { return experience; }
}
