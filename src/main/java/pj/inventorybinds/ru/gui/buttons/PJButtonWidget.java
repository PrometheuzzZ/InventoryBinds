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
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pj.inventorybinds.ru.InventoryBinds;
import pj.inventorybinds.ru.config.ButtonsConfig;
import pj.inventorybinds.ru.config.buttons.ButtonJson;
import pj.inventorybinds.ru.gui.ModEditBindScreen;
import pj.inventorybinds.ru.gui.screen.PJScreen;

import java.util.*;


import static pj.inventorybinds.ru.InventoryBinds.MOD_ID;
import static pj.inventorybinds.ru.InventoryBinds.isShiftDown;
import static pj.inventorybinds.ru.config.ButtonsConfig.swapButtonById;
import static pj.inventorybinds.ru.gui.buttons.DynamicTextureManager.loadDynamicTextures;


@Environment(value = EnvType.CLIENT)
public class PJButtonWidget extends TexturedButtonWidget {


    private static final Identifier TEXTURE_MAIN = Identifier.of(MOD_ID, "textures/gui/button_empty.png");
    private static final Identifier TEXTURE_SETTINGS = Identifier.of(MOD_ID, "textures/gui/button_settings.png");
    private static final Identifier TEXTURE_SWAP = Identifier.of(MOD_ID, "textures/gui/keys.png");
    private final List<Text> description;
    private final int line;
    private final int row;
    private final HandledScreen<?> screen;
    private final String itemID;
    private final int buttonId;
    private final boolean setting;


    public PJButtonWidget(HandledScreen<?> screen, int buttonIndex, int row, List<Text> description, String itemID, PressAction pressAction, int buttonId) {
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

    public PJButtonWidget(HandledScreen<?> screen, int buttonIndex, int row, List<Text> description, String itemID, PressAction pressAction, boolean settings, int buttonId) {
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

            if (!setting) {
                if (isShiftDown) {

                    List<Text> swapList = new ArrayList<>();
                    swapList.add(Text.literal("ㄷ"));
                    swapList.add(Text.literal(" "));
                    swapList.add(Text.literal(" "));
                    swapList.add(Text.literal(" "));

                    context.drawTooltip(MinecraftClient.getInstance().textRenderer, swapList, mouseX - offset, mouseY);


                } else {
                    context.drawTooltip(MinecraftClient.getInstance().textRenderer, this.description, mouseX - offset, mouseY);
                }
            } else {
                context.drawTooltip(MinecraftClient.getInstance().textRenderer, this.description, mouseX - offset, mouseY);
            }
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        try {
            if (this.active && this.visible) {
                if (this.isValidClickButton(button)) {
                    boolean bl = this.isMouseOver(mouseX, mouseY);
                    if (bl) {

                        if (isShiftDown) {
                            swapButtonById(this.buttonId, this.buttonId - 1, mouseX, mouseY);
                        } else {
                            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
                            this.onClick(mouseX, mouseY);

                            return true;
                        }

                    }
                } else {
                    boolean bl = this.isMouseOver(mouseX, mouseY);
                    if (bl) {
                        if (button == 1) {

                            if (!setting) {
                                if (isShiftDown) {
                                    swapButtonById(this.buttonId, this.buttonId + 1, mouseX, mouseY);
                                } else if (ButtonsConfig.getButtonsList().getBindEditorEnabled()) {
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
        } catch (Exception e) {
            e.printStackTrace();
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
       // RenderSystem.enableDepthTest();

        if (!setting) {
            this.drawTexture(context, TEXTURE_MAIN, this.getX(), this.getY(), 0, 0, offset, this.width, this.height, 20, 40);
        } else {
            this.drawTexture(context, TEXTURE_SETTINGS, this.getX(), this.getY(), 0, 0, offset, this.width, this.height, 20, 40);
        }

        if (this.itemID.contains("https")) {

            if (itemID.contains(".webp")) {
                this.drawTexture(context, Identifier.of(MOD_ID, "textures/gui/missing_item.png"), this.getX() + 2, this.getY() + 2, 0, 0, 0, 16, 16, 16, 16);
            } else {

                String url = this.itemID;

                boolean loaded = DynamicTextureManager.checkTextureByURL(url);

                Identifier icon;

                if (loaded) {

                    icon = DynamicTextureManager.getTextureByUrl(url);
                    this.drawTexture(context, icon, this.getX() + 2, this.getY() + 2, 0, 0, 0, 16, 16, 16, 16);

                } else {
                    loadDynamicTextures(url);
                }
            }

        } else if (getItemStack(this.itemID).getItem() != null) {

            context.drawItem(getItemStack(this.itemID), this.getX() + 2, this.getY() + 2);

        } else {

            this.drawTexture(context, Identifier.of(MOD_ID, "textures/gui/missing_item.png"), this.getX() + 2, this.getY() + 2, 0, 0, 0, 16, 16, 16, 16);
        }


    }

    public void drawTexture(DrawContext context, Identifier texture, int x, int y, int u, int v, int hoveredVOffset, int width, int height, int textureWidth, int textureHeight) {
        int i = v;
        if (!this.isNarratable()) {
            i = v + hoveredVOffset * 2;
        } else if (this.isSelected()) {
            i = v + hoveredVOffset;
        }

        //RenderSystem.enableDepthTest();
        context.drawTexture(RenderLayer::getGuiTextured, texture, x, y, (float) u, (float) i, width, height, textureWidth, textureHeight);

    }


    private static ItemStack getItemStack(String itemID) {
        ItemStack itemStack = new ItemStack(Registries.ITEM.get(Identifier.tryParse(itemID)));
        return itemStack;
    }


}

