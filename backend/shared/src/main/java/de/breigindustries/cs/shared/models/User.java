package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private Long id;
    /** Username, unique identifier on discord, mutable. Display name is {@link #displayName effective name}*/
    private String username;
    /** Contains the global name if set, or the {@link #username name (username)} if global name is {@code null} */
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "banner_url")
    private String bannerUrl;

    @OneToOne(mappedBy = "user_description", cascade = CascadeType.ALL)
    private UserDescription userDescription;

    public User(Long id, String username, String displayName, String avatarUrl, String bannerUrl) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.bannerUrl = bannerUrl;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getBannerUrl() { return bannerUrl; }

    public UserDescription getUserDescription() { return userDescription; }
    
}
