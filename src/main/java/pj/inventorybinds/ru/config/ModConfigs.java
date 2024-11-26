package pj.inventorybinds.ru.config;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.MinecraftClient;

import static pj.inventorybinds.ru.InventoryBinds.MOD_ID;
import static pj.inventorybinds.ru.config.ButtonsConfig.loadButtonsFromConfig;


public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static String BUTTONS;

    public static String defaultButtons = "{\n" +
            "\"bind_editor_enabled\":\"true\",\n"+
            "\"new_bind_button_enabled\":\"true\",\n"+
            "  \"buttons\":[\n" +
            "    {\n" +
            "      \"name\":\"Template Command\",\n" +
            "      \"bind\":\"/spawn\",\n" +
            "      \"item_id\":\"grass_block\",\n" +
            "      \"server\":\"*\",\n" +
            "      \"id\":\"0\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\":\"Template Suggest Command\",\n" +
            "      \"bind\":\"!ban\",\n" +
            "      \"item_id\":\"grass_block\",\n" +
            "      \"server\":\"*\",\n" +
            "      \"id\":\"1\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\":\"Template Chat Message\",\n" +
            "      \"bind\":\"Hello World\",\n" +
            "      \"item_id\":\"grass_block\",\n" +
            "      \"server\":\"*\",\n" +
            "      \"id\":\"2\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    public static void registerConfigs() {
        configs = new ModConfigProvider();

        createConfigs();

        CONFIG = SimpleConfig.of(MOD_ID).provider(configs).request();

        assignConfigs();




    }

    private static void createConfigs() {


        configs.addKeyValuePair(new Pair<>("BUTTONS", defaultButtons), "");

    }

    private static void assignConfigs() {

        BUTTONS = CONFIG.getOrDefault("BUTTONS", defaultButtons);

    }

    public static void reloadConfig(){
        CONFIG =  SimpleConfig.of(MOD_ID).provider(configs).request();

        assignConfigs();

        loadButtonsFromConfig();
    }


}