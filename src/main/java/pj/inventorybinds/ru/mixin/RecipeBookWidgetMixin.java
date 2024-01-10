package pj.inventorybinds.ru.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import static pj.inventorybinds.ru.InventoryBinds.setRecipeBookIsOpen;


@Mixin({ RecipeBookWidget.class })
public abstract class RecipeBookWidgetMixin {


    @Shadow public abstract boolean isOpen();


    @Inject(at = { @At("HEAD") }, method = { "render" }, cancellable = true)
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(isOpen()){
            setRecipeBookIsOpen(true);
        } else {
            setRecipeBookIsOpen(false);
        }
    }
    }
