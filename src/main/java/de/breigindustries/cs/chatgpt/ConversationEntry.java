package de.breigindustries.cs.chatgpt;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class ConversationEntry {
    private final long userId;
    private final long messageId;
    private final Timestamp timestamp;
    private final String content;

    // Store historical user- and display names for efficient and offline queueing
    private final String username;
    private final String displayName;

    public ConversationEntry(Message message, String nickname) {
        User author = message.getAuthor();
        this.userId = author.getIdLong();
        this.messageId = message.getIdLong();
        OffsetDateTime timeCreated = message.getTimeCreated();
        Instant instant = timeCreated.toInstant();
        this.timestamp = Timestamp.from(instant);
        this.content = message.getContentDisplay();
        this.username = author.getName();
        this.displayName = nickname;
    }

    /**
     * Returns content formatted for group chat processing, i.e. including the name in the start of the message
     */
    public String getGroupchatContent() {
        return displayName + " (" + username + "): " + content;
    }

    public long getUserId() { return userId; }
    public long getMessageId() { return messageId; }
    public Timestamp getTimestamp() { return timestamp; }
    public String getContent() { return content; }
    
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }

    @Override
    public String toString() {
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd.MM.yyyy HH:mm:ss] ");
        return dateTime.format(formatter) + content;
    }
}