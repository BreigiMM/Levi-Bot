package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "channel")
public class Channel {
    
    @Id
    private Long id;
    private String name;
    /** Matches the ChannelType names of JDA */
    private String type;
    @Column(name = "category_id")
    private Category category;
    @Column(name = "guild_id")
    private Guild guild;

    public Channel(Long id, String name, String type, Category category, Guild guild) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.guild = guild;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public Category getCategory() { return category; }
    public Guild getGuild() { return guild; }

}
