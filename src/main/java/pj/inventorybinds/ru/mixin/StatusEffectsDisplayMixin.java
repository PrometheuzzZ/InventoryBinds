package pj.inventorybinds.ru.mixin;

import com.google.common.collect.Ordering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.sugar.Local;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pj.inventorybinds.ru.InventoryBinds;

import java.util.Iterator;


/**
 * Renders the duration bars under status effects.
 */
@Mixin(StatusEffectsDisplay.class)
public abstract class StatusEffectsDisplayMixin {

    @Shadow @Final private HandledScreen<?> parent;

    @Shadow @Final private static Identifier EFFECT_BACKGROUND_LARGE_TEXTURE;

    @Inject(method={"drawStatusEffectBackgrounds"}, at={@At(value="HEAD")}, cancellable = true)
    private void onDrawStatusEffectBackground(DrawContext context, int x, int height, Iterable<StatusEffectInstance> effects, boolean wide, CallbackInfo ci) {

        onDrawStatusEffectBackgroundHook(context, x, height, effects, wide);

        ci.cancel();

    }

    @Inject(method={"drawStatusEffectSprites"}, at={@At(value="HEAD")}, cancellable = true)
    private void drawStatusEffectSprites(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide, CallbackInfo ci) {

        drawStatusEffectSpritesHook(context, x, height, statusEffects, wide);

        ci.cancel();

    }

    @Inject(method={"drawStatusEffectDescriptions"}, at={@At(value="HEAD")}, cancellable = true)
    private void drawStatusEffectDescriptions(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects, CallbackInfo ci) {

        drawStatusEffectDescriptionsHook(context, x, height, statusEffects);

        ci.cancel();

    }
    @Unique
    private void drawStatusEffectDescriptionsHook(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects) {
        int i = InventoryBinds.getScreenY();

        int xOffset = getXoffset();

        int newX = x + xOffset;

        for(Iterator var6 = statusEffects.iterator(); var6.hasNext(); i += height) {
            StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var6.next();
            Text text = this.getStatusEffectDescription(statusEffectInstance);
            context.drawTextWithShadow(this.parent.getTextRenderer(), text, newX + 10 + 18, i + 6, 16777215);
            Text text2 = StatusEffectUtil.getDurationText(statusEffectInstance, 1.0F, MinecraftClient.getInstance().world.getTickManager().getTickRate());
            context.drawTextWithShadow(this.parent.getTextRenderer(), text2, newX + 10 + 18, i + 6 + 10, 8355711);
        }
    }

    @Unique
    private void drawStatusEffectSpritesHook(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide){
        StatusEffectSpriteManager statusEffectSpriteManager = MinecraftClient.getInstance().getStatusEffectSpriteManager();
        int i = InventoryBinds.getScreenY();

        int xOffset = getXoffset();

        int newX = x + xOffset;

        for(Iterator var8 = statusEffects.iterator(); var8.hasNext(); i += height) {
            StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var8.next();
            RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
            Sprite sprite = statusEffectSpriteManager.getSprite(registryEntry);
            context.drawSpriteStretched(RenderLayer::getGuiTextured, sprite, newX + (wide ? 6 : 7), i + 7, 18, 18);
        }
    }

    @Unique
    private void onDrawStatusEffectBackgroundHook(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide){
        int i = InventoryBinds.getScreenY();

        int xOffset = getXoffset();

        int newX = x + xOffset;

        for(Iterator var7 = statusEffects.iterator(); var7.hasNext(); i += height) {
            StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var7.next();
            if (wide) {
                context.drawGuiTexture(RenderLayer::getGuiTextured, EFFECT_BACKGROUND_LARGE_TEXTURE, newX, i, 120, 32);
            } else {
                context.drawGuiTexture(RenderLayer::getGuiTextured, EFFECT_BACKGROUND_LARGE_TEXTURE, newX, i, 32, 32);
            }
        }

    }

    @Unique
    private int getXoffset(){
        return (InventoryBinds.getButtonsRow()+1)*20+((InventoryBinds.getButtonsRow()-1)*2)+4;
    }

    private Text getStatusEffectDescription(StatusEffectInstance statusEffect) {
        MutableText mutableText = ((StatusEffect)statusEffect.getEffectType().value()).getName().copy();
        if (statusEffect.getAmplifier() >= 1 && statusEffect.getAmplifier() <= 9) {
            MutableText var10000 = mutableText.append(ScreenTexts.SPACE);
            int var10001 = statusEffect.getAmplifier();
            var10000.append(Text.translatable("enchantment.level." + (var10001 + 1)));
        }

        return mutableText;
    }

}