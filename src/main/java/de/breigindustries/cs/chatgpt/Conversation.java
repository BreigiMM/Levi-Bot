package de.breigindustries.cs.chatgpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Conversation {

    private static Map<MessageChannelUnion, Conversation> conversations = new HashMap<>();
    
    private long last_msg_timestamp = 0;
    public final MessageChannelUnion channel;
    private List<ConversationEntry> message_history = new ArrayList<>();

    public Conversation(MessageChannelUnion channel) {
        this.channel = channel;
        conversations.put(channel, this);
    }

    public Conversation(MessageChannelUnion channel, boolean register_convo) {
        this.channel = channel;
        if (register_convo) conversations.put(channel, this);
    }

    public long getLastMsgTimestamp() {
        return last_msg_timestamp;
    }

    public boolean addEntry(ConversationEntry entry) {
        last_msg_timestamp = System.currentTimeMillis();
        return message_history.add(entry);
    }

    public List<ConversationEntry> getHistory() {
        return message_history;
    }

    public boolean isPrivate() {
        return channel.getType() == ChannelType.PRIVATE;
    }

    public static Conversation getConversationByChannel(MessageChannelUnion channel) {
        Conversation convo = conversations.get(channel);
        return convo != null ? convo : new Conversation(channel);
    }

    public void resetLastMsgTimer() {
        last_msg_timestamp = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("- - - - Message History - - - -\n");
        for (ConversationEntry entry : message_history) {
            sb.append(entry.message() + "\n");
        }
        return sb.toString();
    }

}
