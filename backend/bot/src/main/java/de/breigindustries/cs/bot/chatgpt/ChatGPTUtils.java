package de.breigindustries.cs.chatgpt;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.breigindustries.cs.Levi;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPTUtils {

    private static final Logger logger = LoggerFactory.getLogger(ChatGPTUtils.class);
    private static final String OPENAI_KEY = Dotenv.configure().load().get("OPENAI_KEY");
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final OkHttpClient client = new OkHttpClient();

    public static String getChatbotResponse(Conversation conversation) {
        Request request = createHTTPRequest(conversation);
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println(response.toString());
                return "Error: Levi is tired right now and can only think of catnip..";
            }
            JSONObject jsonResponse = new JSONObject(response.body().string());
            String stringResponse = jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim();
                
            logger.info("Got response from model: {}", stringResponse);
            return stringResponse;
        } catch (Exception e) {
            logger.error("Could not get response from OpenAI: {}", e.getMessage());
            return "Error: Levi is tired right now and can only think of catnip...";
        }
    }

    public static Request createHTTPRequest(Conversation conversation) {
        JSONObject conversationJSON = convertConversationToJSON(conversation);
        String jsonString = conversationJSON.toString();
        RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(OPENAI_API_URL)
            // Potentially add a token limit
            .addHeader("Authorization", "Bearer " + OPENAI_KEY)
            .post(body)
            .build();
        return request;
    }

    public static JSONObject convertConversationToJSON(Conversation conversation) {
        JSONObject[] entryJSONS = new JSONObject[conversation.size() + 1]; // Reserve space for the conditioning message
        entryJSONS[0] = Conversation.getTrainingMessageJSON(null);
        for (int i = 1; i < entryJSONS.length; i++) {
            ConversationEntry entry = conversation.getMessages().get(i-1);
            entryJSONS[i] = convertEntryToJSON(entry, !conversation.isPrivate());
        }

        // Assemble conversation and add metadata
        JSONObject conversationJSON = new JSONObject();
        conversationJSON.put("model", "gpt-4o-mini");
        conversationJSON.put("messages", entryJSONS);
        conversationJSON.put("temperature", 0.7);
        return conversationJSON;
    }

    private static JSONObject convertEntryToJSON(ConversationEntry entry, boolean formatForGroupChat) {
        // Determine role
        String role;
        if (entry.getUserId() == Levi.getIdLong()) {
            role = "assistant";
        } else {
            role = "user";
        }

        // Formatting content?
        String content = formatForGroupChat ? entry.getGroupchatContent() : entry.getContent();
        
        // Creating JSON
        return new JSONObject().put("role", role).put("content", content);
    }

    public static void listModels() {
        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/models")
            // Potentially add a token limit
            .addHeader("Authorization", "Bearer " + OPENAI_KEY)
            .build();

        OkHttpClient client = new OkHttpClient();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Levi is tired right now and can only think of catnip...");
        }
    }

}
