package de.breigindustries.cs.bot.chatgpt;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.breigindustries.cs.bot.Levi;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.User;

public class ConversationEntry {
    private static final Logger logger = LoggerFactory.getLogger(ConversationEntry.class);

    private final long userId;
    private final long messageId;
    private final long timestamp;
    private final String content;

    // Store historical user- and display names for efficient and offline queueing
    private final String username;
    private final String displayName;

    public ConversationEntry(Message message, String nickname) {
        User author = message.getAuthor();
        this.userId = author.getIdLong();
        this.messageId = message.getIdLong();
        this.timestamp = message.getTimeCreated().toInstant().toEpochMilli();
        // this.content = message.getContentDisplay();
        this.content = getCompleteMessage(message);
        this.username = author.getName();
        this.displayName = nickname;
    }

    /**
     * Returns content formatted for group chat processing, i.e. including the name in the start of the message
     */
    public String getGroupchatContent() {
        // Differentiate between bot messages and other messages
        if (userId == Levi.getIdLong()) return content;
        return displayName + " (" + username + "): " + content;
    }

    public long getUserId() { return userId; }
    public long getMessageId() { return messageId; }
    public long getTimestamp() { return timestamp; }
    public String getContent() { return content; }
    
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }

    public static String getCompleteMessage(Message message) {
        StringBuilder fullText = new StringBuilder(message.getContentDisplay()).append("\n");

        List<Attachment> attachments = message.getAttachments();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        // logger.debug("Attachment count: {}", attachments.size());

        for (Attachment attachment : attachments) {
            logger.debug("Attachment name: {}", attachment.getFileName());

            CompletableFuture<Void> future = attachment.getProxy().download()
                .thenAccept(inputStream -> {
                    try {
                        // Save the contents of the file to safely process in isTextFile and here
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        inputStream.transferTo(buffer);
                        byte[] data = buffer.toByteArray();
                        
                        // Check if the file is a text file
                        if (!isTextFile(new ByteArrayInputStream(data))) {
                            logger.warn("Determined that {} was not a text file!", attachment.getFileName());
                            return;
                        }
                        logger.debug("File {} is a text file, proceeding to read...", attachment.getFileName());

                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data))))  {
                            fullText.append("Attachment ").append(attachment.getFileName()).append(":\n");
                            String line;
                            while ((line = reader.readLine()) != null) {
                                fullText.append(line).append("\n");
                            }
                            logger.debug("✅ Read: {}", attachment.getFileName());
                        } 
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                })
                .exceptionally(e -> {
                    logger.error("❌ Failed to read: {}, Error: {}", attachment.getFileName(), e.getMessage());
                    return null;
                });    
            futures.add(future);
        }

        // Wait for all reads to finish
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return fullText.toString();
    }
    
    private static boolean isTextFile(InputStream in) throws IOException {
        int bytesRead;
        byte[] buffer = new byte[8000];
        bytesRead = in.read(buffer);
        if (bytesRead == -1) return true; // empty file -> probably text

        for (int i = 0; i < bytesRead; i++) {
            byte b = buffer[i];
            // Allow common ASCII control caracters like \n, \r, \t
            if (b < 0x09 || (b > 0x0D && b < 0x20) || b == 0x7F) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd.MM.yyyy HH:mm:ss] ");
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
            + content;
    }
}