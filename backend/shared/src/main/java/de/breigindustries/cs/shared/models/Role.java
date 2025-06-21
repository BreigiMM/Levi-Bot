package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class Role {
    
    @Id
    private Long id;
    private String name;
    /** Hex string of the color. Can be set with {@code String.format("#%06X", role.getColorRaw())} */
    private String color;
    private Guild guild;

    public Role(Long id, String name, String color, Guild guild) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.guild = guild;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Guild getGuild() { return guild; }
    public void setGuild(Guild guild) { this.guild = guild; }
    
}
