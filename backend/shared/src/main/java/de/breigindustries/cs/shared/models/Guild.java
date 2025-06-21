package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class Guild {
    
    @Id
    private Long id;
    private String name;
    private String iconUrl;

    public Guild(Long id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    
}
