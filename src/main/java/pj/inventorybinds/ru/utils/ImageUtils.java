package pj.inventorybinds.ru.utils;

import com.google.gson.JsonIOException;
import net.minecraft.client.texture.NativeImage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static pj.inventorybinds.ru.config.SimpleConfig.LOGGER;

public class ImageUtils {

    public static BufferedImage getBufferedImgFromNativeImg(NativeImage nativeImage) {
        int width = nativeImage.getWidth();
        int height = nativeImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = nativeImage.getColor(x, y);//ABGR

                bufferedImage.setRGB(x, y, ((color >> 16) & 0xFF) | ((color & 0xFF) << 16) | (color & 0xFF00FF00));//ARGB
            }
        }

        return bufferedImage;
    }


    public static Optional<BufferedImage> getPlayerSkin(String name, GetSkinDecorator getSkinDecorator) throws NullPointerException, JsonIOException, IOException {
        Optional<BufferedImage> skin = getSkinDecorator.getSkin(name);

        if (skin.isEmpty()) {
            LOGGER.warn("[ImageUtils] skin of '{}' was not found", name);
        }

        return skin;
    }

    public static Optional<BufferedImage> getImageFromUrl(String urlLocation) throws IOException {
        try (var httpClient = FzmmUtils.getHttpClient()) {
            HttpGet httpGet = new HttpGet(urlLocation);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                try (InputStream inputStream = resEntity.getContent()) {
                    BufferedImage image = ImageIO.read(inputStream);
                    return Optional.ofNullable(image);
                }
            }
        }
        return Optional.empty();
    }

    public static NativeImage toNativeImage(BufferedImage image) {

        // """NativeImage.Format.RGBA""" = ABGR
        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, image.getWidth(), image.getHeight(), false);
        ColorModel colorModel = image.getColorModel();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                // avoid using image.getRGB(x, y) because it will result in packing
                // in ARGB format, and then it would have to unpack it to repack
                // it in ABGR format. By avoiding this, it can directly pack in ABGR format
                Object elements = image.getRaster().getDataElements(x, y, null);

                int abgr = (colorModel.getAlpha(elements) << 24) |
                        (colorModel.getBlue(elements) << 16) |
                        (colorModel.getGreen(elements) << 8) |
                        colorModel.getRed(elements);

                nativeImage.setColor(x, y, abgr);
            }
        }
        return nativeImage;
    }

    public static BufferedImage withType(BufferedImage image, int type) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), type);

        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        image.flush();
        return newImage;
    }

    public static boolean isEquals(BufferedImage image1, BufferedImage image2) {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight())
            return false;

        for (int y = 0; y < image1.getHeight(); y++) {
            for (int x = 0; x < image1.getWidth(); x++) {
                if (image1.getRGB(x, y) != image2.getRGB(x, y))
                    return false;
            }
        }
        return true;
    }



}
