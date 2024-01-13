package pj.inventorybinds.ru.gui.buttons;


import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pj.inventorybinds.ru.InventoryBinds;

import static pj.inventorybinds.ru.InventoryBinds.MOD_ID;


@Environment(value= EnvType.CLIENT)
public class ButtonWidget extends TexturedButtonWidget {
    private static final Identifier TEXTURE_MAIN = new Identifier(MOD_ID,"textures/gui/button_empty.png");
    private final String description;
    private final int line;
    private final int row;
    private final HandledScreen<?> screen;
    private final Item itemIco;

    private PressAction onPress;

    public ButtonWidget(HandledScreen<?> screen, int buttonIndex, int row, String description, Item items, PressAction pressAction) {
        super(0, 0, 20, 20, new ButtonTextures(TEXTURE_MAIN, TEXTURE_MAIN), pressAction);
        this.line = buttonIndex;
        this.description = description;
        this.screen = screen;
        this.itemIco = items;
        this.row = row;
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
            x+=51;
        }

        if ( InventoryBinds.getRecipeBookIsOpen()) {
            x += 77;
        }

        if (this.row == 1) {
            x += 22;
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
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, Text.literal(this.description),mouseX - offset, mouseY);
            //  this.screen.(matrices, Text.literal(this.description), mouseX - offset, mouseY);
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

        this.drawTexture(context, TEXTURE_MAIN, this.getX(), this.getY(), 0, 0, offset, this.width, this.height, 20, 40);


        if (this.itemIco == Items.ENDER_CHEST) {
            context.drawItem(new ItemStack(this.itemIco), this.getX() + 2, this.getY() + 2);
            context.drawItem(new ItemStack(this.itemIco), this.getX() + 2, this.getY() + 2);
        } else if (this.itemIco == Items.CHEST) {
            context.drawItem(new ItemStack(this.itemIco), this.getX() + 2, this.getY() + 2);
        } else {
            context.drawItem(new ItemStack(this.itemIco), this.getX() + 2, this.getY() + 2);
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
        context.drawTexture(texture, x, y, (float)u, (float)i, width, height, textureWidth, textureHeight);
    }



}

