package pj.inventorybinds.ru.utils;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class FzmmUtils {

    public static final SuggestionProvider<FabricClientCommandSource> SUGGESTION_PLAYER = (context, builder) -> {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        String playerInput = builder.getRemainingLowerCase();
        if (clientPlayer != null) {
            List<String> playerNamesList = clientPlayer.networkHandler.getPlayerList().stream()
                    .map(PlayerListEntry::getProfile)
                    .map(GameProfile::getName)
                    .toList();

            for (String playerName : playerNamesList) {
                if (playerName.toLowerCase().contains(playerInput))
                    builder.suggest(playerName);
            }
        }

        return CompletableFuture.completedFuture(builder.build());

    };


    public static String fetchPlayerUuid(String name) throws IOException, JsonIOException {
        try (var httpClient = getHttpClient()) {
            HttpGet httpGet = new HttpGet("https://api.mojang.com/users/profiles/minecraft/" + name);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            if (((response.getStatusLine().getStatusCode() / 100) != 2) || resEntity == null) {
                return "";
            }

            InputStream inputStream = resEntity.getContent();
            JsonObject obj = (JsonObject) JsonParser.parseReader(new InputStreamReader(inputStream));
            return obj.get("id").getAsString();
        }
    }


    public static CloseableHttpClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setSocketTimeout(3000)
                .build();

        return HttpClients.custom()
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .disableAutomaticRetries()
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent("FZMM/1.0")
                .build();
    }
}