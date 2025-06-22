package de.breigindustries.cs.shared.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class EmbedId implements Serializable {
    
    private Long messageId;
    private Integer index;

    public EmbedId() {}
    
    public EmbedId(Long messageId, Integer index) {
        this.messageId = messageId;
        this.index = index;
    }

    public Long getMessageId() { return messageId; }
    public Integer getIndex() { return index; }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (getClass() != other.getClass()) return false;
        EmbedId o = (EmbedId) other;
        return Objects.equals(messageId, o.messageId) && Objects.equals(index, o.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, index);
    }

}
