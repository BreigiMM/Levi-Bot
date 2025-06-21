package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    private Long id;
    /** Username, unique identifier on discord, mutable. Display name is {@link #effectiveName effective name}*/
    private String name;
    /** Contains the global name if set, or the {@link #name name (username)} if global name is {@code null} */
    private String effectiveName;
    private String avatarUrl;
    private String bannerUrl;

    public User(Long id, String name, String effectiveName, String avatarUrl, String bannerUrl) {
        this.id = id;
        this.name = name;
        this.effectiveName = effectiveName;
        this.avatarUrl = avatarUrl;
        this.bannerUrl = bannerUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEffectiveName() { return effectiveName; }
    public void setEffectiveName(String effectiveName) { this.effectiveName = effectiveName; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
    
}
