package pj.inventorybinds.ru.gui.buttons;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pj.inventorybinds.ru.gui.ModCreateBindScreen;
import pj.inventorybinds.ru.gui.screen.PJScreen;

import java.util.List;

public class ButtonWidgetSettings extends PJButtonWidget {
    public ButtonWidgetSettings(HandledScreen<?> screen, int buttonIndex, int row, List<Text> description, String items) {
        super(screen, buttonIndex, row, description, items, button -> {


            assert MinecraftClient.getInstance().player != null;
            ScreenHandler currentScreenHandler = MinecraftClient.getInstance().player.currentScreenHandler;
                Item item = currentScreenHandler.getCursorStack().getItem();
                Identifier itemInd = Registries.ITEM.getId(item);
                String itemId = itemInd.getPath();


            MinecraftClient.getInstance().setScreen(new PJScreen(new ModCreateBindScreen(itemId)));
        }, true, -1);
    }



}
