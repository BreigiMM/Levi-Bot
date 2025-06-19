package de.breigindustries.cs.bot;

import java.awt.Color;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.breigindustries.cs.bot.chatgpt.AIInteraction;
import de.breigindustries.cs.bot.chatgpt.Conversation;
import de.breigindustries.cs.bot.commands.ButtonHandler;
import de.breigindustries.cs.bot.commands.SlashCommandHandler;
import de.breigindustries.cs.bot.database.DatabaseManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
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

        // Create downloads directory if it doesn't exist already
        File file = new File("downloads");
        if (!file.exists()) {
            file.mkdirs();
        }

        // Start and connect to discord
        String token = dotenv.get("DISCORD_TOKEN");
        logger.info("About to build JDA...");
        jda = JDABuilder.createDefault(token)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(new Levi(), new SlashCommandHandler(), new ButtonHandler())
            .build();
        logger.info("JDA built successfully");
        jda.awaitReady();

        logger.info("Logged on as " + jda.getSelfUser().getEffectiveName());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentDisplay();
        logger.info("Received a message from " + event.getAuthor().getEffectiveName() + ": {}", content);

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
        Conversation conversation = Conversation.getConversationByChannel(event.getChannel());
        
        // Always reply in DMs
        if (event.getChannelType() == ChannelType.PRIVATE) {
            return true;
        }

        // Don't reply in channels other than private and text (also relevant for Conversation constructor)
        if (event.getChannelType() != ChannelType.TEXT) {
            return false;
        }
        
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
        if (now - then < 900_000) {
            return true;
        }

        // Reply if the message is addressed to him
        if (beginningOfMessage.contains("levi")) {
            return true;
        }

        return false;
    }

    public static List<CompletableFuture<Message>> writeMessage(MessageChannelUnion channel, String message) {
        List<CompletableFuture<Message>> futures = new ArrayList<>();

        // Render Latex and split into multiple messages
        Pattern latexBlock = Pattern.compile("\\$\\$(.*?)\\$\\$", Pattern.DOTALL);
        Matcher matcher = latexBlock.matcher(message);

        int lastEnd = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            // Send text before this LaTeX block
            if (start > lastEnd) {
                String textPart = message.substring(lastEnd, start).trim();
                if (!textPart.isEmpty()) {
                    futures.add(writeRawMessage(channel, textPart));
                }
            }

            // Send the LaTeX as an embed
            String latex = matcher.group(1);
            String encoded = URLEncoder.encode(latex, StandardCharsets.UTF_8).replace("+", "%20");
            String url = "https://latex.codecogs.com/png.image?\\dpi{300}%20\\bg_white%20\\large%20\\inline%20\\displaystyle" + encoded;
            System.out.println(url);

            EmbedBuilder embed = new EmbedBuilder()
                .setImage(url)
                .setColor(Color.RED);

            CompletableFuture<Message> future = new CompletableFuture<>();
            channel.sendMessageEmbeds(embed.build()).queue(sentEmbed -> {
                future.complete(sentEmbed);
                logger.debug("Sent an embed in chat: {}", sentEmbed.getContentDisplay());
            }, throwable -> {
                future.completeExceptionally(throwable);
                logger.error("Failed to write message in chat: {}", throwable.getMessage());
            });
            futures.add(future);

            lastEnd = end;
        }

        if (lastEnd < message.length()) {
            String remaining = message.substring(lastEnd).trim();
            if (!remaining.isEmpty()) {
                futures.add(writeRawMessage(channel, remaining));
            }
        }

        return futures;
    }

    private static CompletableFuture<Message> writeRawMessage(MessageChannelUnion channel, String message) {
        CompletableFuture<Message> future = new CompletableFuture<>();

        channel.sendTyping().queue();
        try {
            Thread.sleep(message.length() * 10);        
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        channel.sendMessage(message).queue(sentMessage -> {
            logger.debug("Wrote message in chat: {}", sentMessage.getContentDisplay());
            future.complete(sentMessage);
        }, throwable -> {
            logger.error("Failed to write message in chat: {}", throwable.getMessage());
            future.completeExceptionally(new RuntimeException("Message could not send!"));
        });

        return future;
    }
}