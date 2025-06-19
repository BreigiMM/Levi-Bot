package de.breigindustries.cs.bot.chatgpt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.breigindustries.cs.bot.Levi;
import de.breigindustries.cs.bot.database.ConversationRepository;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AIInteraction {

    private static Logger logger = LoggerFactory.getLogger(AIInteraction.class);
    private static int memoryLimit = Integer.parseInt(Dotenv.configure().load().get("MEMORY_LIMIT"));

    /**
     * Makes Levi do AI-assisted conversation.
     * Includes memorizing the conversation, reading the chat, and sending and saving a reply.
     * @param event
     */
    public static void converse(MessageReceivedEvent event) {     
        // Memorizing the message sent
        Conversation conversation = Conversation.getConversationByChannel(event.getChannel());
        conversation.addEntryFromMessage(event.getMessage());

        Conversation channelConversation = Conversation.createEmptyConversationFromChannel(event.getChannel());
        if (conversation.isPancakeMode()) channelConversation.setPancakeMode();
        Conversation.fillConversation(channelConversation, memoryLimit).thenAccept(filledConversation -> {
            String response = ChatGPTUtils.getChatbotResponse(channelConversation);

            // Send the message in the channel
            Levi.writeMessage(event.getChannel(), response).thenAccept(sentMessage -> {
                conversation.addEntryFromMessage(sentMessage);
                channelConversation.addEntryFromMessage(sentMessage);
                ConversationRepository.saveConversation(conversation);
                logger.debug("Pancake mode of conversation: {}", conversation.isPancakeMode());
            });
        });
    }
}
