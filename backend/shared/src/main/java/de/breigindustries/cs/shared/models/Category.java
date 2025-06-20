package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class Category {
 
    @Id
    private Long id;
    private String name;
    private Guild guild;

    public Category(Long id, String name, Guild guild) {
        this.id = id;
        this.name = name;
        this.guild = guild;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Guild getGuild() { return guild; }
    public void setGuild(Guild guild) { this.guild = guild; }
}
