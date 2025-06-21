package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "message")
public class Message {
    
    @Id
    private Long id;
    private Long timestamp;
    /** Contains formatted content (contentDisplay in JDA) */
    private String content;
    @Column(name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    public Message(Long id, Long timestamp, String content, User author, Channel channel) {
        this.id = id;
        this.timestamp = timestamp;
        this.content = content;
        this.author = author;
        this.channel = channel;
    }

    public Long getId() { return id; }
    public Long getTimestamp() { return timestamp; }
    public String getContent() { return content; }
    public User getAuthor() { return author; }
    public Channel getChannel() { return channel; }
    
}
