package pj.inventorybinds.ru.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static pj.inventorybinds.ru.InventoryBinds.setChatField;


@Mixin(value={ChatScreen.class}, priority=0)
public class ChatScreenMixin {


    @Shadow protected TextFieldWidget chatField;

    @Inject(method={"init"}, at={@At(value="RETURN")})
    private void initChatScreen(CallbackInfo ci) {
        setChatField(this.chatField);
    }
}
