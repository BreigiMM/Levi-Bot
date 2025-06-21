package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_description")
public class UserDescription {
    
    /** Maps 1:1 to the user's id */
    @Id
    private Long id;
    private String content;
    @Column(name = "user_id")
    private User user;

    public UserDescription(Long id, String content, User user) {
        this.id = id;
        this.content = content;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public User getUser() { return user; }

}
