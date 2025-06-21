package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {
    
    @Id
    private Long id;
    private String name;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "user_id")
    private User user;
    @Column(name = "guild_id")
    private Guild guild;

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

}
