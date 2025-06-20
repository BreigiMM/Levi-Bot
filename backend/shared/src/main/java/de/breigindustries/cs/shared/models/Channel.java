package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class Channel {
    
    @Id
    private Long id;
    private String name;
    /** Matches the ChannelType names of JDA */
    private String type;
    private Category category;
    private Guild guild;

    public Channel(Long id, String name, String type, Category category, Guild guild) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.guild = guild;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Guild getGuild() { return guild; }
    public void setGuild(Guild guild) { this.guild = guild; }

}
