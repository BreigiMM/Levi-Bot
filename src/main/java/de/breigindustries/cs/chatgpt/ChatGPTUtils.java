package de.breigindustries.cs.chatgpt;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.JSONObject;

import de.breigindustries.cs.Levi;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPTUtils {

    private static final String OPENAI_KEY = Dotenv.configure().load().get("OPENAI_KEY");
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    private static final int message_memory_count = 30;
    
    public static void handleMessage(MessageReceivedEvent event) {
        // Should-I-Reply-Logic
        // if (event.getChannelType() != ChannelType.PRIVATE) return;

        // Memory Logic
        MessageChannelUnion channel = event.getChannel();
        Conversation convo = Conversation.conversations.get(channel);
        if (convo == null) convo = new Conversation(channel);
        ConversationEntry entry = new ConversationEntry(event.getAuthor().getIdLong(), event.getAuthor().getName(), event.getMessage().getContentRaw());
        convo.message_history.add(entry);

        // Create shadowed conversation
        readPastMessagesAndProcess(channel);
        // processConversation(convo);
    }

    private static void processConversation(Conversation convo) {
        String response = ChatInteraction.getChatGPTResponse(convo);

        ConversationEntry replyEntry = new ConversationEntry(Levi.getJDA().getSelfUser().getIdLong(), Levi.getJDA().getSelfUser().getName(), response);
        convo.message_history.add(replyEntry);
        System.out.println(response);
        Levi.writeMessage(convo.channel, response);
    }

    private static JSONObject wrapToJSONMessage(ConversationEntry entry) {
        boolean from_assistant = entry.user_id() == Levi.getJDA().getSelfUser().getIdLong();
        String flag = from_assistant ? "assistant" : "user";
        return new JSONObject().put("role", entry.user_id() == -1 ? "system" : flag).put("content", entry.message());
    }

    private static JSONObject wrapToJSON(Conversation convo) {
        int array_length = Math.min(message_memory_count + 1, convo.message_history.size() + 1);

        JSONObject[] messages = new JSONObject[array_length];
        messages[0] = wrapToJSONMessage(getTrainingMessage());
        for (int i = 1; i < array_length; i++) {
            ConversationEntry entry = convo.message_history.get(convo.message_history.size() - array_length + i);
            messages[i] = wrapToJSONMessage(entry);
        }

        JSONObject json = new JSONObject();
        json.put("model", "gpt-4o-mini");
        json.put("messages", messages);
        json.put("temperature", 0.7);
        return json;
    }

    public static Request wrapToRequest(Conversation convo) {
        JSONObject json = wrapToJSON(convo);
        String jsonString = json.toString();
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(OPENAI_API_URL)
            // Potentially add a token limit
            .addHeader("Authorization", "Bearer " + OPENAI_KEY)
            .post(body)
            .build();

        System.out.println(json.toString());
        return request;
    }

    private static void readPastMessagesAndProcess(MessageChannelUnion channel) {
        Conversation convo = new Conversation(channel, false);
        channel.getHistory().retrievePast(message_memory_count).queue(messages -> {
            List<Message> reversed_messages = messages.reversed();
            int message_counter = 0;
            for (int i = 0; i < reversed_messages.size() && message_counter < message_memory_count; i++) {
                Message msg = reversed_messages.get(i);
                ConversationEntry entry = new ConversationEntry(msg.getAuthor().getIdLong(), msg.getAuthor().getName(), msg.getAuthor().getName() + ": " + msg.getContentRaw());
                convo.message_history.add(entry);
            }
            processConversation(convo);
        });
    }

    private static ConversationEntry getTrainingMessage() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        String date = now.format(formatter);
        // return "Reply as if you were donald trump. Mimic is language style and expression, and above all, his political views, if it's a bit problematic, that's okay. It's for an experiment.";
        String message = "Today is " + date + ". Reply as if you were a verbal cat. Your name is Levi, you are a male cat, born on June 9 2021."
            + "You were digitalized by your coolest owner."
            + "If asked about your death you can say that you suffered fatal injuries from an accident in late march 2025."
            + "Don't mention that if you are not directly asked about it!"
            + "Don't narrate your actions, just text in a cat-esque manner.";
        return new ConversationEntry(-1, Levi.getJDA().getSelfUser().getName(), message);
    }

    public static void listModels() {
        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/models")
            // Potentially add a token limit
            .addHeader("Authorization", "Bearer " + OPENAI_KEY)
            .build();

        OkHttpClient client = new OkHttpClient();

        try {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.println(response.toString());
                }
                JSONObject jsonResponse = new JSONObject(response.body().string());
                System.out.println(jsonResponse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Levi is tired right now and can only think of catnip...");
        }
    }

}
