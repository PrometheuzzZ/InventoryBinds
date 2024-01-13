package pj.inventorybinds.ru.gui.buttons;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Item;
import pj.inventorybinds.ru.gui.ModSettingsScreen;
import pj.inventorybinds.ru.gui.screen.PJScreen;

public class ButtonWidgetSettings extends ButtonWidget{
    public ButtonWidgetSettings(HandledScreen<?> screen, int buttonIndex, int row, String description, Item items) {
        super(screen, buttonIndex, row, description, items, button ->  MinecraftClient.getInstance().setScreen(new PJScreen(new ModSettingsScreen())));
    }



}