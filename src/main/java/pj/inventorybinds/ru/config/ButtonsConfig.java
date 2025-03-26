package pj.inventorybinds.ru.config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import pj.inventorybinds.ru.config.buttons.ButtonJson;
import pj.inventorybinds.ru.config.buttons.ButtonsList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ButtonsConfig {

    private static ButtonsList buttonsList;

    public static void loadButtonsFromConfig(){

        Gson gson  = new Gson();
        ButtonsList buttonsConfig1;

        buttonsConfig1 = gson.fromJson(ModConfigs.BUTTONS, ButtonsList.class);

        buttonsList = buttonsConfig1;
    }

    public static ButtonsList getButtonsList(){
        return buttonsList;
    }

    public static void addButtonToButtonsList(String name, String bind, String itemId, String server, Boolean hide){

        String configPath = FabricLoader.getInstance().getConfigDir().toString();

        String fullConfigPath = configPath+"\\inventorybinds\\inventorybinds.json";

        ButtonsList config = loadConfig(fullConfigPath);

        ButtonJson newButton = new ButtonJson();
        newButton.setName(name);
        newButton.setCommand(bind);
        newButton.setItemId(itemId);
        newButton.setServer(server);
        newButton.setHide(hide);
        config.getButtons().add(newButton);

        assignButtonIds(config);

        saveConfig(fullConfigPath, config);

    }



    private static ButtonsList loadConfig(String path) {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(path));
            Gson gson = new Gson();
            return gson.fromJson(reader, ButtonsList.class);
        } catch (IOException | JsonSyntaxException e) {
            // Если файл не существует или поврежден, создаем новый объект
            return new ButtonsList();
        }
    }

    private static void saveConfig(String path, ButtonsList config) {
        try (Writer writer = new BufferedWriter(new FileWriter(path))) {
            Gson gson = new Gson();
            gson.toJson(config, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void assignButtonIds(ButtonsList config) {
        for (int i = 0; i < config.getButtons().size(); i++) {
            config.getButtons().get(i).setId(String.valueOf(i));
        }
    }

    public static void removeButtonById(int id) {

        String configPath = FabricLoader.getInstance().getConfigDir().toString();

        String fullConfigPath = configPath+"\\inventorybinds\\inventorybinds.json";

        ButtonsList config = loadConfig(fullConfigPath);

        config.getButtons().removeIf(button -> button.getId().equals(String.valueOf(id)));

        assignButtonIds(config);

        saveConfig(fullConfigPath, config);
    }

    public static void swapButtonById(int old_id, int new_id, double mouseX, double mouseY) {


        String configPath = FabricLoader.getInstance().getConfigDir().toString();

        String fullConfigPath = configPath+"\\inventorybinds\\inventorybinds.json";

        ButtonsList config = loadConfig(fullConfigPath);

        if(new_id+1 > config.getButtons().size()){
            return;
        }

        if(new_id < 0){
            return;
        }

        ButtonJson oldButton = config.getButtons().get(old_id);

        ButtonJson newButton = config.getButtons().get(new_id);

        editButtonById(new_id, oldButton);

        editButtonById(old_id, newButton);

        //MinecraftClient.getInstance().player.closeScreen();

        MinecraftClient.getInstance().setScreen(new InventoryScreen(MinecraftClient.getInstance().player));



    }

    public static void editButtonById(int id, ButtonJson buttonJson) {

        String configPath = FabricLoader.getInstance().getConfigDir().toString();

        String fullConfigPath = configPath+"\\inventorybinds\\inventorybinds.json";

        ButtonsList config = loadConfig(fullConfigPath);

        config.getButtons().get(id).setName(buttonJson.getName());
        config.getButtons().get(id).setCommand(buttonJson.getCommand());
        config.getButtons().get(id).setServer(buttonJson.getServer());
        config.getButtons().get(id).setItemId(buttonJson.getItemId());
        config.getButtons().get(id).setHide(buttonJson.getHide());

        assignButtonIds(config);

        saveConfig(fullConfigPath, config);
    }
}
