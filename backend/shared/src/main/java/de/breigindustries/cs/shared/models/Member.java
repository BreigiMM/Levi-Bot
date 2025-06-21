package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class Member {
    
    @Id
    private Long id;
    private User user;
    private Guild guild;
    private String name;
    private String avatarUrl;

    public Member(Long id, User user, Guild guild, String name, String avatarUrl) {
        this.id = id;
        this.user = user;
        this.guild = guild;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Guild getGuild() { return guild; }
    public void setGuild(Guild guild) { this.guild = guild; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

}
