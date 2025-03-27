package pj.inventorybinds.ru.gui.buttons;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.function.Supplier;

import static pj.inventorybinds.ru.InventoryBinds.MOD_ID;

public class DynamicTextureManager {

    private static final HashMap<String, Identifier> stringIdentifierHashMap = new HashMap<>();


    public static void loadDynamicTextures(String url) {
        try {
            loadTextureFromUrl(url);
        } catch (Exception ignored) {

        }
    }


    public static void addDynamicTexture(String hash, Identifier identifier) {

        if (!checkTextureByHash(hash)) {
            stringIdentifierHashMap.put(hash, identifier);
        }

    }


    public static boolean checkTextureByURL(String url) {
        return stringIdentifierHashMap.containsKey(createSHA256HashFromUrl(url));
    }

    public static boolean checkTextureByHash(String hash) {

        return stringIdentifierHashMap.containsKey(hash);
    }

    public static Identifier getTextureByUrl(String url) {

         String hash = createSHA256HashFromUrl(url);

        if (checkTextureByHash(hash)) {
            return stringIdentifierHashMap.get(hash);
        } else {
            return Identifier.of(MOD_ID, "textures/gui/missing_item.png");
        }


    }

    public static boolean isImageUrl(String urlString) {
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("HEAD");

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            String contentType = connection.getContentType();

            connection.disconnect();

            return contentType != null && contentType.toLowerCase().startsWith("image/");

        } catch (IOException e) {
            return false;
        }
    }


    private static void loadTextureFromUrl(String imageUrl) throws IOException {

        if(!isImageUrl(imageUrl)) {
            DynamicTextureManager.addDynamicTexture(createSHA256HashFromUrl(imageUrl), Identifier.of(MOD_ID, "textures/gui/missing_item.png"));
        } else {


            URL url = new URL(imageUrl);
            BufferedImage bufferedImage = ImageIO.read(url);

            NativeImage nativeImage = bufferedImageToNativeImage(bufferedImage);

            TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
            Supplier<String> supplier = null;
            textureManager.registerTexture(Identifier.of(MOD_ID, createSHA256HashFromUrl(imageUrl)), new NativeImageBackedTexture(supplier,  nativeImage));

            DynamicTextureManager.addDynamicTexture(createSHA256HashFromUrl(imageUrl), Identifier.of(MOD_ID, createSHA256HashFromUrl(imageUrl)));

        }

    }

    private static NativeImage bufferedImageToNativeImage(BufferedImage bufferedImage) {
        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, bufferedImage.getWidth(), bufferedImage.getHeight(), false);
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int color = bufferedImage.getRGB(x, y);
                nativeImage.setColorArgb(x, y, color);
            }
        }
        return nativeImage;
    }

    public static String createSHA256HashFromUrl(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] messageDigest = md.digest(url.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Алгоритм SHA-256 не найден: " + e.getMessage());
        }
    }


}
