package de.breigindustries.cs;

import de.breigindustries.cs.chatgpt.ChatGPTUtils;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Levi extends ListenerAdapter {

    private static JDA jda;
    public static JDA getJDA() { return jda; }

    public static void main(String[] args) throws Exception {
        String token = Dotenv.configure().load().get("DISCORD_TOKEN");
        jda = JDABuilder.createDefault(token)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(new Levi())
            .build();
        jda.awaitReady();
        System.out.println("Logged on as " + jda.getSelfUser().getName());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw().toLowerCase();
        System.out.println("Message from " + event.getAuthor().getName() + ": " + content);

        if (content.equals("hey")) {
            writeMessage(event.getChannel(), "Hello " + event.getAuthor().getName());
        } else if (content.matches("hello there!?")) {
            writeMessage(event.getChannel(), "General Kenobi!");
        } else {
            ChatGPTUtils.handleMessage(event);
        }
    }

    public static void writeMessage(MessageChannelUnion channel, String message) {
        channel.sendTyping().queue();
        try {
            Thread.sleep(message.length() * 10);        
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        channel.sendMessage(message).queue();
    };
}