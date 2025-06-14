package de.breigindustries.cs.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.breigindustries.cs.chatgpt.Conversation;
import de.breigindustries.cs.chatgpt.ConversationEntry;

public class ConversationRepository {

    private static Logger logger = LoggerFactory.getLogger(ConversationRepository.class);
    
    public static void saveConversation(Conversation conversation) {
        // Save conversation
        String sql = """
                INSERT INTO conversation (channel_id, guild_id, last_message_timestamp, is_private, channel_name, category_name, guild_name)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(channel_id) DO UPDATE SET
                    guild_id = excluded.guild_id,
                    last_message_timestamp = excluded.last_message_timestamp,
                    is_private = excluded.is_private,
                    channel_name = excluded.channel_name,
                    category_name = excluded.category_name,
                    guild_name = excluded.guild_name;
                """;
    
        try (Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, conversation.getChannelId());
            statement.setLong(2, conversation.getGuildId());
            statement.setLong(3, conversation.getLastMsgTimestamp());
            statement.setBoolean(4, conversation.isPrivate());
            statement.setString(5, conversation.getChannelName());
            statement.setString(6, conversation.getCategoryName());
            statement.setString(7, conversation.getGuildName());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to save conversation: {}", e.getMessage());
            logger.error("SQLState: {}", e.getSQLState());
        }

        // Save the entries
        saveNewEntries(conversation.getChannelId(), conversation.getMessages());
        logger.info("Saved conversation " + conversation.getChannelName() + " in " + conversation.getGuildName());
    }

    private static void saveNewEntries(long channelId, List<ConversationEntry> entries) {
        String sql = """
                INSERT INTO conversation_entry (message_id, user_id, timestamp, content, username, display_name, conversation_id) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        
        try (Connection connection = DatabaseManager.getConnection()) {
            for (int i = entries.size() - 1; i >= 0; i--) {
                ConversationEntry entry = entries.get(i);

                if (entryExists(connection, entry.getMessageId())) {
                    break; // Stop when we hit the first duplicate
                }

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setLong(1, entry.getMessageId());
                    stmt.setLong(2, entry.getUserId());
                    stmt.setLong(3, entry.getTimestamp());
                    stmt.setString(4, entry.getContent());
                    stmt.setString(5, entry.getUsername());
                    stmt.setString(6, entry.getDisplayName());
                    stmt.setLong(7, channelId);
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to save conversation entries: {}", e.getMessage());
        }
    }

    private static boolean entryExists(Connection connection, long messageId) throws SQLException {
        String checkSql = "SELECT 1 FROM conversation_entry WHERE message_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(checkSql)) {
            stmt.setLong(1, messageId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

}
