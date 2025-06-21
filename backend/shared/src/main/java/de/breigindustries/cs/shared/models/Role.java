package de.breigindustries.cs.shared.models;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    
    @Id
    private Long id;
    private String name;
    /** Hex string of the color. Can be set with {@code String.format("#%06X", role.getColorRaw())} */
    private String color;
    @ManyToOne
    @JoinColumn(name = "guild_id")
    private Guild guild;

    @ManyToMany(mappedBy = "roles")
    private Set<Member> members;

    public Role(Long id, String name, String color, Guild guild) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.guild = guild;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public Guild getGuild() { return guild; }

    public Set<Member> getMembers() { return members; }
    
}
