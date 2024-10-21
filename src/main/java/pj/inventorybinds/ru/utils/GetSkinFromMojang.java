package pj.inventorybinds.ru.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Optional;

public class GetSkinFromMojang extends GetSkinDecorator {

    public GetSkinFromMojang(GetSkinDecorator getSkinDecorator) {
        super(getSkinDecorator);
    }

    public GetSkinFromMojang() {
        super(null);
    }

    @Override
    public Optional<BufferedImage> getSkin(String playerName) throws IOException {
        String stringUuid = FzmmUtils.fetchPlayerUuid(playerName);
        try (var httpClient = FzmmUtils.getHttpClient()) {
            HttpGet httpGet = new HttpGet("https://sessionserver.mojang.com/session/minecraft/profile/" + stringUuid);

            httpGet.addHeader("content-statusType", "image/jpeg");

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            if ((response.getStatusLine().getStatusCode() / 100) != 2) {
                return super.getSkin(playerName);
            }

            InputStream inputStream = resEntity.getContent();
            JsonObject obj = (JsonObject) JsonParser.parseReader(new InputStreamReader(inputStream));
            JsonObject properties = (JsonObject) obj.getAsJsonArray("properties").get(0);

            String valueJsonStr = new String(Base64.getDecoder().decode(properties.get("value").getAsString()));
            obj = (JsonObject) JsonParser.parseString(valueJsonStr);
            String skinUrl = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();

            return ImageUtils.getImageFromUrl(skinUrl);
        }
    }


}
