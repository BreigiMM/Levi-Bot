package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "embed")
public class Embed {
    
    @Id
    /** Maps 1:1 to message id */
    private Long id;
    @Id
    private Integer index;
    private String description;
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    @Column(name = "content_url")
    private String contentUrl;
    @Column(name = "footer_icon_url")
    private String footerIconUrl;
    @Column(name = "footer_text")
    private String footerText;
    @Column(name = "message_id")
    private Message message;

    public Embed(Long id, Integer index, String description, String thumbnailUrl, String contentUrl, String footerIconUrl, String footerText, Message message) {
        this.id = id;
        this.index = index;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.contentUrl = contentUrl;
        this.footerIconUrl = footerIconUrl;
        this.footerText = footerText;
        this.message = message;
    }

    public Long getId() { return id; }
    public Integer getIndex() { return index; }
    public String getDescription() { return description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getContentUrl() { return contentUrl; }
    public String getFooterIconUrl() { return footerIconUrl; }
    public String getFooterText() { return footerText; }
    public Message getMessage() { return message; }

}
