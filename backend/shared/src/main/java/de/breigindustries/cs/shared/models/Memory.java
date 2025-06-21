package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class Memory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private User user;

    public Memory(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

}
