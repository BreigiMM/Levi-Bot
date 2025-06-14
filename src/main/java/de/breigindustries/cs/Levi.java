package de.breigindustries.cs;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.breigindustries.cs.chatgpt.AIInteraction;
import de.breigindustries.cs.chatgpt.Conversation;
import de.breigindustries.cs.commands.SlashCommandHandler;
import de.breigindustries.cs.database.DatabaseManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Levi extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Levi.class);
    private static final Dotenv dotenv = Dotenv.configure().load();

    private static JDA jda;
    public static JDA getJDA() { return jda; }
    public static User getSelfUser() { return jda.getSelfUser(); }
    public static long getIdLong() { return jda.getSelfUser().getIdLong(); }

    public static void main(String[] args) throws Exception {

        // Set up database
        DatabaseManager.ensureDatabaseExists();

        // Start and connect to discord
        String token = dotenv.get("DISCORD_TOKEN");
        logger.info("About to build JDA...");
        jda = JDABuilder.createDefault(token)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(new Levi(), new SlashCommandHandler())
            .build();
        logger.info("JDA build successfully");
        jda.awaitReady();

        System.out.println("Logged on as " + jda.getSelfUser().getName());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw().toLowerCase();
        System.out.println("Message from " + event.getAuthor().getName() + ": " + content);

        if (shouldReply(event)) {
            AIInteraction.converse(event);
        }
    }

    /**
     * Determines if Levi should reply to the text.
     * @param event The MessageReceivedEvent
     */
    public static boolean shouldReply(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        Conversation conversation = Conversation.getConversationByMessageReceivedEvent(event);
        
        // Always reply in DMs
        if (event.getChannelType() == ChannelType.PRIVATE) return true;

        // Don't reply in channels other than private and text (also relevant for Conversation constructor)
        if (event.getChannelType() != ChannelType.TEXT) return false;
        
        // Reset conversation on command
        int greetingScope = Math.min(message.length(), 25);
        String beginningOfMessage = message.toLowerCase().substring(0, greetingScope);
        if (beginningOfMessage.contains("exit")) {
            conversation.resetLastMsgTimer();
            return false;
        }

        // Reply if the conversation is young (< 15 mins)
        long now = System.currentTimeMillis();
        long then = conversation.getLastMsgTimestamp();
        if (now - then > 900_000) return true;

        // Reply if the message is addressed to him
        if (beginningOfMessage.contains("levi")) return true;

        return false;
    }

    public static CompletableFuture<Message> writeMessage(MessageChannelUnion channel, String message) {
        CompletableFuture<Message> future = new CompletableFuture<>();
        
        channel.sendTyping().queue();
        try {
            Thread.sleep(message.length() * 10);        
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        channel.sendMessage(message).queue(sentMessage -> {
            future.complete(sentMessage);
            System.out.println(message);
        }, future::completeExceptionally);

        return future;
    }
}