package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class Embed {
    
    @Id
    private Message message;
    @Id
    private Integer index;
    private String description;
    private String thumbnailUrl;
    private String contentUrl;
    private String footerIconUrl;
    private String footerText;

    public Embed(Message message, Integer index, String description, String thumbnailUrl, String contentUrl, String footerIconUrl, String footerText) {
        this.message = message;
        this.index = index;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.contentUrl = contentUrl;
        this.footerIconUrl = footerIconUrl;
        this.footerText = footerText;
    }

    public Message getMessage() { return message; }
    public void setMessage(Message message) { this.message = message; }
    public Integer getIndex() { return index; }
    public void setIndex(Integer index) { this.index = index; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getContentUrl() { return contentUrl; }
    public void setContentUrl(String contentUrl) { this.contentUrl = contentUrl; }
    public String getFooterIconUrl() { return footerIconUrl; }
    public void setFooterIconUrl(String footerIconUrl) { this.footerIconUrl = footerIconUrl; }
    public String getFooterText() { return footerText; }
    public void setFooterText(String footerText) { this.footerText = footerText; }

}
