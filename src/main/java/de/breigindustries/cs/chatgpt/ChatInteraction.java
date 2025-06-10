package de.breigindustries.cs.chatgpt;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatInteraction {

    private static final OkHttpClient client = new OkHttpClient();

    public static String getChatGPTResponse(Conversation convo) {
        try {
            Request request = ChatGPTUtils.wrapToRequest(convo);

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.println(response.toString());
                    return "Error: Levi is tired right now and can only think of catnip..";
                }
                JSONObject jsonResponse = new JSONObject(response.body().string());
                return jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Levi is tired right now and can only think of catnip...";
        }
    }
}
