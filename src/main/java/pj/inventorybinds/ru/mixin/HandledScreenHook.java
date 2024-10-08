package pj.inventorybinds.ru.mixin;


import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pj.inventorybinds.ru.InventoryBinds;


@Mixin(value={net.minecraft.client.gui.screen.ingame.HandledScreen.class}, priority=100)
public abstract class HandledScreenHook {



    @Shadow protected int backgroundHeight;


    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        InventoryBinds.setScreenBackgroundHeight(this.backgroundHeight);

    }


}
