package de.breigindustries.cs.chatgpt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.breigindustries.cs.Levi;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AIInteraction {

    private static int memoryLimit = Integer.parseInt(Dotenv.configure().load().get("MEMORY_LIMIT"));
    private static final Logger logger = LoggerFactory.getLogger(AIInteraction.class);

    /**
     * Makes Levi do AI-assisted conversation.
     * Includes memorizing the conversation, reading the chat, and sending and saving a reply.
     * @param event
     */
    public static void converse(MessageReceivedEvent event) {     
        // Memorizing the message sent
        Conversation conversation = Conversation.getConversationByMessageReceivedEvent(event);
        conversation.addEntryFromMessage(event.getMessage());

        logger.debug("converse called, conversation created!");
        Conversation channelConversation = Conversation.createEmptyConversationFromMessageReceivedEvent(event);
        Conversation.fillConversation(channelConversation, memoryLimit).thenAccept(filledConversation -> {
            String response = ChatGPTUtils.getChatbotResponse(channelConversation);

            // Send the message in the channel
            Levi.writeMessage(event.getChannel(), response).thenAccept(sentMessage -> {
                conversation.addEntryFromMessage(sentMessage);
            });
        });
        logger.debug("end of converse method");
    } 
}
