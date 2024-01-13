package pj.inventorybinds.ru.mixin;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pj.inventorybinds.ru.InventoryBinds;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Mixin({ AbstractInventoryScreen.class })
public abstract class AbstractInventoryScreenMixin<T extends ScreenHandler> extends HandledScreen<T> {

    @Shadow
    protected abstract Text getStatusEffectDescription(StatusEffectInstance statusEffect);

    private AbstractInventoryScreenMixin() {
        super(null, null, null);
    }


    @Inject(at = { @At("HEAD") }, method = { "drawStatusEffects" }, cancellable = true)
    private void drawStatusEffects(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        assert this.client != null;
        assert this.client.player != null;
        Collection<StatusEffectInstance> effects = this.client.player.getStatusEffects();
        int size = effects.size();
        if (size == 0) {
            ci.cancel();
        }
        int y = this.y - 34;

        int xOff = 34;
        if (size == 1) {
            xOff = 122;
        }
        else if (size > 5) {
            xOff = (this.backgroundWidth - 32) / (size - 1);
        }
        int width = (size - 1) * xOff + ((size == 1) ? 120 : 32);
        int x = this.x + (this.backgroundWidth - width) / 2;
        StatusEffectInstance hovered = null;
        for (final StatusEffectInstance inst : effects) {
            RenderSystem.setShaderTexture(0, AbstractInventoryScreenMixin.BACKGROUND_TEXTURE);
            StatusEffect effect = inst.getEffectType();
            Sprite sprite = this.client.getStatusEffectSpriteManager().getSprite(effect);
            int ew = (size == 1) ? 120 : 32;
            context.drawTexture(BACKGROUND_TEXTURE, x, y, 0, (size == 1) ? 166 : 198, ew, 32);
            RenderSystem.setShaderTexture(0, sprite.getAtlasId());
            context.drawSprite( x + ((size == 1) ? 6 : 7), y + 7, 0, 18, 18, sprite);
            if (size == 1) {
                context.drawSprite(x + 6, y + 7, 0, 18, 18, sprite);
                Text text = this.getStatusEffectDescription(inst);
                context.drawTextWithShadow(this.textRenderer, text, (x + 28), (y + 6), 16777215);
                String string = StatusEffectUtil.getDurationText(inst, 1.0f, MinecraftClient.getInstance().getTickDelta()).getString();
                context.drawTextWithShadow(this.textRenderer, string, (x + 28), (y + 16), 8355711);
            }
            if (mouseX >= x && mouseX < x + ew && mouseY >= y && mouseY < y + 32) {
                hovered = inst;
            }
            x += xOff;
        }
        if (hovered != null && size > 1) {
            final List<Text> list = List.of(this.getStatusEffectDescription(hovered), StatusEffectUtil.getDurationText(hovered, 1.0f, MinecraftClient.getInstance().getTickDelta()));
            context.drawTooltip(this.textRenderer, list, Optional.empty(), mouseX, Math.max(mouseY, 16));
        }
        if (this instanceof RecipeBookProvider rbp) {

            final RecipeBookWidget widget = rbp.getRecipeBookWidget();

            if (widget != null && widget.isOpen()) {
                ci.cancel();
            }
        } else {

            InventoryBinds.setRecipeBookIsOpen(false);
        }


        ci.cancel();


    }


}
