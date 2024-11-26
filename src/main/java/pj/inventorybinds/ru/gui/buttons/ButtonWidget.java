package pj.inventorybinds.ru.gui.buttons;


import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pj.inventorybinds.ru.InventoryBinds;
import pj.inventorybinds.ru.config.ButtonsConfig;
import pj.inventorybinds.ru.config.buttons.ButtonJson;
import pj.inventorybinds.ru.gui.ModEditBindScreen;
import pj.inventorybinds.ru.gui.screen.PJScreen;
import pj.inventorybinds.ru.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;

import static pj.inventorybinds.ru.InventoryBinds.MOD_ID;


@Environment(value = EnvType.CLIENT)
public class ButtonWidget extends TexturedButtonWidget {

    private static ArrayList<DynamicTextureManager> dynamicTextures = new ArrayList<>();



    private static final Identifier TEXTURE_MAIN = Identifier.of(MOD_ID, "textures/gui/button_empty.png");
    private static final Identifier TEXTURE_SETTINGS = Identifier.of(MOD_ID, "textures/gui/button_settings.png");
    private final String description;
    private final int line;
    private final int row;
    private final HandledScreen<?> screen;
    private final String itemID;
    private final int buttonId;
    private final boolean setting;


    private PressAction onPress;

    public ButtonWidget(HandledScreen<?> screen, int buttonIndex, int row, String description, String itemID, PressAction pressAction, int buttonId) {
        super(0, 0, 20, 20, new ButtonTextures(TEXTURE_MAIN, TEXTURE_MAIN), pressAction);
        this.line = buttonIndex;
        this.description = description;
        this.screen = screen;
        this.itemID = itemID;
        this.row = row;
        this.buttonId = buttonId;
        this.setting = false;
        this.setX(getX());
        this.setY(getY());


    }

    public ButtonWidget(HandledScreen<?> screen, int buttonIndex, int row, String description, String itemID, PressAction pressAction, boolean settings, int buttonId) {
        super(0, 0, 20, 20, new ButtonTextures(TEXTURE_SETTINGS, TEXTURE_SETTINGS), pressAction);
        this.line = buttonIndex;
        this.description = description;
        this.screen = screen;
        this.itemID = itemID;
        this.row = row;
        this.buttonId = buttonId;
        this.setting = settings;
        this.setX(getX());
        this.setY(getY());


    }


    public int getX() {

        PressAction pressAction = button -> {
        };

        if (!(screen instanceof RecipeBookProvider)) {
            InventoryBinds.setRecipeBookIsOpen(false);
        }

        int invOffset = 1;

        int x = (MinecraftClient.getInstance().getWindow().getScaledWidth() + getBackgroundWidth()) / 2;
        x += invOffset;

        if ((screen instanceof MerchantScreen)) {
            x += 51;
        }

        if ((screen instanceof CreativeInventoryScreen)) {
            x += 10;
        }

        if (InventoryBinds.getRecipeBookIsOpen()) {
            x += 77;
        }

        if (this.row > 0) {
            x += (22 * this.row);
        }


        return x;
    }

    private int getBackgroundWidth() {
        return 176;
    }


    private int getBackgroundHeight() {
        return InventoryBinds.getScreenBackgroundHeight();
    }

    public int getY() {

        int invOffset = this.line * 22;
        int y = (MinecraftClient.getInstance().getWindow().getScaledHeight() - getBackgroundHeight()) / 2;
        return y + invOffset;
    }


    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.reposition();
        boolean bl = this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
        if (this.visible) {
            this.renderButton(context, mouseX, mouseY, delta);
        }

        if (this.isMouseOver(mouseX, mouseY)) {
            int offset = 0;
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, Text.literal(this.description), mouseX - offset, mouseY);

            //  this.screen.(matrices, Text.literal(this.description), mouseX - offset, mouseY);
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(button)) {
                boolean bl = this.clicked(mouseX, mouseY);
                if (bl) {
                    this.playDownSound(MinecraftClient.getInstance().getSoundManager());
                    this.onClick(mouseX, mouseY);
                    return true;
                }
            } else {
                boolean bl = this.clicked(mouseX, mouseY);
                if (bl) {

                    if (button == 1) {
                        if (!setting) {

                            if (ButtonsConfig.getButtonsList().getBindEditorEnabled()) {

                                ButtonJson buttonJson = ButtonsConfig.getButtonsList().getButtons().get(this.buttonId);

                                MinecraftClient.getInstance().setScreen(new PJScreen(new ModEditBindScreen(buttonJson)));

                            }

                        }

                    }


                }
            }

            return false;
        } else {
            return false;
        }
    }


    public void reposition() {
        if (MinecraftClient.getInstance().player != null) {
            this.isMouseOver(this.getX(), this.getY());
        }
    }


    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        int offset = 0;
        if (this.isHovered()) {
            offset = 20;
        }
        RenderSystem.enableDepthTest();

        if (!setting) {
            this.drawTexture(context, TEXTURE_MAIN, this.getX(), this.getY(), 0, 0, offset, this.width, this.height, 20, 40);
        } else {
            this.drawTexture(context, TEXTURE_SETTINGS, this.getX(), this.getY(), 0, 0, offset, this.width, this.height, 20, 40);
        }

        if (itemID.contains("https")) {
            try {

                String hash = Base64.getEncoder().encodeToString(itemID.getBytes()).toLowerCase().replaceAll("=","");

                Identifier urlTextureID;

                if(!DynamicTextureManager.checkHash(hash)){


                    Optional<BufferedImage> bufferedImage = ImageUtils.getImageFromUrl(itemID);

                    bufferedImage.ifPresent(image -> {

                        NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(ImageUtils.toNativeImage(image));

                        Identifier loadedUrlTextureID = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(hash, nativeImageBackedTexture);

                        DynamicTextureManager.addDynamicTexture(hash, loadedUrlTextureID);

                    });

                    if(DynamicTextureManager.checkHash(hash)){
                        urlTextureID = DynamicTextureManager.getTextureByHash(hash);
                    } else {
                        urlTextureID = new Identifier(MOD_ID, "textures/gui/missing_item.png");
                        DynamicTextureManager.addDynamicTexture(hash, urlTextureID);
                    }



                } else {


                    urlTextureID = DynamicTextureManager.getTextureByHash(hash);

                }





                this.drawTexture(context, urlTextureID, this.getX()+2, this.getY()+2, 0, 0, 0, 16, 16, 16, 16);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            if(getItemStack(this.itemID).getItem() != null) {

                context.drawItem(getItemStack(this.itemID), this.getX() + 2, this.getY() + 2);

            }else {

                this.drawTexture(context, new Identifier(MOD_ID, "textures/gui/missing_item.png"), this.getX()+2, this.getY()+2, 0, 0, 0, 16, 16, 16, 16);

            }
        }

    }

    public void drawTexture(DrawContext context, Identifier texture, int x, int y, int u, int v, int hoveredVOffset, int width, int height, int textureWidth, int textureHeight) {
        int i = v;
        if (!this.isNarratable()) {
            i = v + hoveredVOffset * 2;
        } else if (this.isSelected()) {
            i = v + hoveredVOffset;
        }

        RenderSystem.enableDepthTest();
        context.drawTexture(texture, x, y, (float) u, (float) i, width, height, textureWidth, textureHeight);
    }


    private static ItemStack getItemStack(String itemID) {
        ItemStack itemStack = new ItemStack(Registries.ITEM.get(Identifier.tryParse(itemID)));
        return itemStack;
    }




}

