package de.breigindustries.cs.shared.models;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {
    
    @Id
    private Long id;
    private String name;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "guild_id")
    private Guild guild;

    @ManyToMany
    @JoinTable(
        name = "role_member",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public Member(Long id, String name, String avatarUrl, User user, Guild guild) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.user = user;
        this.guild = guild;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAvatarUrl() { return avatarUrl; }
    public User getUser() { return user; }
    public Guild getGuild() { return guild; }

    public Set<Role> getRoles() { return roles; }

}
