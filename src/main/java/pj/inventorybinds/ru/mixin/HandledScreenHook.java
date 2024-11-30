package pj.inventorybinds.ru.mixin;


import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pj.inventorybinds.ru.InventoryBinds;


@Mixin(value={net.minecraft.client.gui.screen.ingame.HandledScreen.class}, priority=100)
public abstract class HandledScreenHook {


    @Shadow protected int backgroundHeight;

    @Shadow protected int x;

    @Shadow protected int y;

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        InventoryBinds.setScreenBackgroundHeight(this.backgroundHeight);
        InventoryBinds.setScreenX(this.x);
        InventoryBinds.setScreenY(this.y);

    }





}
