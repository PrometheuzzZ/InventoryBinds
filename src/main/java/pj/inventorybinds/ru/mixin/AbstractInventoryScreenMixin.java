package pj.inventorybinds.ru.mixin;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Ordering;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

@Mixin(AbstractInventoryScreen.class)
public abstract class AbstractInventoryScreenMixin<T extends ScreenHandler> extends HandledScreen<T> {
    @Unique
    private static boolean hasInventoryTabs = true;

    private AbstractInventoryScreenMixin() { super(null, null, null); }

    @Shadow
    private Text getStatusEffectDescription(StatusEffectInstance effect) {
        throw new UnsupportedOperationException();
    }

    @Shadow
    private void drawStatusEffectBackgrounds(DrawContext draw, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide) {
        throw new UnsupportedOperationException();
    }

    @Shadow
    private void drawStatusEffectSprites(DrawContext draw, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide) {
        throw new UnsupportedOperationException();
    }

    @Shadow
    private void drawStatusEffectDescriptions(DrawContext draw, int x, int height, Iterable<StatusEffectInstance> statusEffects) {
        throw new UnsupportedOperationException();
    }

    @Inject(at = @At(value = "INVOKE",
            target = "net/minecraft/client/gui/screen/ingame/AbstractInventoryScreen.drawStatusEffectBackgrounds(Lnet/minecraft/client/gui/DrawContext;IILjava/lang/Iterable;Z)V"),
            method = "drawStatusEffects")
    private void drawStatusEffects(DrawContext draw, int mouseX, int mouseY, CallbackInfo info) {
            drawCenteredEffects(draw, mouseX, mouseY);
    }

    @ModifyVariable(at = @At(value = "INVOKE", target = "java/util/Collection.size()I", ordinal = 0),
            method = "drawStatusEffects", ordinal = 0)
    private Collection<StatusEffectInstance> drawStatusEffects(Collection<StatusEffectInstance> original) {
            return List.of();
    }

    @Unique
    private void drawCenteredEffects(DrawContext raw, int mouseX, int mouseY) {
        assert this.client != null;
        assert this.client.player != null;
        Collection<StatusEffectInstance> effects = Ordering.natural().sortedCopy(this.client.player.getStatusEffects());
        int size = effects.size();
        if (size == 0) {
            return;
        }
        boolean wide = size == 1;
        int y = this.y - 34;
        if (((Object) this) instanceof CreativeInventoryScreen || hasInventoryTabs) {
            y -= 28;
            if (false) {
                y -= 22;
            }
        }
        int xOff = 34;
        if (wide) {
            xOff = 122;
        } else if (size > 5) {
            xOff = (this.backgroundWidth - 32) / (size - 1);
        }
        int width = (size - 1) * xOff + (wide ? 120 : 32);
        int x = this.x + (this.backgroundWidth - width) / 2;
        StatusEffectInstance hovered = null;
        int restoreY = this.y;
        try {
            this.y = y;
            for (StatusEffectInstance inst : effects) {
                int ew = wide ? 120 : 32;
                List<StatusEffectInstance> single = List.of(inst);
                this.drawStatusEffectBackgrounds(raw, x, 32, single, wide);
                this.drawStatusEffectSprites(raw, x, 32, single, wide);
                if (wide) {
                    this.drawStatusEffectDescriptions(raw, x, 32, single);
                }
                if (mouseX >= x && mouseX < x + ew && mouseY >= y && mouseY < y + 32) {
                    hovered = inst;
                }
                x += xOff;
            }
        } finally {
            this.y = restoreY;
        }
        if (hovered != null && size > 1) {
            List<Text> list = List.of(this.getStatusEffectDescription(hovered), StatusEffectUtil.getDurationText(hovered, 1.0f, client.world.getTickManager().getTickRate()));
            raw.drawTooltip(client.textRenderer, list, Optional.empty(), mouseX, Math.max(mouseY, 16));
        }
    }



    @ModifyVariable(at = @At(value = "STORE", ordinal = 0),
            method = "drawStatusEffects", ordinal = 2)
          private int changeEffectSpace(int original) {
        return this.x;
    }
}