package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "attachment")
public class Attachment {
    
    @Id
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    private String description;
    @Column(name = "content_type")
    private String contentType;
    private Integer size;
    private byte[] data;
    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    public Attachment(Long id, String fileName, String description, String contentType, Integer size, byte[] data, Message message) {
        this.id = id;
        this.fileName = fileName;
        this.description = description;
        this.contentType = contentType;
        this.size = size;
        this.data = data;
        this.message = message;
    }

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getDescription() { return description; }
    public String getContentType() { return contentType; }
    public Integer getSize() { return size; }
    public byte[] getData() { return data; }
    public Message getMessage() { return message; }

}
