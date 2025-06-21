package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class Message {
    
    @Id
    private Long id;
    private Long timestamp;
    /** Contains formatted content (contentDisplay in JDA) */
    private String content;
    private User author;
    private Channel channel;

    public Message(Long id, Long timestamp, String content, User author, Channel channel) {
        this.id = id;
        this.timestamp = timestamp;
        this.content = content;
        this.author = author;
        this.channel = channel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public Channel getChannel() { return channel; }
    public void setChannel(Channel channel) { this.channel = channel; }
    
}
