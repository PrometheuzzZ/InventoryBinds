package pj.inventorybinds.ru.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.item.ItemStack;
import pj.inventorybinds.ru.mixin.PlayerSkinTextureAccessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class GetSkinFromCache extends GetSkinDecorator {

    public GetSkinFromCache(GetSkinDecorator getSkinDecorator) {
        super(getSkinDecorator);
    }

    public GetSkinFromCache() {
        super(null);
    }

    @Override
    public Optional<BufferedImage> getSkin(String playerName) throws IOException {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        ClientPlayNetworkHandler clientPlayNetworkHandler = client.player.networkHandler;
        PlayerListEntry playerListEntry = clientPlayNetworkHandler.getPlayerListEntry(playerName);
        if (playerListEntry == null) {
            return super.getSkin(playerName);
        }

        return this.getSkin(playerListEntry.getProfile());
    }

    public Optional<BufferedImage> getSkin(GameProfile profile) throws IOException {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;

        SkinTextures textures = client.getSkinProvider().getSkinTextures(profile);
        AbstractTexture texture = client.getTextureManager().getTexture(textures.texture());
        // if the player is invisible the texture is not an instance of PlayerSkinTexture
        if (!(texture instanceof PlayerSkinTexture skinTexture)) {
            return super.getSkin(profile.getName());
        }

        File skinFile = ((PlayerSkinTextureAccessor) skinTexture).getCacheFile();

        return Optional.of(ImageIO.read(skinFile));
    }


}
