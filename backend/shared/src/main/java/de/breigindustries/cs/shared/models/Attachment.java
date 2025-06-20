package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class Attachment {
    
    @Id
    private Long id;
    private String fileName;
    private String description;
    private String contentType;
    private Integer size;
    private byte[] data;

    public Attachment(Long id, String fileName, String description, String contentType, Integer size, byte[] data) {
        this.id = id;
        this.fileName = fileName;
        this.description = description;
        this.contentType = contentType;
        this.size = size;
        this.data = data;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }

}
