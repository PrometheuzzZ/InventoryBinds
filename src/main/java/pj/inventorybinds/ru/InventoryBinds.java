package pj.inventorybinds.ru;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import pj.inventorybinds.ru.config.ButtonsConfig;
import pj.inventorybinds.ru.config.ModConfigs;
import pj.inventorybinds.ru.config.buttons.ButtonsList;
import pj.inventorybinds.ru.config.buttons.ButtonJson;
import pj.inventorybinds.ru.gui.buttons.ButtonWidgetSettings;
import pj.inventorybinds.ru.gui.buttons.PJButtonWidget;

import java.util.ArrayList;
import java.util.List;

public class InventoryBinds implements ModInitializer {

    public static boolean isShiftDown = false;

    public static boolean firstRun = true;

    public static final String MOD_ID = "inventorybinds";

    private static boolean recipeBookIsOpen = false;

    private static int screenBackgroundHeight = 0;
    private static int screenX = 0;
    private static int screenY = 0;
    private static int buttonsRow = 0;

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

    public static void setScreenBackgroundHeight(int px){
        screenBackgroundHeight = px;
    }

    public static int getScreenBackgroundHeight(){
        return  screenBackgroundHeight;
    }


    public static void setScreenX(int px){
        screenX = px;
    }

    public static int getScreenX(){
        return screenX;
    }

    public static void setScreenY(int px){
        screenY = px;
    }

    public static int getScreenY(){
        return screenY;
    }

    public static int getButtonsRow(){
        return buttonsRow;
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

        loadButtons();
    }

    private static void loadButtons(){


        ScreenEvents.AFTER_INIT.register((minecraftClient, screen, width, height) -> {

            if (screen instanceof HandledScreen) {

               if(!firstRun){

                   ModConfigs.reloadConfig();

               } else {

                   firstRun = false;

               }

                ButtonsList buttonsList = ButtonsConfig.getButtonsList();

                int index = 0;
                int row = 0;

                for (ButtonJson buttonJson : buttonsList.getButtons()){

                    if (index % 7 == 0 && index != 0) {
                        index = 0;
                        row++;
                    }

                    PJButtonWidget buttonWidget;

                    Boolean hide = false;

                    try {
                        if(buttonJson.getHide()!=null) {
                            hide = buttonJson.getHide();
                        } else{
                            hide = false;
                        }
                    } catch (Exception e) {
                        hide = false;
                    }


                    String bCommands = buttonJson.getCommand();

                    String ItemIco = buttonJson.getItemId();


                    if(bCommands.charAt(0) == '/'){

                        List<Text> desctiptionList = new ArrayList<>();

                        desctiptionList.add(Text.literal(buttonJson.getName()));

                        String finalCommands = bCommands.substring(1);


                        if(!hide) {
                            desctiptionList.add(Text.literal("字 /" + finalCommands));
                        } else {
                            desctiptionList.add(Text.literal("字 /************"));
                        }

                        buttonWidget = new PJButtonWidget((HandledScreen<?>)screen, index, row, desctiptionList, ItemIco, button -> {
                            assert MinecraftClient.getInstance().player != null;
                            MinecraftClient.getInstance().player.networkHandler.sendChatCommand(finalCommands);
                        }, Integer.parseInt(buttonJson.getId()));
                    } else if (bCommands.charAt(0) == '!'){


                        List<Text> desctiptionList = new ArrayList<>();

                        desctiptionList.add(Text.literal(buttonJson.getName()));

                        String finalCommands = bCommands.replaceAll("!", "");


                        if(!hide) {
                            desctiptionList.add(Text.literal("異 /" + finalCommands));
                        }else {
                            desctiptionList.add(Text.literal("異 /************"));
                        }

                        buttonWidget = new PJButtonWidget((HandledScreen<?>)screen, index, row, desctiptionList, ItemIco, button ->  insertText("/"+finalCommands+" ", true),Integer.parseInt(buttonJson.getId()));
                    } else {

                        List<Text> desctiptionList = new ArrayList<>();
                        desctiptionList.add(Text.literal(buttonJson.getName()));


                            desctiptionList.add(Text.literal("体 " + bCommands));



                        buttonWidget = new PJButtonWidget((HandledScreen<?>)screen, index, row, desctiptionList, ItemIco, button -> {
                            assert MinecraftClient.getInstance().player != null;
                            MinecraftClient.getInstance().player.networkHandler.sendChatMessage(bCommands);
                        }, Integer.parseInt(buttonJson.getId()));
                    }

                    String server = "";

                    try {
                        server = MinecraftClient.getInstance().getNetworkHandler().getServerInfo().address;
                    } catch (Exception e){
                        server = "null";
                    }


                    if(buttonJson.getServer().equalsIgnoreCase(server)) {
                        Screens.getButtons(screen).add(buttonWidget);
                        index++;
                    } else if(buttonJson.getServer().equalsIgnoreCase("*")) {
                        Screens.getButtons(screen).add(buttonWidget);
                        index++;
                    }
                }


                if(ButtonsConfig.getButtonsList().getNewBindButtonEnabled()){

                    if (index % 7 == 0 && index != 0) {
                        index = 0;
                        row++;
                    }

              /*  if(buttonsList.getButtons().size()>=6){
                    ButtonWidgetSettings buttonWidgetSettings = new ButtonWidgetSettings((HandledScreen<?>) screen, 6, 0, Text.translatable("gui.inventorybinds.new_bind").getString(), "AIR");
                    Screens.getButtons(screen).add(buttonWidgetSettings);

                } else {
                    ButtonWidgetSettings buttonWidgetSettings = new ButtonWidgetSettings((HandledScreen<?>)screen, index, 0, Text.translatable("gui.inventorybinds.new_bind").getString(), "AIR");
                    Screens.getButtons(screen).add(buttonWidgetSettings);

                } */

                    List<Text> description = new ArrayList<>();
                    description.add(Text.translatable("gui.inventorybinds.new_bind"));

                    ButtonWidgetSettings buttonWidgetSettings = new ButtonWidgetSettings((HandledScreen<?>)screen, index, row, description, "AIR");
                    Screens.getButtons(screen).add(buttonWidgetSettings);
                }

                buttonsRow = row;

            }

        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean currentShiftState = GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS ||
                    GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS;

            if (currentShiftState != isShiftDown) {
                isShiftDown = currentShiftState;
            }
        });

    }





}
