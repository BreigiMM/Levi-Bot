package de.breigindustries.cs.chatgpt;

import de.breigindustries.cs.Levi;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public record ConversationEntry(long user_id, String message) {
    public ConversationEntry(MessageReceivedEvent event) {
        this(event.getAuthor().getIdLong(), formatMessage(event));
    }

    public ConversationEntry(Message message) {
        this(message.getAuthor().getIdLong(), formatMessage(message, !isPrivateChannel(message.getChannel())));
    }

    private static String formatMessage(MessageReceivedEvent event) {
        return formatMessage(event.getMessage(), !isPrivateChannel(event.getChannel()));
    }

    private static String formatMessage(Message msg, boolean channel_is_group_chat) {
        User author = msg.getAuthor();
        Member member = msg.getMember();
        if (author.getIdLong() == Levi.getJDA().getSelfUser().getIdLong()) channel_is_group_chat = false; // Make him omit his name
        String message = msg.getContentRaw();

        if (!channel_is_group_chat) {
            return message;
        } else {
            String nickname = author.getEffectiveName();
            String author_name = author.getEffectiveName() + " (" + author.getName() + ")";
            return author_name + ": " + message;
        }
    }

    public static boolean isPrivateChannel(MessageChannelUnion channel) {
        return channel.getType() == ChannelType.PRIVATE;
    }
}
