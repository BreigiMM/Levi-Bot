package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "guild")
public class Guild {
    
    @Id
    private Long id;
    private String name;
    @Column(name = "icon_url")
    private String iconUrl;

    public Guild(Long id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getIconUrl() { return iconUrl; }
    
}
