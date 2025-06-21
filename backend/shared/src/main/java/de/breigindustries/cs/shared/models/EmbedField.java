package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
@Table(name = "embed_field")
public class EmbedField {
    
    /** Index of the field inside the embed */
    @Id
    @Column(name = "field_index")
    private Integer fieldIndex;
    /** ID of the message the embed is attached to */
    @Id
    private Long id;
    /** Index of the embed inside the message */
    @Id
    @Column(name = "embed_index")
    private Integer embedIndex;
    private String name;
    private String value;
    @Column(name = "embed_id")
    private Embed embed;

    public EmbedField(Integer fieldIndex, Long id, Integer embedIndex, String name, String value, Embed embed) {
        this.fieldIndex = fieldIndex;
        this.id = id;
        this.embedIndex = embedIndex;
        this.name = name;
        this.value = value;
        this.embed = embed;
    }

    public Integer getFieldIndex() { return fieldIndex; }
    public Long getId() { return id; }
    public Integer getEmbedIndex() { return embedIndex; }
    public String getName() { return name; }
    public String getValue() { return value; }
    public Embed getEmbed() { return embed; }
    
}
