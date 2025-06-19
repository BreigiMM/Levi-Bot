package de.breigindustries.cs.bot.chatgpt;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.breigindustries.cs.bot.Levi;
import de.breigindustries.cs.bot.database.ConversationRepository;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Message;
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
        if (conversation.isPancakeMode()) channelConversation.setPancakeMode(null);
        Conversation.fillConversation(channelConversation, memoryLimit).thenAccept(filledConversation -> {
            String response = ChatGPTUtils.getChatbotResponse(channelConversation);

            // Send the message in the channel
            var futures = Levi.writeMessage(event.getChannel(), response);
            CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allDone.thenAccept(v -> {
                List<Message> sentMessages = futures.stream().map(CompletableFuture::join).toList();
                for (Message sentMessage : sentMessages) {
                    conversation.addEntryFromMessage(sentMessage);
                    channelConversation.addEntryFromMessage(sentMessage);
                    ConversationRepository.saveConversation(conversation);
                }
                logger.debug("Pancake mode of conversation: {}", conversation.isPancakeMode());
            });
        });
    }
}
