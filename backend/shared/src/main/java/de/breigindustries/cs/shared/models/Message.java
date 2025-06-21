package de.breigindustries.cs.shared.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "message")
public class Message {
    
    @Id
    private Long id;
    private Long timestamp;
    /** Contains formatted content (contentDisplay in JDA) */
    private String content;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<Attachment> attachments;

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

    public List<Attachment> getAttachments() { return attachments; }
    
}
