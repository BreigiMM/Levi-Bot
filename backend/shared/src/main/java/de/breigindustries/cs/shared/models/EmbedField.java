package de.breigindustries.cs.shared.models;

import jakarta.persistence.*;

@Entity
public class EmbedField {
    
    @Id
    private Embed embed;
    @Id
    private Integer index;
    private String name;
    private String value;

    public EmbedField(Embed embed, Integer index, String name, String value) {
        this.embed = embed;
        this.index = index;
        this.name = name;
        this.value = value;
    }

    public Embed getEmbed() { return embed; }
    public void setEmbed(Embed embed) { this.embed = embed; }
    public Integer getIndex() { return index; }
    public void setIndex(Integer index) { this.index = index; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
}
