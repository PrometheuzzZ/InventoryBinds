package pj.inventorybinds.ru;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pj.inventorybinds.ru.config.ButtonsConfig;
import pj.inventorybinds.ru.config.ModConfigs;
import pj.inventorybinds.ru.config.buttons.ButtonsList;
import pj.inventorybinds.ru.config.buttons.ButtonJson;
import pj.inventorybinds.ru.gui.buttons.ButtonWidgetSettings;
import pj.inventorybinds.ru.gui.buttons.ButtonWidget;

import java.nio.charset.StandardCharsets;

public class InventoryBinds implements ModInitializer {

    public static final String MOD_ID = "inventorybinds";

    private static boolean recipeBookIsOpen = false;

    private static int screenBackgroundHeight = 0;

    private static TextFieldWidget chatField;

    public static void setChatField(TextFieldWidget chatF){
        chatField = chatF;
    }

    public static void insertText(String text, boolean override) {

        MinecraftClient.getInstance().setScreen(new ChatScreen("init"));

        if (override) {
            chatField.setText(text);
        } else {
            chatField.write(text);
        }
    }
    public static int getScreenBackgroundHeight(){
        return  screenBackgroundHeight;
    }

    public static void setScreenBackgroundHeight(int px){
        screenBackgroundHeight = px;
    }

    public static boolean getRecipeBookIsOpen(){
        return recipeBookIsOpen;
    }

    public static void setRecipeBookIsOpen(boolean open){
        recipeBookIsOpen = open;
    }

    @Override
    public void onInitialize() {
        ModConfigs.registerConfigs();

        ButtonsConfig.loadButtonsFromConfig();

        registerButtons();
    }

    private static void registerButtons(){
        ScreenEvents.AFTER_INIT.register((minecraftClient, screen, width, height) -> {

            if (screen instanceof HandledScreen) {

                ButtonsList buttonsList = ButtonsConfig.getButtonsList();

                int index = 0;
                int row = 0;

                for (ButtonJson buttonJson : buttonsList.getButtons()){
                    if(index==6){
                        index = 0;
                        row++;
                    }

                    ButtonWidget buttonWidget;
                    String bCommands = buttonJson.getCommand();

                    Item ItemIco = Registries.ITEM.get(Identifier.tryParse(buttonJson.getItemId()));

                    if(bCommands.charAt(0) == '/'){
                        String finalCommands = bCommands.substring(1);
                        buttonWidget = new ButtonWidget((HandledScreen<?>)screen, index, row, "字"+buttonJson.getName(), ItemIco, button -> {
                            assert MinecraftClient.getInstance().player != null;
                            MinecraftClient.getInstance().player.networkHandler.sendChatCommand(finalCommands);
                        });
                    } else if (bCommands.charAt(0) == '!'){
                        String finalCommands = bCommands.replaceAll("!", "");
                        buttonWidget = new ButtonWidget((HandledScreen<?>)screen, index, row, "異"+buttonJson.getName(), ItemIco, button ->  insertText("/"+finalCommands+" ", true));
                    } else {
                        buttonWidget = new ButtonWidget((HandledScreen<?>)screen, index, row, "体"+buttonJson.getName(), ItemIco, button -> {
                            assert MinecraftClient.getInstance().player != null;
                            MinecraftClient.getInstance().player.networkHandler.sendChatMessage(bCommands);
                        });
                    }

                    Screens.getButtons(screen).add(buttonWidget);
                    index++;
                }

                if(buttonsList.getButtons().size()>=6){
                    ButtonWidgetSettings buttonWidgetSettings = new ButtonWidgetSettings((HandledScreen<?>) screen, 6, 0, Text.translatable("gui.inventorybinds.settings").getString(), Item.byRawId(0));
                    Screens.getButtons(screen).add(buttonWidgetSettings);

                } else {
                    ButtonWidgetSettings buttonWidgetSettings = new ButtonWidgetSettings((HandledScreen<?>)screen, buttonsList.getButtons().size()+1, 0, Text.translatable("gui.inventorybinds.settings").getString(), Item.byRawId(0));
                    Screens.getButtons(screen).add(buttonWidgetSettings);
                }

            }

        });
    }
}
