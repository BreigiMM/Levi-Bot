package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "embed")
public class Embed {
    
    @EmbeddedId
    private EmbedId id;
    private String description;
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    @Column(name = "content_url")
    private String contentUrl;
    @Column(name = "footer_icon_url")
    private String footerIconUrl;
    @Column(name = "footer_text")
    private String footerText;

    @ManyToOne
    @MapsId("messageId")
    @JoinColumn(name = "message_id")
    private Message message;

    public Embed(Message message, Integer index, String description, String thumbnailUrl, String contentUrl, String footerIconUrl, String footerText) {
        this.id = new EmbedId(message.getId(), index);
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.contentUrl = contentUrl;
        this.footerIconUrl = footerIconUrl;
        this.footerText = footerText;
    }

    public Message getMessage() { return message; }
    public Integer getIndex() { return id.getIndex(); }
    public String getDescription() { return description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getContentUrl() { return contentUrl; }
    public String getFooterIconUrl() { return footerIconUrl; }
    public String getFooterText() { return footerText; }

}
