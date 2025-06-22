package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_description")
public class UserDescription {
    
    /** Maps 1:1 to the user's id */
    @Id
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    private String content;

    public UserDescription(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public User getUser() { return user; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

}
