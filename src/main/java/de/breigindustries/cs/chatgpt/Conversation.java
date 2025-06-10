package de.breigindustries.cs.chatgpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class Conversation {

    public static Map<MessageChannelUnion, Conversation> conversations = new HashMap<>();
    
    private long last_msg_timestamp;
    public final MessageChannelUnion channel;
    List<ConversationEntry> message_history = new ArrayList<>();

    public Conversation(MessageChannelUnion channel) {
        last_msg_timestamp = System.currentTimeMillis();
        this.channel = channel;
        conversations.put(channel, this);
    }

    public Conversation(MessageChannelUnion channel, boolean register_convo) {
        last_msg_timestamp = System.currentTimeMillis();
        this.channel = channel;
        if (register_convo) conversations.put(channel, this);
    }

    public long getLastMsgTimestamp() {
        return last_msg_timestamp;
    }

    public boolean isPrivate() {
        return channel.getType() == ChannelType.PRIVATE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ConversationEntry entry : message_history) {
            sb.append(entry.message() + "\n");
        }
        return sb.toString();
    }

}
