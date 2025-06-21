package de.breigindustries.cs.shared.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {
 
    @Id
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "guild_id")
    private Guild guild;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Channel> channels;

    public Category(Long id, String name, Guild guild) {
        this.id = id;
        this.name = name;
        this.guild = guild;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Guild getGuild() { return guild; }
    
    public List<Channel> getChannels() { return channels; }
    
}
