package pj.inventorybinds.ru.utils;

import com.google.gson.JsonIOException;
import net.minecraft.client.texture.NativeImage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static pj.inventorybinds.ru.config.SimpleConfig.LOGGER;

public class ImageUtils {





    public static Optional<BufferedImage> getImageFromUrl(String urlLocation) throws IOException {
        try (var httpClient = getHttpClient()) {
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
        
        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, image.getWidth(), image.getHeight(), false);
        ColorModel colorModel = image.getColorModel();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                Object elements = image.getRaster().getDataElements(x, y, null);


                nativeImage.setColorArgb(x, y, colorModel.getRGB(elements));
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

    public static CloseableHttpClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setSocketTimeout(3000)
                .build();

        return HttpClients.custom()
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .disableAutomaticRetries()
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent("INVBIND/1.0")
                .build();
    }

}
