package de.breigindustries.cs.shared.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "guild")
public class Guild {
    
    @Id
    private Long id;
    private String name;
    @Column(name = "icon_url")
    private String iconUrl;

    @OneToMany(mappedBy = "guild", cascade = CascadeType.ALL)
    private List<Category> categories;
    @OneToMany(mappedBy = "guild", cascade = CascadeType.ALL)
    private List<Channel> channels;
    @OneToMany(mappedBy = "guild", cascade = CascadeType.ALL)
    private List<Role> roles;
    @OneToMany(mappedBy = "guild", cascade = CascadeType.ALL)
    private List<Member> members;

    public Guild(Long id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getIconUrl() { return iconUrl; }

    public List<Category> getCategories() { return categories; }
    public List<Channel> getChannels() { return channels; }
    public List<Role> getRoles() { return roles; }
    public List<Member> getMembers() { return members; }

}
